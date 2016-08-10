package com.eztcn.user.eztcn.activity.mine;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.customView.SelectPicPopupWindow;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.CommonUtil;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.SDCardUtil;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.utils.IdVerification;

/**
 * @title 用户基本信息
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class UserInfoActivity extends FinalActivity implements OnClickListener,
		IHttpResult {

	@ViewInject(R.id.familyPerson)
	// , click = "onClick"
	private LinearLayout familyPerson;
	@ViewInject(R.id.photoLayout)
	// , click = "onClick"
	private RelativeLayout photoLayout;// 头像
	@ViewInject(R.id.photo)
	private RoundImageView photo;
	@ViewInject(R.id.auth)
	// , click = "onClick"
	private TextView auth;
	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.neck)
	private EditText nickname;
	@ViewInject(R.id.idCard)
	private EditText idCard;
	@ViewInject(R.id.sex)
	// , click = "onClick"
	private TextView sex;
	@ViewInject(R.id.age)
	private EditText age;
	@ViewInject(R.id.wedLock)
	// , click = "onClick"
	private TextView wedLock;
	@ViewInject(R.id.medicalNo)
	private EditText medicalNo;
	@ViewInject(R.id.profession)
	// , click = "onClick"
	private TextView profession;
	@ViewInject(R.id.education)
	// , click = "onClick"
	private TextView education;
	@ViewInject(R.id.address)
	private EditText address;
	@ViewInject(R.id.phone)
	private EditText phone;

	@ViewInject(R.id.idCard_layout)
	private LinearLayout layoutIdCard;

	@ViewInject(R.id.medicalNo_layout)
	private LinearLayout layoutMedicalNo;

	@ViewInject(R.id.phone_layout)
	private LinearLayout layoutPhone;

	@ViewInject(R.id.ads_layout)
	private LinearLayout layoutAds;

	@ViewInject(R.id.bindPhone)
	// , click = "onClick"
	private TextView bindPhone;// 绑定手机

	private TextView save;
	private PatientBean patientBean;
	// private boolean isModified;// 判断是否有数据改动
	private AlertDialog.Builder builder;
	private int professionPosition;
	private int educationPosition;
	private List<EztDictionary> prolist;// 职业字典表
	private String[] pName;// 职业字典表名称
	private List<EztDictionary> eduList;// 教育水平字典表
	private String[] eName;// 教育水平名称

	// 自定义的底部弹出框类（用于图片选择）
	private SelectPicPopupWindow menuWindow;
	private String photoPath;
	private String PIC_NAME;
	private int flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo);

		ViewUtils.inject(UserInfoActivity.this);
		flag = getIntent().getIntExtra("flag", 0);
		if (flag == 1) {// 二维码扫描跳转
			patientBean = (PatientBean) getIntent().getSerializableExtra("userinfo");
			loadTitleBar(true,
					patientBean.getEpName() == null ? "" : patientBean.getEpName()
							+ "的信息", null);
			sex.setEnabled(false);

			wedLock.setEnabled(false);
			profession.setEnabled(false);
			if (patientBean.getEpProfession() == 0) {
				profession.setText("暂无");
			}
			education.setEnabled(false);
			nickname.setEnabled(false);
			layoutAds.setVisibility(View.GONE);
			layoutPhone.setVisibility(View.GONE);
			age.setEnabled(false);
			if (patientBean.getEpAge() == 0) {
				age.setText("暂无");
			}

		} else {// 正常跳入
			if(BaseApplication.patient!=null){
				PIC_NAME = "userinfo_" + BaseApplication.patient.getUserId();
			}
			patientBean = BaseApplication.patient;
			photoLayout.setVisibility(View.VISIBLE);
			layoutIdCard.setVisibility(View.VISIBLE);
			layoutMedicalNo.setVisibility(View.VISIBLE);
			layoutPhone.setVisibility(View.VISIBLE);
			save = loadTitleBar(true, "个人信息", "保存");
			save.setOnClickListener(this);
		}
		builder = new AlertDialog.Builder(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	public void init() {

		if (patientBean == null) {
			return;
		}

		if (flag != 1) {
			if (TextUtils.isEmpty(patientBean.getEpHiid())) {
				auth.setVisibility(View.VISIBLE);
			} else {
				auth.setVisibility(View.GONE);
			}
			if (TextUtils.isEmpty(patientBean.getEpMobile())) {
				bindPhone.setVisibility(View.VISIBLE);
			} else {
				bindPhone.setVisibility(View.GONE);
			}
		}

		name.setText(patientBean.getEpName());
		int uSex = patientBean.getEpSex();
		if (uSex == 0) {
			sex.setText("男");
		} else {
			sex.setText("女");
		}
		photo.setImageResource(R.drawable.userdefault);
		idCard.setText(patientBean.getEpPid());
		if (patientBean.getEpPid() != null) {
			age.setText(StringUtil.getAgeByIdCard(patientBean.getEpPid()) + "");
		}
		medicalNo.setText(patientBean.getEpHiid());
		address.setText(patientBean.getEpAddress());
		phone.setText(patientBean.getEpMobile());
		getProfession();
		getEducation();
	}

	/**
	 * 显示头像
	 */
	public void showUserPhoto() {

		String path = patientBean.getEpPic();
		if (path != null && !path.equals("")) {
			photoPath = EZTConfig.USER_PHOTO + path;
			BitmapUtils bitmapUtils = new BitmapUtils(UserInfoActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
					.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(R.drawable.userman);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.userman);
			bitmapUtils.display(photo, photoPath);
		}
	}

	/**
	 * 判断是否有数据改动
	 */
	public void judgeIsModified() {
		if (patientBean == null) {
			Toast.makeText(getApplicationContext(), "你还未登录", Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}
	/**
	 * 判断参数有效性
	 */
	public boolean judgeParams() {
		if (patientBean == null) {
			Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			return false;
		}
		String n = name.getText().toString();
		if (TextUtils.isEmpty(n)) {
			Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (n.length() < 2 || n.length() > 4) {
			Toast.makeText(mContext, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
			return false;
		}
		String ic = idCard.getText().toString();
		if (TextUtils.isEmpty(ic)) {
			Toast.makeText(mContext, "身份证不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		/*if (!StringUtil.validateCard(ic)) {
			Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
			return false;
		}*/
		try {
			if (!"该身份证有效".equals(IdVerification.IDCardValidate(ic))) {
				Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String mc = medicalNo.getText().toString();
		if (!TextUtils.isEmpty(mc)) {
			if (mc.length() < 15) {
				Toast.makeText(mContext, "医保号格式错误,长度为15~20", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
		return true;
	}

	@OnClick(R.id.sex)
	private void sexClick(View v) {
		String[] sexArrys = { "男", "女" };
		showSelector(sexArrys, 1);
	}

	@OnClick(R.id.wedLock)
	private void wedLockClick(View v) {
		String[] wedArrys = { "未婚", "已婚" };
		showSelector(wedArrys, 2);
	}

	@OnClick(R.id.profession)
	// 选择职业
	private void professionClick(View v) {
		selectProfession();
	}

	@OnClick(R.id.education)
	private void educationClick(View v) {
		selectEducation();
	}

	@OnClick(R.id.photoLayout)
	// 选择头像
	private void photoLayoutClick(View v) {
		// dialogChoicePic(v);
	}

	@OnClick(R.id.auth)
	// 实名认证
	private void authClick(View v) {
		Intent intent;
		intent = new Intent(this, AutonymAuthActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.bindPhone)
	// 绑定手机
	private void bindPhoneClick(View v) {
		startActivity(new Intent(mContext, ModifyPhoneActivity.class));
	}

	public void onClick(View v) {

		if (v == save) {// 保存
			if (judgeParams()) {
				saveUserInfo();
			}
		}
	}

	/**
	 * 选择图
	 */
	private void dialogChoicePic(View v) {
		menuWindow = new SelectPicPopupWindow(mContext, new ChoiceImgClick());
		// 显示窗口
		menuWindow.showAtLocation(v,
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	/**
	 * 选择图片
	 */
	public class ChoiceImgClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_take_photo:// 拍照
				openCamera(11);
				break;
			case R.id.btn_pick_photo:// 从相册选择
				openImgLibrary(22);
				break;
			default:// 取消
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照或图库返回结果
		if (requestCode == 11 || requestCode == 22) {// 身份证
			Bitmap bitmap = getBitmapFromImage(data);
			if (bitmap == null)
				Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
			else {
				photo.setImageBitmap(bitmap);

				File photoFile = CommonUtil.saveImgToSdcard(
						bitmap,
						SDCardUtil.getDirectory(EZTConfig.RES_SAVE_DIR
								+ File.separator + EZTConfig.DIR_SD_CARD_IMG),
						PIC_NAME + ".jpg");
				photoPath = photoFile.getAbsolutePath();
			}
		}
		if (menuWindow != null) {
			menuWindow.dismiss();
		}

	}

	/**
	 * 提交用户数据
	 */
	public void saveUserInfo() {
		UserImpl impl = new UserImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("patinentId", patientBean.getId() + "");
		params.addBodyParameter("userId", patientBean.getUserId() + "");
		params.addBodyParameter("epName", name.getText().toString());
		params.addBodyParameter("epIdnoType", "1");
		params.addBodyParameter("epPid", idCard.getText().toString());
		// params.put("epMobile", phone.getText().toString());
		if (sex.getText().equals("男")) {
			params.addBodyParameter("epSex", "0");
		} else {
			params.addBodyParameter("epSex", "1");
		}
		params.addBodyParameter("nickName", nickname.getText().toString());
		params.addBodyParameter("epHiid", medicalNo.getText().toString());
		params.addBodyParameter("epProfession", professionPosition + "");
		if (wedLock.getText().toString().equals("未婚")) {
			params.addBodyParameter("epWedlock", "0");
		} else {
			params.addBodyParameter("epWedlock", "1");
		}
		params.addBodyParameter("epCulturalLeave", educationPosition + "");
		params.addBodyParameter("epAddress", address.getText().toString());
		impl.userModify(params, this);
		showProgressToast();
	}

	/**
	 * 上传头像到服务器
	 */
	public void uploadPhoto() {
		if (photoPath == null || photoPath.equals("")) {
			return;
		}
		File photoFile = new File(photoPath);
		if (BaseApplication.patient != null && photoFile != null
				&& photoFile.exists()) {

			UserImpl impl = new UserImpl();
			RequestParams params = new RequestParams();
			params.addBodyParameter("patientId", patientBean.getId()+"");
			// try {
			params.addBodyParameter("myfile", photoFile);
			// } catch (FileNotFoundException e) {
			// e.printStackTrace();
			// }
			impl.userUploadPhoto(params, this);
			BaseApplication.patient.setEpPic(photoFile.getName());
		}
	}

	/**
	 * 获取职业列表
	 */
	public void getProfession() {
		professionPosition = patientBean.getEpProfession();
		int length = 0;
		prolist = EztDictionaryDB.getInstance(getApplicationContext())
				.getDictionaryList("professional");
		if (prolist != null) {
			length = prolist.size();
		}
		pName = new String[length + 1];
		pName[0] = "请选择";
		for (int i = 0; i < length; i++) {
			pName[i + 1] = prolist.get(i).getLabel();
			if (professionPosition == Integer.parseInt(prolist.get(i)
					.getValue())) {
				profession.setText(pName[i + 1]);
			}
		}
	}

	/**
	 * 选择职业列表
	 */
	private void selectProfession() {
		builder.setItems(pName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					professionPosition = 0;
				} else {
					professionPosition = Integer.parseInt(prolist
							.get(which - 1).getValue());
				}
				profession.setText(pName[which]);
			}
		});
		builder.create().show();

	}

	/**
	 * 刷新用户信息
	 */
	public void refreshUser() {
		if (patientBean != null) {
			patientBean.setEpName(name.getText().toString());
			patientBean.setEpPid(idCard.getText().toString());
			if (sex.getText().toString().equals("男")) {
				patientBean.setEpSex(0);
			} else {
				patientBean.setEpSex(1);
			}

			patientBean.setEpName(nickname.getText().toString());
			patientBean.setEpHiid(medicalNo.getText().toString());
			patientBean.setEpProfession(professionPosition);
		//	patientBean.setCulturalLeave(educationPosition);
			patientBean.setEpAddress(address.getText().toString());
			patientBean.setEpMilitaryCard(phone.getText().toString());
		}
	}

	/**
	 * 获取教育水平字典表
	 */
	public void getEducation() {

		int length = 0;
		eduList = EztDictionaryDB.getInstance(getApplicationContext())
				.getDictionaryList("levelOfEducation");
		if (eduList != null) {
			length = eduList.size();
		}
		eName = new String[length + 1];
		eName[0] = "请选择";
		EztDictionary dict = null;
		for (int i = 0; i < length; i++) {
			dict = eduList.get(i);
			eName[i + 1] = dict.getLabel();
			if (educationPosition == Integer.parseInt(dict.getValue())) {
				education.setText(eName[i + 1]);
			}
		}
	}

	/**
	 * 选择教育水平
	 */
	public void selectEducation() {
		builder.setItems(eName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					educationPosition = 0;
				} else {
					educationPosition = Integer.parseInt(eduList.get(which - 1)
							.getValue());
				}
				education.setText(eName[which]);
			}
		});
		builder.create().show();
	}

	/**
	 * 选择窗口
	 */
	public void showSelector(String[] arrys, final int type) {
		builder.setItems(arrys, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (type == 1) {
					if (which == 0) {
						sex.setText("男");
					} else {
						sex.setText("女");
					}
				} else {
					if (which == 0) {
						wedLock.setText("未婚");
					} else {
						wedLock.setText("已婚");
					}
				}

				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		String msg;
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (taskID == HttpParams.USER_MODIFY) {
			if (flag) {
				msg = "修改成功";
				refreshUser();
				uploadPhoto();
				finish();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		} else if (taskID == HttpParams.USER_UPLOADPHOTO) {// 头像
			if (flag && patientBean != null) {
				patientBean.setEpPic(map.get("path").toString());
			}

		}
	}
}

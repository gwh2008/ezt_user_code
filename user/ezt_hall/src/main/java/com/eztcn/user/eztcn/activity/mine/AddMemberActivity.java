package com.eztcn.user.eztcn.activity.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztDictionary;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.customView.SelectPicPopupWindow;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.CommonUtil;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.SDCardUtil;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 添加（修改）家庭成员
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
public class AddMemberActivity extends FinalActivity implements IHttpResult,
		OnClickListener {

	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.photo)
	private RoundImageView photo;
	@ViewInject(R.id.relation)
	private Spinner relation;
	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.idCard)
	private EditText idCard;

	// @ViewInject(R.id.drug)
	// private EditText etDrug;// 药物过敏史

	@ViewInject(R.id.sex)
	// , click = "onClick"
	private TextView sex;
	// @ViewInject(R.id.age)
	// private EditText age;
	@ViewInject(R.id.medicalNo)
	private EditText medicalNo;
	@ViewInject(R.id.phone)
	private EditText phone;
	@ViewInject(R.id.photoLayout)
	// , click = "onClick"
	private RelativeLayout photoLayout;
	private int relationPosition = 0;
	private FamilyMember member;
	private int executeType = 0;

	private PopupWindow menuWindow;
	// private String photoPath;// 头像地址
	private File photoFile;
	private TextView tvFinish;// 完成按钮

	private  String PIC_NAME = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmember);
		ViewUtils.inject(AddMemberActivity.this);
		tvFinish = loadTitleBar(true, "添加就诊人", "提交");
		tvFinish.setOnClickListener(this);
		// tvFinish.setBackgroundResource(R.drawable.selector_title_bar_btn_bg);
		if(BaseApplication.patient!=null){
			PIC_NAME = "addmember_" + BaseApplication.patient.getUserId();
		}
		initMemberInfo();
		initRelationSpinner();
	}

	/**
	 * 初始化成员信息
	 */
	public void initMemberInfo() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			member = (FamilyMember) bundle.get("member");
		}
		if (member == null) {
			return;
		}
		executeType = 1;
		title.setText("修改就诊人");
		tvFinish.setText("修改");
		relationPosition = member.getRelation();
		name.setText(member.getMemberName());
		idCard.setText(member.getIdCard());
		int sexPosition = member.getSex();
		if (sexPosition == 0) {
			sex.setText("男");
			photo.setImageResource(R.drawable.userdefault);
		} else {
			sex.setText("女");
			photo.setImageResource(R.drawable.userdefault);
		}
		showUserPhoto();
		phone.setText(member.getPhone());
		medicalNo.setText(member.getMedicalNo());
	}

	/**
	 * 显示头像
	 */
	public void showUserPhoto() {
		String path = member.getFamilyPhoto();
		if (path != null && !path.equals("")) {
			String photoPath = EZTConfig.USER_PHOTO + path;
			BitmapUtils bitmapUtils = new BitmapUtils(AddMemberActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
					.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(R.drawable.userman);
			bitmapUtils.configDefaultLoadFailedImage(R.drawable.userman);
			bitmapUtils.display(photo, photoPath);
		}
	}

	@OnClick(R.id.sex)
	private void sexClick(View v) {
		String[] arrys = { "男", "女" };
		showSelector(arrys);
	}

	@OnClick(R.id.photoLayout)
	// 头像选择
	private void photoLayoutClick(View v) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.sex) {
		} else if (v.getId() == R.id.photoLayout) {// 头像选择
		} else {// 完成
			if (judgeParams()) {
				if (BaseApplication.getInstance().isNetConnected) {
					addMembers();
					uploadPhoto();
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			}
		}
	}
	/**
	 * 判断参数合法性
	 */
	public boolean judgeParams() {
		String n = name.getText().toString();
		// String strDrug = etDrug.getText().toString();
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
		if (!StringUtil.validateCard(ic)) {
			Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.isPhone(phone.getText().toString())) {
			Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
			return false;
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

	public void addMembers() {
		if (BaseApplication.patient == null) {
			return;
		}
		UserImpl impl = new UserImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("name", name.getText().toString());
		if (sex.getText().toString().equals("男")) {
			params.addBodyParameter("sex", "0");
		} else {
			params.addBodyParameter("sex", "1");
		}
		params.addBodyParameter("kinship", relationPosition + "");
		params.addBodyParameter("idnoType", "1");
		params.addBodyParameter("idno", idCard.getText().toString());
		params.addBodyParameter("mobile", phone.getText().toString());
		params.addBodyParameter("hiid", medicalNo.getText().toString());
		if (executeType == 1 && member != null) {
			params.addBodyParameter("id", member.getFamilyId() + "");
			impl.modifyMemberCenter(params, this);

		} else {
			impl.addFamilyMember(params, this);
		}
		showProgressToast();
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

				photoFile = CommonUtil.saveImgToSdcard(
						bitmap,
						SDCardUtil.getDirectory(EZTConfig.RES_SAVE_DIR
								+ File.separator + EZTConfig.DIR_SD_CARD_IMG),
						PIC_NAME + ".jpg");
				// photoPath = photoFile.getAbsolutePath();
			}
		}
		if (menuWindow != null) {
			menuWindow.dismiss();
		}

	}

	/**
	 * 初始化关系列表
	 */
	public void initRelationSpinner() {
		final List<EztDictionary> rList = EztDictionaryDB.getInstance(
				getApplicationContext()).getDictionaryList("kinship");
		if(rList!=null&&rList.size()!=0){
			if (rList.size() > 0) {
				rList.remove(0);
			}
		}
		ArrayList<String> nameList = new ArrayList<String>();
		EztDictionary dict = null;
		int p = 0;
		if(null!=rList){
			for (int i = 0; i < rList.size(); i++) {
				dict = rList.get(i);
				nameList.add(dict.getLabel());
				if (relationPosition == Integer.parseInt(dict.getValue())) {
					p = i;
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
					R.layout.item_spinner_right, R.id.spinner_txt, nameList);
			relation.setAdapter(adapter);
			relation.setSelection(p);
			relation.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
										   int position, long id) {
					relationPosition = Integer.parseInt(rList.get(position)
							.getValue());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	/**
	 * 选择窗口
	 */
	public void showSelector(String[] arrys) {
		Builder builder = new Builder(mContext);
		builder.setItems(arrys, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					sex.setText("男");
				} else {
					sex.setText("女");
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
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) object[1];

		if (!status) {
			Toast.makeText(mContext, object[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) object[2];
		if (map == null || map.size() == 0) {
			Toast.makeText(getApplicationContext(), "服务器异常", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (taskID == HttpParams.ADD_FAMILY) {// 添加家庭成员

			if (flag) {
				msg = "添加成功";
				Intent intent = new Intent(this, FamilyMemberActivity.class);
				setResult(2, intent);
				finish();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		} else if (taskID == HttpParams.USER_UPLOADPHOTO) {// 头像
			if (flag && member != null) {
				member.setFamilyPhoto(map.get("path").toString());
			}
		} else {// 修改家庭成员
			if (flag) {
				msg = "修改成功";
				Intent intent = new Intent(this, FamilyMemberActivity.class);
				setResult(2, intent);
				finish();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 上传头像到服务器
	 */
	public void uploadPhoto() {

		if (BaseApplication.patient != null && photoFile != null
				&& photoFile.exists()) {

			UserImpl impl = new UserImpl();
			RequestParams params = new RequestParams();
			params.addBodyParameter("patientId",
					BaseApplication.patient.getId()+"");
			params.addBodyParameter("myfile", photoFile);
			impl.userUploadPhoto(params, this);
		}
	}
}

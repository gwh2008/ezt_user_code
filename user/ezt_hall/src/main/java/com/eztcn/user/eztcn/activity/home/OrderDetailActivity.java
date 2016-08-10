package com.eztcn.user.eztcn.activity.home;
import java.util.Map;
import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.BackValidateDialog;
import com.eztcn.user.eztcn.customView.BackValidateDialog.CodeSure;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.utils.Constant;
/**
 * @title 预约详情
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class OrderDetailActivity extends FinalActivity implements
		OnClickListener, IHttpResult, CodeSure {
	private String temp_regId, temp_pfId;
	private TextView rightButton;// 退号
	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.d_name)
	private TextView doctorName;
	@ViewInject(R.id.d_title)
	private TextView d_title;
	@ViewInject(R.id.hos_name)
	private TextView hospital;
	@ViewInject(R.id.dept)
	private TextView dept;
	@ViewInject(R.id.seeTime)
	private TextView seeTime;
	@ViewInject(R.id.personName)
	private TextView personName;
	@ViewInject(R.id.payType)
	private TextView payType;
	@ViewInject(R.id.uploadMedical)
	private TextView uploadMedical;
	@ViewInject(R.id.idCard)
	private TextView idCard;
	@ViewInject(R.id.phone)
	private TextView phone;
	@ViewInject(R.id.buttonLayout)
	private LinearLayout buttonLayout;
	@ViewInject(R.id.evaluate)
	private TextView evaluate;
	@ViewInject(R.id.writeLetter)
	private TextView writeLetter;
	@ViewInject(R.id.checkillRecord)
	private TextView checkillRecord;
	@ViewInject(R.id.secondOrder)
	private TextView secondOrder;
	@ViewInject(R.id.illnessDescribe)
	private TextView illnessDescribe;// 病情描述
	@ViewInject(R.id.head_pic)
	private RoundImageView head_pic;
	private Record_Info info;
	private BackValidateDialog validateDialog;
	private Button back_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail);
		ViewUtils.inject(OrderDetailActivity.this);
		rightButton = loadTitleBar(true, "", null);
		rightButton.setBackgroundResource(R.drawable.selector_title_bar_btn_bg);
		rightButton
				.setPadding(ResourceUtils.dip2px(mContext, ResourceUtils
						.getXmlDef(mContext, R.dimen.medium_margin)),
						ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
								mContext, R.dimen.small_margin)), ResourceUtils
								.dip2px(mContext, ResourceUtils.getXmlDef(
										mContext, R.dimen.medium_margin)),
						ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
								mContext, R.dimen.small_margin)));
		rightButton
				.setTextColor(getResources().getColor(android.R.color.black));
		back_number = (Button) this.findViewById(R.id.back_number);
		back_number.setOnClickListener(this);
		init();
	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return;
		}
		int enter_type = bundle.getInt("enterType");
		info = (Record_Info) bundle.get("record");
		if (enter_type == 0) {
			title.setText("预约详情");
			rightButton.setOnClickListener(this);
		} else if (enter_type == 2) {
			title.setText("就诊详情");
			rightButton.setVisibility(View.GONE);
			buttonLayout.setVisibility(View.VISIBLE);
		} else if (enter_type == 4) {
			title.setText("退号详情");
			rightButton.setVisibility(View.GONE);
		}
		if (info == null) {
			return;
		}
		if (info.getPhoto() != null) {
			final Bitmap defaultBit = BitmapFactory.decodeResource(
					getResources(), R.drawable.default_doc_img);

			BitmapUtils bitmapUtils = new BitmapUtils(OrderDetailActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
					.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(defaultBit);
			bitmapUtils.configDefaultLoadFailedImage(defaultBit);
			bitmapUtils
					.display(head_pic, EZTConfig.DOC_PHOTO + info.getPhoto());
		}
		doctorName.setText(info.getDoctorName());
		d_title.setText(EztDictionaryDB.getInstance(this).getLabelByTag(
				"doctorLevel", info.getDoctorLevel() + ""));
		hospital.setText(info.getHospital());
		dept.setText(info.getDept());
		personName.setText(info.getPatientName());
		idCard.setText(info.getIdCard());
		phone.setText(info.getPhone());
		String discribe = info.getDiscribe();
		if (TextUtils.isEmpty(discribe)) {
			discribe = "暂无描述";
		}
		illnessDescribe.setText(discribe);
		// if (info.getPayType() == 0) {
		// payType.setText("自费");
		// } else {
		// payType.setText("医保");
		// }

		payType.setText("到院缴费");
		String beginTime = info.getBeginTime();
		String endTime = info.getEndTime();
		String date = info.getDate();
		try {
			String time = date.substring(0, date.indexOf(" "))
					+ " "
					+ beginTime.substring(beginTime.indexOf(" ") + 1,
							beginTime.lastIndexOf(":"))
					+ "-"
					+ endTime.substring(endTime.indexOf(" ") + 1,
							endTime.lastIndexOf(":"));
			seeTime.setText(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 时间处理
	 */
	public void dateDispose() {
		String beginTime = info.getBeginTime();
		String endTime = info.getEndTime();
		String date = info.getDate();
	}

	@OnClick(R.id.evaluate)
	public void evaluateClick(View v) {
		Intent intent = new Intent();

		intent.setClass(this, EvaluateActivity.class);
		intent.putExtra("record", info);
		startActivity(intent);

	}

	@OnClick(R.id.writeLetter)
	public void writeLetterClick(View v) {
		Intent intent = new Intent();
		intent.setClass(this, WriteLetterActivity.class);
		intent.putExtra("record", info);
		startActivity(intent);
	}

	@OnClick(R.id.checkillRecord)
	public void checkillRecordClick(View v) {
	}

	@OnClick(R.id.secondOrder)
	public void secondOrderClick(View v) {
		Intent intent = new Intent();
	}

	@OnClick(R.id.right_btn1)
	public void right_btn1Click(View v) {
		Intent intent = new Intent();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_number:
			backNumber2(info.getPlatformId() + "", info.getId() + "");
		}
	}

	/**
	 * 退号
	 * @param pfId
	 * @param regId
	 */
	public void backNum(final String pfId, final String regId) {

		AlertDialog.Builder ab = new AlertDialog.Builder(this);
//		ab.setTitle("退号操作");
		ab.setTitle("取消订单");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setPositiveButton("退号", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp_regId = regId;
				temp_pfId = pfId;
				validateDialog = new BackValidateDialog(
						OrderDetailActivity.this, info.getId(),
						BaseApplication.patient.getUserId());
				validateDialog.setSure(OrderDetailActivity.this);
				validateDialog.show();

			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp_regId = "";
				temp_pfId = "";
			}
		});
		ab.create().show();
	}
	private void backNumber2(final String pfId, final String regId) {
		final MyDialog dialog = new MyDialog(mContext, "确定", "取消", "取消订单", null);
		dialog.setDialogCancle(new DialogCancle() {

			@Override
			public void dialogCancle() {
				temp_regId = "";
				temp_pfId = "";
				dialog.dissMiss();
			}
		});
		
		dialog.setDialogSure(new DialogSure() {
			
			@Override
			public void dialogSure() {
				temp_regId = regId;
				temp_pfId = pfId;
				validateDialog = new BackValidateDialog(
						OrderDetailActivity.this, info.getId(),
						BaseApplication.patient!=null?BaseApplication.patient.getUserId():0);
				validateDialog.setSure(OrderDetailActivity.this);
				validateDialog.show();
				dialog.dissMiss();
				
			}
		});
		dialog.show();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		boolean flag=false;
		if(object[1]!=null){
			flag= (Boolean) object[1];
		}
		if (!flag) {
			if (taskID == HttpParams.BACKNUMBER) {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
			}
			if(object[2]!=null){
				Toast.makeText(getApplicationContext(), object[2].toString(),
						Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (taskID == HttpParams.BACKNUMBER) {
			String msg;
			Map<String, Object> map = (Map<String, Object>) object[2];
			boolean f=false;
			if(null!=map&&null!=map.get("flag"))
					f= (Boolean) map.get("flag");
			if (f) {
				msg = "退号成功";
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				// EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
				// "msgId = " + info.getId()); // 退号删除相应的预约提醒消息
				WhereBuilder b = WhereBuilder.b("msgId", "=", info.getId());
				EztDb.getInstance(mContext).delDataWhere(new MessageAll(), b); // 退号删除相应的预约提醒消息
				setResult(1);
				finish();
			} else {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				if(null!=map.get("msg"))
					msg = map.get("msg").toString();
				else
					msg="退号失败，请您稍后再试";
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	@Override
	public void onSureClick(String valideCode) {
		if (valideCode.isEmpty()) {
			Toast.makeText(OrderDetailActivity.this, "请输入验证码",
					Toast.LENGTH_SHORT).show();
		} else {
			validateDialog.dismiss();
			if(null!=BaseApplication.patient){
				RegistratioImpl impl = new RegistratioImpl();
				RequestParams params = new RequestParams();
				params.addBodyParameter("regId", temp_regId);
				params.addBodyParameter("pfId", temp_pfId);
				params.addBodyParameter("mobile",
						BaseApplication.patient.getEpMobile());
				params.addBodyParameter("code", valideCode);
				params.addBodyParameter("userId",
						"" + BaseApplication.patient.getUserId());// 2015-12-30
				// 退号时候要求加
				// 用户id
				impl.backNumber(params, OrderDetailActivity.this);
				showProgressToast();
			}else{
				HintToLogin(Constant.LOGIN_COMPLETE);
			}
		}
	}
}

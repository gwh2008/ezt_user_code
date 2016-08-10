package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.RegistratioImpl;

/**
 * @title 写感谢信
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class WriteLetterActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.head_pic)
	private RoundImageView doctorPhoto;
	@ViewInject(R.id.d_name)
	private TextView doctorName;
	@ViewInject(R.id.d_title)
	private TextView d_title;
	@ViewInject(R.id.hos_name)
	private TextView hospital;
	@ViewInject(R.id.dept)
	private TextView dept;
	@ViewInject(R.id.letterContent)
	private EditText letterContent;
	@ViewInject(R.id.submit)//, click = "onClick"
	private TextView submit;
	private Record_Info info;
	private String thanksType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thanksletter);
		ViewUtils.inject(WriteLetterActivity.this);
		loadTitleBar(true, "写感谢信", null);
		init();
	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return;
		}
		thanksType = bundle.getString("thanksType");
		info = (Record_Info) bundle.get("record");
		if (info == null) {
			return;
		}
		if (info.getPhoto() != null) {
			final Bitmap defaultBit = BitmapFactory.decodeResource(
					getResources(), R.drawable.default_doc_img);
			
			BitmapUtils bitmapUtils=new BitmapUtils(WriteLetterActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		        bitmapUtils.configDefaultLoadingImage(defaultBit);
			    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
			    bitmapUtils.display(doctorPhoto, EZTConfig.DOC_PHOTO + info.getPhoto());
//			FinalBitmap.create(this).display(doctorPhoto,
//					EZTConfig.DOC_PHOTO + info.getPhoto(), defaultBit);
		}
		doctorName.setText(info.getDoctorName());
		d_title.setText(EztDictionaryDB.getInstance(this).getLabelByTag(
				"doctorLevel", info.getDoctorLevel()+""));
		hospital.setText(info.getHospital());
		dept.setText(info.getDept());
	}
	
//	public void onClick(View v) {
		@OnClick(R.id.submit)
		private  void click(View v) {
		String content = letterContent.getText().toString();
		if (!TextUtils.isEmpty(content)) {
			if (content.trim().length() >= 6) {
				postLetter(content);
			} else {
				Toast.makeText(getApplicationContext(), "请填写不少于6个字",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "感谢信不能为空",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void postLetter(String content) {
		if (BaseApplication.patient == null) {
			return;
		}
		RegistratioImpl impl = new RegistratioImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("deptId", info.getDeptId() + "");
		params.addBodyParameter("doctorId", info.getDoctorId() + "");
		params.addBodyParameter("patientId", BaseApplication.patient.getId() + "");
		params.addBodyParameter("tnSignature", thanksType);
		params.addBodyParameter("tnContent", content);
		params.addBodyParameter("registerId", info.getId() + "");
		impl.writeThanksLetter(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		String msg;
		if (object == null) {
			Toast.makeText(mContext, "服务器繁忙", Toast.LENGTH_SHORT).show();
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			Toast.makeText(mContext, "添加异常", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> addMap = (Map<String, Object>) obj[2];
		if (addMap == null || addMap.size() == 0) {
			Toast.makeText(mContext, "添加异常", Toast.LENGTH_SHORT).show();
			return;
		}
		boolean modifyFlag = (Boolean) addMap.get("flag");
		if (modifyFlag) {
			msg = "添加成功";
			finish();
		} else {
			msg = (String) addMap.get("msg");
		}
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
}

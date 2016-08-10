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
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
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
 * @title 写评价
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class EvaluateActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.d_name)
	private TextView doctorName;
	@ViewInject(R.id.d_title)
	private TextView d_title;
	@ViewInject(R.id.hos_name)
	private TextView hospital;
	@ViewInject(R.id.dept)
	private TextView dept;
	@ViewInject(R.id.evaluateContent)
	private EditText evaluateContent;
	@ViewInject(R.id.submit)//, click = "onClick"
	private TextView submit;
	@ViewInject(R.id.serverAttitude)
	private RatingBar serverAttitude;
	@ViewInject(R.id.medicalEffect)
	private RatingBar medicalEffect;
	
	@ViewInject(R.id.head_pic)
	private RoundImageView doctorPhoto;

	private Record_Info info;
	private float sCount = 0f;
	private float mCount = 0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.evaluate);
		ViewUtils.inject(EvaluateActivity.this);
		loadTitleBar(true, "评价", null);
		init();
	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			return;
		}
		int enter_type = bundle.getInt("enterType");
		info = (Record_Info) bundle.get("record");
		if (info == null) {
			return;
		}
		
		if (info.getPhoto() != null) {
			final Bitmap defaultBit = BitmapFactory.decodeResource(
					getResources(), R.drawable.default_doc_img);
			
			BitmapUtils bitmapUtils=new BitmapUtils(EvaluateActivity.this);
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
		serverAttitude
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						sCount = rating;
						if (rating <= 1.0f) {
							ratingBar.setRating(1.0f);
							sCount = 1.0f;
						}
					}
				});
		medicalEffect
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						mCount = rating;
						if (rating <= 1.0f) {
							ratingBar.setRating(1.0f);
							mCount = 1.0f;
						}
					}
				});
	}

//	public void onClick(View v) {
	
	@OnClick(R.id.submit)
	public void click(View v) {
		String content = evaluateContent.getText().toString();
		if (content == null) {
			content = "";
		}
		if (judgeData()) {
			writeEvaluate(content);
		}
	}

	/**
	 * 判断数据
	 */
	public boolean judgeData() {
		if (sCount < 1.0f) {
			Toast.makeText(getApplicationContext(), "请对服务态度打分",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (mCount < 1.0f) {
			Toast.makeText(getApplicationContext(), "请对医术疗效打分",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 写评价
	 */
	public void writeEvaluate(String coutent) {
		if (BaseApplication.patient == null) {
			return;
		}
		RegistratioImpl impl = new RegistratioImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("deptId", info.getDeptId() + "");
		params.addBodyParameter("doctorId", info.getDoctorId() + "");
		params.addBodyParameter("patientId", BaseApplication.patient.getId() + "");
		params.addBodyParameter("registerId", info.getId() + "");
		params.addBodyParameter("serviceStars", sCount + "");
		params.addBodyParameter("effectStars", mCount + "");
		params.addBodyParameter("aeType", "1");
		params.addBodyParameter("aeObjectId", info.getId() + "");
		params.addBodyParameter("content", coutent);
		impl.writeEvaluate(params, this);
		showProgressToast();
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
		Map<String, Object> addMap = (Map<String, Object>) obj[2];
		if (addMap == null || addMap.size() == 0) {
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

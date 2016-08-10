package com.eztcn.user.eztcn.activity.home;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.bean.PhoneRecordBean;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;

/**
 * @title 电话医生记录详情
 * @describe
 * @author ezt
 * @created 2015年2月6日
 */
public class PhoneRecordDetailActivity extends FinalActivity {

	@ViewInject(R.id.title_tv)
	private TextView title;
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
	@ViewInject(R.id.evaluateLayout)
	private RelativeLayout evaluateLayout;
	@ViewInject(R.id.evaluate)
	// , click = "onClick"
	private TextView evaluate;// 去评价
	@ViewInject(R.id.callAgain)
	// , click = "onClick"
	private TextView callAgain;// 再次通话
	@ViewInject(R.id.callTime)
	private TextView callTime;
	@ViewInject(R.id.timeCount)
	private TextView timeCount;// 通话时长
	// @ViewInject(R.id.phoneNumber)
	// private TextView phoneNumber;
	@ViewInject(R.id.illrecord)
	private TextView illrecord;
	@ViewInject(R.id.moneyLayout)
	private LinearLayout moneyLayout;
	@ViewInject(R.id.callMoney)
	private TextView callMoney;
	@ViewInject(R.id.phoneTimeLabel)
	private TextView phoneTimeLabel;

	private PhoneRecordBean record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonerecorddetail);
		ViewUtils.inject(PhoneRecordDetailActivity.this);
		loadTitleBar(true, "预约详情", null);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			record = (PhoneRecordBean) savedInstanceState
					.getSerializable("record");
		}
		init();
	}

	public void init() {
		if (record == null) {
			return;
		}
		int status = record.getCallStatus();
		switch (status) {
		case 0:
			title.setText("预约详情");
			phoneTimeLabel.setText("预约时间：");
			callTime.setText(record.getBeginTime());
			timeCount.setText("0分钟");
			break;
		case 4:
			title.setText("通话详情");
			phoneTimeLabel.setText("通话时间：");
			evaluateLayout.setVisibility(View.VISIBLE);
			moneyLayout.setVisibility(View.VISIBLE);
			timeCount.setText(record.getCall_minute() + "分钟");
			double d = record.getEztCurrency();
			int eztCurrency = 0;
			if (d != 0) {
				eztCurrency = (int) Math.floor(d);
			}
			callMoney.setText(eztCurrency + "个");
			String beginTime = record.getBeginTime();
			String endTime = record.getEndTime();
			callTime.setText(beginTime + "-"
					+ endTime.substring(endTime.indexOf(" ")));
			break;
		default:
			title.setText("已关闭");
			moneyLayout.setVisibility(View.VISIBLE);
			callTime.setText(record.getBeginTime());
			timeCount.setText("0分钟");
			double d2 = record.getEztCurrency();
			int eztCurrency2 = 0;
			if (d2 != 0) {
				eztCurrency = (int) Math.floor(d2);
			}
			callMoney.setText(eztCurrency2 + "个");
			break;
		}
		doctorName.setText(record.getDoctorName());
		Integer docLevel = record.getDoctorLevel();
		if (docLevel != null) {
			d_title.setText(EztDictionaryDB.getInstance(this).getLabelByTag(
					"doctorLevel", docLevel + ""));
		}
		hospital.setText(record.getHospital());
		dept.setText(record.getDept());

		// phoneNumber.setText(record.getReceivePhone());

		String str = record.getPhoto();
		if (!TextUtils.isEmpty(str)) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_doc_img);

			BitmapUtils bitmapUtils = new BitmapUtils(
					PhoneRecordDetailActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils
					.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.configDefaultLoadFailedImage(bitmap);
			bitmapUtils.display(doctorPhoto,
					EZTConfig.DOC_PHOTO + record.getPhoto());
			// FinalBitmap.create(this).display(doctorPhoto,
			// EZTConfig.DOC_PHOTO + record.getPhoto(), bitmap);
		}

	}

	@OnClick(R.id.callAgain)
	public void callAgainClick(View v) {
	}

	@OnClick(R.id.evaluate)
	public void evaluateClick(View v) {
	}

	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.evaluate:
	//
	// break;
	// case R.id.callAgain:
	//
	// break;
	// }
	// }
}

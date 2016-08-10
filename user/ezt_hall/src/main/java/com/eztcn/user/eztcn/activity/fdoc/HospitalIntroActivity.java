package com.eztcn.user.eztcn.activity.fdoc;

import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 医院介绍
 * @describe
 * @author ezt
 * @created 2015年4月24日
 */
public class HospitalIntroActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.hosPhoto)
	private ImageView hosPhoto;
	@ViewInject(R.id.hosAddr)
	private TextView hosAddr;
	@ViewInject(R.id.hosTel)
	private TextView hosTel;
	@ViewInject(R.id.intro_content)
	private TextView intro_content;

	private Hospital hospital;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospitalintro);
		ViewUtils.inject(HospitalIntroActivity.this);
		loadTitleBar(true, "", null);
		hosPhoto.getLayoutParams().height = getWindowHeight() / 3;
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			hospital = (Hospital) savedInstanceState
					.getSerializable("hospital");
		}
		getHospitalDetail();
	}

	public void init() {
		if (hospital == null) {
			return;
		}

		title.setText(hospital.gethName());
		hosAddr.setText(hospital.gethAddress());
		hosTel.setText(hospital.gethTel());
		if (null != hospital.gethIntro())
			intro_content.setText(Html.fromHtml(hospital.gethIntro()));
		// Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
		// R.drawable.ads_default);
		// String photo = EZTConfig.HOS_PHOTO + "hosView" + hospital.getId()
		// + ".jpg";
		// BitmapUtils bitmapUtils=new BitmapUtils(HospitalIntroActivity.this);
		//
		// bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		// bitmapUtils.configDefaultLoadingImage(defaultBit);
		// bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		// bitmapUtils.display(hosPhoto, photo);
		// // FinalBitmap.create(mContext).display(hosPhoto, photo, defaultBit);
	}

	public void getHospitalDetail() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("hospitalId", String.valueOf(hospital.getId()));

		new HospitalImpl().getHospitalDetail(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_HOS_DETAIL:// 获取医生详情
			if (isSucc) {
				Map<String, Object> detailMap = (Map<String, Object>) object[2];
				if (detailMap == null || detailMap.size() == 0) {
					return;
				}
				boolean flag = (Boolean) detailMap.get("flag");
				if (flag) {
					hospital = (Hospital) detailMap.get("hospital");
					init();
				} else {
					Toast.makeText(mContext, detailMap.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}
}

package com.eztcn.user.eztcn.activity.fdoc;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.MapRimHosActivity;
import com.eztcn.user.eztcn.activity.home.AppointCheckInActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity;
import com.eztcn.user.eztcn.activity.home.DoctorList30Activity;
import com.eztcn.user.eztcn.activity.home.DoctorListActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.AttentionImpl;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 医院详情
 * @describe
 * @author ezt
 * @created 2015年4月23日
 */
public class HospitalDetailActivity extends FinalActivity implements
		OnClickListener, IHttpResult {

	private TextView collect;// 收藏医院
	@ViewInject(R.id.hosPhoto)
	private ImageView hosPhoto;
	@ViewInject(R.id.hosName)
	private TextView hosName;
	@ViewInject(R.id.hosAddr)
	private TextView hosAddr;
	@ViewInject(R.id.intro)//, click = "onClick"
	private LinearLayout intro;// 医院介绍
	@ViewInject(R.id.introImg)
	private ImageView introImg;
	@ViewInject(R.id.deptDoctor)//, click = "onClick"
	private LinearLayout deptDoctor;// 科室医生
	@ViewInject(R.id.deptImg)
	private ImageView deptImg;
	@ViewInject(R.id.order)//, click = "onClick"
	private LinearLayout order;// 预约挂号
	@ViewInject(R.id.orderImg)
	private ImageView orderImg;
//	@ViewInject(R.id.orderRegiste)//, click = "onClick"
//	private LinearLayout orderRegiste;// 预约登记
//	@ViewInject(R.id.regImg)
//	private ImageView regImg;
	@ViewInject(R.id.mapNav)//, click = "onClick"
	private Button mapNav;// 地图导航
	@ViewInject(R.id.tvPrompt)//2015-12-18医院对接数据
	private TextView tvPrompt;
	private final int WARING=0;//2015-12-18医院对接 对接的医院状态为0
	
	private Hospital hospital;
	private String hosId;
	private boolean isCollect;
	private String collectId;// 收藏id

	private boolean isDraw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospitaldetail);
		ViewUtils.inject(HospitalDetailActivity.this);
		collect = loadTitleBar(true, "医院详情", "收藏");
		collect.setOnClickListener(this);
		collect.setEnabled(false);
		intro.setEnabled(false);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			hospital = (Hospital) savedInstanceState
					.getSerializable("hospital");
			hosId = hospital.getId() + "";
		}
		initImg();
		getHospitalDetail();
		// init();
		// checkAttentState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkAttentState();
	}

	public void init() {
		if (hospital == null) {
			return;
		}
		hosName.setText(hospital.gethName());
		String addr = hospital.gethAddress();
		hosAddr.setText(TextUtils.isEmpty(addr) ? "地址未知" : hospital
				.gethAddress());
		hosId = hospital.getId() + "";
		Bitmap defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.ads_default);
		String photo = EZTConfig.HOS_PHOTO + "hosView" + hospital.getId()
				+ ".jpg";
		//2015-12-18医院对接中
		RelativeLayout.LayoutParams hosAddrParams=(LayoutParams) hosAddr.getLayoutParams();
		if(hospital.getEhDockingStatus()==WARING){
			String promptStr=hospital.getEhDockingStr();
			if(StringUtils.isNotEmpty(promptStr)){
				tvPrompt.setText(promptStr);
				tvPrompt.setVisibility(View.VISIBLE);
			}
		}else{
			hosAddrParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		}
		
		
		BitmapUtils bitmapUtils=new BitmapUtils(HospitalDetailActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
        bitmapUtils.configDefaultLoadingImage(defaultBit);
	    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	    bitmapUtils.display(hosPhoto, photo);
//		FinalBitmap.create(mContext).display(hosPhoto, photo, defaultBit);
	}

	public void initImg() {
		int height = intro.getMeasuredHeight();
		int imgHeight = introImg.getMeasuredHeight();
		if (imgHeight > height - 20) {

		}
		ViewTreeObserver observer = intro.getViewTreeObserver();
		observer.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (!isDraw) {
					int height = intro.getMeasuredHeight();
					introImg.getLayoutParams().height = height / 2;
					deptImg.getLayoutParams().height = height / 2;
					orderImg.getLayoutParams().height = height / 2;
//					regImg.getLayoutParams().height = height / 2;
				}
				isDraw = true;
				return true;
			}
		});
	}

	/**
	 * 获取医院详情
	 */
	public void getHospitalDetail() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.addBodyParameter("hospitalId", hosId);
		RequestParams params = new RequestParams();
		params.addBodyParameter("hospitalId", hosId);
		
		new HospitalImpl().getHospitalDetail(params, this);
		showProgressToast();
	}
	@OnClick(R.id.intro)
	private void hosIntroClick(View v){
		// 医院介绍
		
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		startActivity(new Intent(this, HospitalIntroActivity.class)
				.putExtra("hospital", hospital));
	}
	@OnClick(R.id.deptDoctor)
	private void doctorClick(View v){
		// 科室医生
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		startActivity(new Intent(this, ChoiceDeptByHosActivity.class)
				.putExtra("hosId", hosId)
				.putExtra("hosName", hospital.gethName())
				.putExtra("isHosDetail", true));
	}
	
	@OnClick(R.id.order)
	private void orderClick(View v){
		// 预约挂号
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		startActivity(new Intent(this, DoctorList30Activity.class)
				.putExtra("hosId", hosId)
				.putExtra("hosName", hospital.gethName())
				.putExtra("isAllSearch", true).putExtra("type", 1));
	}
	@OnClick(R.id.mapNav)
	private void mapNavClick(View v){
		// 跳转地图
		startActivity(new Intent(this, MapRimHosActivity.class)
		.putExtra("lat", hospital.getLat())
		.putExtra("lon", hospital.getLon())
		.putExtra("hosName", hosName.getText().toString())
		.putExtra("hosTel", hospital.gethTel())
		.putExtra("hosAds", hospital.gethAddress()));
	}

	public void onClick(View v) {
		if(collect==v){
			// 收藏医院
			if (BaseApplication.getInstance().isNetConnected) {
				collectHospital();
			} else {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}
		}
		}
	/**
	 * 检查关注状态
	 */
	private void checkAttentState() {
		if (BaseApplication.patient != null) {
//			HashMap<String, Object> params1 = new HashMap<String, Object>();
			RequestParams params1=new RequestParams();
			params1.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
			params1.addBodyParameter("hospitalId", hosId);
			new AttentionImpl().getAttentHosState(params1, this);
			showProgressToast();
		} else {
			collect.setEnabled(true);
		}
	}

	/**
	 * 收藏医院
	 */
	public void collectHospital() {
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}
		if (isCollect) {// 取消收藏
			RequestParams params=new RequestParams();
			params.addBodyParameter("id", collectId);
			new AttentionImpl().cancelAttentHos(params, this);
		} else {
			RequestParams params=new RequestParams();
			params.addBodyParameter("hospitalId", hosId);
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
			new AttentionImpl().attentHos(params, this);
		}
		showProgressToast();
		collect.setEnabled(false);
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		intro.setEnabled(true);
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
		case HttpParams.ATTENT_DOC:// 关注医生
			collect.setEnabled(true);
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						collectId = (String) map.get("id");
						collect.setText("已收藏");
						isCollect = flag;
					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			return;

		case HttpParams.CANCEL_ATTENT_DOC:// 取消关注
			collect.setEnabled(true);
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {// 成功
						collect.setText("收藏");
						isCollect = false;
					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}
				}

			} else {
				Toast.makeText(mContext, object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}
			return;

		case HttpParams.GET_ATTENT_DOC_STATE:// 获取关注状态
			collect.setEnabled(true);
			if (isSucc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						collectId = (String) map.get("id");
						collect.setText("已收藏");
						// collect.setEnabled(false);
					} else {
						collect.setText("收藏");
					}
					isCollect = flag;
				}

			} else {
				Logger.i("获取关注状态", object[3]);
			}
			break;
		}
	}

}

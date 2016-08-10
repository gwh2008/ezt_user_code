package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.CentreIntroActivity;
import com.eztcn.user.eztcn.activity.home.PatientExchangeActivity;
import com.eztcn.user.eztcn.activity.mine.ShoppingCarActivity;
import com.eztcn.user.eztcn.adapter.ChatPagerAdapter;
import com.eztcn.user.eztcn.adapter.Tumour_ProjectListAdapter;
import com.eztcn.user.eztcn.adapter.Tumour_ProjectListAdapter.Ionclick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Tumour;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 外地患者服务
 * @describe
 * @author ezt
 * @created 2015年2月27日
 */
public class ForeignPatientActivity extends FinalActivity implements
		OnClickListener, IHttpResult {

	@ViewInject(R.id.quick_help_tv)
	// , click = "onClick"
	private TextView tvQuickHelp;// 快速求助

	@ViewInject(R.id.quick_tel_tv)
	// , click = "onClick"
	private TextView tvQuickTel;// 肿瘤服务电话

	// @ViewInject(R.id.home_img_scroll)
	// private MyImgScroll imgScroll;// 广告宣传图

	// @ViewInject(R.id.home_loading_img)
	// private ImageView adsLoadImg;// 未加载网络图片时显示图片

	// @ViewInject(R.id.home_point_layout)
	// private LinearLayout pointLayout;// 原点标记

	@ViewInject(R.id.top_img)
	// , click = "onClick"
	private ImageView imgTop;// 宣传图片

	@ViewInject(R.id.project_hor_pager)
	private ViewPager vPager;// 肿瘤服务项目横向滚动

	@ViewInject(R.id.quick_help_tv)
	private TextView tvInside;// 院内服务说明

	@ViewInject(R.id.quick_help_tv)
	private TextView tvoutSide;// 院外服务说明

	@ViewInject(R.id.quick_help_tv)
	private TextView tvInHos;// 住院服务说明

	@ViewInject(R.id.recovery_case_tv)
	// , click = "onClick"
	private TextView tvRecoveryCase;// 康复病例

	@ViewInject(R.id.patient_exchange_tv)
	// , click = "onClick"
	private TextView tvFriends;// 患友交流

	// @ViewInject(R.id.mail_case_tv, click = "onClick")
	// private TextView tvMailCase;

	private Intent intent = null;

	/*
	 * 肿瘤服务项目科室
	 */
	private int vPagerNum = 0;// vPager页数
	private GridView[] gvViews;
	private Tumour_ProjectListAdapter[] tAdapters;
	private Tumour_ProjectListAdapter adapter;

	private ImageView imgShop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foreign_patient);
		ViewUtils.inject(ForeignPatientActivity.this);
		imgShop = loadTitleBar(true, "外地患者服务", R.drawable.shopping_icon);

		imgShop.setOnClickListener(this);
		initialTumourData();
	}

	@OnClick(R.id.quick_help_tv)
	private void helpClick(View v) {// 快速求助
		intent = new Intent();
		intent.setClass(mContext, ForeignPatient_QuickHelpActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.recovery_case_tv)
	private void caseClick(View v) {// 康复病历
		intent = new Intent();
		intent.setClass(mContext, ForeignPatient_RecoveryCaseActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.quick_tel_tv)
	private void telClick(View v) {
		// 肿瘤服务电话
		hintTelDialog("022-23377545", "拨打肿瘤服务电话？");
	}

	@OnClick(R.id.top_img)
	private void topImgClick(View v) {
		intent = new Intent();// 宣传图点击跳转
		intent.setClass(mContext, CentreIntroActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.patient_exchange_tv)
	private void exchangeClick(View v) {
		// 患友交流
		intent = new Intent();// 宣传图点击跳转
		intent.setClass(mContext, PatientExchangeActivity.class);
		startActivity(intent);
	}


	@Override
	public void onClick(View v) {
		// intent = new Intent();
		// switch (v.getId()) {
		// case R.id.quick_help_tv:// 快速求助
		// intent.setClass(mContext, ForeignPatient_QuickHelpActivity.class);
		// break;
		//
		// // case R.id.mail_case_tv:// 邮寄病历
		// // intent.setClass(mContext, ForeignPatient_MailCaseActivity.class);
		// // break;
		//
		// case R.id.recovery_case_tv:// 康复病历
		// intent.setClass(mContext, ForeignPatient_RecoveryCaseActivity.class);
		// break;
		//
		// case R.id.quick_tel_tv:// 肿瘤服务电话
		// hintTelDialog("022-23377545", "拨打肿瘤服务电话？");
		// break;
		//
		// case R.id.top_img:// 宣传图点击跳转
		// intent.setClass(mContext, CentreIntroActivity.class);
		// break;
		//
		// case R.id.patient_exchange_tv:// 患友交流
		// intent.setClass(mContext, PatientExchangeActivity.class);
		//
		// break;
		//
		// // case R.id.inside_service_layout:// 院内服务
		// // intent.setClass(mContext,
		// // ForeignPatient_InsideServiceActivity.class);
		// // break;
		//
		// // case R.id.outside_service_layout:// 院外服务
		// // intent = null;
		// // break;
		//
		// // case R.id.hos_service_layout:// 住院服务
		// // intent = null;
		// // break;
		//
		// default:// 购物车
		if (imgShop == v) {
			if (BaseApplication.getInstance().patient != null) {
				startActivity(new Intent(mContext, ShoppingCarActivity.class));
			} else {
				HintToLogin(1);
			}
			intent = null;
		}
	}

	/**
	 * 获取肿瘤服务项目
	 */
	private void initialTumourData() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("hospitalId", "94");
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getProjectList(params, this);
		showProgressToast();
	}
	/**
	 * 肿瘤服务项目
	 */
	private void initialhorVPagerData(ArrayList<Tumour> list) {
		vPagerNum = list.size() / 6;
		if (list.size() % 6 != 0) {
			vPagerNum += 1;
		}
		tAdapters = new Tumour_ProjectListAdapter[vPagerNum];
		gvViews = new GridView[vPagerNum];
		for (int i = 0; i < vPagerNum; i++) {
			int startNum = i * 6;
			int endNum = (i + 1) * 6;

			gvViews[i] = createGridView(list, startNum, endNum);
			tAdapters[i] = adapter;
		}
		vPager.setAdapter(new ChatPagerAdapter(gvViews));
		vPager.setCurrentItem(0);
	}

	/**
	 * 横向滑动gridview 创建
	 * 
	 * @param dList
	 * @return
	 */
	private GridView createGridView(ArrayList<Tumour> dList, int startNum,
			int endNum) {
		final GridView view = new GridView(this);
		ArrayList<Tumour> list = new ArrayList<Tumour>();
		if (endNum > dList.size()) {
			endNum = dList.size();
		}

		for (int i = startNum; i < endNum; i++) {
			list.add(dList.get(i));
		}
		adapter = new Tumour_ProjectListAdapter(mContext);
		adapter.setList(list);
		adapter.AdapterOnclick(click);
		view.setAdapter(adapter);
		view.setNumColumns(3);
		view.setSelector(android.R.color.transparent);
		view.setBackgroundResource(android.R.color.white);
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setGravity(Gravity.CENTER);
		return view;
	}

	Ionclick click = new Ionclick() {

		@Override
		public void adapterOnclick(int pos) {
			Tumour t = tAdapters[vPager.getCurrentItem()].getList().get(pos);
			String deptId = t.getId();

			startActivity(new Intent(mContext,
					TumourServicePackageActivity.class).putExtra("deptId",
					deptId));
		}
	};
	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					ArrayList<Tumour> list = (ArrayList<Tumour>) map
							.get("data");
					initialhorVPagerData(list);
				} else {
					Toast.makeText(mContext, map.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
		hideProgressToast();
	}
}

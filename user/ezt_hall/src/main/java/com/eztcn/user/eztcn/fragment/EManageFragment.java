package com.eztcn.user.eztcn.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.Activity_Dargon;
import com.eztcn.user.eztcn.activity.home.CallENurseActivity;
import com.eztcn.user.eztcn.activity.home.ENurseHelpActivity;
import com.eztcn.user.eztcn.activity.mine.MyHealthCardActivity;
import com.eztcn.user.eztcn.customView.MyViewPager;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title e护帮
 * @describe
 * @author ezt
 * @created 2016年1月8日
 */
public class EManageFragment extends FinalFragment implements OnClickListener{
	private Activity mActivity;
	private View rootView;
	private MyViewPager eManageMsgVP;

	public static EManageFragment newInstance() {
		EManageFragment fragment = new EManageFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_emanage, null);
			initView();
		}

		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initView() {
		// 操作：点击进入【我的医管家】 需要登录
		TextView myEManageTV = (TextView) rootView
				.findViewById(R.id.eManageMyEManage);
		myEManageTV.setOnClickListener(this);
		// 广告 操作：点击进入【套餐详情】
		eManageMsgVP = (MyViewPager) rootView.findViewById(R.id.eManageMsgVP);
		eManageMsgVP.setVisibility(View.GONE);
		ImageView eManageAdImg = (ImageView) rootView
				.findViewById(R.id.eManageAdImg);
		eManageAdImg.setOnClickListener(this);
		// e护帮
		View eMangeEHelpLayout = rootView.findViewById(R.id.eMangeEHelpLayout);
		eMangeEHelpLayout.setOnClickListener(this);
		// 人人医管家
		View eMangeRenrenLayout = rootView
				.findViewById(R.id.eMangeRenrenLayout);
		eMangeRenrenLayout.setOnClickListener(this);
		// 银行医管家
		View eMangeBankLayout = rootView.findViewById(R.id.eMangeBankLayout);
		eMangeBankLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.eManageMyEManage: {
			// 操作：点击进入【我的医管家】
			// 说明：需做登录判断，未登录提示先登录（具体和预约挂号要求登录做法一样）
			if (null == BaseApplication.patient) {
				((FinalActivity) mActivity).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			startActivity(new Intent(mActivity, MyHealthCardActivity.class));
		}
			break;
		case R.id.eManageAdImg: {
			// 操作：点击进入【套餐详情】
			// 说明：广告图为一张，介绍医管家内容banner
			inDevloping();
		}
			break;

		case R.id.eMangeEHelpLayout: {
			// 操作：点击进入【e护帮】
			// 说明：整套移植，基本不变（线上v2.5.4，路径:e护帮整套）
			startActivity(new Intent(mActivity, ENurseHelpActivity.class));
		}
			break;

		case R.id.eMangeRenrenLayout: {
			// 操作：点击进入【人人医管家】
			// 说明：直接以售卖套餐卡的形式展示，类似“我要e护帮”进去的页面，数据来源和e护帮一样需要后台添加（线上v2.5.4，路径:e护帮>我要医护帮）
			startActivity(new Intent(mActivity, CallENurseActivity.class));

		}
			break;
		case R.id.eMangeBankLayout: {
			// 操作：点击进入【银行医管家】
			// 说明：直接图文展示医指通龙卡详情（线上v2.5.4，路径:首页>医指通龙卡）
			startActivity(new Intent(mActivity, Activity_Dargon.class));
		}
			break;
		}
	}

	private void inDevloping() {
		Toast.makeText(mActivity, mActivity.getString(R.string.function_hint),
				Toast.LENGTH_SHORT).show();
	}
}

package com.eztcn.user.eztcn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.fdoc.ForeignPatientActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalListActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.activity.home.AllSearchActivity;
import com.eztcn.user.eztcn.activity.home.DoctorListActivity;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;

/**
 * @title 找医
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class FDoctorFragment extends FinalFragment implements OnClickListener,
		OnTouchListener {
	private View rootView;// 缓存Fragment view
	private RelativeLayout layoutHos, layoutDept, layoutSelf,
			layoutForeignPatient;
	private TextView tvTitle;

	private LinearLayout findDocLayout, allSearchLayout;

	float lastY = 0;

	private Animation showAnim;

	
	public static FDoctorFragment newInstance() {
		FDoctorFragment fragment = new FDoctorFragment();
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_find_doc, null);
			initialView();
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	/**
	 * 初始化界面
	 */
	private void initialView() {
		layoutHos = (RelativeLayout) rootView.findViewById(R.id.find_doc_hos);// 按医院找
		layoutDept = (RelativeLayout) rootView.findViewById(R.id.find_doc_dept);// 按科室找
		layoutSelf = (RelativeLayout) rootView.findViewById(R.id.find_doc_self);// 自查
		layoutForeignPatient = (RelativeLayout) rootView
				.findViewById(R.id.foreign_patient);// 外患服务
		findDocLayout = (LinearLayout) rootView
				.findViewById(R.id.find_doc_hos_layout);

		allSearchLayout = (LinearLayout) rootView
				.findViewById(R.id.all_search_layout);// 全站搜索

		tvTitle = (TextView) rootView.findViewById(R.id.title_tv);// 标题
		tvTitle.setText("找医");
		layoutHos.setOnClickListener(this);
		layoutDept.setOnClickListener(this);
		layoutSelf.setOnClickListener(this);
		findDocLayout.setOnTouchListener(this);
		layoutForeignPatient.setOnClickListener(this);
		allSearchLayout.setOnClickListener(this);

		showAnim = new AnimationUtils().loadAnimation(getActivity(),
				R.anim.alpha_in);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all_search_layout:// 全站搜索
			startActivity(new Intent(getActivity(), AllSearchActivity.class));
			break;

		case R.id.find_doc_hos:// 按医院找
			startActivity(new Intent(getActivity(), HospitalListActivity.class));
			// String strHosName = SystemPreferences
			// .getString(EZTConfig.KEY_HOS_NAME);
			//
			// String strDeptName = SystemPreferences
			// .getString(EZTConfig.KEY_STR_DEPT);
			//
			// startActivity(new Intent(this.getActivity(),
			// DoctorListActivity.class)
			// .putExtra("type", 1)
			// .putExtra("hosName",
			// TextUtils.isEmpty(strHosName) ? "选择医院" : strHosName)
			// .putExtra(
			// "deptName",
			// TextUtils.isEmpty(strDeptName) ? "选择科室"
			// : strDeptName));// type为1为医院找，2为科室找
			break;

		case R.id.find_doc_dept:// 按科室找
			String deptName = SystemPreferences
					.getString(EZTConfig.KEY_DF_STR_DEPT);
			String deptId = SystemPreferences
					.getString(EZTConfig.KEY_DF_DEPT_ID);
			startActivity(new Intent(this.getActivity(),
					DoctorListActivity.class).putExtra("type", 2)
					.putExtra("deptName", deptName)
					.putExtra("deptTypeId", deptId));
			break;

		case R.id.find_doc_self:// 症状自查

			// Toast.makeText(getActivity(), getString(R.string.function_hint),
			// Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this.getActivity(),
					SymptomSelfActivity.class));
			break;

		case R.id.foreign_patient:// 外患服务
			// Toast.makeText(getActivity(), getString(R.string.function_hint),
			// Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getActivity(),
					ForeignPatientActivity.class));

			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = ev.getY();
			break;

		case MotionEvent.ACTION_UP:
			int distanceY = (int) Math.abs(ev.getY() - lastY);
			if (ev.getY() > lastY && distanceY > 50) {// 显示
				if (allSearchLayout.getVisibility() == View.GONE) {
					allSearchLayout.startAnimation(showAnim);
					allSearchLayout.setVisibility(View.VISIBLE);
				}

			} else if (ev.getY() < lastY && distanceY > 50) {// 隐藏
				if (allSearchLayout.getVisibility() == View.VISIBLE) {
					allSearchLayout.clearAnimation();
					allSearchLayout.setVisibility(View.GONE);
				}

			}

			break;

		}
		return true;
	}

}

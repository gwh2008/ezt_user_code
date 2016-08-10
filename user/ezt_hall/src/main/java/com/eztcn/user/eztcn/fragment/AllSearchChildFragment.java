package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.AllSearchActivity;
import com.eztcn.user.eztcn.adapter.AllSearchDeptAdapter;
import com.eztcn.user.eztcn.adapter.AllSearchHosAdapter;
import com.eztcn.user.eztcn.adapter.AllSearchKnowleLibAdapter;
import com.eztcn.user.eztcn.adapter.AllSearchProfessorAdapter;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.customView.MyListView;

/**
 * @title 自定义Fragment
 * @describe 全站搜索子栏目(全部)数据显示
 * @author ezt
 * @created 2015年2月5日
 */
public class AllSearchChildFragment extends FinalFragment implements OnClickListener {
	private AllSearchHosAdapter hosAdapter;
	private AllSearchDeptAdapter deptAdapter;
	private AllSearchProfessorAdapter proAdapter;
	private AllSearchKnowleLibAdapter libAdapter;

	private TextView tvHosNum, tvDeptNum, tvProfessorNum, tvKnowLibNum;
	private MyListView lvAllHos, lvAllDept, lvAllProfessor, lvAllKnowLib;
	private View view;
	private ScrollView scrollView1;
	private Activity mActivity;

	private ArrayList<Hospital> hosList;// 医院列表
	private ArrayList<Dept> deptList;// 科室列表
	private ArrayList<Doctor> docList;// 医生列表
	private ArrayList<Information> knowLibList;// 知识库列表

	public void setHosList(ArrayList<Hospital> hosList) {
		this.hosList = hosList;
	}

	public void setDeptList(ArrayList<Dept> deptList) {
		this.deptList = deptList;
	}

	public void setDocList(ArrayList<Doctor> docList) {
		this.docList = docList;
	}

	public void setKnowLibList(ArrayList<Information> knowLibList) {
		this.knowLibList = knowLibList;
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
		if (view == null) {
			view = LinearLayout.inflate(mActivity, R.layout.item_search_all,
					null);
			initialView();
		}
		hosAdapter.setList(hosList);
		deptAdapter.setList(deptList);
		proAdapter.setList(docList);
		libAdapter.setList(knowLibList);
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	/**
	 * 初始化
	 */
	private void initialView() {

		tvHosNum = (TextView) view.findViewById(R.id.item_search_title_hos);// 医院条数
		tvDeptNum = (TextView) view.findViewById(R.id.item_search_title_dept);// 科室条数
		tvProfessorNum = (TextView) view
				.findViewById(R.id.item_search_title_professor);// 专家条数
		tvKnowLibNum = (TextView) view
				.findViewById(R.id.item_search_title_knowlib);// 知识库条数
		tvHosNum.setOnClickListener(this);
		tvDeptNum.setOnClickListener(this);
		tvProfessorNum.setOnClickListener(this);
		tvKnowLibNum.setOnClickListener(this);

		lvAllHos = (MyListView) view.findViewById(R.id.item_search_hos_lv);
		lvAllDept = (MyListView) view.findViewById(R.id.item_search_dept_lv);
		lvAllProfessor = (MyListView) view
				.findViewById(R.id.item_search_professor_lv);
		lvAllKnowLib = (MyListView) view
				.findViewById(R.id.item_search_knowlib_lv);
		scrollView1= (ScrollView) view
				.findViewById(R.id.scrollView1);
		// 全部item 点击事件
		lvAllHos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((AllSearchActivity) mActivity).toDoctorList(hosAdapter
						.getList().get(position));

			}
		});

		lvAllDept.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String deptName = deptAdapter.getList().get(position)
						.getdName();
				int deptId = deptAdapter.getList().get(position).getId();
				String dHid = deptAdapter.getList().get(position).getdHosId();
				String hosName = deptAdapter.getList().get(position)
						.getdHosName();

				((AllSearchActivity) mActivity).toDoctorList(deptName, hosName,
						deptId, dHid);
			}
		});

		lvAllProfessor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				String docId = proAdapter.getList().get(position).getId();
//				String deptId1 = proAdapter.getList().get(position)
//						.getDocDeptId();
//				String deptDocId = proAdapter.getList().get(position)
//						.getDeptDocId();
//				((AllSearchActivity) mActivity).toDoctorInfo(deptId1, docId,
//						deptDocId);
				
				
				Doctor doctor=proAdapter.getList().get(position);
				String docId = doctor.getId();
				String deptId1 = doctor
						.getDocDeptId();
				String deptDocId = doctor
						.getDeptDocId();
				int ehDockingStatus=doctor.getEhDockingStatus();//2015-12-21 医院对接
				((AllSearchActivity) mActivity).toDoctorInfo(deptId1, docId,
						deptDocId,ehDockingStatus);

			}
		});

		lvAllKnowLib.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String infoId = libAdapter.getList().get(position).getId();
				((AllSearchActivity) mActivity).toInfomation(infoId);

			}
		});

		hosAdapter = new AllSearchHosAdapter(mActivity);
		deptAdapter = new AllSearchDeptAdapter(mActivity);
		proAdapter = new AllSearchProfessorAdapter(mActivity);
		libAdapter = new AllSearchKnowleLibAdapter(mActivity);
		lvAllHos.setAdapter(hosAdapter);
		lvAllDept.setAdapter(deptAdapter);
		lvAllProfessor.setAdapter(proAdapter);
		lvAllKnowLib.setAdapter(libAdapter);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.item_search_title_hos:// 医院
			((AllSearchActivity) mActivity).mPager.setCurrentItem(1);
			break;
		case R.id.item_search_title_dept:// 科室
			((AllSearchActivity) mActivity).mPager.setCurrentItem(2);
			break;
		case R.id.item_search_title_professor:// 医生
			((AllSearchActivity) mActivity).mPager.setCurrentItem(3);
			break;
		case R.id.item_search_title_knowlib:// 知识库
			((AllSearchActivity) mActivity).mPager.setCurrentItem(4);
			break;
		}

	}

}
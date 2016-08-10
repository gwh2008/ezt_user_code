package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.DoctorIndex30Activity;
import com.eztcn.user.eztcn.adapter.QuickAppointListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.SymptomQueryImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 疾病详情
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public class DiseaseDetailActivity extends FinalActivity implements
		 IHttpResult {

	@ViewInject(R.id.all_layout)
	private LinearLayout layoutAll;

	@ViewInject(R.id.intro_tv)
	private TextView tvIntro;// 概述

	@ViewInject(R.id.about_dept_tv)
	private TextView tvAboutDept;// 相关科室

	@ViewInject(R.id.zhenduan_tv)
	private TextView tvZhenDuan;// 诊断与鉴别

	@ViewInject(R.id.zhenduan_layout)
	private RelativeLayout layoutZhenDuan;

	@ViewInject(R.id.zhenduan_img)
	private ImageView img1;

	@ViewInject(R.id.baojian_tv)
	private TextView tvBaoJian;// 预防保健

	@ViewInject(R.id.baojian_layout)
	private RelativeLayout layoutBaoJian;

	@ViewInject(R.id.baojian_img)
	private ImageView img2;

	@ViewInject(R.id.progress)
	private ProgressBar progress;

	@ViewInject(R.id.detail_doc_lv)
	private MyListView lvDocle;// 推荐医生列表

	private int currentPage = 1;// 当前页数
	private int pageSize = 6;// 每页条数
	private QuickAppointListAdapter docAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_disease_detail);
		ViewUtils.inject(DiseaseDetailActivity.this);
		Diseases disease = (Diseases) getIntent().getSerializableExtra(
				"disease");
		loadTitleBar(true, disease.getdName(), null);
		initialView();

		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			initialData(disease.getId() + "");
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取数据
	 */
	private void initialData(String id) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("regDiseasesId", id);
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("regDiseasesId", id);
		new SymptomQueryImpl().getDiseaseDetails(params, this);
	}

	/**
	 * 初始化控件
	 */
	private void initialView() {
		// 推荐医生
		docAdapter = new QuickAppointListAdapter(this, 1);
		lvDocle.setAdapter(docAdapter);
	}

	/**
	 * 初始化view
	 */
	private void initial(String intro, String diagnose, String prevent,
			String nursing, String dept) {
		intro = intro.replaceAll("\r\n", "");

		diagnose = diagnose.replaceAll("\r\n", "");

		prevent = prevent.replaceAll("\r\n", "");

		nursing = nursing.replaceAll("\r\n", "");

		dept = dept.replaceAll("\r\n", "");

		tvIntro.setText(!TextUtils.isEmpty(intro) ? "\t\t" + intro : "暂无相关内容");

		tvZhenDuan.setText(!TextUtils.isEmpty(diagnose) ? "\t\t" + diagnose
				: "暂无相关内容");

		tvAboutDept.setText(!TextUtils.isEmpty(dept) ? dept : "");

		if (!TextUtils.isEmpty(prevent) || !TextUtils.isEmpty(nursing)) {
			tvBaoJian.setText("\t\t" + prevent + "\n" + "\t\t" + nursing);
		} else {
			tvBaoJian.setText("暂无相关内容");
		}

		String[] strs = dept.split(" ");
		dept = strs[strs.length - 1];
		dept = dept.replace("\n", "");
		dept = dept.replace("\r", "");

		initialDocList(dept);
		progress.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取推荐医生
	 */
	private void initialDocList(String dept) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("deptCateName", dept);// 科室名称模糊搜索
//		params.put("orderLevel", "0");// 是否按职务排行
//		params.put("orderRate", "2");// 是否按预约率排序
//		params.put("orderYnRemain", "2");// 是否有号
//		params.put("orderYnEvaluation", "1");// 按评价高低
//		params.put("dcOrderParm", "6");// 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
//		params.put("rowsPerPage", pageSize);
//		params.put("page", currentPage);
		
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("deptCateName", dept);// 科室名称模糊搜索
		params.addBodyParameter("orderLevel", "0");// 是否按职务排行
		params.addBodyParameter("orderRate", "2");// 是否按预约率排序
		params.addBodyParameter("orderYnRemain", "2");// 是否有号
		params.addBodyParameter("orderYnEvaluation", "1");// 按评价高低
		params.addBodyParameter("dcOrderParm", "6");// 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
		params.addBodyParameter("rowsPerPage", ""+pageSize);
		params.addBodyParameter("page", ""+currentPage);
		new HospitalImpl().getRankingDocList(params, this);

	}
@OnItemClick(R.id.detail_doc_lv)
	private void lvDocleItemClick(AdapterView<?> parent, View view, int position,
			long id){
		// 推荐医生
		String docId = docAdapter.getList().get(position).getId();
		String deptId = docAdapter.getList().get(position).getDocDeptId();
		String deptDocId = docAdapter.getList().get(position)
				.getDeptDocId();
		Intent intent = new Intent(mContext, DoctorIndex30Activity.class)
				.putExtra("deptId", deptId).putExtra("docId", docId)
				.putExtra("deptDocId", deptDocId);
		startActivity(intent);
	}
	

	@Override
	public void result(Object... object) {
		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			switch (type) {
			case HttpParams.GET_DISEASE_DETAILS:// 获取疾病详情
				Diseases diseases = (Diseases) object[2];
				if (diseases != null) {
					initial(diseases.getIntro(), diseases.getDiagnose(),
							diseases.getPrevent(), diseases.getNursing(),
							diseases.getDept());
					layoutAll.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(mContext, "暂无疾病数据", Toast.LENGTH_SHORT).show();//2015-12-17 服务器未返回疾病详情数据处理
				}

				break;
			case HttpParams.GET_RANKING_DOC_LIST:// 获取推荐医生列表
				
				
				
				progress.setVisibility(View.GONE);
				// ArrayList<Doctor> docList = (ArrayList<Doctor>) object[2];//
				// 2015-12-26 接口對接
				ArrayList<Doctor> docList = null;

				Map<String, Object> map = (Map<String, Object>) object[2];
				if(map.containsKey("docList")){
					docList=(ArrayList<Doctor>) map.get("docList");
				}
				
				
				if (docList != null && docList.size() != 0) {
					docAdapter.setList(docList);
				} else {
					if (docList == null) {
						Toast.makeText(mContext,
								getString(R.string.request_fail),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(mContext, "暂无推荐医生", Toast.LENGTH_SHORT)
								.show();
					}

				}

				break;

			}

		} else {
			String str = (String) object[3];
			Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
		}

	}
	@OnClick(R.id.zhenduan_layout)
	public void zhenduanClick(View v){
		// 诊断鉴别
					if (tvZhenDuan.getVisibility() == View.VISIBLE) {// 隐藏
						tvZhenDuan.setVisibility(View.GONE);
						img1.setImageResource(R.drawable.arrows_bottom);
					} else {// 显示
						tvZhenDuan.setVisibility(View.VISIBLE);
						img1.setImageResource(R.drawable.arrows_top);
					}

	}
	@OnClick(R.id.baojian_layout)
	private void baojianClick(View v){
		// 预防保健
					if (tvBaoJian.getVisibility() == View.VISIBLE) {// 隐藏
						tvBaoJian.setVisibility(View.GONE);
						img2.setImageResource(R.drawable.arrows_bottom);
					} else {// 显示
						tvBaoJian.setVisibility(View.VISIBLE);
						img2.setImageResource(R.drawable.arrows_top);
					}
	}

	

}

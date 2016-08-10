package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationDetailActivity;
import com.eztcn.user.eztcn.adapter.DiseaseListAdapter;
import com.eztcn.user.eztcn.adapter.InformationAdapter;
import com.eztcn.user.eztcn.adapter.SymptomListAdapter;
import com.eztcn.user.eztcn.adapter.SymptomListAdapter.Iclick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.api.ISymptomQueryApi;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.Symptom;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.HorizontalListView;
import com.eztcn.user.eztcn.customView.MyGridView;
import com.eztcn.user.eztcn.impl.SymptomQueryImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 症状详情
 * @describe
 * @author ezt
 * @created 2014年12月24日
 */
public class SymptomDetailActivity extends FinalActivity implements
		IHttpResult, Iclick {//OnItemClickListener, 

	@ViewInject(R.id.all_layout)
	private LinearLayout layoutAll;

	@ViewInject(R.id.disease_hor_lv)
	private HorizontalListView horLv;// 相关疾病

	@ViewInject(R.id.detail_article_lv)//, itemClick = "onItemClick"
	private ListView lvArticle;// 医生博文列表

	@ViewInject(R.id.about_symptom_gv)//, itemClick = "onItemClick"
	private MyGridView gvAboutSymptom;// 相关症状

	@ViewInject(R.id.dept_tv)
	private TextView tvDept;// 相关科室

	@ViewInject(R.id.intro_tv)
	private TextView tvIntro;// 描述详情

	@ViewInject(R.id.none_layout)
	private LinearLayout noneLayout;

	@ViewInject(R.id.none_txt)
	private TextView tvNone;

	@ViewInject(R.id.progress)
	private ProgressBar progress;// 相关症状进度加载

	@ViewInject(R.id.progress2)
	private ProgressBar progress2;// 相关博文进度加载

	@ViewInject(R.id.progress3)
	private ProgressBar progress3;// 可能患疾病进度加载

	private InformationAdapter adapter;
	private DiseaseListAdapter dListAdapter;
	private SymptomListAdapter sAdapter;
	private boolean isHavDate=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symptom_detail);
		ViewUtils.inject(SymptomDetailActivity.this);
		Symptom symptom = (Symptom) getIntent().getSerializableExtra("symptom");
		
		if(getIntent().hasExtra("isHavDate")){
			isHavDate=getIntent().getBooleanExtra("isHavDate",false);
		}
		
		initialView();
		loadTitleBar(true, symptom.getStrName(), null);
		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			initialData(symptom.getId(), symptom.getStrName());
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取数据
	 */
	public void initialData(String id, String title) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("symptomId", id);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("symptomId", id);
		new SymptomQueryImpl().getSymptomDetailsOfId(params, this);
		initialDiseaseData(id);
		initialAboutArticle(title);
	}

	/**
	 * 初始化控件
	 */
	private void initialView() {

		tvNone.setText("暂无相关博文");
//		horLv.setOnItemClickListener(this);

		// 相关疾病
		dListAdapter = new DiseaseListAdapter(mContext, false);
		horLv.setAdapter(dListAdapter);

		// 相关博文
		adapter = new InformationAdapter(mContext);
		lvArticle.setAdapter(adapter);

		// 相关症状
		sAdapter = new SymptomListAdapter(mContext, false);
		sAdapter.AdapterClick(this);
		gvAboutSymptom.setAdapter(sAdapter);
	}

	/**
	 * 初始化相关数据
	 */
	private void getData(String intro, String dept) {
		if (!TextUtils.isEmpty(intro))
			tvIntro.setText("\t\t" + intro);
		if (!TextUtils.isEmpty(dept))
			tvDept.setText(dept);

		if (!TextUtils.isEmpty(dept)) {
			String[] depts = dept.split(" ");
			if(isHavDate){
				getAboutSymptoms(depts[depts.length - 1]);
			}else
			if (adapter.getList() == null) {
				getAboutSymptoms(depts[depts.length - 1]);
			}

		}
	}

	/**
	 * 获取相关症状列表
	 * 
	 * @param dept
	 */
	private void getAboutSymptoms(String dept) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("subordinateDept", dept);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("subordinateDept", dept);
		new SymptomQueryImpl().getSymptomListOfDept(params, this);
		progress.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取相关疾病列表
	 */
	private void initialDiseaseData(String symptomId) {

//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("symptomsId", symptomId);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("symptomsId", symptomId);
		new SymptomQueryImpl().getDiseaseListOfId(params, this);

	}

	/**
	 * 根据关键字获取相关博文
	 * 
	 * @param Keyword
	 */
	private void initialAboutArticle(String Keyword) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("keyval", Keyword);
		RequestParams params=new RequestParams();
		params.addBodyParameter("keyval", Keyword);
//		ISymptomQueryApi api = new SymptomQueryImpl();
		SymptomQueryImpl api = new SymptomQueryImpl();
		api.getArticleOfSymptom(params, this);
	}
	@OnItemClick(R.id.disease_hor_lv)
	private void diseaseItemClick(AdapterView<?> parent, View view, int position,
			long id){// 相关疾病
		startActivity(new Intent(SymptomDetailActivity.this,
				DiseaseDetailActivity.class).putExtra("disease",
				dListAdapter.getList().get(position)));
	}
	@OnItemClick(R.id.detail_article_lv)
	private void detialAtricleItemClick(AdapterView<?> parent, View view, int position,
			long id){// 相关博文
		ArrayList<Information> lists = (ArrayList<Information>) adapter
				.getList();
		if (lists != null) {
			String infoId = lists.get(position).getId();
			String url = lists.get(position).getImgUrl();

			startActivity(new Intent(SymptomDetailActivity.this,
					InformationDetailActivity.class).putExtra("infoId",
					infoId).putExtra("infoUrl",
					EZTConfig.OFFICIAL_WEBSITE + url));
		}
	}
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		switch (parent.getId()) {
//		case R.id.disease_hor_lv:// 相关疾病
//			startActivity(new Intent(SymptomDetailActivity.this,
//					DiseaseDetailActivity.class).putExtra("disease",
//					dListAdapter.getList().get(position)));
//			break;
//
//		case R.id.detail_article_lv:// 相关博文
//			ArrayList<Information> lists = (ArrayList<Information>) adapter
//					.getList();
//			if (lists != null) {
//				String infoId = lists.get(position).getId();
//				String url = lists.get(position).getImgUrl();
//
//				startActivity(new Intent(SymptomDetailActivity.this,
//						InformationDetailActivity.class).putExtra("infoId",
//						infoId).putExtra("infoUrl",
//						EZTConfig.OFFICIAL_WEBSITE + url));
//			}
//
//			break;
//		}
//
//	}

	@Override
	public void result(Object... object) {

		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			switch (type) {
			case HttpParams.GET_SYMPTOM_LIST_DEPT:// 获取症状列表
				progress.setVisibility(View.GONE);
				ArrayList<Symptom> list2 = (ArrayList<Symptom>) object[2];
				if (list2 != null && list2.size() != 0) {
					sAdapter.setList(list2);
				} else {
					sAdapter.setList(new ArrayList<Symptom>());
//					Toast.makeText(mContext, getString(R.string.request_fail), 2015-12-22
//							Toast.LENGTH_SHORT).show();
				}
				break;

			case HttpParams.GET_SYMPTOM_DETAILS:// 获取症状详情
				Symptom symptom = (Symptom) object[2];
				if (symptom != null) {
					getData(symptom.getIntro(), symptom.getStrDept());
					layoutAll.setVisibility(View.VISIBLE);
//					progress.setVisibility(View.VISIBLE);
//					progress2.setVisibility(View.VISIBLE);
//					progress3.setVisibility(View.VISIBLE);
				} else {

				}
				break;
			case HttpParams.GET_DISEASE_LIST_ID:// 根据症状id获取疾病列表
				progress3.setVisibility(View.GONE);
				ArrayList<Diseases> list = (ArrayList<Diseases>) object[2];
				if (list != null && list.size() != 0) {
					horLv.setVisibility(View.VISIBLE);
					dListAdapter.setList(list);
				} else {
//					Toast.makeText(mContext, getString(R.string.request_fail),   2015-12-22
//							Toast.LENGTH_SHORT).show();
					horLv.setVisibility(View.GONE);//2015-12-29 没有名称的相关疾病 不显示列表
				}

				break;

			case HttpParams.GET_ARTICLE_OF_SYMPTOM:// 获取症状相关博文
				progress2.setVisibility(View.GONE);
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					if ((Boolean) map.get("flag")) {
//						if(map.containsKey("message")){//2015-12-22 提示信息
//							String msgStr=map.get("message").toString();
//							Toast.makeText(SymptomDetailActivity.this, msgStr, Toast.LENGTH_SHORT).show();
//						}
						ArrayList<Information> infoList = (ArrayList<Information>) map
								.get("infoList");
						if (infoList != null && infoList.size() > 0) {
							adapter.setList(infoList);
							lvArticle.setVisibility(View.VISIBLE);
							noneLayout.setVisibility(View.GONE);
						} else {// 暂无相关博文
							lvArticle.setVisibility(View.GONE);
							noneLayout.setVisibility(View.VISIBLE);
						}

					} else {
						Logger.i("相关博文列表", map.get("msg"));
					}
				}

				break;
			}

		} else {
			switch (type) {
			case HttpParams.GET_SYMPTOM_LIST_DEPT:
				progress.setVisibility(View.GONE);
				break;

			case HttpParams.GET_SYMPTOM_DETAILS:
				String str = (String) object[3];
				Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
				break;

			case HttpParams.GET_DISEASE_LIST_ID:
				progress3.setVisibility(View.GONE);
				break;

			case HttpParams.GET_ARTICLE_OF_SYMPTOM:
				progress2.setVisibility(View.GONE);
				break;
			}
		}

	}

	@Override
	public void click(int pos) {
		Symptom symptom = sAdapter.getList().get(pos);
		startActivity(new Intent(mContext, SymptomDetailActivity.class)
				.putExtra("symptom", symptom).putExtra("isHavDate", true));
		mContext.finish();
	}
}

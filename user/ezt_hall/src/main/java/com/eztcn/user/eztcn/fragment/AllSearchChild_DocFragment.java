package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Map;

import xutils.http.RequestParams;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.AllSearchActivity;
import com.eztcn.user.eztcn.adapter.AllSearchProfessorAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 自定义Fragment
 * @describe 全站搜索子栏目(医生)数据显示
 * @author ezt
 * @created 2015年2月5日
 */
public class AllSearchChild_DocFragment extends FinalFragment implements
		OnItemClickListener, IHttpResult, OnLoadMoreListener, OnRefreshListener {

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private AllSearchProfessorAdapter adapter;
	private PullToRefreshListView lt;
	private Activity mActivity;
	
	private String strSearch = "";
	public ArrayList<Doctor> docList;// 医生列表
	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}
	public void setDocList(ArrayList<Doctor> docList) {
		this.docList = docList;
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
		if (lt == null) {
			lt = new PullToRefreshListView(mActivity);
			initialView();

		}
		adapter.setList(docList);
		if (docList != null && docList.size() != 0) {
			lt.setVisibility(View.VISIBLE);
		}else{
			lt.setVisibility(View.GONE);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) lt.getParent();
		if (parent != null) {
			parent.removeView(lt);
		}
		return lt;
	}

	/**
	 * 初始化
	 */
	private void initialView() {
		adapter = new AllSearchProfessorAdapter(mActivity);
		lt.setAdapter(adapter);
		lt.setCanLoadMore(true);
		lt.setCanRefresh(true);
		lt.setAutoLoadMore(true);
		lt.setMoveToFirstItemAfterRefresh(false);
		lt.setDoRefreshOnUIChanged(false);
		lt.setOnLoadListener(this);
		lt.setOnRefreshListener(this);
		lt.setOnItemClickListener(this);
//		lt.setDivider(getResources().getDrawable(android.R.color.transparent));
		lt.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_SEARCH_DOC:// 获取医生列表
			ArrayList<Doctor> docData = null;
			if (isSuc) {
				Map<String, Object> deptMap = (Map<String, Object>) object[2];

				if (deptMap != null) {
					docList = (ArrayList<Doctor>) deptMap
							.get("docList");
					if (docList != null && docList.size() > 0) {

						if (currentPage == 1) {// 第一次加载或刷新
							docData = docList;
							if (docList.size() < EZTConfig.PAGE_SIZE) {
								lt.setAutoLoadMore(false);
								lt.onLoadMoreComplete();
							}
							lt.onRefreshComplete();

						} else {// 加载更多
							docData = (ArrayList<Doctor>) adapter.getList();
							if (docData == null || docData.size() <= 0)
								docData = docList;
							else
								docData.addAll(docList);
							lt.onLoadMoreComplete();
						}
						adapter.setList(docData);

					} else {
						if (adapter.getList() != null) {// 加载
							lt.setAutoLoadMore(false);
							lt.onLoadMoreComplete();
						} else {// 刷新
							lt.onRefreshComplete();
						}

						Logger.i("全站搜索获取医生列表", "暂无数据");
					}
				} else {
					if (adapter != null) {
						if (adapter.getList() != null) {// 加载
							lt.setAutoLoadMore(false);
							lt.onLoadMoreComplete();
							docData = (ArrayList<Doctor>) adapter.getList();
						} else {// 刷新
							lt.onRefreshComplete();
						}
					}

					Logger.i("全站搜索获取医生列表", "获取数据失败");
				}

			} else {

			}
			break;

		default:
			break;
		}

	}

	/**
	 * 搜索医生列表
	 */
	public void getDocData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", currentPage+"");
//		params.put("rowsPerPage", pageSize + "");
//		params.put("search", strSearch);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", currentPage+"");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("search", strSearch);
		
		new HospitalImpl().getSearchDoc(params, this);

	}

	@Override
	public void onRefresh() {
		lt.setAutoLoadMore(true);
		currentPage = 1;
		getDocData();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getDocData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		String docId = adapter.getList().get(position).getId();
		String deptId1 = adapter.getList().get(position)
				.getDocDeptId();
		String deptDocId = adapter.getList().get(position)
				.getDeptDocId();
		int ehDockingStatus = adapter.getList().get(position)//2015-12-21 医院对接提示
				.getEhDockingStatus();
		((AllSearchActivity) mActivity).toDoctorInfo(deptId1, docId, deptDocId,ehDockingStatus);
	}

}
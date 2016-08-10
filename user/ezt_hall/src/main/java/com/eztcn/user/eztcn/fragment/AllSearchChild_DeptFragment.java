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
import com.eztcn.user.eztcn.adapter.AllSearchDeptAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 自定义Fragment
 * @describe 全站搜索子栏目(科室)数据显示
 * @author ezt
 * @created 2015年2月5日
 */
public class AllSearchChild_DeptFragment extends FinalFragment implements
		OnItemClickListener, IHttpResult, OnLoadMoreListener, OnRefreshListener {

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private AllSearchDeptAdapter adapter;
	private PullToRefreshListView lt;
	private Activity mActivity;

	private ArrayList<Dept> deptList;// 科室列表
	private String strSearch = "";

	public void setDeptList(ArrayList<Dept> deptList) {
		this.deptList = deptList;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
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
		adapter.setList(deptList);
		if (deptList != null && deptList.size() != 0) {
			lt.setVisibility(View.VISIBLE);
		} else {
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
		adapter = new AllSearchDeptAdapter(mActivity);
		lt.setAdapter(adapter);
		lt.setCanLoadMore(true);
		lt.setCanRefresh(true);
		lt.setAutoLoadMore(true);
		lt.setMoveToFirstItemAfterRefresh(false);
		lt.setDoRefreshOnUIChanged(false);
		lt.setOnLoadListener(this);
		lt.setOnRefreshListener(this);
		lt.setOnItemClickListener(this);
		// lt.setDivider(getResources().getDrawable(android.R.color.transparent));
		lt.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_SEARCH_DEPT:// 获取科室列表

			ArrayList<Dept> deptData = null;
			if (isSuc) {
				Map<String, Object> deptMap = (Map<String, Object>) object[2];
				if (deptMap != null) {
					deptList = (ArrayList<Dept>) deptMap.get("deptList");
					if (deptList != null && deptList.size() > 0) {

						if (currentPage == 1) {// 第一次加载或刷新
							deptData = deptList;
							if (deptList.size() < EZTConfig.PAGE_SIZE) {
								lt.setAutoLoadMore(false);
								lt.onLoadMoreComplete();
							}
							lt.onRefreshComplete();

						} else {// 加载更多
							deptData = (ArrayList<Dept>) adapter.getList();
							if (deptData == null || deptData.size() <= 0)
								deptData = deptList;
							else
								deptData.addAll(deptList);

							lt.onLoadMoreComplete();
						}
						adapter.setList(deptData);

					} else {

						if (adapter.getList() != null) {// 加载
							lt.setAutoLoadMore(false);
							lt.onLoadMoreComplete();

							deptData = (ArrayList<Dept>) adapter.getList();
						} else {// 刷新
							lt.onRefreshComplete();

						}

						Logger.i("全站搜索获取科室列表", "暂无数据");
					}
				} else {
					if (adapter != null) {
						if (adapter.getList() != null) {// 加载
							lt.setAutoLoadMore(false);
							lt.onLoadMoreComplete();
							deptData = (ArrayList<Dept>) adapter.getList();
						} else {// 刷新
							lt.onRefreshComplete();
						}
					}

					Logger.i("全站搜索获取科室列表", "获取数据失败");
				}
				if (deptData != null) {
					deptList = deptData;
				}
			} else {

			}

			break;

		default:
			break;
		}

	}

	/**
	 * 搜索科室列表
	 */
	public void getDeptData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", currentPage + "");
//		params.put("rowsPerPage", pageSize + "");
//		params.put("search", strSearch);
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", currentPage + "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("search", strSearch);
		new HospitalImpl().getSearchDept(params, this);
	}

	@Override
	public void onRefresh() {
		lt.setAutoLoadMore(true);
		currentPage = 1;
		getDeptData();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getDeptData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		position = position - 1;
		String deptName = adapter.getList().get(position).getdName();
		int deptId = adapter.getList().get(position).getId();
		String dHid = adapter.getList().get(position).getdHosId();
		String hosName = adapter.getList().get(position).getdHosName();
		((AllSearchActivity) mActivity).toDoctorList(deptName, hosName, deptId,
				dHid);

	}

}
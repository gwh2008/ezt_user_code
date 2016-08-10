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
import com.eztcn.user.eztcn.adapter.AllSearchHosAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 自定义Fragment
 * @describe 全站搜索子栏目(医院)数据显示
 * @author ezt
 * @created 2015年2月5日
 */
public class AllSearchChild_HosFragment extends FinalFragment implements
		OnItemClickListener, IHttpResult, OnLoadMoreListener, OnRefreshListener {

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private AllSearchHosAdapter adapter;
	private PullToRefreshListView lt;
	private Activity mActivity;

	private ArrayList<Hospital> hosList;// 医院列表
	private String strSearch = "";

	public void setHosList(ArrayList<Hospital> hosList) {
		this.hosList = hosList;
	}

	public void setStrSearch(String strSearch) {
		this.strSearch = strSearch;
	}

	public String getStrSearch() {
		return strSearch;
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
			initialView();
		}
		adapter.setList(hosList);
		if (hosList != null && hosList.size() != 0) {
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
		lt = new PullToRefreshListView(mActivity);
		adapter = new AllSearchHosAdapter(mActivity);
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
		case HttpParams.GET_SEARCH_HOS:// 获取医院列表
			ArrayList<Hospital> hosData = null;
			if (isSuc) {
				Map<String, Object> hosMap = (Map<String, Object>) object[2];
				if (hosMap != null) {
					hosList = (ArrayList<Hospital>) hosMap.get("hosList");
					if (hosList != null && hosList.size() > 0) {

						if (currentPage == 1) {// 第一次加载或刷新
							hosData = hosList;
							if (hosList.size() < EZTConfig.PAGE_SIZE) {
								lt.setAutoLoadMore(false);
								lt.onLoadMoreComplete();
							}
							lt.onRefreshComplete();

						} else {// 加载更多
							hosData = (ArrayList<Hospital>) adapter.getList();
							if (hosData == null || hosData.size() <= 0)
								hosData = hosList;
							else
								hosData.addAll(hosList);

							lt.onLoadMoreComplete();

						}
						adapter.setList(hosData);

					} else {
						if (adapter != null) {
							if (adapter.getList() != null) {// 加载

								lt.setAutoLoadMore(false);
								lt.onLoadMoreComplete();

								hosData = (ArrayList<Hospital>) adapter
										.getList();
							} else {// 刷新
								lt.onRefreshComplete();
							}

						}
						Logger.i("全站搜索获取医院列表", "暂无数据");
					}
				} else {
					if (adapter.getList() != null) {// 加载
						lt.setAutoLoadMore(false);
						lt.onLoadMoreComplete();
						hosData = (ArrayList<Hospital>) adapter.getList();
					} else {// 刷新
						lt.onRefreshComplete();
					}

					Logger.i("全站搜索获取医院列表", "获取数据失败");
				}

			} else {

			}
			break;

		default:
			break;
		}

	}

	/**
	 * 搜索医院列表
	 */
	public void getHosData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", currentPage + "");
//		params.put("rowsPerPage", pageSize + "");
//		params.put("search", getStrSearch());
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", currentPage + "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("search", getStrSearch());
		new HospitalImpl().getSearchHos(params, this);
	}

	@Override
	public void onRefresh() {
		lt.setAutoLoadMore(true);
		currentPage = 1;
		getHosData();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getHosData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		((AllSearchActivity) mActivity).toDoctorList(adapter.getList().get(
				position));

	}

}
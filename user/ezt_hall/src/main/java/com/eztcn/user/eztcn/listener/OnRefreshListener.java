package com.eztcn.user.eztcn.listener;

/**
 * @title 自定义listview 刷新，加载接口
 * @describe
 * @author ezt
 * @created 2014年11月24日
 */
public interface OnRefreshListener {

	/**
	 * 下拉刷新
	 */
	void onDownPullRefresh();

	/**
	 * 上拉加载更多
	 */
	void onLoadingMore();
}
package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.TradeDetailAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ChargeCurrency;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 交易明细
 * @describe
 * @author ezt
 * @created 2015年2月4日
 */
public class TradeDetailActivity extends FinalActivity implements IHttpResult,
		OnLoadMoreListener, OnRefreshListener {

	// private ImageView search;
	@ViewInject(R.id.detailList)//, itemClick = "onItemClick" 原代码为空
	private PullToRefreshListView detailList;
	private TradeDetailAdapter adapter;
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private ArrayList<ChargeCurrency> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tradedetail);
		ViewUtils.inject(TradeDetailActivity.this);
		loadTitleBar(true, "交易明细", null);
		adapter = new TradeDetailAdapter(mContext);
		detailList.setAdapter(adapter);

		detailList.setCanLoadMore(true);
		detailList.setCanRefresh(true);
		detailList.setAutoLoadMore(true);
		detailList.setMoveToFirstItemAfterRefresh(false);
		detailList.setDoRefreshOnUIChanged(false);
		detailList.setOnLoadListener(this);
		detailList.setOnRefreshListener(this);
		getTradeRecord();
	}

	/**
	 * 获取记录
	 */
	public void getTradeRecord() {
		RequestParams params=new RequestParams();
		if(BaseApplication.patient!=null){
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		}
		params.addBodyParameter("rowsPerPage", pageSize+ "");
		params.addBodyParameter("page", currentPage+ "");
		new PayImpl().getCurrencyRecord(params, this);
		showProgressToast();
	}

	/**
	 * listview item点击事件
	 * @param //parent
	 * @param //view
	 * @param //position
	 * @param //id
	 */
	@Override
	public void result(Object... object) {
		hideProgressToast();
		int resultType = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		Map<String, Object> map = null;
		if (isSuc) {
			map = (Map<String, Object>) object[2];
			if (map == null) {
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			switch (resultType) {
			case HttpParams.GET_PAY_RECORD:// 充值记录

				break;

			case HttpParams.GET_CURRENCY_RECORD:// 医通币记录

				if (flag) {// 获取成功
					ArrayList<ChargeCurrency> data = null;
					list = (ArrayList<ChargeCurrency>) map.get("list");
					if (list != null && list.size() > 0) {
						detailList.setVisibility(View.VISIBLE);
						if (currentPage == 1) {// 第一次加载或刷新
							detailList.setVisibility(View.VISIBLE);
							data = list;
							if (list.size() < pageSize) {
								detailList.setAutoLoadMore(false);
								detailList.onLoadMoreComplete();
							}
							detailList.onRefreshComplete();

						} else {// 加载更多
							data = (ArrayList<ChargeCurrency>) adapter
									.getList();
							if (data == null || data.size() <= 0)
								data = list;
							else
								data.addAll(list);

							if (list.size() < pageSize) {
								detailList.setAutoLoadMore(false);
							}
							detailList.onLoadMoreComplete();

						}
						adapter.setList(data);

					} else {
						if (adapter.getList() != null) {// 加载
							detailList.setAutoLoadMore(false);
							detailList.onLoadMoreComplete();
							data = (ArrayList<ChargeCurrency>) adapter
									.getList();
						} else {// 刷新
							detailList.onRefreshComplete();
						}

					}
					if (data != null) {
						list = data;
					}

				} else {
					Logger.i("获取医通币记录", "获取失败---");
				}

				break;
			}
		} else {// 服务器访问错误
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		detailList.setAutoLoadMore(true);
		getTradeRecord();

	}

	@Override
	public void onLoadMore() {
		if (list != null) {
			if (list.size() < pageSize) {
				detailList.setAutoLoadMore(false);
				detailList.onLoadMoreComplete();

			} else {
				currentPage++;
				getTradeRecord();
			}
		}

	}
}

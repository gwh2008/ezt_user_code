package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ENurseMealAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.ENurseHelpImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 我要医护帮
 * @describe
 * @author ezt
 * @created 2015年6月15日
 */
public class CallENurseActivity extends FinalActivity implements IHttpResult,
		OnLoadMoreListener, OnRefreshListener {

	@ViewInject(R.id.mealList)//, itemClick = "onItemClick"
	private PullToRefreshListView mealList;

	private ArrayList<HealthCard> list;
	private ENurseMealAdapter adapter;
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callenurse);
		ViewUtils.inject(CallENurseActivity.this);
//		loadTitleBar(true, "我要医护帮", null);
		loadTitleBar(true, "人人医管家", null);
		init();
		getENurseData();
	}

	public void init() {
		adapter = new ENurseMealAdapter(mContext);
		mealList.setAdapter(adapter);
		mealList.setCanLoadMore(true);
		mealList.setCanRefresh(true);
		mealList.setAutoLoadMore(true);
		mealList.setMoveToFirstItemAfterRefresh(false);
		mealList.setDoRefreshOnUIChanged(false);
		mealList.setOnLoadListener(this);
		mealList.setOnRefreshListener(this);
	}

	/**
	 * 获取医护内容
	 */
	public void getENurseData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("type", "478");// 478:健康卡 1:医护帮
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("type", "478");// 478:健康卡 1:医护帮
		new ENurseHelpImpl().getENurseCard(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[2] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (!flag) {
			Toast.makeText(mContext, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (taskID == HttpParams.GET_MEALCARD_LIST) {
			ArrayList<HealthCard> data = null;
			list = (ArrayList<HealthCard>) map.get("list");
			// 成功
			if (list != null && list.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					mealList.setVisibility(View.VISIBLE);
					data = list;
					if (list.size() < pageSize) {
						mealList.setAutoLoadMore(false);
						mealList.onLoadMoreComplete();
					}
					mealList.onRefreshComplete();

				} else {// 加载更多
					data = (ArrayList<HealthCard>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = list;
					else
						data.addAll(list);

					if (list.size() < pageSize) {
						mealList.setAutoLoadMore(false);
					}
					mealList.onLoadMoreComplete();

				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null) {// 加载
					mealList.setAutoLoadMore(false);
					mealList.onLoadMoreComplete();
					data = (ArrayList<HealthCard>) adapter.getList();
				} else {// 刷新
					mealList.onRefreshComplete();
				}

			}
			if (data != null) {
				list = data;
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * listivew item点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
	@OnItemClick(R.id.mealList)
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// if (BaseApplication.eztUser == null) {
		// HintToLogin(22);
		// } else {
		Intent intent = new Intent(this, CardMealDetailActivity.class);
		intent.putExtra("info", list.get(position - 1));
		startActivity(intent);
		// }
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		if (list != null) {
			if (list.size() < pageSize) {
				mealList.setAutoLoadMore(false);
				mealList.onLoadMoreComplete();

			} else {
				currentPage++;
				getENurseData();
			}
		}

	}

	// 刷新
	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		mealList.setAutoLoadMore(true);
		getENurseData();
	}
}

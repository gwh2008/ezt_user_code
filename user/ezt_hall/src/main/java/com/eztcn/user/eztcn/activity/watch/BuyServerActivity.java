//package com.eztcn.user.eztcn.activity.watch;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.BaseApplication;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.adapter.ENurseMealAdapter;
//import com.eztcn.user.eztcn.api.IHttpResult;
//import com.eztcn.user.eztcn.bean.HealthCard;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.customView.PullToRefreshListView;
//import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
//import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
//import com.eztcn.user.eztcn.impl.ServerImpl;
//import com.eztcn.user.eztcn.utils.HttpParams;
//
///**
// * @title 服务购买 is die
// * @describe
// * @author ezt
// * @created 2015年6月15日
// */
//public class BuyServerActivity extends FinalActivity implements IHttpResult,
//		OnLoadMoreListener, OnRefreshListener,OnClickListener {
//
//	@ViewInject(R.id.mealList, itemClick = "onItemClick")
//	private PullToRefreshListView listView;
//
//	private ArrayList<HealthCard> list;
//	private ENurseMealAdapter adapter;
//	private int currentPage = 1;// 当前页数
//	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.callenurse);
//		TextView bindBtn=loadTitleBar(true, "服务购买", "激活绑定");
//		bindBtn.setOnClickListener(this);
//		init();
//		gainCardsData();
//	}
//
//	/**
//	 * 获取套餐列表（已经购买）
//	 */
//	private void gainCardsData() {
//		ServerImpl api = new ServerImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", BaseApplication.eztUser.getUserId());
//		params.put("rowsPerPage", pageSize);
//		params.put("page", currentPage);
//		api.getHealthcardList(params, this);
//		showProgressToast();
//	}
//	public void init() {
//		adapter = new ENurseMealAdapter(mContext);
//		listView.setAdapter(adapter);
//		listView.setCanLoadMore(true);
//		listView.setCanRefresh(true);
//		listView.setAutoLoadMore(true);
//		listView.setMoveToFirstItemAfterRefresh(false);
//		listView.setDoRefreshOnUIChanged(false);
//		listView.setOnLoadListener(this);
//		listView.setOnRefreshListener(this);
//	}
//
//
//	@Override
//	public void result(Object... object) {
//		hideProgressToast();
//		if (object == null) {
//			return;
//		}
//		Object[] obj = object;
//		Integer taskID = (Integer) obj[0];
//		if (taskID == null) {
//			return;
//		}
//		boolean status = (Boolean) obj[1];
//		if (!status) {
//			Toast.makeText(mContext, obj[2] + "", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		Map<String, Object> map = (Map<String, Object>) obj[2];
//		if (map == null || map.size() == 0) {
//			return;
//		}
//		if (taskID ==HttpParams.GET_HEALTHCARD_LIST) {
// 
//			// 获取所有用户绑定的套餐
//			list = (ArrayList<HealthCard>) map.get("healthCardList");
//			ArrayList<HealthCard> data = null;
//			// 成功
//			if (list != null && list.size() > 0) {
//				if (currentPage == 1) {// 第一次加载或刷新
//					data = list;
//					if (list.size() < pageSize) {
//						listView.setAutoLoadMore(false);
//						listView.onLoadMoreComplete();
//					}
//					listView.onRefreshComplete();
//
//				} else {// 加载更多
//					data = (ArrayList<HealthCard>) adapter.getList();
//					if (data == null || data.size() <= 0)
//						data = list;
//					else
//						data.addAll(list);
//
//					if (list.size() < pageSize) {
//						listView.setAutoLoadMore(false);
//					}
//					listView.onLoadMoreComplete();
//
//				}
//				adapter.setList(data);
//
//			} else {
//				if (adapter.getList() != null) {// 加载
//					listView.setAutoLoadMore(false);
//					listView.onLoadMoreComplete();
//					data = (ArrayList<HealthCard>) adapter.getList();
//				} else {// 刷新
//					listView.onRefreshComplete();
//				}
//
//			}
//			if (data != null) {
//				list = data;
//			}
//
//			
//		} else {
//			Toast.makeText(mContext, getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	/**
//	 * listivew item点击事件
//	 * 
//	 * @param parent
//	 * @param view
//	 * @param position
//	 * @param id
//	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// if (BaseApplication.eztUser == null) {
//		// HintToLogin(22);
//		// } else {
//		Intent intent = new Intent(this,ServerCardDetialActivity.class);
//		intent.putExtra("info", list.get(position - 1));
//		startActivity(intent);
//		// }
//	}
//
//	@Override
//	public void onLoadMore() {
//		if (list != null) {
//			if (list.size() < pageSize) {
//				listView.setAutoLoadMore(false);
//				listView.onLoadMoreComplete();
//			} else {
//				currentPage++;
//				gainCardsData();
//			}
//		}
//
//	}
//
//	// 刷新
//	@Override
//	public void onRefresh() {
//		currentPage = 1;// 还原页值
//		listView.setAutoLoadMore(true);
//		gainCardsData();
//	}
//
//	@Override
//	public void onClick(View v) {
//		//绑定激活
//		startActivity(new Intent(BuyServerActivity.this,
//				ActivateServerActivity.class));
//	}
//}

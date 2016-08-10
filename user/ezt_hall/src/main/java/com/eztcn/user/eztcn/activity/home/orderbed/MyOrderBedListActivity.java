package com.eztcn.user.eztcn.activity.home.orderbed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.OrderBedAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.OrderBed;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.OrderBedImpl;
import com.eztcn.user.eztcn.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
/**
 * 我的预约病床的列表。
 * @author LX
 *@date2016-3-31 @time下午5:27:44
 */

public class MyOrderBedListActivity extends FinalActivity implements 
   OnRefreshListener, OnLoadMoreListener, OnItemClickListener,IHttpResult  {
	
	@ViewInject(R.id.my_order_bed_lv)
	private PullToRefreshListView my_order_bed_lv;
	private OrderBedAdapter adapter;
	private ArrayList<OrderBed>  order_list;
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private int result_Code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_bed_list);
		ViewUtils.inject(MyOrderBedListActivity.this);
		loadTitleBar(true, "预约病床", null);
		adapter = new OrderBedAdapter(mContext);
		setAdapter();
	}
	private void setAdapter() {

		my_order_bed_lv.setAdapter(adapter);
		my_order_bed_lv.setCanLoadMore(true);
		my_order_bed_lv.setCanRefresh(true);
		my_order_bed_lv.setAutoLoadMore(true);
		my_order_bed_lv.setMoveToFirstItemAfterRefresh(false);
		my_order_bed_lv.setDoRefreshOnUIChanged(false);
		my_order_bed_lv.setOnLoadListener(this);
		my_order_bed_lv.setOnRefreshListener(this);
		my_order_bed_lv
				.setOnItemClickListener(MyOrderBedListActivity.this);
		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			initialData();
		} else {// 无网络

			if (adapter.getList() != null && adapter.getList().size() != 0) {
				my_order_bed_lv.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initialData() {
		
		RequestParams params = new RequestParams();
		OrderBedImpl impl = new OrderBedImpl();
		if(null==BaseApplication.patient){//内存丢失可能导致空指针，建议这时候做自动登录
			hideProgressToast();
            Toast.makeText(mContext, "抱歉，暂无数据", Toast.LENGTH_SHORT).show();
			return;
		}
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()+ "");
		impl.gainOrderBedList(params, this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		   OrderBed order_bed = order_list.get(position - 1);
			String order_id = order_bed.getId()+"";
			Intent check_bed_intent = new Intent();
			check_bed_intent.putExtra("order_id", order_id);
			check_bed_intent.setClass(mContext,
					OrderBedStatusActivity.class);
			startActivity(check_bed_intent);
	}
	@Override
	public void onLoadMore() {

		if (order_list != null) {
			if (order_list.size() < pageSize
					|| (order_list.size() > pageSize && order_list.size()
							% pageSize != 0)) {
				my_order_bed_lv.setAutoLoadMore(false);
				my_order_bed_lv.onLoadMoreComplete();
			} else {
				currentPage++;
				initialData();
			}
		}
	
	}
	@Override
	public void onRefresh() {
		
		my_order_bed_lv.setAutoLoadMore(true);
		currentPage = 1;
		initialData();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {
		
		Boolean isSucc = false;
		if (null != object[1])
			isSucc = (Boolean) object[1];
		
		if(isSucc){

			Map<String, Object> map;
			ArrayList<OrderBed> data = null;
			map = (Map<String, Object>) object[2];
			if (map.containsKey("data")) {
				Object obj=map.get("data");
				result_Code = (Integer) map.get("number");
				order_list =(ArrayList<OrderBed>) JsonUtil.fromJsonList(obj.toString(), OrderBed.class);
			}
			hideProgressToast();
			if (order_list != null && order_list.size() > 0) {
				if (currentPage == 1) {
					// 第一次加载或刷新
					data =  order_list;
					if (order_list.size() < pageSize) {
						my_order_bed_lv.setAutoLoadMore(false);
						my_order_bed_lv.onLoadMoreComplete();
					}
					my_order_bed_lv.onRefreshComplete();
				} else {// 加载更多
					data = (ArrayList<OrderBed>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = order_list;
					else
						data.addAll(order_list);
					my_order_bed_lv.onLoadMoreComplete();

				}
				adapter.setList(data);
				my_order_bed_lv.setVisibility(View.VISIBLE);

			} else {
				
				if (adapter.getList() != null && adapter.getList().size() != 0) {// 加载
					if (currentPage > 1) {
						currentPage--;
					}
					if (order_list != null) {
						if (order_list.size() == 0) {
							my_order_bed_lv.setAutoLoadMore(false);
						}
					} else {
						Toast.makeText(mContext,
								getString(R.string.request_fail),
								Toast.LENGTH_SHORT).show();
					}
					my_order_bed_lv.onLoadMoreComplete();
					data = (ArrayList<OrderBed>) adapter.getList();
					if (result_Code != 0) {
						adapter.mList.clear();
						adapter.notifyDataSetChanged();
						my_order_bed_lv.setVisibility(View.GONE);
					}

				} else {// 刷新
					my_order_bed_lv.onRefreshComplete();
					Toast.makeText(mContext, "抱歉，暂时没有数据", Toast.LENGTH_SHORT)
							.show();
					hideProgressToast();
				}
			}
			if (data != null) {
				order_list = data;
			}
			
		}else{
			
			hideProgressToast();
			if (currentPage > 1) {
				currentPage--;
			}else{
				my_order_bed_lv.setVisibility(View.INVISIBLE);
			}
			my_order_bed_lv.onLoadMoreComplete();
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
	}

}

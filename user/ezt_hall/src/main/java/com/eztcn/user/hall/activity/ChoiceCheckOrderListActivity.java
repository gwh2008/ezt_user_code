package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ordercheck.ChoiceCheckStatusActivity30;
import com.eztcn.user.eztcn.adapter.MyServiceCheckOrderAdapter30;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrder;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.OrderCheckImpl;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;

/**
 * 预约检查列表页面。
 * @author 蒙
 */
public class ChoiceCheckOrderListActivity extends BaseActivity implements
		IHttpResult, OnRefreshListener, OnLoadMoreListener, OnItemClickListener {

	@ViewInject(R.id.choice_check_lv)
	PullToRefreshListView choice_check_lv;
	private MyServiceCheckOrderAdapter30 adapter;
	private ArrayList<CheckOrder> checkList;
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private int result_Code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

    @Override
    protected int preView() {
        return R.layout.new_activity_choice_check_list;
    }

    @Override
    protected void initView() {
        ViewUtils.inject(ChoiceCheckOrderListActivity.this);
        loadTitleBar(true, "预约检查", null);
        adapter = new MyServiceCheckOrderAdapter30(this);
        setAdapter();
    }

    @Override
    protected void initData() {

    }

    private void setAdapter() {
		choice_check_lv.setAdapter(adapter);
		choice_check_lv.setCanLoadMore(true);
		choice_check_lv.setCanRefresh(true);
		choice_check_lv.setAutoLoadMore(true);
		choice_check_lv.setMoveToFirstItemAfterRefresh(false);
		choice_check_lv.setDoRefreshOnUIChanged(false);
		choice_check_lv.setOnLoadListener(this);
		choice_check_lv.setOnRefreshListener(this);
		choice_check_lv
				.setOnItemClickListener(ChoiceCheckOrderListActivity.this);
		if (BaseApplication.getInstance().isNetConnected) {
			showProgressDialog("正在加载数据");
			initialData();
		} else {// 无网络

			if (adapter.getList() != null && adapter.getList().size() != 0) {
				choice_check_lv.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(mContext, getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initialData() {
		RequestParams params = new RequestParams();
		OrderCheckImpl impl = new OrderCheckImpl();
		if(BaseApplication.patient!=null){
			params.addBodyParameter("userId", BaseApplication.patient.getUserId()
					+ "");// Patinet_id
			impl.gainCheckOrderListByPId(params, this);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		Boolean isSucc = false;
		if (null != object[1])
			isSucc = (Boolean) object[1];
		if (isSucc) {
			Map<String, Object> map;
			ArrayList<CheckOrder> data = null;
			map = (Map<String, Object>) object[2];
			if (map.containsKey("checkOrderList")) {

				checkList = (ArrayList<CheckOrder>) map.get("checkOrderList");
				result_Code = (Integer) map.get("number");
			}
			dismissProgressDialog();
			if (checkList != null && checkList.size() > 0) {
				if (currentPage == 1) {
					// 第一次加载或刷新
					data = checkList;
					if (checkList.size() < pageSize) {
						choice_check_lv.setAutoLoadMore(false);
						choice_check_lv.onLoadMoreComplete();
					}
					choice_check_lv.onRefreshComplete();
				} else {// 加载更多
					data = (ArrayList<CheckOrder>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = checkList;
					else
						data.addAll(checkList);
					choice_check_lv.onLoadMoreComplete();

				}
				adapter.setList(data);
				choice_check_lv.setVisibility(View.VISIBLE);

			} else {
				if (adapter.getList() != null && adapter.getList().size() != 0) {// 加载
					if (currentPage > 1) {
						currentPage--;
					}
					if (checkList != null) {
						if (checkList.size() == 0) {
							choice_check_lv.setAutoLoadMore(false);
						}
					} else {
						Toast.makeText(mContext,
								getString(R.string.request_fail),
								Toast.LENGTH_SHORT).show();
					}
					choice_check_lv.onLoadMoreComplete();
					data = (ArrayList<CheckOrder>) adapter.getList();
					if (result_Code != 0) {
						adapter.mList.clear();
						adapter.notifyDataSetChanged();
						choice_check_lv.setVisibility(View.GONE);
					}

				} else {// 刷新
					choice_check_lv.onRefreshComplete();
					Toast.makeText(mContext, "抱歉，暂时没有数据", Toast.LENGTH_SHORT)
							.show();
					dismissProgressDialog();
				}
			}
			if (data != null) {
				checkList = data;
			}

		} else {
			dismissProgressDialog();
			if (currentPage > 1) {
				currentPage--;
			}else{
				choice_check_lv.setVisibility(View.INVISIBLE);
			}
			choice_check_lv.onLoadMoreComplete();
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRefresh() {
		choice_check_lv.setAutoLoadMore(true);
		currentPage = 1;
		initialData();
	}

	@Override
	public void onLoadMore() {

		if (checkList != null) {
			if (checkList.size() < pageSize
					|| (checkList.size() > pageSize && checkList.size()
							% pageSize != 0)) {
				choice_check_lv.setAutoLoadMore(false);
				choice_check_lv.onLoadMoreComplete();
			} else {
				currentPage++;
				initialData();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {

		CheckOrder order = checkList.get(position - 1);
			String order_id = order.getId();
			Intent check_order_intent = new Intent();
			check_order_intent.putExtra("order_id", order_id);
			check_order_intent.setClass(mContext,
					ChoiceCheckStatusActivity30.class);
			startActivity(check_order_intent);
	}

}

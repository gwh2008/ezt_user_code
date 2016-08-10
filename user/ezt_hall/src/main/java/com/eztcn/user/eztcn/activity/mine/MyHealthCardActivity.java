package com.eztcn.user.eztcn.activity.mine;
import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.HealthCardActivateActivity;
import com.eztcn.user.eztcn.activity.home.HealthCardDetailActivity;
import com.eztcn.user.eztcn.adapter.HealthCardListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @title 健康卡
 * @describe
 * @author ezt
 * @created 2015年1月13日
 */
public class MyHealthCardActivity extends FinalActivity implements IHttpResult,
		OnLoadMoreListener, OnRefreshListener,// , OnItemClickListener,
		OnClickListener {

	@ViewInject(R.id.serverList)
	// , itemClick = "onItemClick"
	private PullToRefreshListView lv;

	private HealthCardListAdapter adapter;

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private ArrayList<HealthCard> list;
	@ViewInject(R.id.myServerDragonCard)
	private View myServerDragonCard;

	private DragonCard card;// 龙卡
	/**
	 * 激活健康卡
	 */
	private TextView activeTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myserver);
		ViewUtils.inject(MyHealthCardActivity.this);
		// 此为线上安卓v2.5.4>我的钱包>我的服务，改为我的医管家
		// TextView tv = loadTitleBar(true, "我的服务", "激 活");
		activeTV = loadTitleBar(true, "我的医管家", "激 活");
		// tv.setBackgroundResource(R.drawable.selector_border_small_white);
		activeTV.setTextColor(Color.WHITE);
		activeTV.setOnClickListener(this);

		adapter = new HealthCardListAdapter(this);
		lv.setAdapter(adapter);
		lv.setCanLoadMore(true);
		lv.setCanRefresh(true);
		lv.setAutoLoadMore(true);
		lv.setMoveToFirstItemAfterRefresh(false);
		lv.setDoRefreshOnUIChanged(false);
		lv.setOnLoadListener(this);
		lv.setOnRefreshListener(this);

		initialData();
		showProgressToast();
	}

	@OnClick(R.id.myServerDragonCard)
	public void dragonCardClick(View v) {
		Intent intent = new Intent();
		if (null == card) {
			intent.setClass(getApplicationContext(),
					DragonToActiveActivity.class);
		} else {
			intent.setClass(getApplicationContext(), DragonCardActivity.class);
		}
		startActivity(intent);
	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		if (BaseApplication.patient == null) {
			Toast.makeText(getApplicationContext(), "暂无服务卡信息",
					Toast.LENGTH_SHORT).show();
			return;
		}
		EztServiceCardImpl api = new EztServiceCardImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId",
				BaseApplication.getInstance().patient.getUserId() + "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		api.getHealthcardList(params, this);
		getHealthDragonInfo();
	}

	/**
	 * 获取龙卡信息
	 */
	private void getHealthDragonInfo() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("CustID", BaseApplication.patient.getEpHiid());
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.getCCbInfo(params, this);
	}

	@Override
	public void result(Object... object) {

		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		ArrayList<HealthCard> data = null;

		switch (type) {
		case HttpParams.GAIN_CCBINFO: {
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("flag")) {
					if ((Boolean) map.get("flag")) {
						// 绑定卡了
						if (map.containsKey("data")) {
							card = (DragonCard) map.get("data");
						}
					} else {
						// 用户信息不存在 未绑定卡
						card = null;
					}
				}
			}

		}
			break;
		case HttpParams.GET_HEALTHCARD_LIST: {

			if (isSuc) {
				list = (ArrayList<HealthCard>) object[2];
				// 成功
				if (list != null && list.size() > 0) {
					if (currentPage == 1) {// 第一次加载或刷新
						lv.setVisibility(View.VISIBLE);
						data = list;
						if (list.size() < pageSize) {
							lv.setAutoLoadMore(false);
							lv.onLoadMoreComplete();
						}
						lv.onRefreshComplete();

					} else {// 加载更多
						data = (ArrayList<HealthCard>) adapter.getList();
						if (data == null || data.size() <= 0)
							data = list;
						else
							data.addAll(list);

						if (list.size() < pageSize) {
							lv.setAutoLoadMore(false);
						}
						lv.onLoadMoreComplete();

					}
					adapter.setList(data);

				} else {
					if (adapter.getList() != null) {// 加载
						lv.setAutoLoadMore(false);
						lv.onLoadMoreComplete();
						data = (ArrayList<HealthCard>) adapter.getList();
					} else {// 刷新
						lv.onRefreshComplete();
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
			break;
		}
	}

	// 加载更多
	@Override
	public void onLoadMore() {
		if (list != null) {
			if (list.size() < pageSize) {
				lv.setAutoLoadMore(false);
				lv.onLoadMoreComplete();

			} else {
				currentPage++;
				initialData();
			}
		}

	}

	// 刷新
	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lv.setAutoLoadMore(true);
		initialData();
	}

	@OnItemClick(R.id.serverList)
	private void serverListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		position = position - 1;
		String carId = adapter.getList().get(position).getId();
		String name = adapter.getList().get(position).getCardName();
		Intent intent = new Intent(MyHealthCardActivity.this,
				HealthCardDetailActivity.class);
		intent.putExtra("carId", carId);
		intent.putExtra("name", name);
		startActivity(intent);
	}
	@Override
	public void onClick(View v) {// 激活卡
		if (activeTV == v)
			startActivity(new Intent(mContext, HealthCardActivateActivity.class));
	}
}

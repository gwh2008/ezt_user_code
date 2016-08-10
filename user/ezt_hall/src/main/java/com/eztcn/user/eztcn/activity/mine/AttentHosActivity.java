package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import xutils.view.annotation.event.OnItemLongClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalDetailActivity;
import com.eztcn.user.eztcn.adapter.HospitalListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.AttentionImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @title 收藏医院
 * @describe
 * @author ezt
 * @created 2015年4月27日
 */
public class AttentHosActivity extends FinalActivity implements
		OnLoadMoreListener, OnRefreshListener, IHttpResult {

	@ViewInject(R.id.hosList)//, itemClick = "onItemClick", itemLongClick = "onItemLongClick"
	private PullToRefreshListView lv;

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	private List<Hospital> hosList;
	private HospitalListAdapter adapter;

	boolean isOnResult = false;// 是否取消收藏返回

	private int clickedPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attenthos);
		ViewUtils.inject(AttentHosActivity.this);
		loadTitleBar(true, "收藏医院", null);

		adapter = new HospitalListAdapter(this);
		lv.setAdapter(adapter);

		lv.setCanLoadMore(true);
		lv.setCanRefresh(true);
		lv.setAutoLoadMore(true);
		lv.setMoveToFirstItemAfterRefresh(false);
		lv.setDoRefreshOnUIChanged(false);
		lv.setOnLoadListener(this);
		lv.setOnRefreshListener(this);
		// getAttentHos();
	}

	@Override
	protected void onResume() {
		super.onResume();
		currentPage = 1;
		adapter.setList(new ArrayList<Hospital>());
		getAttentHos();
	}

	/**
	 * 获取收藏医院
	 */
	public void getAttentHos() {
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		new AttentionImpl().getAttentHos(params, this);
		showProgressToast();
	}
	/**
	 * 取消收藏
	 */
	public void cancelCollect(String collectId) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("id", collectId);
		new AttentionImpl().cancelAttentHos(params, this);
		showProgressToast();
	}
	@OnItemClick(R.id.hosList)
	private void hosListItemClick(AdapterView<?> parent, View view, int position,
			long id) {

				Intent intent = new Intent(this, HospitalDetailActivity.class);
				intent.putExtra("hospital", adapter.getList().get(position - 1));
				startActivity(intent);
	}
	/**
	 * item点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@OnItemLongClick(R.id.hosList)
	private boolean hosListItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (hosList.size() > 0) {
			showOperateView(position - 1);
		}
		return false;
	}
	
	/**
	 * 长按删除
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @retur
	/**
	 * 提示窗口
	 */
	public void showOperateView(final int position) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("提示");
		ab.setMessage("是否取消该医院的收藏？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String cId = hosList.get(position).getCollectId();
				cancelCollect(cId);
				clickedPosition = position;
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				clickedPosition = -1;
			}
		});
		ab.create().show();
	}

	@Override
	public void onLoadMore() {
		if (hosList != null) {
			if (hosList.size() < pageSize) {
				lv.setAutoLoadMore(false);
				lv.onLoadMoreComplete();

			} else {
				currentPage++;
				getAttentHos();
			}
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lv.setAutoLoadMore(true);
		getAttentHos();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) object[1];
		if (!status) {
			Toast.makeText(getApplicationContext(), "服务器繁忙", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		switch (taskID) {
		case HttpParams.GET_ATTENT_HOS_LIST:
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map == null || map.size() == 0) {
				return;
			}
			boolean flag = (Boolean) map.get("flag");
			if (!flag) {
				Toast.makeText(getApplicationContext(),
						map.get("msg").toString(), Toast.LENGTH_SHORT).show();
				return;
			}
			List<Hospital> data = null;
			hosList = (List<Hospital>) map.get("list");
			if (hosList != null && hosList.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					data = hosList;
					if (hosList.size() < pageSize) {
						lv.setAutoLoadMore(false);
						lv.onLoadMoreComplete();
					}
					lv.onRefreshComplete();

				} else {// 加载更多
					data = (List<Hospital>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = hosList;
					else
						data.addAll(hosList);
					lv.onLoadMoreComplete();
				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null) {// 加载
					lv.setAutoLoadMore(false);
					lv.onLoadMoreComplete();
					data = (List<Hospital>) adapter.getList();
					if (isOnResult) {
						adapter.setList(new ArrayList<Hospital>());
						isOnResult = false;
						Toast.makeText(mContext, "暂无收藏的医院", Toast.LENGTH_SHORT)
								.show();
					} else {
						adapter.setList(data);
						if (data.size() == 0) {
							Toast.makeText(mContext, "暂无收藏的医院",
									Toast.LENGTH_SHORT).show();
						}

					}
				} else {// 刷新
					lv.onRefreshComplete();
					Toast.makeText(mContext, "暂无收藏的医院", Toast.LENGTH_SHORT)
							.show();
				}

			}
			if (data != null) {
				hosList = data;
			}

			break;
		case HttpParams.CANCEL_ATTENT_HOS:
			Map<String, Object> cancelMap = (Map<String, Object>) object[2];
			if (cancelMap != null && cancelMap.size() != 0) {
				boolean f = (Boolean) cancelMap.get("flag");
				if (f) {// 成功
					if (hosList != null && hosList.size() > clickedPosition) {
						hosList.remove(clickedPosition);
						adapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							cancelMap.get("msg").toString(), Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}
	}
}

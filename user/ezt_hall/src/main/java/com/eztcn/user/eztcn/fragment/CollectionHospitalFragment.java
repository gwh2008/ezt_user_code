/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.http.RequestParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
 * @author Liu Gang
 * 
 *         2016年1月11日 上午9:47:56
 * 
 */
public class CollectionHospitalFragment extends FinalFragment implements
		OnLoadMoreListener, OnRefreshListener, IHttpResult,
		OnItemClickListener, OnItemLongClickListener {
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private List<Hospital> hosList;
	private HospitalListAdapter adapter;
	boolean isOnResult = false;// 是否取消收藏返回
	private int clickedPosition = -1;
	private PullToRefreshListView collectionHospitialLV;
	private Activity activity;
	private View rootView;

	public static CollectionHospitalFragment newInstance() {
		CollectionHospitalFragment fragment = new CollectionHospitalFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_collection_hospital,
					null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (null != parent) {
			parent.removeAllViews();
		}
		initView();
		return rootView;
	}

	private void initView() {
		collectionHospitialLV = (PullToRefreshListView) rootView
				.findViewById(R.id.collectionHospitialLV);
		adapter = new HospitalListAdapter(activity);
		collectionHospitialLV.setAdapter(adapter);
		collectionHospitialLV.setCanLoadMore(true);
		collectionHospitialLV.setCanRefresh(true);
		collectionHospitialLV.setAutoLoadMore(true);
		collectionHospitialLV.setMoveToFirstItemAfterRefresh(false);
		collectionHospitialLV.setDoRefreshOnUIChanged(false);
		collectionHospitialLV.setOnLoadListener(this);
		collectionHospitialLV.setOnRefreshListener(this);
		collectionHospitialLV.setOnItemClickListener(this);
		collectionHospitialLV.setOnItemLongClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		currentPage = 1;
		adapter.setList(new ArrayList<Hospital>());
		getAttentHos();
	}

	/**
	 * 获取收藏医院
	 */
	public void getAttentHos() {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		new AttentionImpl().getAttentHos(params, this);
		((FinalActivity) activity).showProgressToast();
	}

	/**
	 * 取消收藏
	 */
	public void cancelCollect(String collectId) {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", collectId);
		new AttentionImpl().cancelAttentHos(params, this);
		((FinalActivity) activity).showProgressToast();
	}

	@Override
	public void result(Object... object) {
		((FinalActivity)activity).hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) object[1];
		if (!status) {
			Toast.makeText(activity, "服务器繁忙", Toast.LENGTH_SHORT)
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
				Toast.makeText(activity,
						map.get("msg").toString(), Toast.LENGTH_SHORT).show();
				return;
			}
			List<Hospital> data = null;
			hosList = (List<Hospital>) map.get("list");
			if (hosList != null && hosList.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					data = hosList;
					if (hosList.size() < pageSize) {
						collectionHospitialLV.setAutoLoadMore(false);
						collectionHospitialLV.onLoadMoreComplete();
					}
					collectionHospitialLV.onRefreshComplete();

				} else {// 加载更多
					data = (List<Hospital>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = hosList;
					else
						data.addAll(hosList);
					collectionHospitialLV.onLoadMoreComplete();
				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null&&adapter.getList().size()>0) {// 加载
					collectionHospitialLV.setAutoLoadMore(false);
					collectionHospitialLV.onLoadMoreComplete();
					data = (List<Hospital>) adapter.getList();
					if (isOnResult) {
						adapter.setList(new ArrayList<Hospital>());
						isOnResult = false;
						Toast.makeText(activity, "暂无收藏的医院", Toast.LENGTH_SHORT)
								.show();
					} else {
						adapter.setList(data);
						if (data.size() == 0) {
							Toast.makeText(activity, "暂无收藏的医院",
									Toast.LENGTH_SHORT).show();
						}

					}
				} else {// 刷新
					collectionHospitialLV.onRefreshComplete();
//					Toast.makeText(activity, "暂无收藏的医院", Toast.LENGTH_SHORT)
//							.show();
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
					Toast.makeText(activity,
							cancelMap.get("msg").toString(), Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		collectionHospitialLV.setAutoLoadMore(true);
		getAttentHos();
	}

	@Override
	public void onLoadMore() {
		if (hosList != null) {
			if (hosList.size() < pageSize) {
				collectionHospitialLV.setAutoLoadMore(false);
				collectionHospitialLV.onLoadMoreComplete();
			} else {
				currentPage++;
				getAttentHos();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(activity, HospitalDetailActivity.class);
		intent.putExtra("hospital", adapter.getList().get(position-1));
		startActivity(intent);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (hosList.size() > 0) {
			showOperateView(position-1);
		}
		return false;
	}
	
	
	/**
	 * 提示窗口
	 */
	public void showOperateView(final int position) {
		AlertDialog.Builder ab = new AlertDialog.Builder(activity);
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
}

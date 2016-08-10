/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;

import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.DoctorIndex30Activity;
import com.eztcn.user.eztcn.adapter.AttentDocListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.AttentionDoctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.impl.AttentionImpl;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @author Liu Gang
 * 
 *         2016年1月11日 上午9:47:56
 */
public class CollectionDocFragment extends FinalFragment implements IHttpResult,
		OnLoadMoreListener, OnRefreshListener, OnItemClickListener {
	private PullToRefreshListView collectionDocLV;
	private ArrayList<AttentionDoctor> docList;
	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private AttentDocListAdapter adapter;
	private View rootView;
	private Activity activity;
	private boolean isOnResult = false;// 是否取消关注返回
	public static CollectionDocFragment newInstance() {
		CollectionDocFragment fragment = new CollectionDocFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_collection_doc, null);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		return rootView;
	}

	private void initView() {
		collectionDocLV = (PullToRefreshListView) rootView
				.findViewById(R.id.collectionDocLV);
		adapter = new AttentDocListAdapter(activity, 0);
		collectionDocLV.setAdapter(adapter);

		collectionDocLV.setCanLoadMore(true);
		collectionDocLV.setCanRefresh(true);
		collectionDocLV.setAutoLoadMore(true);
		collectionDocLV.setMoveToFirstItemAfterRefresh(false);
		collectionDocLV.setDoRefreshOnUIChanged(false);
		collectionDocLV.setOnLoadListener(this);
		collectionDocLV.setOnRefreshListener(this);
		collectionDocLV.setOnItemClickListener(this);
		getData();
		((FinalActivity) activity).showProgressToast();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 1) {// 取消关注返回
			isOnResult = true;
			getData();
			((FinalActivity) activity).showProgressToast();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		new AttentionImpl().getAttentDocs(params, this);
	}

	@Override
	public void onLoadMore() {
		if (docList != null) {
			if (docList.size() < pageSize) {
				collectionDocLV.setAutoLoadMore(false);
				collectionDocLV.onLoadMoreComplete();

			} else {
				currentPage++;
				getData();
			}
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		collectionDocLV.setAutoLoadMore(true);
		getData();
	}
	@Override
	public void result(Object... object) {
		((FinalActivity)activity).hideProgressToast();
		boolean isTrue = (Boolean) object[1];
		if (isTrue) {
			ArrayList<AttentionDoctor> data = null;
			docList = (ArrayList<AttentionDoctor>) object[2];
			if (docList != null && docList.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					data = docList;
					if (docList.size() < pageSize) {
						collectionDocLV.setAutoLoadMore(false);
						collectionDocLV.onLoadMoreComplete();
					}
					collectionDocLV.onRefreshComplete();

				} else {// 加载更多
					data = (ArrayList<AttentionDoctor>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = docList;
					else
						data.addAll(docList);
					collectionDocLV.onLoadMoreComplete();
				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null&&adapter.getList().size()>0) {// 加载
					collectionDocLV.setAutoLoadMore(false);
					collectionDocLV.onLoadMoreComplete();
					data = (ArrayList<AttentionDoctor>) adapter.getList();
					if (isOnResult) {
						adapter.setList(new ArrayList<AttentionDoctor>());
						isOnResult = false;
						Toast.makeText(activity, "暂无关注医生", Toast.LENGTH_SHORT)
								.show();
					} else {
						adapter.setList(data);
					}

				} else {// 刷新
					collectionDocLV.onRefreshComplete();
//					Toast.makeText(activity, "暂无关注医生", Toast.LENGTH_SHORT)
//							.show();
				}

			}
			if (data != null) {
				docList = data;
			}

		} else {
			Logger.i("关注医生", object[3]);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AttentionDoctor attentionDoctor=adapter.getList().get(position-1);
		String docId=attentionDoctor.getDeptDocId();
		String deptId=attentionDoctor.getDeptDocId();
		String deptDocId=attentionDoctor.getDeptDocId();
		int ehDockingStatus=attentionDoctor.getEhDockingStatus();
		
		Intent intent = new Intent(activity, DoctorIndex30Activity.class)
		.putExtra("deptId", deptId).putExtra("docId", docId)
		.putExtra("deptDocId", deptDocId).putExtra("ehDockingStatus", ehDockingStatus);
		startActivityForResult(intent, 1);
	}
}

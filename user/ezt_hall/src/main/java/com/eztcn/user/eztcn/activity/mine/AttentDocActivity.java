package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;

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
 * @title 关注医生列表
 * @describe
 * @author ezt
 * @created 2015年1月4日
 */
public class AttentDocActivity extends FinalActivity implements
		IHttpResult, OnLoadMoreListener, OnRefreshListener {//OnItemClickListener, 

	@ViewInject(R.id.attent_doc_lv)//, itemClick = "onItemClick"
	private PullToRefreshListView lv;

	private AttentDocListAdapter adapter;

	private ArrayList<AttentionDoctor> docList;

	private int currentPage = 1;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attent_doc);
		loadTitleBar(true, "已关注的医生", null);
		ViewUtils.inject(AttentDocActivity.this);
		adapter = new AttentDocListAdapter(this, 0);
		lv.setAdapter(adapter);

		lv.setCanLoadMore(true);
		lv.setCanRefresh(true);
		lv.setAutoLoadMore(true);
		lv.setMoveToFirstItemAfterRefresh(false);
		lv.setDoRefreshOnUIChanged(false);
		lv.setOnLoadListener(this);
		lv.setOnRefreshListener(this);
		getData();
		showProgressToast();
	}

	/**
	 * 获取数据
	 */
	private void getData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		if(BaseApplication.patient!=null){
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		}
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		new AttentionImpl().getAttentDocs(params, this);
	}

	@OnItemClick(R.id.attent_doc_lv)
	private void attent_doc_lvItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String docId = adapter.getList().get(position - 1).getDocId() + "";
		String deptId = adapter.getList().get(position - 1).getDeptId();
		String deptDocId = adapter.getList().get(position - 1).getDeptDocId();
		//2015-12-13 医院对接
				int ehDockingStatus=adapter.getList().get(position-1).getEhDockingStatus();
//		Intent intent = new Intent(mContext, DoctorIndexActivity.class)
//				.putExtra("deptId", deptId).putExtra("docId", docId)
//				.putExtra("deptDocId", deptDocId);
		Intent intent = new Intent(mContext, DoctorIndex30Activity.class)
		.putExtra("deptId", deptId).putExtra("docId", docId)
		.putExtra("deptDocId", deptDocId).putExtra("ehDockingStatus", ehDockingStatus).putExtra("canReg", true);
		startActivityForResult(intent, 1);
	}
	@Override
	public void result(Object... object) {
		hideProgressToast();
		boolean isTrue = (Boolean) object[1];

		if (isTrue) {
			ArrayList<AttentionDoctor> data = null;
			docList = (ArrayList<AttentionDoctor>) object[2];
			if (docList != null && docList.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					data = docList;
					if (docList.size() < pageSize) {
						lv.setAutoLoadMore(false);
						lv.onLoadMoreComplete();
					}
					lv.onRefreshComplete();

				} else {
					// 加载更多
					data = (ArrayList<AttentionDoctor>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = docList;
					else
						data.addAll(docList);
					lv.onLoadMoreComplete();
				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null) {// 加载
					lv.setAutoLoadMore(false);
					lv.onLoadMoreComplete();
					data = (ArrayList<AttentionDoctor>) adapter.getList();
					if (isOnResult) {
						adapter.setList(new ArrayList<AttentionDoctor>());
						isOnResult = false;
						Toast.makeText(mContext, "暂无关注医生", Toast.LENGTH_SHORT)
								.show();
					} else {
						adapter.setList(data);
					}

				} else {// 刷新
					lv.onRefreshComplete();
					Toast.makeText(mContext, "暂无关注医生", Toast.LENGTH_SHORT)
							.show();
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
	public void onLoadMore() {
		if (docList != null) {
			if (docList.size() < pageSize) {
				lv.setAutoLoadMore(false);
				lv.onLoadMoreComplete();

			} else {
				currentPage++;
				getData();
			}
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lv.setAutoLoadMore(true);
		getData();
	}

	boolean isOnResult = false;// 是否取消关注返回

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {

		if (arg0 == 1) {// 取消关注返回
			isOnResult = true;
			showProgressToast();
			getData();
		}
		super.onActivityResult(arg0, arg1, arg2);

	}
}

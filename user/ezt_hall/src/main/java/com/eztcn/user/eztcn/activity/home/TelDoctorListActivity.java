package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.TelDoctorListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.PayImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 电话医生列表
 * @describe
 * @author ezt
 * @created 2014年12月11日
 */

public class TelDoctorListActivity extends FinalActivity implements
		OnClickListener,  IHttpResult, OnLoadMoreListener,
		OnRefreshListener {//OnItemClickListener,

	private TextView tvNoopsyche;// 智能筛选

	@ViewInject(R.id.teldoc_lv)//, itemClick = "onItemClick"
	private PullToRefreshListView lvDoc;// 医生列表

	private int online = 0;
	private int hosLevel;
	private int docLevel;
	private int evaluateStatus;
	private String deptCateId;
	private Double eztCurrency;

	private TelDoctorListAdapter adapter;
	private ArrayList<Doctor> docList;
	private int currentPage = 1;// 当前页数
	private int pageSize = 10;// 每页条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teldoc_list);
		ViewUtils.inject(TelDoctorListActivity.this);
		tvNoopsyche = loadTitleBar(true, "电话医生", "筛选");
		tvNoopsyche.setVisibility(View.GONE);
		tvNoopsyche.setOnClickListener(this);
		Drawable rightDrawable = getResources().getDrawable(
				R.drawable.ic_choice);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		tvNoopsyche.setCompoundDrawables(rightDrawable, null, null, null);
		tvNoopsyche.setCompoundDrawablePadding(5);
		adapter = new TelDoctorListAdapter(this);
		lvDoc.setAdapter(adapter);

		lvDoc.setCanLoadMore(true);
		lvDoc.setCanRefresh(true);
		lvDoc.setAutoLoadMore(true);
		lvDoc.setMoveToFirstItemAfterRefresh(false);
		lvDoc.setDoRefreshOnUIChanged(false);
		lvDoc.setOnLoadListener(this);
		lvDoc.setOnRefreshListener(this);

		if (BaseApplication.getInstance().isNetConnected) {
			showProgressToast();
			getDoctorList();
			getUserInfo();
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取用户账户信息
	 */
	private void getUserInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		new PayImpl().getCurrencyMoney(params, this);
		showProgressToast();
	}

	@Override
	public void onClick(View v) {
		startActivityForResult(new Intent(TelDoctorListActivity.this,
				ChoiceNoopsycheActivity.class).putExtra("type", 3), 3);// 1为选择医院进入，2为选择科室进入，3为电话医生进入
	}
		@OnItemClick(R.id.teldoc_lv)
		public void itemClick(AdapterView<?> parent, View view, int position,
				long id) {
		Intent intent = new Intent(TelDoctorListActivity.this,
				DoctorIndexActivity.class);
		Doctor doctor = docList.get(position - 1);
		if (doctor != null) {
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("deptId", doctor.getDocDeptId());
			intent.putExtra("deptDocId", doctor.getDeptDocId());
			
			//2015-12-18 医院对接
			intent.putExtra("ehDockingStatus",doctor.getEhDockingStatus());
		}
		startActivity(intent);
	}
	/**
	 * 获取电话医生列表
	 */
	public void getDoctorList() {
		RequestParams params=new RequestParams();
		params.addBodyParameter("deptCateId", deptCateId);
		params.addBodyParameter("ynOnline", online + "");
		params.addBodyParameter("ynehLevel", hosLevel + "");
		params.addBodyParameter("ynOrderEvalation", evaluateStatus + "");
		params.addBodyParameter("ynOrderEdLevel", docLevel + "");
		params.addBodyParameter("rowsPerPage", pageSize + "");
		params.addBodyParameter("page", currentPage + "");
		new TelDocImpl().getTelDocList(params, this);
	}

	@Override
	public void finish() {
		super.finish();
		SystemPreferences.save(EZTConfig.KEY_SET_THREEHOS, false);
		SystemPreferences.save(EZTConfig.KEY_SET_EVALUATE, false);
		SystemPreferences.save(EZTConfig.KEY_SET_LEVEL, false);
		SystemPreferences.save(EZTConfig.KEY_SET_STATE, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 3 && resultCode == 33) {// 智能筛选返回
			deptCateId = SystemPreferences.getString("bigDeptId");
			boolean bool = SystemPreferences.getBoolean(
					EZTConfig.KEY_SET_STATE, false);
			online = bool ? 1 : 0;
			boolean bool2 = SystemPreferences.getBoolean(
					EZTConfig.KEY_SET_THREEHOS, false);
			hosLevel = bool2 ? 1 : 0;
			boolean bool3 = SystemPreferences.getBoolean(
					EZTConfig.KEY_SET_EVALUATE, false);
			evaluateStatus = bool3 ? 1 : 0;
			boolean bool4 = SystemPreferences.getBoolean(
					EZTConfig.KEY_SET_LEVEL, false);
			docLevel = bool4 ? 1 : 0;
		}

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
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		if (map.get("flag") != null) {
			boolean bool = (Boolean) map.get("flag");
			if (!bool) {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		switch (taskID) {
		case HttpParams.GET_TEL_DOC_LIST:

			hideProgressToast();
			ArrayList<Doctor> data = null;
			docList = (ArrayList<Doctor>) map.get("doctorList");
			if (docList == null) {
				docList = new ArrayList<Doctor>();
			}
			if (docList != null && docList.size() > 0) {
				if (currentPage == 1) {// 第一次加载或刷新
					data = docList;
					if (docList.size() < pageSize) {
						lvDoc.setAutoLoadMore(false);
						lvDoc.onLoadMoreComplete();
					}
					lvDoc.onRefreshComplete();

				} else {// 加载更多
					data = (ArrayList<Doctor>) adapter.getList();
					if (data == null || data.size() <= 0)
						data = docList;
					else
						data.addAll(docList);

					if (docList.size() < pageSize) {
						lvDoc.setAutoLoadMore(false);
					}
					lvDoc.onLoadMoreComplete();

				}
				adapter.setList(data);

			} else {
				if (adapter.getList() != null) {// 加载
					lvDoc.setAutoLoadMore(false);
					lvDoc.onLoadMoreComplete();
					data = (ArrayList<Doctor>) adapter.getList();
				} else {// 刷新
					lvDoc.onRefreshComplete();
				}

			}
			if (data != null) {
				docList = data;
			}

			break;
		case HttpParams.GET_CURRENCY_MONEY:// 获取账户信息
			boolean flag = (Boolean) map.get("flag");
			if (flag) {
				eztCurrency = (Double) map.get("remain");
				if (eztCurrency == null) {
					eztCurrency = 0.0;
				}
				if (adapter != null) {
					adapter.setUserCurrency(eztCurrency);
				}
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		currentPage = 1;// 还原页值
		lvDoc.setAutoLoadMore(true);
		getDoctorList();
	}

	@Override
	public void onLoadMore() {
		if (docList != null) {
			if (docList.size() < pageSize) {
				lvDoc.setAutoLoadMore(false);
				lvDoc.onLoadMoreComplete();

			} else {
				currentPage++;
				getDoctorList();
			}
		}

	}
}

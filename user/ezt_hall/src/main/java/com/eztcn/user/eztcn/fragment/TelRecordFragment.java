package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.EvaluateActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter;
import com.eztcn.user.eztcn.adapter.Telphone_RecordAdapter;
import com.eztcn.user.eztcn.adapter.Telphone_RecordAdapter.onPhoneRecordListener;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.LInsItem;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;
import com.eztcn.user.eztcn.bean.PhoneRecordBean;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

public class TelRecordFragment extends FinalFragment implements 
		OnRefreshListener, OnItemClickListener,IHttpResult{
	private View rootView;
	private List<Record_Info> list;// 预约挂号
	private Order_RecordAdapter adapter;
	private PullToRefreshListView dataLV;
	private Activity mActivity;
	private int itemType;
	public static final int ITEMTYPE_READY_REGED = 0;
	public static final int ITEMTYPE_READY_BACKNUM = 4;
	public static final int READY_VISIT = 6, READY_MISS = 7,
			READY_BACK_NUM = 4;

	private boolean refreshIng = false;
	private List<PhoneRecordBean> telList;
	private Telphone_RecordAdapter telAdapter;

	public View onCreateView(android.view.LayoutInflater inflater,
			android.view.ViewGroup container,
			android.os.Bundle savedInstanceState) {
		mActivity = getActivity();
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.record, null);// 缓存Fragment
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		return rootView;
	}

	public static TelRecordFragment newInstance(int itemType) {
		TelRecordFragment fragment = new TelRecordFragment();
		Bundle b = new Bundle();
		b.putInt("itemType", itemType);
		fragment.setArguments(b);
		return fragment;
	}

	private void initView() {
		dataLV = (PullToRefreshListView) rootView.findViewById(R.id.dataLV);
		dataLV.setCanLoadMore(true);
		dataLV.setCanRefresh(true);
		dataLV.setOnRefreshListener(this);
		dataLV.setDoRefreshOnUIChanged(false);
	}

	/**
	 * 获取通话记录
	 * @param status
	 *            -1取消通话0待通话1通话中2通话成功3未接通(含未接占线)4扣费成功
	 */
	private void getCallRecord(int status) {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("crStatus", status + "");
		params.addBodyParameter("rowsPerPage", "20");
		params.addBodyParameter("page", "1");
		new TelDocImpl().getTelDocRecord(params, this);
		((FinalActivity) mActivity).showProgressToast();
	}

	/**
	 * 初始化电话医生记录adapter
	 */
	public void initTelPhoneRecord() {

		if (telList == null) {
			telList = new ArrayList<PhoneRecordBean>();
		}
		telAdapter = new Telphone_RecordAdapter(mActivity, itemType);
		dataLV.setAdapter(telAdapter);
		telAdapter.setList(telList);
		telAdapter.setOnPhoneRecordListener(new onPhoneRecordListener() {

			@Override
			public void onRecordClick(View v, int position, int type) {
				PhoneRecordBean record = telList.get(position);
				if (record == null) {
					return;
				}
				if (type == 5) {
					cancelPhoneOrder(record.getCallRegisterId());
				} else {
					Intent intent = new Intent(mActivity,
							EvaluateActivity.class);
					intent.putExtra("enterType", 2);
					intent.putExtra("record", record);
					startActivity(intent);
				}
			}
		});
		if (telList.size() == 0) {
			Toast.makeText(mActivity, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		dataLV.setOnItemClickListener(TelRecordFragment.this);
	}

	/**
	 * 取消电话预约
	 * 
	 * @param registerId
	 *            电话医生预约ID
	 */
	public void cancelPhoneOrder(final int registerId) {
		AlertDialog.Builder ab = new AlertDialog.Builder(mActivity);
		ab.setTitle("温馨提示");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setMessage("确定取消电话医生预约？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params=new RequestParams();
				params.addBodyParameter("registerId", registerId + "");
				new TelDocImpl().cancelPhoneOrder(params,
						TelRecordFragment.this);
				((FinalActivity) mActivity).showProgressToast();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}

	@Override
	public void onRefresh() {
		refreshIng = true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {
		((FinalActivity) mActivity).hideProgressToast();
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
			Toast.makeText(mActivity, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		String msg = "";
		switch (taskID) {
		case HttpParams.GET_TEL_DOC_RECORD:
			telList = (List<PhoneRecordBean>) map.get("list");
			break;

		case HttpParams.CANCEL_PHONEORDER:// 取消电话医生预约
			boolean cancelFlag = (Boolean) map.get("flag");
			if (cancelFlag) {
				msg = "取消成功";
				getCallRecord(0);
			} else {
				msg = map.get("msg").toString();
			}
			Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (itemType) {
		case 5:// 电话医生-已预约
		case 6:// 电话医生-已通话
		case 9:// 电话医生-已关闭
			initTelPhoneRecord();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根

	}
}

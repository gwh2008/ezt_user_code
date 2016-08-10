package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.AlreadyRegDetailActivity;
import com.eztcn.user.eztcn.activity.home.EvaluateActivity;
import com.eztcn.user.eztcn.activity.home.OrderDetailActivity;
import com.eztcn.user.eztcn.activity.home.WriteLetterActivity;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter.onRecordClickListener;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.BackValidateDialog;
import com.eztcn.user.eztcn.customView.BackValidateDialog.CodeSure;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

public class RegRecordFragment extends FinalFragment implements
		onRecordClickListener, OnItemClickListener, CodeSure, IHttpResult,
		OnRefreshListener {
	private View rootView;
	private List<Record_Info> list;// 预约挂号
	private Order_RecordAdapter adapter;
	private PullToRefreshListView dataLV;
	private Activity mActivity;
	private int itemType;
	public static final int ITEMTYPE_READY_REGED = 0;
	public static final int ITEMTYPE_READY_VISIT = 2;
	public static final int ITEMTYPE_READY_BACKNUM = 4;
	public static final int ITEMTYPE_READY_MISS = 3;
	private static final int ENTRY_REGISTER_ORDER = 3;// 预约记录
	private static final int ENTRY_READY_VISIT = 6;// 已就诊记录
	private static final int ENTRY_REGISTER_FAILS = 7;// 爽约记录
	private static final int ENTRY_REGISTER_BACKNUM = 4;// 退号记录

	private BackValidateDialog validateDialog;
	private String temp_regId, temp_pfId;
	private boolean refreshIng = false;

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
		initListRecord();
		loadRecordData();
		return rootView;
	}

	/**
	 * 2016-02-15 加载记录数据 统一管理 加载页面和下拉刷新时候调用
	 */
	private void loadRecordData() {
		int entryType = 0;
		switch (itemType) {

		case ITEMTYPE_READY_REGED:
			entryType = ENTRY_REGISTER_ORDER;
			break;
		case ITEMTYPE_READY_VISIT: {// 2016-02-15我的记录已就诊
			entryType = ENTRY_READY_VISIT;
		}
			break;
		case ITEMTYPE_READY_MISS: {// 2016-02-15已爽约
			entryType = ENTRY_REGISTER_FAILS;
		}
			break;
		case ITEMTYPE_READY_BACKNUM: {// 2016-02-15已退号
			entryType = ENTRY_REGISTER_BACKNUM;
		}
			break;
		}
		setOrderRecord(entryType);
	}

	public static RegRecordFragment newInstance(int itemType) {
		RegRecordFragment fragment = new RegRecordFragment();
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
		dataLV.setDoRefreshOnUIChanged(true);
	}

	/**
	 * 初始化预约挂号adapter
	 * 
	 * @param itemType
	 *            0、已预约 1、已登记 2、已就诊 3、爽约 4、已退号
	 */
	private void initListRecord() {
		itemType = getArguments().getInt("itemType");
		if (list == null) {
			list = new ArrayList<Record_Info>();
		}
		adapter = new Order_RecordAdapter(mActivity, itemType);
		dataLV.setAdapter(adapter);
		adapter.setList(list);
		adapter.setOnRecordClickListener(this);
		if (list.size() == 0) {
			if (itemType == 0) {// 没有已预约数据时相应的预约提醒消息清除
				WhereBuilder whereBuilder = WhereBuilder.b("typeId", "=",
						"register");
				EztDb.getInstance(mActivity).delDataWhere(new MsgType(),
						whereBuilder);
			}
			// Toast.makeText(mActivity, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		dataLV.setOnItemClickListener(this);
	}

	@Override
	public void onRecordClick(View v, int position, int type) {

		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mActivity, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (list == null) {
			return;
		}
		Record_Info record = list.get(position);
		if (record == null) {
			return;
		}
		Intent intent = new Intent();
		switch (type) {
		case 1:// 退号
			backNums(record.getPlatformId() + "", record.getId() + "");
			break;
		case 2:
			intent.setClass(mActivity, EvaluateActivity.class);
			intent.putExtra("record", record);
			startActivity(intent);
			break;
		case 3:
			intent.setClass(mActivity, WriteLetterActivity.class);
			intent.putExtra("thanksType", "预约挂号");
			intent.putExtra("record", record);
			startActivity(intent);
			break;
		}

	}

	public void backNums(final String pfId, final String regId) {
		AlertDialog.Builder ab = new AlertDialog.Builder(mActivity);
//		ab.setTitle("退号操作");
		ab.setTitle("取消订单");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setPositiveButton("退号", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 2015-12-30 退号获取图片验证码
				temp_regId = regId;
				temp_pfId = pfId;
				validateDialog = new BackValidateDialog(mActivity, Integer
						.parseInt(regId), BaseApplication.patient.getUserId());// 2015-12-30退号获取验证码
				validateDialog.setSure(RegRecordFragment.this);
				validateDialog.show();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp_regId = "";
				temp_pfId = "";
			}
		});
		ab.create().show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getParent() == dataLV) {// 记录列表进入
			Intent intent = new Intent();
			switch (itemType) {
			case ITEMTYPE_READY_REGED: {
				intent.setClass(mActivity, AlreadyRegDetailActivity.class);
				intent.putExtra("id", list.get(position - 1).getId());
				startActivityForResult(intent, 22);
			}
				break;
			default: {
				intent.setClass(mActivity, OrderDetailActivity.class);
				intent.putExtra("enterType", itemType);
				intent.putExtra("record", list.get(position- 1));
				startActivityForResult(intent, 1);
			}
				break;
			}
			return;
		}
	}

	@Override
	public void onSureClick(String valideCode) {
		if (valideCode.isEmpty()) {
			Toast.makeText(mActivity, "请输入验证码", Toast.LENGTH_SHORT).show();
		} else {
			validateDialog.dismiss();
			RegistratioImpl impl = new RegistratioImpl();
			// HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params = new RequestParams();
			params.addBodyParameter("regId", temp_regId);
			params.addBodyParameter("pfId", temp_pfId);
			params.addBodyParameter("mobile",
					BaseApplication.patient.getEpMobile());
			params.addBodyParameter("code", valideCode);
			params.addBodyParameter("userId",
					"" + BaseApplication.patient.getUserId());// 2015-12-30
			// 退号时候要求加
			// 用户id
			impl.backNumber(params, RegRecordFragment.this);
			((FinalActivity) mActivity).showProgressToast();
		}
	}

	@Override
	public void result(Object... object) {
		((FinalActivity) mActivity).hideProgressToast();
		if (refreshIng) {
			dataLV.onRefreshComplete();
			refreshIng = false;
		}
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
		if (!flag) {
			if (taskID == HttpParams.BACKNUMBER) {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
			}
			Toast.makeText(mActivity, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (taskID) {

		case HttpParams.GET_REG_RECORD:
			list = (List<Record_Info>) map.get("list");
			break;

		case HttpParams.BACKNUMBER:
			String msg;
			if (flag) {
				msg = "退号成功";
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				// EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
				// "msgId = " + this.regId); // 退号删除相应的预约提醒消息
				WhereBuilder b = WhereBuilder.b("msgId", "=", temp_regId);
				EztDb.getInstance(mActivity).delDataWhere(new MessageAll(), b); // 退号删除相应的预约提醒消息
			} else {
				// 退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
						+ temp_regId);
				msg = map.get("msg").toString();
			}
			Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
			setOrderRecord(ENTRY_REGISTER_ORDER);

			return;
		}

		switch (itemType) {
		case 0:
		case 2:
		case 3:
		case 4:
			initListRecord();
			return;
		}
	}

	/**
	 * 获取预约记录(3、预约记录 6、已就诊记录)
	 */
	public void setOrderRecord(int entryType) {

		if (BaseApplication.patient == null) {
			return;
		}
		RegistratioImpl impl = new RegistratioImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId()
				+ "");
		params.addBodyParameter("rstatus", entryType + "");
		params.addBodyParameter("rowsPerPage", "100");
		params.addBodyParameter("page", "1");
		impl.getRegRecord(params, this);
		((FinalActivity) mActivity).showProgressToast();
	}

	@Override
	public void onRefresh() {
		refreshIng = true;
		loadRecordData();
	}
}

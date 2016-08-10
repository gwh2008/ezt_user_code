package com.eztcn.user.eztcn.activity.home;

import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.RegisteredDetailAdapter;
import com.eztcn.user.eztcn.adapter.RegisteredDetailAdapter.onOrderClicklistener;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 登记详情(已登记模块)
 * @describe 已登记进入详细信息
 * @author ezt
 * @created 2014年12月11日
 */
public class AlreadyRegDetailActivity extends FinalActivity implements
		OnClickListener, IHttpResult, onOrderClicklistener {

	private ImageView delete;
	@ViewInject(R.id.regDate)
	private TextView regDate;// 登记时间
	@ViewInject(R.id.persionName)
	private TextView persionName;
	@ViewInject(R.id.seeTime)
	private TextView seeTime;// 期望就诊时间
	@ViewInject(R.id.seeArea)
	private TextView seeArea;// 就诊地区
	@ViewInject(R.id.dept)
	private TextView dept;
	@ViewInject(R.id.payType)
	private TextView payType;// 缴费方式
	@ViewInject(R.id.seeStatus)
	private TextView seeStatus;// 就诊状态
	@ViewInject(R.id.uploadMedical)
	private TextView uploadMedical;// 病历
	@ViewInject(R.id.illnessDescribe)
	private TextView illnessDescribe;// 病情描述

	@ViewInject(R.id.affirmOrder)//, click = "onClick"
	private Button affirmOrder;
	@ViewInject(R.id.registeredList)//, itemClick = "onItemClick"
	private ListView registeredList;

	@ViewInject(R.id.all_layout)
	private LinearLayout allLayout;// 所有布局

	private List<Record_Info> list;
	private String recordId; // 抢单id
	private String regId;// 挂号id
	private int id;// 登记id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alreadyregdetail);
		ViewUtils.inject(AlreadyRegDetailActivity.this);
		delete = loadTitleBar(true, "登记详情", R.drawable.ic_del);
		delete.setVisibility(View.GONE);
		delete.setOnClickListener(this);
		id = getIntent().getIntExtra("id", 0);
		showProgressToast();
		initRecordInfo();
		getRegedDoctor();
	}

	/**
	 * 初始化登记信息
	 */
	public void initRecordInfo() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("rId", id);
//		params.put("ynShowDoctor", "0");
//		IRegistrationApi api = new RegistratioImpl();
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("rId", ""+id);
		params.addBodyParameter("ynShowDoctor", "0");
		RegistratioImpl api = new RegistratioImpl();
		
		api.orderRegisterInfo(params, this);
	}

	/**
	 * 初始化已登记医生列表
	 */
	public void initRegedList() {
		RegisteredDetailAdapter adapter = new RegisteredDetailAdapter(this);
		registeredList.setAdapter(adapter);
		adapter.setList(list);
		adapter.setOnOrderClick(this);
	}

	/**
	 * 获取抢单医生列表
	 */
	public void getRegedDoctor() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("rId", id);
//		params.put("ynShowDoctor", "1");
		
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("rId", ""+id);
		params.addBodyParameter("ynShowDoctor", "1");
		new RegistratioImpl().getRegedDoctor(params, this);
	}

	/**
	 * 确定预约
	 */
	public void affirmOrder() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("rId", id);
//		params.put("rdId", recordId);
		
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("rId", ""+id);
		params.addBodyParameter("rdId", recordId);
		new RegistratioImpl().affirmOrder(params, this);
		showProgressToast();
	}

	/**
	 * 取消预约登记
	 */
	public void cancelRegedRecord() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("rId", id);
		RequestParams params=new RequestParams();
		params.addBodyParameter("rId", ""+id);
		new RegistratioImpl().cancelRegedRecord(params, this);
		showProgressToast();
	}

	/**
	 * 提示
	 */
	public void hintView() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("提示");
		ab.setMessage("确定取消预约登记？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelRegedRecord();
			}
		});
		ab.setNegativeButton("取消", null);
		ab.create().show();
	}

	@Override
	public void orderClick(int position) {
		recordId = list.get(position).getId() + "";
		affirmOrder();
	}
	@Override
	public void onClick(View v) {
		if (v == delete) {// 删除
			hintView();
		} else if (v == affirmOrder) {
			Toast.makeText(mContext, "请求已发送,稍后请留意下发的预约短信", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}
	@OnItemClick(R.id.registeredList)
	private void  itemClick(AdapterView<?> parent, View view, int position,
			long id){
		Intent intent = new Intent(this, DoctorIndex30Activity.class);
		intent.putExtra("docId", list.get(position).getDoctorId() + "");
		intent.putExtra("deptId", list.get(position).getDeptId() + "");
		startActivity(intent);
	}
	/**
	 * listView itemClick
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
//	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		Intent intent = new Intent(this, DoctorIndexActivity.class);
//		intent.putExtra("docId", list.get(position).getDoctorId() + "");
//		intent.putExtra("deptId", list.get(position).getDeptId() + "");
//		startActivity(intent);
//	}

	@Override
	public void result(Object... object) {

		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		if (taskID == null) {
			return;
		}
		Map<String, Object> map = (Map<String, Object>) object[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (!flag) {
			Toast.makeText(mContext, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
			hideProgressToast();
			return;
		}
		switch (taskID) {
		case HttpParams.GETREGEDDOCTOR:
			hideProgressToast();
			list = (List<Record_Info>) map.get("list");
			initRegedList();
			break;
		case HttpParams.CANCELREGEDRECORD:// 取消预约登记
			hideProgressToast();
			if (flag) {
				setResult(22);
				finish();
			}
			break;
		case HttpParams.AFFIRMORDER:// 确认预约
			hideProgressToast();
			regId = map.get("regId").toString();
			Intent intent = new Intent(AlreadyRegDetailActivity.this,
					OrderRegWaitActivity.class);
			intent.putExtra("registerId", regId);
			startActivity(intent);
			break;

		case HttpParams.GET_CHECKIN_DETAILS:// 获取预约登记详情
			hideProgressToast();
			OrderRegisterRecord record = (OrderRegisterRecord) map.get("data");
			if (record == null) {
				return;
			}
			allLayout.setVisibility(View.VISIBLE);
			delete.setVisibility(View.VISIBLE);
			regDate.setText(record.getCreateTime());
			persionName.setText(record.getPatientName());
			String orderDate = record.getOrderDate();
			if (orderDate != null && !orderDate.equals("")) {
				seeTime.setText(orderDate.substring(0, orderDate.indexOf(" ")));
			}
			seeArea.setText(record.getCity() + "  " + record.getCounty());
			dept.setText(record.getDept());
			payType.setText(record.getPayType() == 0 ? "自费" : "医保");
			seeStatus.setText(record.getSeeDocStatus() == 0 ? "初诊" : "复诊");
			uploadMedical.setText(record.getMedicalNum());
			illnessDescribe.setText(record.getIllDiscribe());

			break;
		}
	}

}

package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.PhoneDoctorTimeAdapter;
import com.eztcn.user.eztcn.adapter.PhoneDoctorTimeAdapter.TimeClickListener;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.CallTimeList;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.TelDocState;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 预约通话时间
 * @describe
 * @author ezt
 * @created 2015年6月18日
 */
public class PhoneDoctorTimeActivity extends FinalActivity implements
		IHttpResult, TimeClickListener {

	@ViewInject(R.id.doctorName)
	private TextView doctorName;
	@ViewInject(R.id.timeList)
	private ListView timeList;

	private List<CallTimeList> list;
	private PhoneDoctorTimeAdapter adapter;
	private List<CallTimeList> showList;

	private Doctor doctor;
	private String doctorId = null;

	/***** popupwindow ********************************/
	private PopupWindow pWindow;
	private ListView pop_list;
	private List<CallTimeList> popupList;
	private PopupWindowAdapter popupAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonedoctortime);
		ViewUtils.inject(PhoneDoctorTimeActivity.this);
		init(savedInstanceState);
		getOrderTime(doctorId);
		configOrderTime();
	}

	/**
	 * 初始化医生信息
	 */
	public void init(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		int type = 1;
		if (savedInstanceState != null) {
			type = savedInstanceState.getInt("type");
			doctor = (Doctor) savedInstanceState.getSerializable("info");
		}
		if (type == 2) {
			TelDocState state = (TelDocState) savedInstanceState
					.getSerializable("state");
			if (doctor != null && state != null) {
				String mt = state.getCiGuaranteedRate();
				doctor.setFees(Double.parseDouble(mt));
				String sr = state.getCiStandardRate();
				doctor.setMoneyOfMinute(Double.parseDouble(sr));
				doctor.setMinTime(Integer.parseInt(state.getCiGuaranteedTime()));
			}
		}
		if (doctor != null) {
			doctorId = doctor.getId();
			doctorName.setText(doctor.getDocName());
		}
		initListData();
	}

	public void initListData() {
		adapter = new PhoneDoctorTimeAdapter(this);
		timeList.setAdapter(adapter);
		adapter.setOnTimeClickListener(this);
	}

	/**
	 * 获取可预约时间段
	 */
	public void getOrderTime(String docId) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("doctorId", docId);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("doctorId", docId);
		new TelDocImpl().getTelDocTime(params, this);
		showProgressToast();
	}

	/**
	 * @title 根据日期排序
	 * @describe
	 * @author ezt
	 * @created 2015年6月25日
	 */
	class TimeComparator implements Comparator<CallTimeList> {
		Date date1;
		Date date2;

		@SuppressWarnings("deprecation")
		@Override
		public int compare(CallTimeList lhs, CallTimeList rhs) {
			date1 = new Date(lhs.getDate().replace("-", "/"));
			date2 = new Date(rhs.getDate().replace("-", "/"));
			if (date1.getTime() > date2.getTime()) {
				return 1;
			} else if (date1.getTime() == date2.getTime()) {
				date1 = new Date(lhs.getBeginTime().replace("-", "/"));
				date2 = new Date(rhs.getBeginTime().replace("-", "/"));
				if (date1.after(date2)) {
					return 1;
				}
			}
			return -1;
		}
	}

	/**
	 * 合并相同星期的list
	 */
	private void configTime() {
		showList = new ArrayList<CallTimeList>();
		int week = -1;
		int temp;
		CallTimeList tempList;
		for (int i = 0; i < list.size(); i++) {
			tempList = list.get(i);
			temp = tempList.getWeek();
			if (temp != week) {
				showList.add(tempList);
			}
			week = temp;
		}
		adapter.setList(showList);
	}

	/**
	 * 设置时间段窗口
	 */
	public void configOrderTime() {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setGravity(Gravity.CENTER);
		pop_list = new ListView(mContext);
		pop_list.setLayoutParams(new LayoutParams(getWindowWidth() - 50,
				LayoutParams.WRAP_CONTENT));
		layout.addView(pop_list);
		pop_list.setBackgroundColor(Color.WHITE);
		pWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		ColorDrawable cd = new ColorDrawable(Color.parseColor("#b0000000"));
		pWindow.setBackgroundDrawable(cd);
		pWindow.setAnimationStyle(R.style.popup_anim);
		pWindow.setFocusable(true);
		popupAdapter = new PopupWindowAdapter(this);
		pop_list.setAdapter(popupAdapter);
		pop_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pWindow.dismiss();
				Intent intent = new Intent(PhoneDoctorTimeActivity.this,
						AppointConsultActivity.class);
				intent.putExtra("time", popupList.get(position));
				intent.putExtra("type", 1);
				intent.putExtra("info", doctor);
				startActivity(intent);
			}
		});
	}

	/**
	 * 提取相应数据显示
	 * 
	 * @param week
	 * @param time
	 */
	public void showOrderTime(int week, int time) {
		popupList = new ArrayList<CallTimeList>();
		List<String> tempList = new ArrayList<String>();
		CallTimeList temp;
		String begin;
		String end;
		for (int i = 0; i < list.size(); i++) {
			temp = list.get(i);
			if (temp.getWeek() == week && temp.getTimeMark() == time) {
				popupList.add(temp);
				begin = temp.getBeginTime();
				end = temp.getEndTime();
				tempList.add(begin.substring(begin.indexOf(" "),
						begin.lastIndexOf(":"))
						+ " - "
						+ end.substring(end.indexOf(" "), end.lastIndexOf(":")));
			}
		}
		popupAdapter.setList(tempList);

	}

	@Override
	public void onTimeClick(View v, int position, int week, int time) {
		showOrderTime(week, time);
		if (popupAdapter.getList() == null
				|| popupAdapter.getList().size() == 0) {
			Toast.makeText(getApplicationContext(), "暂无该时段的可预约时间",
					Toast.LENGTH_SHORT).show();
		} else {
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
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
			Toast.makeText(mContext, obj[2] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (!flag) {
			Toast.makeText(mContext, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (taskID == HttpParams.GET_TEL_DOC_TIME) {
			list = (List<CallTimeList>) map.get("list");
			if (list == null) {
				list = new ArrayList<CallTimeList>();
			}
			TimeComparator comparator = new TimeComparator();
			Collections.sort(list, comparator);
			configTime();
		}
	}
}

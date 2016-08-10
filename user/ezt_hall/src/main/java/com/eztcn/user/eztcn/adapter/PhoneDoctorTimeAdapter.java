package com.eztcn.user.eztcn.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.CallTimeList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @title 电话预约时间
 * @describe
 * @author ezt
 * @created 2015年6月18日
 */
public class PhoneDoctorTimeAdapter extends BaseArrayListAdapter<CallTimeList>
		implements OnClickListener {

	private Context mContext;
	private TimeClickListener listener;
	private SimpleDateFormat sdf;

	public PhoneDoctorTimeAdapter(Activity context) {
		super(context);
		mContext = context;
		sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		CallTimeList info;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_calldoctor_time, null);
			holder.week = (TextView) convertView.findViewById(R.id.week);
			holder.date = (TextView) convertView.findViewById(R.id.orderDate);
			holder.morning = (TextView) convertView.findViewById(R.id.morning);
			holder.afternoon = (TextView) convertView
					.findViewById(R.id.afternoon);
			holder.evening = (TextView) convertView.findViewById(R.id.evening);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		info = mList.get(position);
		holder.week.setText(parseWeek(info.getWeek()));
		holder.date.setText(info.getDate());
		holder.morning.setOnClickListener(this);
		holder.afternoon.setOnClickListener(this);
		holder.evening.setOnClickListener(this);
		holder.morning.setTag(position + "," + info.getWeek() + "," + 0);
		holder.afternoon.setTag(position + "," + info.getWeek() + "," + 1);
		holder.evening.setTag(position + "," + info.getWeek() + "," + 2);
		return convertView;
	}

	class ViewHolder {
		TextView week;
		TextView date;
		TextView morning;
		TextView afternoon;
		TextView evening;
	}

	/**
	 * 解析星期
	 * 
	 * @param week
	 * @return
	 */
	public String parseWeek(int week) {
		String temp = "";
		switch (week) {
		case 0:
			temp = "星期天";
			break;
		case 1:
			temp = "星期一";
			break;
		case 2:
			temp = "星期二";
			break;
		case 3:
			temp = "星期三";
			break;
		case 4:
			temp = "星期四";
			break;
		case 5:
			temp = "星期五";
			break;
		case 6:
			temp = "星期六";
			break;
		}

		return temp;
	}

	@Override
	public void onClick(View v) {
		String[] temp = v.getTag().toString().split(",");
		int position = Integer.parseInt(temp[0]);
		int week = Integer.parseInt(temp[1]);
		listener.onTimeClick(v, position, week, Integer.parseInt(temp[2]));
	}

	/**
	 * 时间段点击事件
	 */
	public void setOnTimeClickListener(TimeClickListener listener) {
		this.listener = listener;
	}

	/**
	 * @title 时间段点击事件接口
	 * @describe
	 * @author ezt
	 * @created 2015年6月23日
	 */
	public interface TimeClickListener {

		/**
		 * 时间点击事件
		 * 
		 * @param v
		 *            view
		 * @param position
		 *            list位置
		 * @param week
		 *            星期
		 * @param time
		 *            (0上午/1中午/2下午)
		 */
		public void onTimeClick(View v, int position, int week, int time);
	}
}

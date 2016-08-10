package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @title 医院adapter
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */

public class HospitalDataAdapter extends BaseArrayListAdapter<Hospital> {
	//private final int NORMAL=1;//(正常，非对接状态) 2015-12-17
	private final int WARING=0;//(警告，医院为对接状态对接状态) 2015-12-17
	private int selectedPosition = -1;
	public HospitalDataAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView hosName;
		RelativeLayout layout;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hospital, null);
			holder.hosName = (TextView) convertView
					.findViewById(R.id.item_hos_name);
			holder.layout = (RelativeLayout) convertView
					.findViewById(R.id.colorlayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Hospital hos = mList.get(position);
		
		
		//2015-12-17 医院对接中
//		holder.hosName.setText(hos.gethName());
		
		String showName=hos.getEhDockingStatus()==WARING?hos.gethName()+"("+hos.getEhDockingStr()+")":hos.gethName();
		holder.hosName.setText(showName);

		// 设置选中效果
		if (selectedPosition == position) {
			holder.hosName.setTextColor(mContext.getResources().getColor(
					R.color.main_color));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.choicehoiceAll));
		} else {
			holder.hosName.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					android.R.color.white));
//			holder.layout.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}
	
	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

}

package com.eztcn.user.eztcn.adapter;

import java.text.DecimalFormat;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDictionaryDB;

/**
 * @title 医院adapter
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */

public class HospitalListAdapter extends BaseArrayListAdapter<Hospital> {
	private final int WARING=0;//(警告，医院为对接状态对接状态) 2015-12-17
	private int selectedPosition = -1;

	private final Bitmap defaultBit;

	public HospitalListAdapter(Activity context) {
		super(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_info_default);
	}

	class ViewHolder {
		TextView hosName;
		TextView hosLevel;// 医院等级
		TextView patientCount;// 患者数
		ImageView logo;
		LinearLayout layout;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hospital_list,
					null);
			holder.hosName = (TextView) convertView
					.findViewById(R.id.item_hos_name);
			holder.hosLevel = (TextView) convertView
					.findViewById(R.id.hosLevel);
			holder.patientCount = (TextView) convertView
					.findViewById(R.id.patientCount);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.colorlayout);
			holder.logo = (ImageView) convertView.findViewById(R.id.logo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Hospital hos = mList.get(position);
		//2015-12-17 医院对接中
//		holder.hosName.setText(hos.gethName());
		
		String showName=hos.getEhDockingStatus()==WARING?hos.gethName()+"("+hos.getEhDockingStr()+")":hos.gethName();
		holder.hosName.setText(showName);
		holder.patientCount.setText(caculateCount(hos.getId()));
		// 设置选中效果
		if (selectedPosition == position) {
			holder.hosName.setTextColor(mContext.getResources().getColor(
					R.color.main_color));
			holder.layout.setBackgroundColor(mContext.getResources().getColor(
					R.color.light_gray));
		} else {
			holder.hosName.setTextColor(mContext.getResources().getColor(
					R.color.dark_black));
			holder.layout.setBackgroundColor(Color.TRANSPARENT);
		}
		String hLevel = EztDictionaryDB.getInstance(mContext).getLabelByTag(
				"ehLevel", hos.getHosLevel());
		holder.hosLevel.setText(hLevel);
		String photo = EZTConfig.HOS_PHOTO + "hosView" + hos.getId() + ".jpg";
//		FinalBitmap.create(mContext).display(holder.logo, photo, defaultBit,
//				defaultBit);
		
		BitmapUtils 
		bitmapUtils=new BitmapUtils(mContext);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		        bitmapUtils.configDefaultLoadingImage(defaultBit);
			    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
			    bitmapUtils.display(holder.logo, photo);
		
		return convertView;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	// public String caculateCount() {
	// String count = "";
	// Random random = new Random();
	// int temp = random.nextInt(20) + 1;
	// int r = random.nextInt(10);
	// count = temp + "." + r + "万";
	// return count;
	// }
	DecimalFormat format;

	public String caculateCount(int hosId) {
		String count = "";
		double d = hosId * 0.1;
		format = new DecimalFormat("#.##");
		return format.format(d) + "万";
	}

}

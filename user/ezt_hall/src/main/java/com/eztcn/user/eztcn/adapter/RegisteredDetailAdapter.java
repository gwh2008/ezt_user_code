package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;

/**
 * @title 登记详情医生列表
 * @describe
 * @author ezt
 * @created 2015年1月18日
 */
public class RegisteredDetailAdapter extends BaseArrayListAdapter<Record_Info>
		implements OnClickListener {

	public RegisteredDetailAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Record_Info record;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_registered_doctor, null);
			holder.photo = (RoundImageView) convertView
					.findViewById(R.id.head_pic);
			holder.doctorName = (TextView) convertView
					.findViewById(R.id.d_name);
			holder.docTitle = (TextView) convertView.findViewById(R.id.d_title);
			holder.hosName = (TextView) convertView.findViewById(R.id.hos_name);
			holder.dept = (TextView) convertView.findViewById(R.id.dept);

			holder.tvRegTime = (TextView) convertView
					.findViewById(R.id.reg_time_tv);
			holder.affirmOrder = (Button) convertView
					.findViewById(R.id.affirmOrder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		record = mList.get(position);
		holder.doctorName.setText(record.getDoctorName());
		holder.docTitle.setText(EztDictionaryDB.getInstance(mContext)
				.getLabelByTag("doctorLevel", record.getDoctorLevel() + ""));
		holder.hosName.setText(record.getHospital());
		holder.dept.setText(record.getDept());
		String startTime = "";
		String endTime = "";
		if (!TextUtils.isEmpty(record.getBeginTime())
				&& !TextUtils.isEmpty(record.getEndTime())) {
			startTime = record.getBeginTime().split(" ")[1];
			endTime = record.getEndTime().split(" ")[1];
		}

		holder.tvRegTime.setText("就诊时间 " + record.getRegTime() + " "
				+ startTime.substring(0, startTime.lastIndexOf(":")) + "-"
				+ endTime.substring(0, endTime.lastIndexOf(":")));
		if (record.getPhoto() != null) {
			final Bitmap defaultBit = BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.default_doc_img);
//			FinalBitmap.create(mContext).display(holder.photo,
//					EZTConfig.DOC_PHOTO + record.getPhoto(), defaultBit);
			
			BitmapUtils 
			bitmapUtils=new BitmapUtils(mContext);
				bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
			        bitmapUtils.configDefaultLoadingImage(defaultBit);
			        bitmapUtils.configDefaultLoadFailedImage(defaultBit);
				    bitmapUtils.display(holder.photo, EZTConfig.DOC_PHOTO + record.getPhoto());
		}
		holder.affirmOrder.setTag(position);
		holder.affirmOrder.setOnClickListener(this);
		return convertView;
	}

	class ViewHolder {
		RoundImageView photo;
		TextView doctorName;
		TextView docTitle;
		TextView hosName;
		TextView dept;
		TextView tvRegTime;
		Button affirmOrder;
	}

	@Override
	public void onClick(View v) {
		Integer position = (Integer) v.getTag();
		if (position != null) {
			listener.orderClick(position);
		}
	}

	onOrderClicklistener listener;

	public void setOnOrderClick(onOrderClicklistener listener) {
		this.listener = listener;
	}

	public interface onOrderClicklistener {

		public void orderClick(int position);
	}
}

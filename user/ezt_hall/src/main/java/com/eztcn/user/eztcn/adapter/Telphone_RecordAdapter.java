package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.PhoneRecordBean;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;

/**
 * @title 预约记录adapter
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class Telphone_RecordAdapter extends
		BaseArrayListAdapter<PhoneRecordBean> {

	private int itemType;
	private onPhoneRecordListener listener;
//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public Telphone_RecordAdapter(Activity context, int type) {
		super(context);
		itemType = type;
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	class ViewHolder {
		RoundImageView head_pic;
		TextView d_name;
		TextView d_title;
		TextView hos_name;
		TextView dept;
		TextView date;
		TextView time;
		TextView cancel;
		TextView thankLetter;
	}

	ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.item_order_record_list, null);
			holder.head_pic = (RoundImageView) convertView
					.findViewById(R.id.head_pic);
			holder.d_name = (TextView) convertView.findViewById(R.id.d_name);
			holder.hos_name = (TextView) convertView
					.findViewById(R.id.hos_name);
			holder.d_title = (TextView) convertView.findViewById(R.id.d_title);
			holder.dept = (TextView) convertView.findViewById(R.id.dept);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.cancel = (TextView) convertView.findViewById(R.id.cancel);
			holder.thankLetter = (TextView) convertView
					.findViewById(R.id.thankLetter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final PhoneRecordBean record = mList.get(position);
		judgeClickItem(record, position);
		holder.d_name.setText(record.getDoctorName());
		holder.hos_name.setText(record.getHospital());
		holder.dept.setText(record.getDept());
		if (record.getDoctorLevel() != null) {
			holder.d_title.setText(EztDictionaryDB.getInstance(mContext)
					.getLabelByTag("doctorLevel", record.getDoctorLevel()+""));
		}
		String begin = record.getBeginTime();
		String end = record.getEndTime();
		if (begin != null && end != null) {
			holder.time.setText(begin + "-"
					+ end.substring(end.indexOf(" ") + 1));
		}
		final String imgurl = EZTConfig.DOC_PHOTO + record.getPhoto();
//		fb.display(holder.head_pic, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.head_pic, imgurl);
		return convertView;
	}

	/**
	 * 判断显示类型
	 */
	public void judgeClickItem(PhoneRecordBean record, final int position) {
		int type = record.getCallStatus();
		switch (type) {
		case 0:
			holder.cancel.setVisibility(View.VISIBLE);
			holder.cancel.setText("取消预约");
			holder.cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRecordClick(v, position, 5);
				}
			});
			break;
		case 2:
			holder.cancel.setVisibility(View.VISIBLE);
			holder.cancel.setText("评价");
			holder.cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRecordClick(v, position, 6);
				}
			});
			break;
		default:
			holder.cancel.setVisibility(View.GONE);
			break;
		}
	}

	/**
	 * 1、取消预约 2、评价
	 * 
	 * @param listener
	 */
	public void setOnPhoneRecordListener(onPhoneRecordListener listener) {
		this.listener = listener;
	}

	public interface onPhoneRecordListener {
		public void onRecordClick(View v, int position, int type);
	}
}

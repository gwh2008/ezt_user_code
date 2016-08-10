package com.eztcn.user.eztcn.adapter;
import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.hall.config.Config;
import com.eztcn.user.hall.utils.GlideUtils;

/**
 * @title 预约记录adapter
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class Order_RecordAdapter extends BaseArrayListAdapter<Record_Info> {

	private int itemType;
	private onRecordClickListener listener;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public Order_RecordAdapter(Activity context, int type) {
		super(context);
		itemType = type;
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.new_default_doctor_head_man);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	class ViewHolder {
		ImageView head_pic;
		TextView d_name;
		TextView d_title;
		TextView hos_name;
//		TextView dept;
//		TextView date;
		TextView time;
//		TextView cancel;
//		TextView thankLetter;
		TextView stateTv;
//		TextView tel;
	}

	ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.new_item_my_registration, null);
			
			holder.stateTv=(TextView) convertView.findViewById(R.id.stateTv);
			holder.head_pic = (ImageView) convertView
					.findViewById(R.id.head_pic);
			holder.d_name = (TextView) convertView.findViewById(R.id.d_name);
			holder.hos_name = (TextView) convertView
					.findViewById(R.id.hos_name);
//			holder.tel = (TextView) convertView
//					.findViewById(R.id.tel);
			holder.d_title = (TextView) convertView.findViewById(R.id.d_title);
//			holder.dept = (TextView) convertView.findViewById(R.id.dept);
			holder.time = (TextView) convertView.findViewById(R.id.time);
//			holder.cancel = (TextView) convertView.findViewById(R.id.cancel);
//			holder.thankLetter = (TextView) convertView
//					.findViewById(R.id.thankLetter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final Record_Info recordInfo = mList.get(position);
		judgeClickItem(recordInfo, position);
		
		
		if((recordInfo.getDoctorId() == 6364 && recordInfo
				.getDeptId() == 1469)
				|| (recordInfo.getDoctorId() == 8345 && recordInfo
						.getDeptId() == 6089)){/*
			holder.d_title.setVisibility(View.GONE);
			holder.tel.setVisibility(View.VISIBLE);
			
			if((recordInfo.getDoctorId() == 6364 && recordInfo
					.getDeptId() == 1469)){
				//第一中心医院
				holder.head_pic.setImageResource(R.drawable.tianjin_first_hospital);
				
				holder.time.setText("天津市第一中心医院");
				
			}else if(recordInfo.getDoctorId() == 8345 && recordInfo
					.getDeptId() == 6089){
				//工人医院
				holder.head_pic.setImageResource(R.drawable.tianjin_worker_hospital);
				holder.time.setText("天津市工人医院");
			}
			holder.tel.setText(recordInfo.getPhone());
			holder.hos_name.setText("院内制剂");
			holder.dept.setText("舒筋节利门诊");
		*/
		convertView.setVisibility(View.GONE);
		}else{
			holder.d_title.setVisibility(View.VISIBLE);
//			holder.tel.setVisibility(View.GONE);
		//TODO 订单状态设定  stateTv  已预约（绿） 待支付（橙） 已取消（灰）已就诊（黑）
		holder.d_name.setText(recordInfo.getDoctorName());
			switch(recordInfo.getRrStatus()){
				case 3 :
				case  0:
					holder.stateTv.setText("已预约");
					holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.red));
					break;
				case 4:
					holder.stateTv.setText("已取消");
					holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
					break;
				case 6 :
					holder.stateTv.setText("已就诊");
					holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
					break;
				case 7 :
					holder.stateTv.setText("已爽约");
					holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
					break;
				default:
					holder.stateTv.setText("已预约");
					holder.stateTv.setTextColor(mContext.getResources().getColor(R.color.text_color_gray));
					break;
			}
		holder.hos_name.setText(recordInfo.getHospital());
//		holder.dept.setText(recordInfo.getDept());
		holder.d_title.setText(EztDictionaryDB.getInstance(mContext)
				.getLabelByTag("doctorLevel", recordInfo.getDoctorLevel()+""));
		String d = recordInfo.getDate();
		String begin = recordInfo.getBeginTime();
		String end = recordInfo.getEndTime();
		holder.time.setText(d.substring(0, d.indexOf(" "))
				+ " "
				+ begin.substring(begin.indexOf(" ") + 1,
						begin.lastIndexOf(":")) + "-"
				+ end.substring(end.indexOf(" ") + 1, end.lastIndexOf(":")));
		final String img_url = EZTConfig.DOC_PHOTO + recordInfo.getPhoto();
		bitmapUtils.display(holder.head_pic, img_url);

//			GlideUtils.into(mContext, url,holder.head_pic);
		}
		return convertView;
	}

	/**
	 * 判断显示类型
	 */
	public void judgeClickItem(Record_Info record, final int position) {
		switch (itemType) {
	/*	case 0:
			*//*holder.cancel.setText("退号");
			holder.thankLetter.setVisibility(View.GONE);
			holder.cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRecordClick(v, position, 1);
				}
			});*//*
			break;
		case 1:

			break;
		case 2:
			holder.cancel.setText("评价");
			holder.thankLetter.setVisibility(View.VISIBLE);
			holder.thankLetter.setText("感谢信");
			holder.cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRecordClick(v, position, 2);
				}
			});
			holder.thankLetter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onRecordClick(v, position, 3);
				}
			});
			break;
		case 3:
			holder.cancel.setVisibility(View.GONE);
			holder.thankLetter.setVisibility(View.GONE);
			break;
		case 4:
			holder.cancel.setVisibility(View.GONE);
			holder.thankLetter.setVisibility(View.GONE);
			break;
		case 7:
			holder.cancel.setVisibility(View.GONE);
			holder.thankLetter.setVisibility(View.GONE);
			break;
		case 8:
			holder.cancel.setVisibility(View.GONE);
			holder.thankLetter.setVisibility(View.GONE);*/
//			break;
		}
	}

	/**
	 * 1、退号
	 * 
	 * @param listener
	 */
	public void setOnRecordClickListener(onRecordClickListener listener) {
		this.listener = listener;
	}

	public interface onRecordClickListener {
		public void onRecordClick(View v, int position, int type);
	}
}

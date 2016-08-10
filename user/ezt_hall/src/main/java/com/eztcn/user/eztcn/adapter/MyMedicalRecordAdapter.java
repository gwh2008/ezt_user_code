package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 我的病历
 * @describe
 * @author ezt
 * @created 2014年12月12日
 */
public class MyMedicalRecordAdapter extends BaseArrayListAdapter<MedicalRecord> {

//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public MyMedicalRecordAdapter(Activity context) {
		super(context);
//		fb = FinalBitmap.create(context);
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.medicalrecord_item, null);
			holder.recordName = (TextView) convertView
					.findViewById(R.id.recordName);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.person = (TextView) convertView.findViewById(R.id.person);
			holder.hospital = (TextView) convertView
					.findViewById(R.id.hospital);
			holder.dept = (TextView) convertView.findViewById(R.id.dept);
			holder.head_pic = (RoundImageView) convertView
					.findViewById(R.id.head_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MedicalRecord recrod = mList.get(position);

		if ("男".equals(recrod.getStrSex())) {
			defaultBit = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.userman);
		} else {
			defaultBit = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.userwomen);
		}
		final String imgurl = EZTConfig.DOC_PHOTO + recrod.getHeadImgUrl();
//		fb.display(holder.head_pic, imgurl, defaultBit, defaultBit);
		   bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		    bitmapUtils.display(holder.head_pic, imgurl);
		holder.recordName.setText(recrod.getRecordNum());
		String time = StringUtil.judgeTime(recrod.getCreateTime());
		holder.time.setText(time);
		holder.person.setText(recrod.getStrName());
		holder.hospital.setText(recrod.getHosName());

		return convertView;
	}

	class ViewHolder {
		RoundImageView head_pic;
		TextView recordName;
		TextView time;
		TextView person;
		TextView hospital;
		TextView dept;
	}
}

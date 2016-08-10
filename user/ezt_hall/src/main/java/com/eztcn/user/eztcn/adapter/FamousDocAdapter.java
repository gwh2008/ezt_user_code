/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @author Liu Gang
 * 
 *         2016年1月6日 下午5:59:30
 * 
 */
public class FamousDocAdapter extends BaseArrayListAdapter<Doctor> {
	private BitmapUtils bitmapUtils;

	public FamousDocAdapter(Activity context) {
		super(context);
		bitmapUtils = new BitmapUtils(context);

	}

	private class ViewHolder {
		private ImageView docImg;
		private TextView docName;
		private TextView docPosition;
		private TextView docGoodAt;
	}

	private ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_famdoc, null);
			holder.docImg = (ImageView) convertView
					.findViewById(R.id.famdocImg);
			holder.docName = (TextView) convertView.findViewById(R.id.docName);
			holder.docPosition = (TextView) convertView
					.findViewById(R.id.docPosition);
			holder.docGoodAt = (TextView) convertView
					.findViewById(R.id.docGoodAt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Doctor doctor = mList.get(position);
		// 医生擅长
		if (!TextUtils.isEmpty(doctor.getDocGoodAt()))
			holder.docGoodAt.setText(doctor.getDocGoodAt());
		// 医生职位
		if (!TextUtils.isEmpty(doctor.getDocPosition()))
			holder.docPosition.setText(doctor.getDocPosition());
		// 医生名称
		if (!TextUtils.isEmpty(doctor.getDocName()))
			holder.docName.setText(doctor.getDocName());
		if (position == 0) {
			holder.docImg.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.famdocr));
		} else {
			holder.docImg.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.famdocl));
		}
		// 医生头像
		// if (!TextUtils.isEmpty(doctor.getDocHeadUrl()))
		// bitmapUtils.display(holder.docImg,
		// EZTConfig.DOC_PHOTO + doctor.getDocHeadUrl());
		return convertView;
	}

}

package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.eztcn.user.R;

/**
 * @title 选择挂号医院adapter
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
@SuppressLint("UseSparseArrays")
public class RimHospitalAdapter extends BaseArrayListAdapter<PoiInfo> {

	private PoiInfo info;
//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public RimHospitalAdapter(Activity context) {
		super(context);
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.button_green);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	class ViewHolder {
		TextView hos_name;
		TextView hos_address;
		TextView hos_distance;
		ImageView hos_pic;
		RelativeLayout hos_pos;

	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_rim_hos, null);
			holder.hos_name = (TextView) convertView
					.findViewById(R.id.item_rim_hos_name);// 医院名称
			holder.hos_address = (TextView) convertView
					.findViewById(R.id.item_rim_hos_ads);// 医院地址
			holder.hos_distance = (TextView) convertView
					.findViewById(R.id.item_rim_hos_distance);// 与我的距离
			holder.hos_pic = (ImageView) convertView
					.findViewById(R.id.item_rim_hos_img);// 图片

			holder.hos_pos = (RelativeLayout) convertView
					.findViewById(R.id.item_rim_pos_layout);//

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		info = mList.get(position);
		holder.hos_name.setText(info.name);
		holder.hos_address.setText("地址：" + info.address);
		double d = DistanceUtil.getDistance(latlng, info.location);
		if (d != 0) {
			holder.hos_distance.setText((int) Math.floor(d) + "m");
		}
		// if (info != null) {
		// holder.hos_address.setText(info.gethAddress());
		// holder.hos_name.setText(info.gethName());
		// holder.hos_distance.setText(info.gethTel())
		// final String imgurl = EZTConfig.DOC_PHOTO + info.gethLogo();
		// holder.hos_pic.setTag(imgurl);
		// // 预设一个图片
		// holder.hos_pic.setImageBitmap(defaultBit);
		// if (imgurl != null && !imgurl.equals("")) {
		// Bitmap bitmap = null;
		// if (fb.mImageCache != null) {
		// bitmap = fb.getBitmapFromCache(imgurl);
		// }
		//
		// if (bitmap != null) {
		// holder.hos_pic.setImageBitmap(bitmap);
		// } else {
		//
		// Bitmap bit = fb.display(holder.hos_pic, imgurl,
		// new ImageDownloadedCallBack() {
		//
		// @Override
		// public void onImageDownloaded(View imageView,
		// Bitmap bitmap) {
		// // 通过 tag 来防止图片错位
		// if (imageView.getTag() != null
		// && imageView.getTag()
		// .equals(imgurl)) {
		// if (imageView instanceof ImageView) {
		// ((ImageView) imageView)
		// .setImageBitmap(bitmap);
		// }
		//
		// }
		//
		// }
		// });
		// if (bit != null) {
		// holder.hos_pic.setImageBitmap(bit);
		// } else {
		// holder.hos_pic.setImageBitmap(defaultBit);
		// }
		// }
		// }
		// }
		return convertView;
	}

	LatLng latlng;

	public void setLocation(LatLng latlng) {
		this.latlng = latlng;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		if (observer != null) {
			super.unregisterDataSetObserver(observer);
		}
	}
}

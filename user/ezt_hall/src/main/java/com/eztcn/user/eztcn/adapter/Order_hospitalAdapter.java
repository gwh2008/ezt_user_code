package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 选择挂号医院adapter
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
@SuppressLint("UseSparseArrays")
public class Order_hospitalAdapter extends BaseArrayListAdapter<Hospital> {

	private Hospital info;
//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public Order_hospitalAdapter(Activity context) {
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
		TextView hos_tell;
		ImageView hos_pic;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_hos_list,
					null);
			holder.hos_name = (TextView) convertView
					.findViewById(R.id.hos_name);
			holder.hos_address = (TextView) convertView
					.findViewById(R.id.hos_address);
			holder.hos_tell = (TextView) convertView
					.findViewById(R.id.hos_tell);
			holder.hos_pic = (ImageView) convertView.findViewById(R.id.hos_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		info = mList.get(position);
		if (info != null) {
			holder.hos_address.setText(info.gethAddress());
			holder.hos_name.setText(info.gethName());
			holder.hos_tell.setText(info.gethTel());
			final String imgurl = EZTConfig.DOC_PHOTO + info.gethLogo();
//			fb.display(holder.hos_pic, imgurl, defaultBit, defaultBit);
			bitmapUtils.display(holder.hos_pic, imgurl);
		}

		return convertView;
	}
}

package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ChildOrder;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 订单子列表adapter
 * @describe
 * @author ezt
 * @created 2015年4月1日
 */
public class ChildOrderListAdapter extends BaseArrayListAdapter<ChildOrder> {

	private ViewHolder holder;
//	private FinalBitmap fb;
	
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public ChildOrderListAdapter(Activity context) {
		super(context);
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_info_default);
		
		bitmapUtils=new BitmapUtils(mContext);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	    bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_child_order,
					null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_informate_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.item_name);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.item_price);
			holder.tvAmount = (TextView) convertView
					.findViewById(R.id.item_amount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChildOrder child = mList.get(position);
		holder.tvName.setText(child.getOrderName());
		holder.tvPrice.setText(child.getTotalPrice() + "元");
		holder.tvAmount.setText("X" + child.getOrderAmount());
		final String imgurl = EZTConfig.PACKAGE_IMG + child.getImgUrl();
//		fb.display(holder.img, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.img, imgurl);
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView tvName, tvPrice, tvAmount;
	}
}

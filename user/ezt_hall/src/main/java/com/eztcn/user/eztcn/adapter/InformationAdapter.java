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
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 资讯adapter
 * @describe
 * @author ezt
 * @created 2015年1月9日
 */
public class InformationAdapter extends BaseArrayListAdapter<Information> {

//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;

	private Bitmap defaultBit;

	public InformationAdapter(Activity context) {
		super(context);

//		fb = FinalBitmap.create(context);
		if(null!=context.getResources()){
			defaultBit = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.homeloading);

			bitmapUtils=new BitmapUtils(context);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
			bitmapUtils.configDefaultLoadingImage(defaultBit);
			bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		}

	}

	static class ViewHolder {
		TextView tvTitle, tvInfo;
		View lineView;
		ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_information, null);

			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_informate_title);// 标题
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.item_informate_info);// 内容
			holder.lineView = convertView.findViewById(R.id.item_hor_line);
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_informate_img);// 图片
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Information info = mList.get(position);
		if (info != null) {
			holder.tvTitle.setText(info.getInfoTitle());
			if(info.getInfoDescription()!=null){
//				if (info.getInfoDescription().length() > 27) {
//					holder.tvInfo.setText(info.getInfoDescription()
//							.substring(0, 27) + "...");
//				} else {
//					holder.tvInfo.setText(info.getInfoDescription() + "...");
//				}
				holder.tvInfo.setText(info.getInfoDescription());
			}
			
			final String imgurl = EZTConfig.WEB_VIEW_BASEULR + info.getImgUrl();//更改了图片的基地址
//			fb.display(holder.img, imgurl, defaultBit, defaultBit);
			bitmapUtils.display(holder.img, imgurl);
		}

		return convertView;
	}

}

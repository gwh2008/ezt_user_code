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
 * @title 全站搜索知识库adapter
 * @describe
 * @author ezt
 * @created 2014年12月27日
 */
public class AllSearchKnowleLibAdapter extends
		BaseArrayListAdapter<Information> {

//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	
	
	public AllSearchKnowleLibAdapter(Activity context) {
		super(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_info_default);
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		    
//		fb = FinalBitmap.create(context);

	}

	static class ViewHolder {
		TextView tvTitle, tvInfo;

		ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_know_lib, null);

			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_informate_title);// 标题
			holder.tvInfo = (TextView) convertView
					.findViewById(R.id.item_informate_info);// 内容

			holder.img = (ImageView) convertView
					.findViewById(R.id.item_informate_img);// 图片
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Information info = mList.get(position);
		holder.tvTitle.setText(info.getInfoTitle());
		if (info.getInfoDescription().length() > 27) {
			holder.tvInfo.setText(info.getInfoDescription()
					.substring(0, 27) + "...");
		} else {
			holder.tvInfo.setText(info.getInfoDescription() + "...");
		}
		final String imgurl = EZTConfig.OFFICIAL_WEBSITE + info.getImgUrl();
//		fb.display(holder.img, imgurl, defaultBit, defaultBit);
		
		bitmapUtils.display(holder.img, imgurl);
		return convertView;
	}

}

package com.eztcn.user.phone.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.eztcn.user.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @return
	 */
	public static ImageView getImageView(Context context, int id) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
//		ImageLoader.getInstance().displayImage(url, imageView);
        imageView.setImageResource(id);
		return imageView;
	}
}

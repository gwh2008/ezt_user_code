package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.MedicalRecord_ImgType;

public class EMR_ImageAdapter extends
		BaseArrayListAdapter<MedicalRecord_ImgType> {

	public EMR_ImageAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_emr_image, null);
		}
		ImageView img = (ImageView) convertView.findViewById(R.id.img);
		String url = mList.get(position).getImgUrl();
		// if (!url.contains("/")) {
		// url = EZTConfig.IMG_SERVER + "/images/medical/" + url;
		// }
		// try {
		// bitmap = BitmapFactory.decodeStream(new FileInputStream(mList.get(
		// position).getImgUrl()));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
//		img.setImageBitmap(bitmap);
		final Bitmap defaultBit = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.ic_info_default);
//		FinalBitmap.create(mContext).display(img, url, defaultBit);
		
		BitmapUtils 
		bitmapUtils=new BitmapUtils(mContext);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		        bitmapUtils.configDefaultLoadingImage(defaultBit);
			    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		bitmapUtils.display(img, url);
		return convertView;
	}

}

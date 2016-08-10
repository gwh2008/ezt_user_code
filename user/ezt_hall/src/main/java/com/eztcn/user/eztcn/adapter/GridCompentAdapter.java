/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import org.apache.commons.lang.StringUtils;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.compent.ItemCompent;

/**
 * @author Liu Gang
 * 
 * 2015年12月1日
 * 上午10:20:55
 * 
 */
public class GridCompentAdapter extends BaseArrayListAdapter<ItemCompent>{
//	private FinalBitmap finalBitmap;
	private BitmapUtils bitmapUtils;
	private Bitmap loadingBitmap;
	private Bitmap laodfailBitmap;
	/**
	 * 被添加的次数相当于
	 */
	public GridCompentAdapter(Activity context) {
		super(context);
//		finalBitmap=FinalBitmap.create(mContext);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	      

	}
	private Holder holder;
	private class Holder{
		ImageView img;
		TextView name1;
		TextView name2;
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null==convertView){
			holder=new Holder();
			convertView=View.inflate(mContext, R.layout.item_grid, null);
			holder.img=(ImageView) convertView.findViewById(R.id.imgHead);
			holder.name1=(TextView) convertView.findViewById(R.id.name1);
			holder.name2=(TextView) convertView.findViewById(R.id.name2);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		
		ItemCompent compent=mList.get(position);
		
		  bitmapUtils.configDefaultLoadingImage(loadingBitmap);
		    bitmapUtils.configDefaultLoadFailedImage(laodfailBitmap);
//		finalBitmap.display(holder.img, compent.getImgUrl(), loadingBitmap, laodfailBitmap);
		bitmapUtils.display(holder.img, compent.getImgUrl());
	
		holder.name1.setText(compent.getName1());
		
		if(!StringUtils.isEmpty(compent.getName2())){
			holder.name2.setVisibility(View.VISIBLE);
			holder.name2.setText(compent.getName2());
		}else{
			holder.name2.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}

}

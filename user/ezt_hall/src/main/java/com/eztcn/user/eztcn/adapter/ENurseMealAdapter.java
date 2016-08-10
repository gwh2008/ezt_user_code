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
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 医护帮导诊服务adapter
 * @describe
 * @author ezt
 * @created 2015年6月24日
 */
public class ENurseMealAdapter extends BaseArrayListAdapter<HealthCard> {

//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public ENurseMealAdapter(Activity context) {
		super(context);
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.healthcard_default);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		HealthCard card;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_enursemeal, null);
			holder.cardCover = (ImageView) convertView
					.findViewById(R.id.cardCover);
			holder.cardName = (TextView) convertView
					.findViewById(R.id.cardName);
			holder.cardPrice = (TextView) convertView
					.findViewById(R.id.cardPrice);
			holder.cardCount = (TextView) convertView
					.findViewById(R.id.cardCount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		card = mList.get(position);
		holder.cardName.setText(card.getCardName());
		double price = card.getCardPrice();
		holder.cardPrice.setText("￥ " + price);
		// holder.cardCount.setText("月销量：" + card.get);
		final String imgurl = EZTConfig.PACKAGE_IMG + card.getCardCover();
//		fb.display(holder.cardCover, imgurl, null, defaultBit);
		bitmapUtils.display(holder.cardCover, imgurl);
		return convertView;
	}

	class ViewHolder {
		ImageView cardCover;// 封面图片
		TextView cardName;// 名称
		TextView cardPrice;// 价格
		TextView cardCount;// 月销量
	}
}

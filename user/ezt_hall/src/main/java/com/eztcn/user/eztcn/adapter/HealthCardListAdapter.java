package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.HealthCard;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @title 健康卡列表 adapter
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class HealthCardListAdapter extends BaseArrayListAdapter<HealthCard> {

	private ViewHolder holder;
//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public HealthCardListAdapter(Activity context) {
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

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_health_card,
					null);
			holder.cardCover = (ImageView) convertView
					.findViewById(R.id.cardCover);
			holder.tvCardNum = (TextView) convertView
					.findViewById(R.id.item_card_num);
			holder.tvState = (TextView) convertView
					.findViewById(R.id.item_state);
			holder.tvEndValidity = (TextView) convertView
					.findViewById(R.id.item_endvalidity_tv);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.item_card_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HealthCard h = mList.get(position);
		holder.tvCardNum.setText(h.getCardNum());
		int state = h.getState();
		String strState = "";

		if (state == 0) {
			strState = "未使用";
		} else if (state == 1) {
			strState = "已使用";
		} else {
			strState = "使用中";
		}
		String endValidity = h.getHcEndValidity();
		String startValidity = h.getHcBeginServiceValidity();
		if (!TextUtils.isEmpty(endValidity)
				&& !TextUtils.isEmpty(startValidity)) {
			String sTime = startValidity.split(" ")[0];
			String eTime = endValidity.split(" ")[0];
			holder.tvEndValidity.setText("有效期 " + sTime + " 至 " + eTime);
		}
		holder.tvName.setText(h.getCardName());
		holder.tvState.setText(strState);
		final String imgurl = EZTConfig.PACKAGE_IMG + h.getCardCover();
//		fb.display(holder.cardCover, imgurl, null, defaultBit);
		bitmapUtils.display(holder.cardCover, imgurl);
		return convertView;
	}

	class ViewHolder {

		TextView tvCardNum, tvState, tvEndValidity, tvName;
		ImageView cardCover;
	}

}

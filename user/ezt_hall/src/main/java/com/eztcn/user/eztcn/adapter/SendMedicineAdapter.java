/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Product;

/**
 * @author EZT
 * 
 */
public class SendMedicineAdapter extends BaseArrayListAdapter<Product> {
	public SendMedicineAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Product product = mList.get(position);
		if (null == convertView) {
			convertView = View.inflate(mContext, R.layout.item_send_medicine,
					null);
			holder = new ViewHolder();

			holder.iv_product_img = (ImageView) convertView
					.findViewById(R.id.iv_informate_img);
			holder.tv_product_name = (TextView) convertView
					.findViewById(R.id.tv_item_name);
			holder.tv_product_sales = (TextView) convertView
					.findViewById(R.id.tv_item_sales);
			holder.tv_product_prices = (TextView) convertView
					.findViewById(R.id.tv_item_price);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
				product.getImageUrl());
		holder.iv_product_img.setImageBitmap(bitmap);
		holder.tv_product_name.setText(product.getP_Name());
		holder.tv_product_sales.setText(String.valueOf(product.getP_Sales()));
		holder.tv_product_prices.setText("￥" + product.getP_Price());
		return convertView;
	}

	private class ViewHolder {
		private ImageView iv_product_img;
		private TextView tv_product_name;
		private TextView tv_product_sales;
		private TextView tv_product_prices;
	}

}

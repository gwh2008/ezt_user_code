package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.discover.ProductDetailActivity;
import com.eztcn.user.eztcn.bean.EztProduct;

public class ShoppingCarAdapter extends BaseArrayListAdapter<EztProduct>
		implements OnClickListener, OnFocusChangeListener {

	private boolean[] selectBox;
	private ViewHolder holder;

	private ProductListener listener;

	public ShoppingCarAdapter(Activity context) {
		super(context);
	}

	public ShoppingCarAdapter(Activity context, boolean[] selectBox) {
		super(context);
		this.selectBox = selectBox;
	}

	/**
	 * 更新选中状态
	 * 
	 * @param selectBox
	 */
	public void setChecked(boolean[] selectBox) {
		this.selectBox = selectBox;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_products, null);
			holder.productName = (TextView) convertView
					.findViewById(R.id.productName);// 产品名称
			holder.count = (EditText) convertView.findViewById(R.id.count);// 数量
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);// 选中
			holder.proMoney = (TextView) convertView.findViewById(R.id.money);// 价格
			holder.add = (TextView) convertView.findViewById(R.id.add);// 加
			holder.subtract = (TextView) convertView
					.findViewById(R.id.subtract);// 减
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.proMoney.setText(mList.get(position).getProductPrice() + "元");
		holder.add.setOnClickListener(this);
		holder.subtract.setOnClickListener(this);
		holder.count.setTag(position);
		holder.add.setTag(holder);
		holder.subtract.setTag(holder);
		holder.count.setOnFocusChangeListener(this);
		if (selectBox != null && selectBox.length > 0) {
			holder.checkbox.setChecked(selectBox[position]);
		}
		holder.checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						selectBox[position] = isChecked;
						mList.get(position).setSelect(isChecked);
						listener.onCountChanged(1);
					}
				});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						ProductDetailActivity.class);
				intent.putExtra("product", mList.get(position));
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		EditText count;
		TextView add;
		TextView subtract;
		TextView productName;
		TextView proMoney;
		CheckBox checkbox;
	}

	@Override
	public void onClick(View v) {
		ViewHolder h = (ViewHolder) v.getTag();
		int position = (Integer) h.count.getTag();
		EztProduct product = mList.get(position);
		int nums = Integer.parseInt(h.count.getText().toString());
		if (v == h.add) {
			nums++;
		} else {
			if (nums > 1) {
				nums--;
			} else {
				Toast.makeText(mContext, "受不了了，不能再减少啦", Toast.LENGTH_SHORT)
						.show();
				return;
			}
		}
		product.setProductCount(nums);
		h.count.setText(nums + "");
		listener.onCountChanged(1);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		TextView tv = (TextView) v;
		String nums = tv.getText().toString();
		int p = (Integer) tv.getTag();
		if (nums == null || nums.equals("")) {
			nums = "1";
			tv.setText(nums);
		}
		if (Integer.parseInt(nums) <= 0) {
			nums = "1";
			tv.setText(nums);
		}
		EztProduct ep = mList.get(p);
		ep.setProductCount(Integer.parseInt(nums));
		listener.onCountChanged(1);
	}

	/**
	 * 数据、状态更新
	 */
	public void setOnCountChangedListener(ProductListener listener) {
		this.listener = listener;
	}

	public interface ProductListener {
		public void onCountChanged(int p);
	}
}

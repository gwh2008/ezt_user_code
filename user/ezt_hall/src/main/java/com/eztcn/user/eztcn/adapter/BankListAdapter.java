package com.eztcn.user.eztcn.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 银行列表显示adapter
 * @describe
 * @author ezt
 * @created 2015年6月16日
 */
public class BankListAdapter extends BaseAdapter {

	private Context context;
	private List<String> bankList;
	private List<Integer> bankImgList;

	public BankListAdapter(Context context, List<String> bankList,
			List<Integer> bankImgList) {
		this.context = context;
		this.bankList = bankList;
		this.bankImgList = bankImgList;
	}

	@Override
	public int getCount() {
		return bankList.size();
	}

	@Override
	public Object getItem(int position) {
		return bankList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_list_bank, null);
			holder.bankName = (TextView) convertView
					.findViewById(R.id.bankName);
			holder.bankImg = (ImageView) convertView
					.findViewById(R.id.bank_Img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.bankName.setText(bankList.get(position));
		holder.bankImg.setImageResource(bankImgList.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView bankName;
		ImageView bankImg;
	}
}

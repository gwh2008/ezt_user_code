/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.LInsItem;

/**
 * @author Liu Gang
 * 
 *         2015年11月25日 下午2:25:53 大检验项列表 适配器
 */
public class OrderBigInsAdapter extends BaseArrayListAdapter<LInsItem> {

	public OrderBigInsAdapter(Activity context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	class ViewHolder {
		TextView tvType;
		TextView tvName;
		TextView tvTime;
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_order_lins_list,
					null);
			holder.tvType = (TextView) convertView
					.findViewById(R.id.tvReportType);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvReportName);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tvReportTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		LInsItem lInsItem = mList.get(position);
		holder.tvName.setText(lInsItem.getbItemName());
		if (null != lInsItem.getTimeStr()) {
			if (null == lInsItem.getTimeStr()) {
				holder.tvTime.setText("");
			} else
				holder.tvTime.setText(lInsItem.getTimeStr());
		}
		holder.tvType.setText(lInsItem.getState() + "报告");

		return convertView;
	}

}

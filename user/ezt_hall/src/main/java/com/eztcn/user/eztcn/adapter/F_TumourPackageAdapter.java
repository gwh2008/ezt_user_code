package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ForeignPatient_Service;

/**
 * @title 服务套餐adapter
 * @describe （外患）
 * @author ezt
 * @created 2015年2月28日
 */
public class F_TumourPackageAdapter extends
		BaseArrayListAdapter<ForeignPatient_Service> implements OnClickListener {

	private ViewHolder holder;

	public F_TumourPackageAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_tumourpackage,
					null);

			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_package_title);
			holder.tvIntro = (TextView) convertView
					.findViewById(R.id.item_package_intro);
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.item_package_price);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.item_package_time);
			holder.tvBt = (TextView) convertView
					.findViewById(R.id.item_package_buy);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvBt.setTag(position);
		holder.tvBt.setOnClickListener(this);
		ForeignPatient_Service service = mList.get(position);
		holder.tvTitle.setText(service.getTitle());
		holder.tvIntro.setText(service.getIntro());
		holder.tvPrice.setText("套餐优惠价：" + service.getPrice() + "元");
		holder.tvTime.setText("可节省时间：" + service.getTime());

		return convertView;
	}

	class ViewHolder {

		TextView tvTitle, tvIntro, tvPrice, tvTime, tvBt;

	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		iClick.Click(pos);
	}

	IClick iClick;

	public interface IClick {
		public void Click(int pos);

	}

	public void adapterClick(IClick iClick) {
		this.iClick = iClick;
	}

}

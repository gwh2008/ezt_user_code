package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Tumour;

/**
 * @title 肿瘤服务项目科室adapter
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class Tumour_ProjectListAdapter extends BaseArrayListAdapter<Tumour>
		implements OnClickListener {

	public Tumour_ProjectListAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView deptName;// 肿瘤科室名称
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_tumour_project,
					null);
			holder.deptName = (TextView) convertView
					.findViewById(R.id.item_dept_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Tumour d = mList.get(position);
		holder.deptName.setText(d.getStrName());
		holder.deptName.setOnClickListener(this);
		holder.deptName.setTag(position);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		Iclick.adapterOnclick(pos);
	}

	Ionclick Iclick;

	public interface Ionclick {

		public void adapterOnclick(int pos);
	}

	public void AdapterOnclick(Ionclick click) {
		this.Iclick = click;
	};

}

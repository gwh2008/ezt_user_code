package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Dept;

/**
 * @title 全站搜索科室adapter
 * @describe
 * @author ezt
 * @created 2014年12月27日
 */
public class AllSearchDeptAdapter extends BaseArrayListAdapter<Dept> {

	public AllSearchDeptAdapter(Activity context) {
		super(context);
	}

	static class ViewHolder {
		TextView tvDeptName, tvIntro, tvHosName;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_search_all_dept, null);

			holder.tvDeptName = (TextView) convertView
					.findViewById(R.id.item_search_dept_name);// 科室名称
			holder.tvIntro = (TextView) convertView
					.findViewById(R.id.item_search_dept_intro);// 科室简介
			holder.tvHosName = (TextView) convertView
					.findViewById(R.id.item_search_dhos_name);// 医院

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Dept dept = mList.get(position);
		String deptName = dept.getdName();

		String hosIntro = dept.getIntro();

		if (hosIntro != null) {
			hosIntro = "科室简介：" + (hosIntro.replace(" ", ""));
		}

		holder.tvDeptName
				.setText(TextUtils.isEmpty(deptName) ? "科室" : deptName);
		holder.tvIntro.setText(TextUtils.isEmpty(hosIntro) ? "科室简介：无"
				: hosIntro);
		holder.tvHosName.setText(TextUtils.isEmpty(dept.getdHosName()) ? "所属医院"
				: dept.getdHosName());
		return convertView;
	}

}

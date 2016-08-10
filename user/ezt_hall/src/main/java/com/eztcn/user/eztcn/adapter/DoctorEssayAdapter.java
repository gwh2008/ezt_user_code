package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.eztcn.user.R;

/**
 * @title 医生博文列表adapter
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
public class DoctorEssayAdapter extends BaseArrayListAdapter<String> {

	private ViewHolder holder;

	public DoctorEssayAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_doctoressay,
					null);
		}
		return convertView;
	}

	class ViewHolder {

	}
}

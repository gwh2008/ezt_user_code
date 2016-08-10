package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.DocEvaluateBean;

/**
 * @title 用户评价
 * @describe
 * @author ezt
 * @created 2014年12月31日
 */
public class UserEvaluateAdapter extends BaseArrayListAdapter<DocEvaluateBean> {

	private ViewHolder holder;
	private DocEvaluateBean evaluate;

	public UserEvaluateAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_userevaluate,
					null);
			holder.userName = (TextView) convertView
					.findViewById(R.id.userName);
			holder.totalStar = (TextView) convertView
					.findViewById(R.id.totalStar);
			holder.effectStar = (TextView) convertView
					.findViewById(R.id.effectStar);
			holder.serverStar = (TextView) convertView
					.findViewById(R.id.serverStar);
			holder.evaluateContent = (TextView) convertView
					.findViewById(R.id.evaluateContent);
			holder.treatmentTime = (TextView) convertView
					.findViewById(R.id.treatmentTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		evaluate = mList.get(position);
		holder.userName.setText(evaluate.getUserName());
		holder.totalStar.setText(evaluate.getTotalRate() + "");
		holder.effectStar.setText(evaluate.getMedicalEffect() + "");
		holder.serverStar.setText(evaluate.getServerAttitude() + "");
		holder.evaluateContent.setText(evaluate.getContent());
		holder.treatmentTime.setText("就诊于" + evaluate.getCreateTime());
		return convertView;
	}

	class ViewHolder {
		LinearLayout evaluateLayout;
		TextView userName;
		TextView evaluateType;
		TextView totalStar;
		TextView effectStar;
		TextView serverStar;
		TextView evaluateContent;
		TextView treatmentTime;
	}
}

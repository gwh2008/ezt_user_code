package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ThanksLetter;
import com.eztcn.user.eztcn.customView.RoundImageView;

public class ThanksLetterAdapter extends BaseArrayListAdapter<ThanksLetter> {

	private ViewHolder holder;
	private ThanksLetter letter;

	public ThanksLetterAdapter(Activity context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_thanksletter,
					null);
			holder.patientPhoto = (RoundImageView) convertView
					.findViewById(R.id.patientPhoto);
			holder.patientName = (TextView) convertView
					.findViewById(R.id.patientName);
			holder.evaluateContent = (TextView) convertView
					.findViewById(R.id.evaluateContent);
			holder.evaluateType = (TextView) convertView
					.findViewById(R.id.evaluateType);
			holder.treatmentTime = (TextView) convertView
					.findViewById(R.id.treatmentTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		letter = mList.get(position);
		holder.patientName.setText(letter.getPatientName());
		holder.evaluateContent.setText(letter.getContent());
		holder.evaluateType.setText(letter.getSignature());
		holder.treatmentTime.setText("就诊于" + letter.getCreatetime());
		return convertView;
	}

	class ViewHolder {

		RoundImageView patientPhoto;
		TextView patientName;
		TextView evaluateType;
		TextView evaluateContent;
		TextView treatmentTime;
	}
}

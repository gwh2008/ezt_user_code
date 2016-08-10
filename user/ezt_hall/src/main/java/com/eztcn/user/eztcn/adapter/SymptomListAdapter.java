package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Symptom;

/**
 * @title 症状(/症状详情——相关症状)adapter
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class SymptomListAdapter extends BaseArrayListAdapter<Symptom> implements
		OnClickListener {

	private boolean isTrue;// true为症状，false为相关症状

	public SymptomListAdapter(Activity context, boolean isTrue) {
		super(context);
		this.isTrue = isTrue;
	}

	class ViewHolder {
		TextView symptomName;// 症状名称
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_symptom, null);
			holder.symptomName = (TextView) convertView
					.findViewById(R.id.item_symptom_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Symptom s = mList.get(position);

		if (!isTrue) {
//			holder.symptomName.setBackground(mContext.getResources()
//					.getDrawable(R.drawable.selector_dark_gray_bg));
            holder.symptomName.setBackgroundDrawable(mContext.getResources()
                    .getDrawable(R.drawable.selector_dark_gray_bg));
			holder.symptomName.setTag(position);
			holder.symptomName.setOnClickListener(this);

		}

		holder.symptomName.setText(s.getStrName());

		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		Click.click(pos);
	}

	Iclick Click;

	public interface Iclick {
		public void click(int pos);
	}

	public void AdapterClick(Iclick click) {
		this.Click = click;
	}

}

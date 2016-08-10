package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 疾病adapter
 * @describe
 * @author ezt
 * @created 2015年3月11日
 */
public class DiseaseListAdapter extends BaseArrayListAdapter<Diseases>
		implements OnClickListener {

	private boolean isTrue = false;// true为常见病用到，false为症状详情横向疾病列表用到

	public DiseaseListAdapter(Activity context, boolean isTrue) {
		super(context);
		this.isTrue = isTrue;
	}

	class ViewHolder {
		TextView diseasesName;// 疾病名称
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_disease, null);
			holder.diseasesName = (TextView) convertView
					.findViewById(R.id.item_disease_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Diseases d = mList.get(position);
		holder.diseasesName.setText(d.getdName());

		if (!isTrue) {
			if(Build.VERSION.SDK_INT >15)
			{
			holder.diseasesName.setBackground(mContext.getResources()
					.getDrawable(R.drawable.selector_dark_gray_bg));
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
							mContext, R.dimen.item_hor_height_large)) * 2,
					ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
							mContext, R.dimen.item_hor_height_large)));
			convertView.setPadding(ResourceUtils.dip2px(mContext,
					ResourceUtils.getXmlDef(mContext, R.dimen.small_margin)),
					ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
							mContext, R.dimen.more_large_margin)),
					ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
							mContext, R.dimen.small_margin)), 0);
			holder.diseasesName.setLayoutParams(params);
			holder.diseasesName.setOnClickListener(this);
		} else {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, ResourceUtils.dip2px(mContext,
							ResourceUtils.getXmlDef(mContext,
									R.dimen.item_hor_height)));
			holder.diseasesName.setLayoutParams(params);

		}

		return convertView;
	}

	@Override
	public void onClick(View v) {

	}

}

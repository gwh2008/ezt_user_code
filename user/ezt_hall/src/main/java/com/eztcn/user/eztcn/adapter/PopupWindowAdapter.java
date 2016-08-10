package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title pop 下拉框adapter
 * @describe
 * @author ezt
 * @created 2014年12月10日
 */
public class PopupWindowAdapter extends BaseArrayListAdapter<String> {

	public PopupWindowAdapter(Activity context) {
		super(context);
	}

	static class ViewHolder {
		TextView functionName;// 功能文字
	}
	
	private int flag;//标记（1为选择时间，2为选择城市）

	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}



	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_popwindow, null);

			holder.functionName = (TextView) convertView
					.findViewById(R.id.text_pop);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.functionName.setText(mList.get(position));

		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// (int) mContext.getResources().getDimension(R.dimen.item_heigh3));
		// convertView.setLayoutParams(params);
		return convertView;
	}

}

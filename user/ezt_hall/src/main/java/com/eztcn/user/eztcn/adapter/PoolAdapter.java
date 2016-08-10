/**
 * 
 */
package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Pool;

/**
 * @author Liu Gang
 * 
 *         2016年3月4日 下午4:53:00
 * 
 */
public class PoolAdapter extends BaseArrayListAdapter<Pool> {
	private RegClickListener regClickListener;
	public interface RegClickListener{
		void onRegClick(int position);
	}
	public PoolAdapter(Activity context,RegClickListener regClickListener) {
		super(context);
		this.regClickListener=regClickListener;
	}

	private class Holder {
		TextView dateTv, weekTv, regTv;
	}

	private Holder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_pool, null);
			holder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
			holder.weekTv = (TextView) convertView.findViewById(R.id.weekTv);
			holder.regTv = (TextView) convertView.findViewById(R.id.regTv);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		Pool tempPool=mList.get(position);
		String dateStr=tempPool.getDate();
		String weekStr=tempPool.getDateWeek();
		holder.dateTv.setText(dateStr);
		holder.weekTv.setText(weekStr);
		holder.regTv.setTag(position);
		holder.regTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int position=(Integer) v.getTag();
				regClickListener.onRegClick(position);
			}
		});
		return convertView;
	}

}

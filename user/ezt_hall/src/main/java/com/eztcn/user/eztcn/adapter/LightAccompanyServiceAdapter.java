package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.LightAccompanying;

/**
 * @title 轻陪诊服务adapter
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */

public class LightAccompanyServiceAdapter extends
		BaseArrayListAdapter<LightAccompanying> {

	public LightAccompanyServiceAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView tvServiceName, tvNums, tvUsedState;

	}

	ViewHolder holder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_lightaccompanyservice, null);
			holder.tvServiceName = (TextView) convertView
					.findViewById(R.id.name_tv);
			holder.tvNums = (TextView) convertView
					.findViewById(R.id.card_nums_tv);
			holder.tvUsedState = (TextView) convertView
					.findViewById(R.id.used_state_tv);// 使用状态

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final LightAccompanying l = mList.get(position);
		final int rNums = l.getRemainNums();// 剩余次数

		// 根据剩余次数判断(后台更新接口后，换回根据使用状态判断)
		if (rNums <= 0) {// 已使用
			holder.tvUsedState.setText("已使用");
			holder.tvUsedState.setBackgroundColor(mContext.getResources()
					.getColor(R.color.light_gray));
		} else {// 可使用
			holder.tvUsedState.setText("使用服务");
		}

		// switch (l.getItemStatus()) {//根据使用状态判断
		// case 0:// 可使用
		// holder.tvUsedState.setText("使用服务");
		// break;
		//
		// case 1:// 已使用
		// holder.tvUsedState.setText("已使用");
		// holder.tvUsedState.setBackgroundColor(mContext.getResources()
		// .getColor(R.color.light_gray));
		// break;
		//
		// case 2:// 使用中
		// holder.tvUsedState.setText("使用中");
		// break;
		// }

		holder.tvUsedState.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clickListener.adapterOnclick(position, rNums);
			}
		});
		holder.tvServiceName.setText("• " + l.getItemName());
		holder.tvNums.setText(rNums + "次");
		return convertView;
	}

	ItemBtOnclick clickListener;

	public interface ItemBtOnclick {
		/**
		 * 
		 * @param pos
		 * @param status
		 *            剩余次数(后台更新后换回使用状态)
		 */
		public void adapterOnclick(int pos, int status);
	}

	public void click(ItemBtOnclick clickListener) {
		this.clickListener = clickListener;
	}

}

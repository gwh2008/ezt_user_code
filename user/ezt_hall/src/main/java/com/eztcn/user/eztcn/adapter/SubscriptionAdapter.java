package com.eztcn.user.eztcn.adapter;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.discover.SubscriptionActivity;
import com.eztcn.user.eztcn.bean.Information;

/**
 * @title 订阅资讯adapter
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class SubscriptionAdapter extends BaseArrayListAdapter<Information>
		implements OnClickListener {

	public Set<Integer> checkedRecId;
	private SubscriptionActivity activity;

	public SubscriptionAdapter(Activity context) {
		super(context);
		checkedRecId = new HashSet<Integer>();// 保存所订阅的id值
		this.activity = (SubscriptionActivity) context;
	}

	static class ViewHolder {
		TextView tvTitle, tvNum, tvHint;
		ImageView img;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_subscription, null);

			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.item_sub_title);// 订阅资讯标题
			holder.tvNum = (TextView) convertView
					.findViewById(R.id.item_sub_num);// 订阅数量
			holder.tvHint = (TextView) convertView
					.findViewById(R.id.item_sub_hint);// 订阅提示文字
			holder.img = (ImageView) convertView
					.findViewById(R.id.item_sub_img);// 订阅按钮
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvHint.setTag(position);
		holder.img.setTag(holder);
		holder.img.setOnClickListener(this);

		// holder.functionName.setText(mList.get(position));

		return convertView;
	}

	@Override
	public void onClick(View v) {

		ViewHolder holder = (ViewHolder) v.getTag();
		int index = (Integer) holder.tvHint.getTag();
		if ("订阅".equals(holder.tvHint.getText().toString())) {
			holder.tvHint.setText("取消");
			holder.img.setImageResource(R.drawable.ic_search_small);
			if (checkedRecId.size() < 1) {
				activity.btYes.setText("确定");
			}
			checkedRecId.add(index);
		} else {
			holder.tvHint.setText("订阅");
			holder.img.setImageResource(R.drawable.ic_launcher);
			checkedRecId.remove(index);

			if (checkedRecId.size() < 1) {
				activity.btYes.setText("取消");
			}
		}

	}

}

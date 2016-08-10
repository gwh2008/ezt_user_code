package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eztcn.user.R;

/**
 * @title 表情gridview填充器
 * @describe
 * @author ezt
 * @created 2014年11月14日
 */
public class ExpressionAdapter extends BaseArrayListAdapter<Integer> {

	public ExpressionAdapter(Activity context) {
		super(context);
	}

	private int flag = 0;// 标记长按触发监听事件(1->2->3)，和短按触发监听事件(1->3->2)

	static class ViewHolder {
		ImageView img;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_face, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img_face);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int imgId = mList.get(position);
		holder.img.setBackgroundResource(imgId);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onclick.onClick(position);
			}
		});

		return convertView;
	}

	public faceOnClick onclick;

	public void setOnclick(faceOnClick onclick) {
		this.onclick = onclick;
	}

	public interface faceOnClick {
		void onClick(int position);
	}

}

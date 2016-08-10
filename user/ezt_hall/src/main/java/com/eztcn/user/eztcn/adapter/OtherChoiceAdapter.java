package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.ChatOtherChoice;

/**
 * @title 其他选项gridview填充器
 * @describe
 * @author ezt
 * @created 2014年11月14日
 */
public class OtherChoiceAdapter extends BaseArrayListAdapter<ChatOtherChoice> {

	public OtherChoiceAdapter(Activity context) {
		super(context);

	}

	static class ViewHolder {
		ImageView img;
		TextView text;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_other_choice, null);
			holder.img = (ImageView) convertView
					.findViewById(R.id.img_other_choice);
			holder.text = (TextView) convertView
					.findViewById(R.id.text_other_choice);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ChatOtherChoice other = mList.get(position);
		holder.img.setBackgroundResource(other.getImgId());

		holder.text.setText(other.getChoiceName());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onclick.onClick(position);
			}
		});

		return convertView;
	}

	public OtherChoiceOnClick onclick;

	public void setOnclick(OtherChoiceOnClick onclick) {
		this.onclick = onclick;
	}

	public interface OtherChoiceOnClick {
		void onClick(int position);
	}

}

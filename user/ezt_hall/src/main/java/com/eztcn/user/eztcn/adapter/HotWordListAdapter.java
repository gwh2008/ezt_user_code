package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 搜索列表 热词adapter
 * @describe
 * @author ezt
 * @created 2015年4月3日
 */
public class HotWordListAdapter extends BaseArrayListAdapter<String> implements
		OnClickListener {

	public HotWordListAdapter(Activity context) {
		super(context);
	}

	class ViewHolder {
		TextView tvHotWord;// 热词名称
	}

	ViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.item_hot_word, null);
			holder.tvHotWord = (TextView) convertView
					.findViewById(R.id.item_hot_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String s = mList.get(position);
//		holder.tvHotWord.setOnClickListener(this);
		holder.tvHotWord.setTag(position);
		holder.tvHotWord.setText(s);

		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		if(null!=Click)
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

package com.eztcn.user.eztcn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eztcn.user.R;

/**
 * @title 帮助与反馈adapter
 * @describe
 * @author ezt
 * @created 2014年10月29日
 */
public class QuestionAdapter extends BaseAdapter {

	private Context context;
	private String[] q;
	private String[] r;

	public QuestionAdapter(Context context, String[] q, String[] r) {
		super();
		this.context = context;
		this.q = q;
		this.r = r;
	}

	@Override
	public int getCount() {
		return q.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_question, null);
		}
		TextView question = (TextView) convertView.findViewById(R.id.question);
		TextView reply = (TextView) convertView.findViewById(R.id.replyContent);
		question.setText(q[position]);
		reply.setText(r[position]);
		return convertView;
	}

}

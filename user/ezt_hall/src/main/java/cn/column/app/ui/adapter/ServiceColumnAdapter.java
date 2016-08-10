package cn.column.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.eztcn.user.R;

import java.util.List;

import cn.column.app.common.entity.TitleEntity;

public class ServiceColumnAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<TitleEntity> list;
	private Context context;
	private Activity activity;

	public ServiceColumnAdapter(Activity ac, Context c, List<TitleEntity> l) {
		mInflater = LayoutInflater.from(c);
		context = c;
		activity = ac;
		list = l;
		
	}
	
	public void addItems(TitleEntity entity)
	{
		list.add(entity);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		TitleEntity entity = list.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.column_edit_items, null);

			viewHolder = new ViewHolder();
			viewHolder.tvNewsId = (TextView) convertView.findViewById(R.id.column_tv_id);
			viewHolder.tvNewsTitle = (TextView) convertView.findViewById(R.id.column_tv_newstitle);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvNewsId.setTag(position);
		viewHolder.tvNewsId.setText(entity.getTitleid());
		viewHolder.tvNewsTitle.setText(entity.getTitleName());

		return convertView;
	}

	static class ViewHolder {
		public TextView tvNewsId;
		public TextView tvNewsTitle;
	}
}

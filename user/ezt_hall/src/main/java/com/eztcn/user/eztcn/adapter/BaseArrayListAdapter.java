package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @title Adapter 基类
 * @describe
 * @author ezt
 * @created 2014年11月10日
 */
public abstract class BaseArrayListAdapter<T> extends BaseAdapter {

	public List<T> mList;
	protected Activity mContext;
	protected ListView mListView;

	public BaseArrayListAdapter(Activity context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView,
			ViewGroup parent);

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return mList;
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	public ListView getListView() {
		return mListView;
	}

	public void setListView(ListView listView) {
		mListView = listView;
	}

}

package com.eztcn.user.hall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.interfaces.IFormListener;
import com.eztcn.user.hall.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * @title 选择城市adapter, 里面含有特殊的热门城市头部，该类基本不可复用
 * @describe
 * @created 2014年12月21日
 */
public class CitySortAdapter extends BaseAdapter {
    public ArrayList<City> mList;
    protected Activity mContext;

    public CitySortAdapter(Activity context) {
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

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
    public void updateListView(ArrayList<City> data) {
        mList = data;
        notifyDataSetChanged();
    }

    public ArrayList<City> getList() {
        return mList;
    }

    public View getView( int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.new_item_all_city_list, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        City mContent = mList.get(position);
        if (position == 0) {//0位置的特殊显示
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            if ("@".equals(mContent.getSortLetters())) {
                viewHolder.tvLetter.setText("热门城市");//该字符串可以任意修改
            } else {
                viewHolder.tvLetter.setText(mContent.getSortLetters());
            }
        } else {//非0位置再判断是否显示，判断这个条目的字母和上一个是否相同，相同就不显示，不相同就显示
            if (!mContent.getSortLetters().equals(mList.get(position - 1).getSortLetters())) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(mContent.getSortLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }
        }

        viewHolder.tvTitle.setText(mContent.getCityname());

        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

}
package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Route_ChildEntity;
import com.eztcn.user.eztcn.bean.Route_ParentEntity;

/**
 * @title 父类分组adapter
 * @describe 就医行程
 * @author ezt
 * @created 2015年3月9日
 */

public class Route_ParentAdapter extends BaseExpandableListAdapter {

	private Context mContext;// 上下文

	private ArrayList<Route_ParentEntity> mParents;// 数据源

	private OnChildTreeViewClickListener mTreeViewClickListener;// 点击子ExpandableListView子项的监听

	public Route_ParentAdapter(Context context, ArrayList<Route_ParentEntity> parents) {
		this.mContext = context;
		this.mParents = parents;
	}
	
	

	@Override
	public Route_ChildEntity getChild(int groupPosition, int childPosition) {
		return mParents.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mParents.get(groupPosition).getChilds() != null ? mParents
				.get(groupPosition).getChilds().size() : 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isExpanded, View convertView, ViewGroup parent) {

		final ExpandableListView eListView = getExpandableListView();

		ArrayList<Route_ChildEntity> childs = new ArrayList<Route_ChildEntity>();

		final Route_ChildEntity child = getChild(groupPosition, childPosition);

		childs.add(child);

		final Route_ChildAdapter route_ChildAdapter = new Route_ChildAdapter(
				this.mContext, childs);

		eListView.setAdapter(route_ChildAdapter);

		/**
		 * 
		 * 
		 * 点击子ExpandableListView子项时，调用回调接口
		 * */
		eListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupIndex, int childIndex, long arg4) {

				if (mTreeViewClickListener != null) {

					mTreeViewClickListener.onClickPosition(groupPosition,
							childPosition, childIndex);
				}
				return false;
			}
		});

		/**
		 * 
		 * 
		 * 子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度=
		 * （子ExpandableListView的child数量 + 1 ）* 每一项的高度
		 * */
		eListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {

				LayoutParams lp = new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, (child
								.getChildNames().size() + 1)
								* (int) mContext.getResources().getDimension(
										R.dimen.item_hor_height));
				eListView.setLayoutParams(lp);
			}
		});

		/**
		 * 子ExpandableListView关闭时，此时只剩下group这一项，
		 * 所以子ExpandableListView的总高度即为一项的高度
		 * */
		eListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {

				LayoutParams lp = new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
								.getResources().getDimension(
										R.dimen.item_hor_height));
				eListView.setLayoutParams(lp);
			}
		});
		return eListView;

	}

	/**
	 * 动态创建子ExpandableListView
	 * */
	public ExpandableListView getExpandableListView() {
		ExpandableListView mExpandableListView = new ExpandableListView(
				mContext);
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				(int) mContext.getResources().getDimension(
						R.dimen.item_hor_height));
		mExpandableListView.setLayoutParams(lp);
		mExpandableListView.setDividerHeight(0);// 取消group项的分割线
		mExpandableListView.setChildDivider(null);// 取消child项的分割线
		mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
		return mExpandableListView;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mParents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mParents != null ? mParents.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_parent_group, null);
			holder.parentGroupTV = (TextView) convertView
					.findViewById(R.id.parentGroupTV);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.parentGroupTV
				.setText(mParents.get(groupPosition).getGroupName());

		return convertView;
	}

	/**
	 * 
	 * 
	 * Holder优化
	 * */
	class ViewHolder {

		private TextView parentGroupTV;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 * 设置点击子ExpandableListView子项的监听
	 * */
	public void setOnChildTreeViewClickListener(
			OnChildTreeViewClickListener treeViewClickListener) {
		this.mTreeViewClickListener = treeViewClickListener;
	}

	/**
	 * 
	 * 点击子ExpandableListView子项的回调接口
	 * */
	public interface OnChildTreeViewClickListener {

		void onClickPosition(int parentPosition, int groupPosition,
				int childPosition);
	}

}

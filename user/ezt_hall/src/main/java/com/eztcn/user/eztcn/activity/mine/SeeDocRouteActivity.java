package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.Route_ParentAdapter;
import com.eztcn.user.eztcn.adapter.Route_ParentAdapter.OnChildTreeViewClickListener;
import com.eztcn.user.eztcn.bean.Route_ChildEntity;
import com.eztcn.user.eztcn.bean.Route_ParentEntity;

/**
 * @title 就医行程
 * @describe
 * @author ezt
 * @created 2015年3月9日
 */
public class SeeDocRouteActivity extends FinalActivity implements
		OnGroupExpandListener, OnChildTreeViewClickListener {

	@ViewInject(R.id.eList)
	private ExpandableListView eLt;

	private ArrayList<Route_ParentEntity> parents;

	private Route_ParentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_doc_route);
		ViewUtils.inject(SeeDocRouteActivity.this);
		initialData();
		initEList();
	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		parents = new ArrayList<Route_ParentEntity>();

		for (int i = 0; i < 10; i++) {

			Route_ParentEntity parent = new Route_ParentEntity();

			parent.setGroupName("父类父分组第" + i + "项");

			ArrayList<Route_ChildEntity> childs = new ArrayList<Route_ChildEntity>();

			for (int j = 0; j < 8; j++) {

				Route_ChildEntity child = new Route_ChildEntity();

				child.setGroupName("子类父分组第" + j + "项");

				ArrayList<String> childNames = new ArrayList<String>();

				ArrayList<Integer> childColors = new ArrayList<Integer>();

				for (int k = 0; k < 5; k++) {

					childNames.add("子类第" + k + "项");

					childColors.add(Color.parseColor("#ff00ff"));

				}

				child.setChildNames(childNames);

				childs.add(child);

			}

			parent.setChilds(childs);

			parents.add(parent);

		}

	}

	private void initEList() {

		eLt.setOnGroupExpandListener(this);

		adapter = new Route_ParentAdapter(mContext, parents);

		eLt.setAdapter(adapter);

		adapter.setOnChildTreeViewClickListener(this);

	}

	@Override
	public void onGroupExpand(int groupPosition) {
		for (int i = 0; i < parents.size(); i++) {
			if (i != groupPosition) {
				eLt.collapseGroup(i);
			}
		}
	}

	@Override
	public void onClickPosition(int parentPosition, int groupPosition,
			int childPosition) {

		// do something
		String childName = parents.get(parentPosition).getChilds()
				.get(groupPosition).getChildNames().get(childPosition)
				.toString();
		Toast.makeText(
				mContext,
				"点击的下标为： parentPosition=" + parentPosition
						+ "   groupPosition=" + groupPosition
						+ "   childPosition=" + childPosition + "\n点击的是："
						+ childName, Toast.LENGTH_SHORT).show();

	}

}

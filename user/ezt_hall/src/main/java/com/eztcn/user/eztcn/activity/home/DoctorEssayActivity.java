package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.DoctorEssayAdapter;

/**
 * @title 医生博文列表
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
public class DoctorEssayActivity extends FinalActivity {

	@ViewInject(R.id.essayList)
	// , itemClick = "onItemClick"
	private ListView essayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctoressay);
		ViewUtils.inject(DoctorEssayActivity.this);
		loadTitleBar(true, "医生博文", null);
		initListData();
	}

	public void initListData() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		DoctorEssayAdapter adapter = new DoctorEssayAdapter(mContext);
		essayList.setAdapter(adapter);
		adapter.setList(list);
	}

	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	@OnItemClick(R.id.essayList)
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}
}

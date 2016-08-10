package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ExamReportAdapter;

/**
 * @title 检测报告
 * @describe
 * @author ezt
 * @created 2015年3月3日
 */
public class ExamReportActivity extends FinalActivity {

	@ViewInject(R.id.reportList)//, itemClick = "onItemClick"
	private ListView reportList;

	private List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examreport);
		ViewUtils.inject(ExamReportActivity.this);
		loadTitleBar(true, "检测报告", null);
		getReport();
	}

	/**
	 * 初始化检测报告
	 */
	public void initReport() {
		ExamReportAdapter adapter = new ExamReportAdapter(this);
		reportList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 获取检测报告
	 */
	public void getReport() {
		list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		initReport();
	}

	@OnItemClick(R.id.reportList)
	private void reportListItemClick(AdapterView<?> parent, View view, int position,
			long id){
		Intent intent = new Intent(this, ExamReportDetailActivity.class);
		startActivity(intent);
	}
	
	/**
	 * listView item
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		Intent intent = new Intent(this, ExamReportDetailActivity.class);
//		startActivity(intent);
//	}
}

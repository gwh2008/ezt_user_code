package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ExamReportDetailAdapter;

/**
 * @title 检测报告详情
 * @describe
 * @author ezt
 * @created 2015年3月3日
 */
public class ExamReportDetailActivity extends FinalActivity {

	@ViewInject(R.id.detailList)//, itemClick = "onItemClick"
	private ListView detailList;

	private List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.examreportdetail);
		ViewUtils.inject(ExamReportDetailActivity.this);
		loadTitleBar(true, "报告详情", null);
		getReportDetail();
	}

	/**
	 * 初始化详情
	 */
	public void initReportDetail() {
		ExamReportDetailAdapter adapter = new ExamReportDetailAdapter(this);
		detailList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 获取详情
	 */
	public void getReportDetail() {
		list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		initReportDetail();
	}
}

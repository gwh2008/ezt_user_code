package com.eztcn.user.eztcn.activity.discover;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.SubscriptionAdapter;
import com.eztcn.user.eztcn.bean.Information;

/**
 * @title 订阅(微资讯)
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class SubscriptionActivity extends FinalActivity {

	@ViewInject(R.id.subscription_lt)
	private ListView lvSub;// 订阅资讯列表

	@ViewInject(R.id.subscription_yes_bt)
	public Button btYes;// 确定

	@ViewInject(R.id.title_tv)
	private TextView tvTitle;// 标题

	private SubscriptionAdapter adapter;

	private ArrayList<Information> infoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscription);
		ViewUtils.inject(SubscriptionActivity.this);
		tvTitle.setText("订阅");

		adapter = new SubscriptionAdapter(this);
		lvSub.setAdapter(adapter);
		initialData();
	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		infoList = new ArrayList<Information>();
		for (int i = 0; i < 5; i++) {
			infoList.add(new Information());
		}
		adapter.setList(infoList);
	}
	@OnClick(R.id.subscription_yes_bt)
	private void yesClick(View v){
		if ("确定".equals(btYes.getText().toString())) {
			String indexs = "";
			Iterator<Integer> it = adapter.checkedRecId.iterator();
			while (it.hasNext()) {
				indexs += it.next() + "-";
			}
			setResult(11, new Intent().putExtra("info", indexs));
			finish();
		} else {
			finish();
		}

	
	}

}

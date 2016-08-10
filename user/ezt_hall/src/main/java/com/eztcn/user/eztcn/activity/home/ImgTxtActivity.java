package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.ImgTxtAskAdapter;

/**
 * @title 图文问诊
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class ImgTxtActivity extends FinalActivity {

	@ViewInject(R.id.buy)//, click = "onClick"
	private Button buy;// 购买问诊
	@ViewInject(R.id.askList)//, itemClick = "onItemClick"
	private ListView askList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imgtxt);
		ViewUtils.inject(ImgTxtActivity.this);
		loadTitleBar(true, "图文问诊", null);
		init();
	}

	public void init() {
		List<String> list = new ArrayList<String>();
		list.add("");
		list.add("");
		list.add("");
		ImgTxtAskAdapter adapter = new ImgTxtAskAdapter(mContext);
		adapter.setList(list);
		askList.setAdapter(adapter);
	}
	@OnClick(R.id.buy)
	public void buyClick(View v){
//		Intent intent = new Intent(this, BuyAskDoctorActivity.class);
//		startActivity(intent);
//		finish();
	}
//	public void onClick(View v) {
////		Intent intent = new Intent(this, BuyAskDoctorActivity.class);
////		startActivity(intent);
////		finish();
//	}

//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
	@OnItemClick(R.id.askList)
		public void itemClick(AdapterView<?> parent, View view, int position,
				long id) {

	}
}

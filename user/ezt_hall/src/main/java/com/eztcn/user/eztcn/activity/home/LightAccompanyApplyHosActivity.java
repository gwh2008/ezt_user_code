package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.LightAccompany_ApplyHosAdapter;
import com.eztcn.user.eztcn.bean.Hospital;

/**
 * @title 适用医院
 * @describe
 * @author ezt
 * @created 2015年3月30日
 */
public class LightAccompanyApplyHosActivity extends FinalActivity {

	@ViewInject(R.id.hos_lv)
	private ListView lvHos;

	private LightAccompany_ApplyHosAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightaccompany_applyhos);
		ViewUtils.inject(LightAccompanyApplyHosActivity.this);
		loadTitleBar(true, "适用医院", null);
		ArrayList<Hospital> hosList = (ArrayList<Hospital>) getIntent()
				.getSerializableExtra("list");
		adapter = new LightAccompany_ApplyHosAdapter(this);
		lvHos.setAdapter(adapter);
		adapter.setList(hosList);
	}

}

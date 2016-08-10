package com.eztcn.user.eztcn.activity.home;

import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.UnEvaluateAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 未评价列表
 * @describe
 * @author ezt
 * @created 2015年1月29日
 */
public class UnEvaluateListActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.unEvaluateList )//itemClick = "onItemClick"
	private ListView unEvaluateList;

	private String doctorId;
	private String hosName;
	private List<Record_Info> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unevaluate);
		ViewUtils.inject(UnEvaluateListActivity.this);
		loadTitleBar(true, "未评价列表", null);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			doctorId = savedInstanceState.getString("doctorId");
			hosName = savedInstanceState.getString("hos");
		}
		getUnevaluateList();
	}

	/**
	 * 初始化未评价医生列表
	 */
	public void initList() {
		if (list.size() == 0) {
			Toast.makeText(getApplicationContext(), "暂无评价记录",
					Toast.LENGTH_SHORT).show();
		}
		UnEvaluateAdapter adapter = new UnEvaluateAdapter(mContext);
		unEvaluateList.setAdapter(adapter);
		adapter.setList(list);
	}

	/**
	 * 获取未评价列表
	 */
	public void getUnevaluateList() {
		if (BaseApplication.patient != null && doctorId != null) {
			RequestParams params=new RequestParams();
			params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
			params.addBodyParameter("doctorId", doctorId);
			params.addBodyParameter("aeType", "1");
			new RegistratioImpl().getUnEvaluateList(params, this);
			showProgressToast();
		}
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		String msg;
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		switch (taskID) {
		case HttpParams.UNEVALUATE_LIST:
			list = (List<Record_Info>) map.get("list");
			if (list != null) {
				initList();
			}
			break;
		}
	}
	@OnItemClick(R.id.unEvaluateList)
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(UnEvaluateListActivity.this,
				EvaluateActivity.class);
		Record_Info info = list.get(position);
		info.setHospital(hosName);
		intent.putExtra("record", info);
		startActivity(intent);
	}
}

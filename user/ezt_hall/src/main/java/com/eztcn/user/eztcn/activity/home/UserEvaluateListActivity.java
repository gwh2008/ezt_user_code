package com.eztcn.user.eztcn.activity.home;

import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.UserEvaluateAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.DocEvaluateBean;
import com.eztcn.user.eztcn.impl.RegistratioImpl;

/**
 * @title 用户评价
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
public class UserEvaluateListActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.evaluateList)//, itemClick = "onItemClick"
	private ListView evaluateList;

	private String doctorName;
	private String doctorId;
	private List<DocEvaluateBean> list;
	private UserEvaluateAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userevaluate);
		loadTitleBar(true, "用户评价", null);
		ViewUtils.inject(UserEvaluateListActivity.this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			doctorName = bundle.getString("doctorName");
			doctorId = bundle.getString("doctorId");
		}
		adapter = new UserEvaluateAdapter(mContext);
		evaluateList.setAdapter(adapter);
		
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		getEvaluateRecord();
	}

	/**
	 * 获取医生综合评价列表
	 */
	public void getEvaluateRecord() {
		if (doctorId == null) {
			return;
		}
		RegistratioImpl impl = new RegistratioImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("doctorId", doctorId);
//		params.put("page", "1");
//		params.put("rowsPerPage", "20");
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("doctorId", doctorId);
		params.addBodyParameter("page", "1");
		params.addBodyParameter("rowsPerPage", "20");
		
		impl.readEvaluateRecord(params, this);
		showProgressToast();
	}

	public void initListData() {
		if (list.size() == 0) {
			Toast.makeText(getApplicationContext(), "暂无评价记录",
					Toast.LENGTH_SHORT).show();
		}
		adapter.setList(list);
	}

//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//
//	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		boolean status = (Boolean) object[1];
		if (!status) {
			Toast.makeText(mContext, object[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) object[2];
		if (map == null) {
			return;
		}
		list = (List<DocEvaluateBean>) map.get("evaluate");
		if (list != null) {
			initListData();
		}
	}
}

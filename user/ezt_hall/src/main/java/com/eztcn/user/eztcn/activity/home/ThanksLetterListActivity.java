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
import com.eztcn.user.eztcn.adapter.ThanksLetterAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ThanksLetter;
import com.eztcn.user.eztcn.impl.RegistratioImpl;

/**
 * @title 感谢信
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
public class ThanksLetterListActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.evaluateList)//, itemClick = "onItemClick"
	private ListView thanksList;

	private List<ThanksLetter> list;
	private String doctorId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userevaluate);
		ViewUtils.inject(ThanksLetterListActivity.this);
		loadTitleBar(true, "感谢信", null);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			doctorId = bundle.getString("doctorId");
		}
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		getThanksData();
	}

	/**
	 * 获取感谢信列表
	 */
	public void getThanksData() {
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
		impl.readThanksLetter(params, this);
		showProgressToast();
	}

	public void initListData() {
		if (list.size() == 0) {
			Toast.makeText(this, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		ThanksLetterAdapter adapter = new ThanksLetterAdapter(mContext);
		thanksList.setAdapter(adapter);
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
		// list = new ArrayList<ThanksLetter>();
		list = (List<ThanksLetter>) map.get("thanks");
		if (list != null) {
			initListData();
		}

	}
}

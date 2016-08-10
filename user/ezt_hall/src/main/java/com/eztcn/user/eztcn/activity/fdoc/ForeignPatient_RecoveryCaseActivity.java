package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.InformationAdapter;
import com.eztcn.user.eztcn.api.IForeignPatient;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 康复病历
 * @describe (外地患者服务)
 * @author ezt
 * @created 2015年2月27日
 */
public class ForeignPatient_RecoveryCaseActivity extends FinalActivity
		implements IHttpResult {//OnItemClickListener

	@ViewInject(R.id.lv)//, itemClick = "onItemClick"
	private ListView lv;

	private InformationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recovery_case);
		ViewUtils.inject(ForeignPatient_RecoveryCaseActivity.this);
		loadTitleBar(true, "康复病历", null);
		adapter = new InformationAdapter(this);
		lv.setAdapter(adapter);
		initialData();

	}

	/**
	 * 初始化数据
	 */
	private void initialData() {

//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("listid", "30");
//		IForeignPatient api = new ForeignPatientImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("listid", "30");
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getRecoveryCaseList(params, this);
		showProgressToast();

		// ArrayList<Information> list = new ArrayList<Information>();
		// for (int i = 0; i < 5; i++) {
		// Information info = new Information();
		// list.add(info);
		// }
		// adapter.setList(list);
	}
	@OnItemClick(R.id.lv)
	private void lvItemClick(AdapterView<?> parent, View view, int position,
			long id){
		String strId = adapter.getList().get(position).getId();
		String title = adapter.getList().get(position).getInfoTitle();
		startActivity(new Intent(mContext,
				ForeignPatient_RCDetailsActivity.class).putExtra("id", strId)
				.putExtra("title", title));
	}
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		String strId = adapter.getList().get(position).getId();
//		String title = adapter.getList().get(position).getInfoTitle();
//		startActivity(new Intent(mContext,
//				ForeignPatient_RCDetailsActivity.class).putExtra("id", strId)
//				.putExtra("title", title));
//
//	}

	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					ArrayList<Information> list = (ArrayList<Information>) map
							.get("data");
					adapter.setList(list);
				} else {
					Toast.makeText(mContext, map.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
		hideProgressToast();

	}

}

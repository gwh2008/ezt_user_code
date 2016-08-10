package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 患友交流
 * @describe
 * @author ezt
 * @created 2015年3月24日
 */
public class PatientExchangeActivity extends FinalActivity implements
		IHttpResult {

	@ViewInject(R.id.info_tv)
	private TextView tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_exchange);
		ViewUtils.inject(PatientExchangeActivity.this);
		loadTitleBar(true, "患友交流", null);
		initialData();
	}

	/**
	 * 获取数据
	 */
	private void initialData() {

//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("infoid", "98");
//		IForeignPatient api = new ForeignPatientImpl();
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("infoid", "98");
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getPatientGroup(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null) {
				boolean flag = (Boolean) map.get("flag");
				if (flag) {
					String body = (String) map.get("body");
					tvInfo.setText(Html.fromHtml(body));
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

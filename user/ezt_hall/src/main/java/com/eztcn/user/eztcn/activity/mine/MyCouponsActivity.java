package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MyCouponsAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Coupons;
import com.eztcn.user.eztcn.impl.PayImpl;

/**
 * @title 优惠券列表
 * @describe
 * @author ezt
 * @created 2015年5月5日
 */
public class MyCouponsActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.lv_coupons)
	private ListView lv;

	private MyCouponsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycoupons);
		ViewUtils.inject(MyCouponsActivity.this);
		loadTitleBar(true, "优惠券", null);
		adapter = new MyCouponsAdapter(this);
		lv.setAdapter(adapter);
		initialData();
	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		showProgressToast();
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("yesOrNo", "Y");
		PayImpl api = new PayImpl();
		api.getCouponList(params, this);
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			boolean isFlag = (Boolean) map.get("flag");
			if (isFlag) {// 成功
				ArrayList<Coupons> cList = (ArrayList<Coupons>) map.get("data");
				if (cList != null && cList.size() != 0) {
					adapter.setList(cList);
				} else {
					lv.setVisibility(View.GONE);
				}
			} else {
//				Toast.makeText(mContext, getString(R.string.service_error),
//						Toast.LENGTH_SHORT).show();
			}
		} else {
//			Toast.makeText(mContext, getString(R.string.service_error),
//					Toast.LENGTH_SHORT).show();
		}

	}

}

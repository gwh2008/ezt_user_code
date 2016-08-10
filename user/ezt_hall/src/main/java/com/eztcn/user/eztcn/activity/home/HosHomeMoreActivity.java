/**
 * 
 */
package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalIntroActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.adapter.HosHomeAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Function;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.compent.IntentParams;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @author Liu Gang
 * 
 *         2016年3月3日 下午2:27:51 医院首页
 */
public class HosHomeMoreActivity extends FinalActivity {
	/**
	 * 服务器的功能列表
	 */
	private List<Function> serFuns;
	private List<Function> adapterList;
	@ViewInject(R.id.hosFunGridView_more)
	private GridView hosFunGridView_more;
	private HosHomeAdapter hosFunAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hoshome_more);
		ViewUtils.inject(HosHomeMoreActivity.this);
		loadTitleBar(true, "更多", null);
		hosFunAdapter = new HosHomeAdapter(mContext);
		hosFunGridView_more.setAdapter(hosFunAdapter);
		getIntentData();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		// 没有去处前面列表的
		serFuns = (List<Function>) intent.getSerializableExtra("functionList");
		adapterList = new ArrayList<Function>();
		for (int i = 7; i < serFuns.size(); i++) {
			Function function = serFuns.get(i);
			adapterList.add(function);
		}
		hosFunAdapter.setList(adapterList);
	}

	@OnItemClick(R.id.hosFunGridView_more)
	public void funListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Function function = hosFunAdapter.getList().get(position);
		if (function.getIsOpen() == 0) {
			Toast.makeText(mContext, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
		} else {// 处理功能跳转
			if (null != function.getJumpLink()) {
				try {
					Intent intent = new Intent(HosHomeMoreActivity.this,
							Class.forName(function.getJumpLink()));
					List<IntentParams> params = function.getIntentParamList();
					if (null != params && params.size() > 0) {// 设置参数
						for (int i = 0; i < params.size(); i++) {
							IntentParams param = params.get(i);
							Object obj = param.getValue();
							if (obj instanceof Boolean) {
								intent.putExtra(param.getKey(),
										(Boolean) param.getValue());
							} else if (obj instanceof String) {
								intent.putExtra(param.getKey(),
										String.valueOf(obj));
							} else if (obj instanceof Hospital) {
								Hospital hos = (Hospital) obj;
								intent.putExtra(param.getKey(), hos);
							}

						}
					}
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

	}

}

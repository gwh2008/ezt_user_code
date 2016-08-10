package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.AreaDataAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.impl.CityImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * @title 选择就诊区域
 * @describe （预约登记）
 * @author ezt
 * @created 2015年2月25日
 */
public class AppointCheckIn_ChoiceAreaActivity extends FinalActivity implements
	 IHttpResult {//	OnItemClickListener,

	@ViewInject(R.id.choice_city_lt)//, itemClick = "onItemClick"
	private ListView lvCity;

	@ViewInject(R.id.choice_area_lt)//, itemClick = "onItemClick"
	private ListView lvArea;

	@ViewInject(R.id.tv_line)
	private TextView tvLine;

	private AreaDataAdapter cityAdapter, areaAdapter;

	private String city_id;// 选中的城市id
	private int selectCityPos = -1;// 选中城市下标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin_choice_area);
		ViewUtils.inject(AppointCheckIn_ChoiceAreaActivity.this);
		loadTitleBar(true, "选择就诊区域", null);

		cityAdapter = new AreaDataAdapter(this);
		lvCity.setAdapter(cityAdapter);

		areaAdapter = new AreaDataAdapter(this);
		lvArea.setAdapter(areaAdapter);

		initialData();
	}

	/**
	 * 初始化数据
	 */
	private void initialData() {
		showProgressToast();
		String[] cityNames = getResources().getStringArray(
				R.array.appoint_city_name);
		String[] cityIds = getResources().getStringArray(
				R.array.appoint_city_id);
		ArrayList<City> cityList = new ArrayList<City>();
		for (int i = 1; i < cityNames.length; i++) {
			City city = new City();
			city.setCityId(cityIds[i]);
			city.setAreaName(cityNames[i]);
			cityList.add(city);
		}
		String myCity = BaseApplication.selectCity;
		if (!"".equals(myCity)) {
			for (int i = 1; i < cityNames.length; i++) {
				if (myCity.equals(cityNames[i])) {
					city_id = cityIds[i];
					selectCityPos = i - 1;
					break;
				} else if (i == cityNames.length - 1) {
					city_id = cityIds[1];
					selectCityPos = 0;
				}
			}
		} else {
			city_id = cityIds[1];
			selectCityPos = 0;
		}
		cityAdapter.setList(cityList);
		cityAdapter.setSelectedPosition(selectCityPos);
		cityAdapter.notifyDataSetChanged();
		getAreaData();

	}

	/**
	 * 获取区域列表
	 * 
	 * @param cityid
	 */
	private void getAreaData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		CityImpl impl = new CityImpl();
//		params.put("cityid", city_id);
		
		RequestParams params=new RequestParams();
		CityImpl impl = new CityImpl();
		params.addBodyParameter("cityid", city_id);
		impl.getAreaList(params, this);
	}

	
	@OnItemClick(R.id.choice_city_lt)
	public void cityItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	// 选择城市
			city_id = cityAdapter.getList().get(position).getCityId() + "";
			selectCityPos = position;
			cityAdapter.setSelectedPosition(selectCityPos);
			cityAdapter.notifyDataSetChanged();
			showProgressToast();
			getAreaData();
	}
	
	
	
	@OnItemClick(R.id.choice_area_lt)
	public void areaItemClick(AdapterView<?> parent, View view, int position,
			long id) {


		// 选择区域
			String strName = areaAdapter.getList().get(position).getAreaName();
			String areaId = areaAdapter.getList().get(position).getAreaId();
			String strCity = cityAdapter.getList().get(selectCityPos)
					.getAreaName();
			setResult(
					2,
					new Intent().putExtra("strArea", strName)
							.putExtra("strCity", strCity)
							.putExtra("areaId", areaId)
							.putExtra("cityId", city_id));
			finish();
	}
	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//
//		switch (parent.getId()) {
//		case R.id.choice_city_lt:// 选择城市
//			city_id = cityAdapter.getList().get(position).getCityId() + "";
//			selectCityPos = position;
//			cityAdapter.setSelectedPosition(selectCityPos);
//			cityAdapter.notifyDataSetChanged();
//			showProgressToast();
//			getAreaData();
//			break;
//
//		default:// 选择区域
//			String strName = areaAdapter.getList().get(position).getAreaName();
//			String areaId = areaAdapter.getList().get(position).getAreaId();
//			String strCity = cityAdapter.getList().get(selectCityPos)
//					.getAreaName();
//			setResult(
//					2,
//					new Intent().putExtra("strArea", strName)
//							.putExtra("strCity", strCity)
//							.putExtra("areaId", areaId)
//							.putExtra("cityId", city_id));
//			finish();
//			break;
//		}
//
//	}

	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_AREAS:// 获取区域列表
			if (isSucc) {
				ArrayList<City> lists = (ArrayList<City>) object[2];
				areaAdapter.setList(lists);
				if (lists != null && lists.size() != 0) {
					lvArea.setVisibility(View.VISIBLE);
				} else {
					lvArea.setVisibility(View.INVISIBLE);
				}
				lvCity.setVisibility(View.VISIBLE);
				tvLine.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();

			}
			hideProgressToast();
			break;
		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
}

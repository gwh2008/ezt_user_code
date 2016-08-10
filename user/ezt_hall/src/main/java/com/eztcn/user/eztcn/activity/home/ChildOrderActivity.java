package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.bean.City;

/**
 * @title 儿童挂号
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public class ChildOrderActivity extends FinalActivity implements
		OnClickListener, OnMarkerClickListener {

	TextView locCity;// 定位城市
	@ViewInject(R.id.hint_show)
	private TextView hint_show;// 提示信息
	@ViewInject(R.id.map)
	private MapView map;
	@ViewInject(R.id.hospital)
	private TextView hospital;
	@ViewInject(R.id.telPhone)
	private TextView telPhone;
	@ViewInject(R.id.addr)
	private TextView addr;

	private BaiduMap mBaidumap;
	private Marker regeoMarker;
	private UiSettings uiSetting;
	private LatLng latlng = new LatLng(22.5475800000, 114.0549520000);

	private TextView showInfo;

	private String city;
	private String hosName;
	private String hosId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.childorder);

		ViewUtils.inject(ChildOrderActivity.this);
		locCity = loadTitleBar(true, "儿童挂号", "深圳");
		initRightButton();
		initCityInfo();
		initMap();
	}

	public void initRightButton() {
		Drawable rightDrawable = getResources().getDrawable(
				R.drawable.position_icon);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		locCity.setCompoundDrawables(rightDrawable, null, null, null);
		locCity.setCompoundDrawablePadding(5);
		locCity.setOnClickListener(this);
		city = TextUtils.isEmpty(BaseApplication.selectCity) ? "选择城市"
				: BaseApplication.selectCity;
		locCity.setText(city);
		// if (city.contains("深圳") || city.contains("天津")) {
		// hint_show.setVisibility(View.GONE);
		// } else {
		// hint_show.setVisibility(View.VISIBLE);
		// }
	}

	private void initMap() {
		if (mBaidumap == null) {
			mBaidumap = map.getMap();
			mBaidumap.setMyLocationEnabled(true);
			uiSetting = mBaidumap.getUiSettings();
		}
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
		map.showZoomControls(false);
		uiSetting = mBaidumap.getUiSettings();
		uiSetting.setRotateGesturesEnabled(false);
		uiSetting.setCompassEnabled(false);
		initInfoWidget();
	}

	/**
	 * 初始化显示信息控件
	 */
	public void initInfoWidget() {
		showInfo = new TextView(this);
		showInfo.setMinWidth(getWindowWidth() / 2);
		showInfo.setMaxWidth(getWindowWidth() - 20);
		showInfo.setPadding(5, 5, 10, 5);
		showInfo.setBackground(getResources().getDrawable(
				R.drawable.custom_info_bubble));
		showInfo.setGravity(Gravity.CENTER);
		addMarkersToMap();
	}

	/**
	 * 判断定位到的城市（深圳、天津）
	 */
	public void initCityInfo() {
		if (city.contains("深圳")) {
			hosName = "深圳市儿童医院";
			hosId = "83";
			hint_show.setVisibility(View.GONE);
		} else if (city.contains("天津")) {
			hosName = "天津市儿童医院";
			hosId = "9";
			hint_show.setVisibility(View.GONE);
		} else {
			hosId = "0";
			hint_show.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBaidumap != null) {
			mBaidumap.clear();
			mBaidumap = null;
		}
	}

	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		String title = null;
		String content = null;
		String tel = null;
		if (hosId.equals("83")) {
			title = "深圳市儿童医院";
			tel = "(0755)83936101";
			content = "地址：深圳市福田区益田路7019号(红荔路少年宫)";
			latlng = new LatLng(22.553227, 114.062074);
		} else if (hosId.equals("9")) {
			title = "天津市儿童医院";
			tel = "(022)58116666";
			content = "地址：马场道225号";
			latlng = new LatLng(39.106318, 117.206255);
		} else {
			Toast.makeText(getApplicationContext(), "该城市暂未开通此项功能",
					Toast.LENGTH_SHORT).show();
			return;
		}
		regeoMarker = (Marker) mBaidumap.addOverlay(new MarkerOptions()
				.anchor(0.5f, 0.5f)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_gcoding))
				.position(latlng));
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.zoomBy(mBaidumap
				.getMaxZoomLevel() - 2));
		showInfo.setText(title);
		InfoWindow window = new InfoWindow(showInfo, latlng, -20);
		mBaidumap.showInfoWindow(window);
		showInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!hosId.equals("0")) {
					Intent intent = new Intent(ChildOrderActivity.this,
							DoctorListActivity.class);
					intent.putExtra("type", 1).putExtra("hosName", hosName);
					intent.putExtra("isAllSearch", true);
					intent.putExtra("hosId", hosId + "");
					intent.putExtra("deptName", "选择科室");
					startActivity(intent);
				}
			}
		});
		initHosInfo(title, tel, content);
	}

	/**
	 * 初始化医院数据
	 */
	public void initHosInfo(String title, String tel, String snippet) {
		hospital.setText(title);
		telPhone.setText(tel);
		addr.setText(snippet);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ChoiceCityActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 11) {
			City city = (City) data.getSerializableExtra("city");
			if (locCity != null && city != null) {
				locCity.setText(city.getCityName());
				BaseApplication.selectCity = city.getCityName();
				this.city = city.getCityName();
				initCityInfo();
				addMarkersToMap();
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}

package com.eztcn.user.eztcn.activity.discover;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;

/**
 * @title 周边医院具体位置（地图显示）
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class MapRimHosActivity extends FinalActivity implements
		 OnMarkerClickListener, OnGetRoutePlanResultListener {

	@ViewInject(R.id.map_rim_hos_tv)
	private TextView tvHos;// 医院名称

	@ViewInject(R.id.map_hos_tel_tv)
	private TextView tvTel;// 电话

	@ViewInject(R.id.map_hos_ads_tv)
	private TextView tvAds;// 地址

	@ViewInject(R.id.map_rim_hos_bt)
	private Button btTo;// 到这里去按钮

	@ViewInject(R.id.map_rim_hos_driving)
	private Button btDriving;// 驾车

	@ViewInject(R.id.map_rim_hos_bus)
	private Button btBus;// 公交

	@ViewInject(R.id.map_rim_hos_walk)
	private Button btWalk;// 步行

	@ViewInject(R.id.map_rim_hos_type)
	private LinearLayout layoutType;

	@ViewInject(R.id.map_rim_hos_map)
	private MapView map;
	private BaiduMap mBaidumap;

	private ProgressDialog progDialog = null;
	private Marker regeoMarker;
	private UiSettings uiSetting;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = false;// 是否首次定位
	private LatLng lp = null;
	// 规划路径
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private int routeType = 3;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private PlanNode startPoint = null;// 自己当前的位置
	private PlanNode endPoint = null;// 医院位置

	// 浏览路线节点相关
	RouteLine route = null;
	private TextView showInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_rim_hos);
		ViewUtils.inject(MapRimHosActivity.this);
		loadTitleBar(true, "医院位置", null);
		// 医院经纬度
		double lat = getIntent().getDoubleExtra("lat", 0.00);
		double lon = getIntent().getDoubleExtra("lon", 0.00);
		String hosName = getIntent().getStringExtra("hosName");
		String tel = getIntent().getStringExtra("hosTel");
		String ads = getIntent().getStringExtra("hosAds");
		tvAds.setText("地址：" + ads);
		tvTel.setText("电话：" + tel);
		tvHos.setText(hosName);
		LatLng end = new LatLng(lat, lon);
		endPoint = PlanNode.withLocation(end);
		init(end);
		location();
	}

	/**
	 * 定位
	 */
	public void location() {
		// if (lp == null) {
		// 开启定位图层
		mBaidumap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// }
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || map == null)
				return;
			BaseApplication.getInstance().lat = location.getLatitude();
			BaseApplication.getInstance().lon = location.getLongitude();
			lp = new LatLng(BaseApplication.getInstance().lat,
					BaseApplication.getInstance().lon);// 设置中心位置
			startPoint = PlanNode.withLocation(lp);
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaidumap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaidumap.animateMapStatus(u);
			}
		}
	}

	/**
	 * 初始化
	 */
	private void init(LatLng latlng) {
		if (mBaidumap == null) {
			mBaidumap = map.getMap();
			mBaidumap.setMyLocationEnabled(true);
			mBaidumap.setMapStatus(MapStatusUpdateFactory.zoomBy(5));
			regeoMarker = (Marker) mBaidumap.addOverlay(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_gcoding))
					.position(latlng));
			uiSetting = mBaidumap.getUiSettings();
		}
		mBaidumap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
		mBaidumap.setOnMarkerClickListener(this);
		progDialog = new ProgressDialog(this);
		map.showZoomControls(false);
		uiSetting = mBaidumap.getUiSettings();
		uiSetting.setRotateGesturesEnabled(false);
		uiSetting.setCompassEnabled(false);

		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		showInfo = new TextView(this);
		showInfo.setMinWidth(getWindowWidth() / 2);
		showInfo.setMaxWidth(getWindowWidth() - 20);
		showInfo.setPadding(5, 5, 10, 5);
		showInfo.setBackground(getResources().getDrawable(
				R.drawable.custom_info_bubble));
		showInfo.setGravity(Gravity.CENTER);
		showInfo.setSingleLine(false);
	}
	@OnClick(R.id.map_rim_hos_driving)
	private void drivingClick(View v){
		// 驾车
		drivingRoute();
		btTo.performClick();
	}
	@OnClick(R.id.map_rim_hos_bus)
	private void busClick(View v){
		// 公交
		busRoute();
		btTo.performClick();
	}
	@OnClick(R.id.map_rim_hos_walk)
	private void walkClick(View v){
		//步行
		walkRoute();
		btTo.performClick();
	}
	@OnClick(R.id.map_rim_hos_bt)
	private void btClick(View v){
		// 获取具体路线
		if (startPoint != null && endPoint != null) {
			selectWaypath();
			if (!layoutType.isShown()) {
				layoutType.setVisibility(View.VISIBLE);
			}
			btTo.setVisibility(View.GONE);
		} else {
			return;
		}
	}

	/**
	 * 选择驾车模式
	 */
	private void drivingRoute() {
		routeType = 2;// 标识为驾车模式
		btDriving.setBackgroundResource(R.drawable.mode_driving_on);
		btBus.setBackgroundResource(R.drawable.mode_transit_off);
		btWalk.setBackgroundResource(R.drawable.mode_walk_off);
		route = null;
		mBaidumap.clear();
		mSearch.drivingSearch((new DrivingRoutePlanOption()).from(startPoint)
				.to(endPoint));
	}

	/**
	 * 选择步行模式
	 */
	private void walkRoute() {
		routeType = 3;// 标识为步行模式
		btDriving.setBackgroundResource(R.drawable.mode_driving_off);
		btBus.setBackgroundResource(R.drawable.mode_transit_off);
		btWalk.setBackgroundResource(R.drawable.mode_walk_on);
		route = null;
		mBaidumap.clear();
		mSearch.walkingSearch((new WalkingRoutePlanOption()).from(startPoint)
				.to(endPoint));
	}

	/**
	 * 选择公交模式
	 */
	private void busRoute() {
		routeType = 1;// 标识为公交模式
		btDriving.setBackgroundResource(R.drawable.mode_driving_off);
		btBus.setBackgroundResource(R.drawable.mode_transit_on);
		btWalk.setBackgroundResource(R.drawable.mode_walk_off);
		route = null;
		mBaidumap.clear();
		mSearch.transitSearch((new TransitRoutePlanOption()).from(startPoint)
				.city(BaseApplication.selectCity).to(endPoint));
	}

	/**
	 * 显示进度条对话框
	 */
	public void showDialog(String hint) {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage(hint);
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {// 关闭页面停止定位，否则我的位置所显示的点不能显示出来
		super.onDestroy();
		// deactivate();
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient.unRegisterLocationListener(myListener);
		}
	}

	/**
	 * 显示路线节点信息
	 */
	public void showWindowInfo(String info, LatLng location) {
		showInfo.setText(info);
		InfoWindow window = new InfoWindow(showInfo, location, -20);
		mBaidumap.showInfoWindow(window);
	}

	/**
	 * 选择路线
	 */
	public void selectWaypath() {
		showDialog("正在获取具体路径");
		switch (routeType) {
		case 1:
			busRoute();
			break;
		case 2:
			drivingRoute();
			break;
		case 3:
			walkRoute();
			break;
		}
	}

	/**
	 * 驾车结果回调
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		dismissDialog();
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// nodeIndex = -1;
			// mBtnPre.setVisibility(View.VISIBLE);
			// mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	/**
	 * 公交路线查询回调
	 */
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		dismissDialog();
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			if (result.error.ordinal() == 6) {
				Toast.makeText(this, "抱歉，您的起点太近", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			}
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// nodeIndex = -1;
			// mBtnPre.setVisibility(View.VISIBLE);
			// mBtnNext.setVisibility(View.VISIBLE);
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	/**
	 * 步行路线结果回调
	 */
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		dismissDialog();
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// nodeIndex = -1;
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
		LatLng latlng;
		List<DrivingStep> list;
		DrivingStep step;

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			list = route.getAllStep();
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (list != null && list.size() > 0) {
				step = list.get(0);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return super.getStartMarker();
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {

			return super.getTerminalMarker();
		}

		@Override
		public boolean onRouteNodeClick(int position) {
			if (list != null && list.size() > 0) {
				step = list.get(position);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return false;
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
		LatLng latlng;
		List<WalkingStep> list;
		WalkingStep step;

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			list = route.getAllStep();
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (list != null && list.size() > 0) {
				step = list.get(0);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return super.getStartMarker();
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {

			return super.getTerminalMarker();
		}

		@Override
		public boolean onRouteNodeClick(int arg0) {
			if (list != null && list.size() > 0) {
				step = list.get(arg0);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return false;
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay {
		TransitStep step;
		LatLng latlng;
		List<TransitStep> list;

		public MyTransitRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			list = route.getAllStep();
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (list != null && list.size() > 0) {
				step = list.get(0);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return super.getStartMarker();
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {

			return super.getTerminalMarker();
		}

		@Override
		public boolean onRouteNodeClick(int arg0) {
			if (list != null && list.size() > 0) {
				step = list.get(arg0);
				latlng = step.getEntrace().getLocation();
				mBaidumap.animateMapStatus(MapStatusUpdateFactory
						.newLatLng(latlng));
				showWindowInfo(step.getInstructions(), latlng);
			}
			return false;
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		endPoint = PlanNode.withLocation(marker.getPosition());
		return false;
	}
}

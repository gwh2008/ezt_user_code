package com.eztcn.user.eztcn.activity.discover;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
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
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.NearbyViewPagerAdapter;
import com.eztcn.user.eztcn.adapter.RimHospitalAdapter;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.utils.CacheUtils;

/**
 * @title 周边医院列表
 * @describe
 * @author ezt
 * @created 2014年12月16日
 */
public class RimHospitalActivity extends FinalActivity implements
		OnLoadMoreListener, OnRefreshListener, OnClickListener,
		OnMapClickListener, OnGetRoutePlanResultListener,
		OnGetPoiSearchResultListener, OnMarkerClickListener,
		OnItemClickListener {

	// @ViewInject(R.id.rim_hos_lv, itemClick = "onItemClick")
	private PullToRefreshListView lvHos;// 周边医院列表
	private PullToRefreshListView lvDrug;// 周边药店
	private RimHospitalAdapter adapter;
	private int currentPage = 0;// 当前页数
	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
	private String deepType = "医院";// 搜索类型
	private String city = "";// 搜索的城市
	private LatLng lp = null;
	private int searchRadius = 2000;// 搜索半径
	private PoiResult poiResult; // poi返回的结果
	private ArrayList<PoiInfo> poiItems;// poi数据

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirstLoc = true;// 是否首次定位

	private final String RIM_HOS_DATA = "RimHosData";// 缓存key
	private CacheUtils mCache;

	@ViewInject(R.id.rim_hos_maps)
	private MapView map;
	@ViewInject(R.id.rim_hos_maps_layout)
	private RelativeLayout layoutMap;

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

	private BaiduMap mBaidumap = null;
	private PoiNearbySearchOption option;// 搜索配置
	private PoiSearch poiSearch;// 搜索类
	private UiSettings uiSetting = null;
	// 规划路径
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private PlanNode startPoint = null;// 自己当前的位置
	private PlanNode endPoint = null;// 医院位置
	/**
	 * 1代表公交模式，2代表驾车模式，3代表步行模式
	 */
	private int routeType = 3;

	private PoiOverlay poiOverlay;
	private TextView tvLeft;
	private ImageView imgRight;
	// 浏览路线节点相关
	RouteLine route = null;
	OverlayManager routeOverlay = null;
	Marker locationMarker;
	private TextView showInfo;

	private boolean isShowLine = false;// 是否显示路线
	private int selectIndex = 0;// 选中下标
	private boolean isMap = false;// 地图列表切换标记
	private float zoom;// 缩放级别

	/**** tab viewpage ***************************/
	@ViewInject(R.id.nearbyLayout)
	private LinearLayout nearbyLayout;
	@ViewInject(R.id.nearbyHos)
	private TextView nearbyHos;// 周边医院
	@ViewInject(R.id.nearbyDrug)
	private TextView nearbyDrug;// 周边药店
	@ViewInject(R.id.pageLine)
	private View pageLine;// tab滑动条
	@ViewInject(R.id.content_vPager)
	private ViewPager viewPager;

	NearbyViewPagerAdapter nearAdapter;
	private List<PullToRefreshListView> viewList;
	private int currIndex;// 当前页卡编号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rim_hospital);
		ViewUtils.inject(RimHospitalActivity.this);
		mCache = CacheUtils.get(this);
		imgRight = loadTitleBar(true, "周边", R.drawable.ic_map);
		map = (MapView) findViewById(R.id.rim_hos_maps);
		tvLeft = (TextView) findViewById(R.id.left_btn);
		tvLeft.setOnClickListener(this);
		imgRight.setEnabled(false);
		imgRight.setOnClickListener(this);
		initialView();
		location();
	}

	/**
	 * 初始化tab
	 */
	public void initViewPage() {
		viewList = new ArrayList<PullToRefreshListView>();
		viewList.add(lvHos);
		viewList.add(lvDrug);

		// 初始化颜色
		pageLine.setBackgroundColor(getResources().getColor(
				R.color.title_bar_bg));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				getWindowWidth() / viewList.size(), 5);
		pageLine.setLayoutParams(params);

		nearAdapter = new NearbyViewPagerAdapter(viewList);
		viewPager.setAdapter(nearAdapter);
		viewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				slideView(position);
				currIndex = position;
				if (position == 0) {
					nearbyHos.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
					nearbyDrug.setTextColor(getResources().getColor(
							R.color.dark_black));
					deepType = "医院";
					currentPage = 0;
					adapter = new RimHospitalAdapter(RimHospitalActivity.this);
					lvHos.setAdapter(adapter);
				} else {
					nearbyHos.setTextColor(getResources().getColor(
							R.color.dark_black));
					nearbyDrug.setTextColor(getResources().getColor(
							R.color.title_bar_bg));
					deepType = "药店";
					currentPage = 0;
					adapter = new RimHospitalAdapter(RimHospitalActivity.this);
					lvDrug.setAdapter(adapter);
				}
				initialHosList();
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	// 底部滑动， positionId， 你要滑动到的位置
	private void slideView(int positionIndex) {
		TranslateAnimation tran = null;
		tran = new TranslateAnimation(calcPosition(currIndex),
				calcPosition(positionIndex), 0, 0);
		tran.setDuration(100);
		tran.setFillAfter(true);
		pageLine.startAnimation(tran);
	}

	// 计算位置 position 需要移动到的位置
	private int calcPosition(int positionIndex) {
		int position = getWindowWidth() / viewList.size();
		int currentPosition = 0;
		for (int i = 0; i < positionIndex; i++) {
			currentPosition += position;
		}
		return currentPosition;
	}

	/**
	 * 初始化View
	 */
	private void initialView() {
		if (lvHos == null) {
			lvHos = new PullToRefreshListView(this);
			lvHos.setSelector(android.R.color.transparent);
			lvHos.setBackgroundResource(android.R.color.white);
			lvHos.setCanLoadMore(true);
			lvHos.setCanRefresh(true);
			lvHos.setAutoLoadMore(true);
			lvHos.setMoveToFirstItemAfterRefresh(false);
			lvHos.setDoRefreshOnUIChanged(false);
			lvHos.setOnLoadListener(this);
			lvHos.setOnRefreshListener(this);
			lvHos.setVerticalScrollBarEnabled(false);
			lvHos.setOnItemClickListener(this);

			adapter = new RimHospitalAdapter(this);
			lvHos.setAdapter(adapter);

			lvDrug = new PullToRefreshListView(this);
			lvDrug.setSelector(android.R.color.transparent);
			lvDrug.setBackgroundResource(android.R.color.white);
			lvDrug.setCanLoadMore(true);
			lvDrug.setCanRefresh(true);
			lvDrug.setAutoLoadMore(true);
			lvDrug.setMoveToFirstItemAfterRefresh(false);
			lvDrug.setDoRefreshOnUIChanged(false);
			lvDrug.setOnLoadListener(this);
			lvDrug.setOnRefreshListener(this);
			lvDrug.setVerticalScrollBarEnabled(false);
			lvDrug.setOnItemClickListener(this);
		}
		BaseApplication app = BaseApplication.getInstance();
		city = app.city;
		lp = new LatLng(BaseApplication.getInstance().lat,
				BaseApplication.getInstance().lon);// 设置中心位置
		startPoint = PlanNode.withLocation(lp);
		if (mBaidumap == null) {
			mBaidumap = map.getMap();
			// 地图点击事件处理
			mBaidumap.setOnMapClickListener(this);
			mBaidumap.setMyLocationEnabled(true);
			map.showZoomControls(false);
			uiSetting = mBaidumap.getUiSettings();
			uiSetting.setRotateGesturesEnabled(false);
			uiSetting.setCompassEnabled(false);
			// 初始化搜索模块，注册事件监听
			mSearch = RoutePlanSearch.newInstance();
			mSearch.setOnGetRoutePlanResultListener(this);
			mBaidumap.setOnMarkerClickListener(this);
		}
		zoom = new MapStatus.Builder().build().zoom;
		initViewPage();
		initInfoWidget();
		initialHosList();
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
	 * 初始化数据
	 */
	private void initialHosList() {
		option = new PoiNearbySearchOption();
		option.location(lp);
		option.keyword(deepType);
		option.radius(searchRadius);
		option.pageNum(currentPage);// 设置查第一页
		option.pageCapacity(pageSize);// 设置每页最多返回多少条poiitem
		option.sortType(PoiSortType.distance_from_near_to_far);
		poiSearch = PoiSearch.newInstance();
		poiSearch.searchNearby(option);
		if (lp != null) {
			adapter.setLocation(lp);
			if (BaseApplication.getInstance().isNetConnected) {
				showProgressToast();
				poiSearch.setOnGetPoiSearchResultListener(this);
			} else {
				List<PoiInfo> cacheData = (List<PoiInfo>) mCache
						.getAsObject(RIM_HOS_DATA);
				adapter.setList(cacheData);
				if (adapter.getList() != null && adapter.getList().size() != 0) {
					nearbyLayout.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mLocClient != null) {
			mLocClient.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mLocClient != null) {
			mLocClient.stop();
		}
	}

	@Override
	protected void onDestroy() {// 关闭页面停止定位，否则我的位置所显示的点不能显示出来
		super.onDestroy();
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient.unRegisterLocationListener(mMyLocationListener);
			mBaidumap.clear();
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

	@Override
	public void onBackPressed() {
		if (isShowLine) {
			layoutType.setVisibility(View.GONE);
			isShowLine = false;
			btTo.setVisibility(View.VISIBLE);
			imgRight.setVisibility(View.VISIBLE);
			endPoint = null;
			initialMapMarker(selectIndex);
		} else {
			super.onBackPressed();
		}

	}
	@OnClick(R.id.left_btn)
	private void backClick(View v){
		// 返回按钮
		onBackPressed();
	}
	@OnClick(R.id.nearbyHos)
private void nearbyHosClick(View v){// 周边医院
		if (currIndex != 0) {
			currIndex = 0;
//			currentPage = 0;
//			adapter = new RimHospitalAdapter(this);
//			lvHos.setAdapter(adapter);
			viewPager.setCurrentItem(currIndex);
		}
}
	@OnClick(R.id.nearbyDrug)
	private void nearByDrugClick(View v){
		if (currIndex != 1) {// 周边药店
			currIndex = 1;
//			currentPage = 0;
//			adapter = new RimHospitalAdapter(this);
//			lvDrug.setAdapter(adapter);
			viewPager.setCurrentItem(currIndex);
		}
	}
	@OnClick(R.id.map_rim_hos_driving)
	private void drivingClick(View v){
		// 驾车
		drivingRoute();
		btTo.performClick();
	}
	
	@OnClick(R.id.map_rim_hos_bus)
	private void busClick(View v){
		//公交
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
	private void gainLineClick(View v){
		// 获取具体路线
		if (startPoint != null && endPoint != null) {
			isShowLine = true;
			mBaidumap.clear();
			selectWaypath();
			if (!layoutType.isShown()) {
				layoutType.setVisibility(View.VISIBLE);
			}
			btTo.setVisibility(View.GONE);
			imgRight.setVisibility(View.GONE);

		} else {
			return;
		}
	}
	@Override
	public void onClick(View v) {

	
		// 列表地图切换
			if (!isMap) {// 地图
				isMap = true;
				imgRight.setImageResource(R.drawable.ic_hoslist);
				nearbyLayout.setVisibility(View.GONE);
				layoutMap.setVisibility(View.VISIBLE);
				btTo.setVisibility(View.VISIBLE);
				initialMapMarker(0);

				if (zoom < 0) {
					zoom = 5;
				} else {
					zoom = new MapStatus.Builder().build().zoom;
				}
				mBaidumap.animateMapStatus(MapStatusUpdateFactory.zoomBy(zoom));
			} else {// 列表
				isMap = false;
				imgRight.setImageResource(R.drawable.ic_map);
				nearbyLayout.setVisibility(View.VISIBLE);
				layoutMap.setVisibility(View.GONE);
			}
		

	}

	/**
	 * 选择路线
	 */
	public void selectWaypath() {
		showProgressToast();
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
	 * 初始化医院标记图标
	 */
	private void initialMapMarker(int index) {
		ArrayList<PoiInfo> pItems = (ArrayList<PoiInfo>) adapter.getList();
		mBaidumap.clear();// 清理之前的图标
		poiOverlay = new PoiOverlay(mBaidumap);
		poiOverlay.setData(poiResult);
		poiOverlay.removeFromMap();
		poiOverlay.addToMap();
		poiOverlay.zoomToSpan();
		LatLng point = pItems.get(index).location;
		Bundle bundle;
		for (int i = 0; i < pItems.size(); i++) {
			point = pItems.get(i).location;
			bundle = new Bundle();
			bundle.putInt("listPosition", i);
			locationMarker = (Marker) mBaidumap.addOverlay(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.icon_gcoding))
					.position(point).title(pItems.get(i).name)
					.extraInfo(bundle));
		}
		mBaidumap.setOnMarkerClickListener(this);
		PoiInfo item = pItems.get(index);
		String hosName = item.name;
		String hosTel = item.phoneNum;
		String hosAds = item.address;
		initialHosInfo(hosName, hosTel, hosAds);
		endPoint = PlanNode.withLocation(point);
	}

	/**
	 * listView itemClick
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		toRimHosInfo(position);

	}

	/**
	 * 跳转详情
	 */
	private void toRimHosInfo(int pos) {
		if (BaseApplication.getInstance().isNetConnected) {

			double lat = adapter.getList().get(pos).location.latitude;
			double lon = adapter.getList().get(pos).location.longitude;
			String hosName = adapter.getList().get(pos).name;
			String hosTel = adapter.getList().get(pos).phoneNum;
			String hosAds = adapter.getList().get(pos).address;

			startActivity(new Intent(RimHospitalActivity.this,
					MapRimHosActivity.class).putExtra("lat", lat)
					.putExtra("lon", lon).putExtra("hosName", hosName)
					.putExtra("hosTel", hosTel).putExtra("hosAds", hosAds));
		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void initialHosInfo(String hosName, String tel, String ads) {
		tvHos.setText(hosName);
		tvTel.setText(tel);
		tvAds.setText(ads);
	}

	/**
	 * 驾车结果回调
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		hideProgressToast();
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
			routeOverlay = overlay;
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
		hideProgressToast();
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
		hideProgressToast();
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
			routeOverlay = overlay;
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
		Bundle bundle = marker.getExtraInfo();
		int position = bundle.getInt("listPosition");
		PoiInfo info = adapter.getList().get(position);
		mBaidumap.setMapStatus(MapStatusUpdateFactory.newLatLng(info.location));
		initialHosInfo(info.name, info.phoneNum, info.address);
		showInfo.setText(info.name);
		InfoWindow window = new InfoWindow(showInfo, info.location, -45);
		mBaidumap.showInfoWindow(window);
		endPoint = PlanNode.withLocation(info.location);
		return false;
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
	}

	/**
	 * 显示路线节点信息
	 */
	public void showWindowInfo(String info, LatLng location) {
		showInfo.setText(info);
		InfoWindow window = new InfoWindow(showInfo, location, -20);
		mBaidumap.showInfoWindow(window);
	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

	@Override
	public boolean onMapPoiClick(MapPoi mp) {
		return false;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		hideProgressToast();
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if (result != null && result.getAllPoi() != null) {// 搜索poi的结果
				poiResult = result;
				poiItems = (ArrayList<PoiInfo>) poiResult.getAllPoi();// 取得第一页的poiitem数据，页数从数字0开始
				if (poiItems != null && poiItems.size() > 0) {
					List<PoiInfo> data = null;
					if (currentPage == 0) {// 第一次加载或刷新
						data = poiItems;
						if (poiItems.size() < pageSize) {
							viewList.get(currIndex).setAutoLoadMore(false);
							viewList.get(currIndex).onLoadMoreComplete();
						}
						viewList.get(currIndex).onRefreshComplete();
						mCache.put(RIM_HOS_DATA, poiItems);

					} else {// 加载更多
						data = (List<PoiInfo>) adapter.getList();
						if (data == null || data.size() <= 0)
							data = poiItems;
						else
							data.addAll(poiItems);

						if (poiItems.size() < pageSize) {
							viewList.get(currIndex).setAutoLoadMore(false);
						}
						viewList.get(currIndex).onLoadMoreComplete();
					}
					adapter.setList(data);
					viewList.get(currIndex).setVisibility(View.VISIBLE);
					imgRight.setEnabled(true);
				} else {

					if (adapter.getList() != null) {// 加载
						viewList.get(currIndex).setAutoLoadMore(false);
						viewList.get(currIndex).onLoadMoreComplete();
					} else {// 刷新
						viewList.get(currIndex).onRefreshComplete();
					}
				}
			} else {
				Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			}

		}
	}

	@Override
	public void onRefresh() {
		currentPage = 0;// 还原页值
		viewList.get(currIndex).setAutoLoadMore(true);
		// option.pageNum(currentPage);
		// poiSearch.setOnGetPoiSearchResultListener(this);
		initialHosList();
	}

	@Override
	public void onLoadMore() {
		if (poiItems != null && poiSearch != null && poiResult != null) {
			if (poiResult.getTotalPageNum() > currentPage) {
				currentPage++;
				initialHosList();
			} else {
				viewList.get(currIndex).setAutoLoadMore(false);
				viewList.get(currIndex).onLoadMoreComplete();
			}

		}

	}
}

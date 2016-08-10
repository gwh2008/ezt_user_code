///**
// * 
// */
//package com.eztcn.user.eztcn.activity.watch;
//
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
//import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.InfoWindow;
//import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.model.LatLng;
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.bean.MessageItem;
//import com.eztcn.user.eztcn.db.SystemPreferences;
//import com.eztcn.user.eztcn.utils.StringUtil;
//import com.eztcn.user.eztcn.utils.message.MessageReceiver;
//import com.eztcn.user.eztcn.utils.message.MessageUtil;
//
///**
// * @author Liu Gang
// * 
// *         2015年9月14日 下午4:50:25 我的表 用来“监视”亲属的位置！
// * 
// *         该界面前需要像intent传入 name
// */
//public class MyWatchActivity extends FinalActivity implements OnClickListener {
//	@ViewInject(R.id.bmapView)
//	private MapView mMapView;
//	private BaiduMap mBaiduMap;
//	private Marker mFamilyMaker;
//	private InfoWindow mInfoWindow;
//
//	// 初始化全局 bitmap 信息，不用时及时 recycle
//	BitmapDescriptor bitmap_position = BitmapDescriptorFactory
//			.fromResource(R.drawable.icon_gcoding);
//	private String name;
//	private EditText edit_phone;
//	private Dialog dialog;
//	private String address, latitude, longitude;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_overlay);
//
//		mBaiduMap = mMapView.getMap();
//		// 设置地图缩放级别
//		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
//		mBaiduMap.setMapStatus(msu);
//		mBaiduMap.setOnMarkerClickListener(markerClickListener);
//		gainIntentData();
//		String phoneNumber = SystemPreferences.getString(name, "");
//		if ("".equals(phoneNumber)) {
//			View view = LayoutInflater.from(MyWatchActivity.this).inflate(
//					R.layout.activity_location_phone, null);
//			dialog = new Dialog(MyWatchActivity.this);
//			dialog.setContentView(view);
//			TextView textView = (TextView) view.findViewById(R.id.phone_please);
//			textView.setText("请输入" + name + "的手机号");
//			Button btn = (Button) view.findViewById(R.id.btnsure);
//			edit_phone = (EditText) view.findViewById(R.id.text_phone);
//			btn.setOnClickListener(this);
//			dialog.show();
//		} else {
//			messageTrans(phoneNumber);
//		}
//
//	}
//	/**
//	 * 点击可查看详情
//	 */
//	private OnMarkerClickListener markerClickListener = new OnMarkerClickListener() {
//
//		@Override
//		public boolean onMarkerClick(final Marker marker) {
//			Button button = new Button(getApplicationContext());
//			button.setBackgroundResource(R.drawable.popup);
//			button.setText("查看详情");
//			LatLng ll = marker.getPosition();
//			OnInfoWindowClickListener listener = null;
//			listener = new OnInfoWindowClickListener() {
//				public void onInfoWindowClick() {
//					new Builder(MyWatchActivity.this)
//							.setMessage(marker.getTitle().toString())
//							.setPositiveButton("确定", null).create().show();
//					mBaiduMap.hideInfoWindow();
//				}
//			};
//			mInfoWindow = new InfoWindow(
//					BitmapDescriptorFactory.fromView(button), ll, -47, listener);
//			mBaiduMap.showInfoWindow(mInfoWindow);
//			return true;
//		}
//	};
//
//	private void gainIntentData() {
//		name = getIntent().getStringExtra("name");
//	}
//
//	public void initOverlay() {
//		// add marker overlay
//		LatLng family_latLng = new LatLng(Double.parseDouble(latitude),
//				Double.parseDouble(longitude));
//		// 设置 marker 覆盖物的位置坐标,图标，zIndex 是否可拖拽
//		OverlayOptions familyOptions = new MarkerOptions()
//				.position(family_latLng).icon(bitmap_position).zIndex(9)
//				.title(address).draggable(true);
//		// 地图覆盖物选型基类
//		mFamilyMaker = (Marker) (mBaiduMap.addOverlay(familyOptions));
//		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(family_latLng);
//		mBaiduMap.setMapStatus(u);
//		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
//			public void onMarkerDrag(Marker marker) {
//			}
//
//			public void onMarkerDragEnd(Marker marker) {
//				Toast.makeText(
//						MyWatchActivity.this,
//						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
//								+ marker.getPosition().longitude,
//						Toast.LENGTH_LONG).show();
//			}
//
//			public void onMarkerDragStart(Marker marker) {
//			}
//		});
//		hideProgressToast();
//	}
//
//	@Override
//	protected void onPause() {
//		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
//		mMapView.onPause();
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
//		mMapView.onResume();
//		super.onResume();
//	}
//
//	@Override
//	protected void onDestroy() {
//		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
//		mMapView.onDestroy();
//		super.onDestroy();
//		// 回收 bitmap 资源
//		bitmap_position.recycle();
//		hideProgressToast();
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btnsure: {
//			String edit_phone_str = edit_phone.getText().toString();
//			if (StringUtil.isPhone(edit_phone_str)) {
//				messageTrans(edit_phone_str);
//				dialog.dismiss();
//				showProgressToast();
//			} else {
//				Toast.makeText(MyWatchActivity.this, "请输入正确格式的手机号",
//						Toast.LENGTH_SHORT).show();
//			}
//		}
//			break;
//		}
//
//	}
//	/**
//	 * 像手表发送信息去获取定位经纬度
//	 * @param phone
//	 */
//	private void messageTrans(String phone) {
//		MessageUtil msgUtil = new MessageUtil();
//		SystemPreferences.save(name, phone);
//		msgUtil.send(phone, "ezt_get_location", MyWatchActivity.this);
////		msgUtil.sendSMS(phone, "ezt_get_location", MyWatchActivity.this);
//		MessageReceiver reciver = new MessageReceiver(handler);
//		registerReceiver(reciver, new IntentFilter(
//				"android.provider.Telephony.SMS_RECEIVED"));
//	}
//
//	private Handler handler = new Handler() {
//		//接收到短信后 初始化坐标 然后定位
//		public void handleMessage(android.os.Message msg) {
//			MessageItem msgItem = (MessageItem) msg.obj;
//
//			String msgContent = msgItem.getBody();
//			String prefix = "location:";
//			if (msgContent.startsWith(prefix)) {
//				msgContent = msgContent.subSequence(prefix.length(),
//						msgContent.length()).toString();
//				String[] msg_temp = msgContent.split(",");
//				address = msg_temp[0];
//				latitude = msg_temp[1];
//				longitude = msg_temp[2];
//				initOverlay();
//			}
//		};
//
//	};
//}

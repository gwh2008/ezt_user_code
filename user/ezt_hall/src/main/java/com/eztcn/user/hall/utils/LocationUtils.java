package com.eztcn.user.hall.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.eztcn.user.hall.interfaces.LocationListener;

public class LocationUtils {
	private static LocationClient locationClient;
	private static LocationClientOption option;
	static {
		// 设置定位条件
		option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(500);
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setIgnoreKillProcess(false);

	}

    /**
     * 只会定位一次，就会停止定位
     * @param context
     * @param locationListener
     */
	public static void location(Context context,final LocationListener locationListener) {
		if (locationClient == null) {
			locationClient = new LocationClient(context);
			locationClient.setLocOption(option);
		}
		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {

				location.getLocType();
				locationListener.onReceiveLocation(location);
				if (locationClient != null && locationClient.isStarted()) {
					locationClient.stop();
				}
			}

		});
		if (locationClient != null&&!locationClient.isStarted()) {
			locationClient.start();
		}
		
		/*
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
		 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */
		locationClient.requestLocation();
	}
}

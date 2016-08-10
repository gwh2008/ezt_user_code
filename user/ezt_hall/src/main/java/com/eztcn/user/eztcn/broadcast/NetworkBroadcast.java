package com.eztcn.user.eztcn.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.eztcn.user.eztcn.BaseApplication;

/**
 * @title 接收网络状态改变广播
 * @describe 实时检测网络连接状况
 * @author ezt
 * @created 2014年11月10日
 */
public class NetworkBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			// 判断网络连接状态
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {

					BaseApplication.getInstance().isNetConnected = true;
					return;
				}
			} else {
				BaseApplication.getInstance().isNetConnected = false;
			}
		}

	}
}
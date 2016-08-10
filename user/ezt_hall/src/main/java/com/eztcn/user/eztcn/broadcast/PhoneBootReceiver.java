package com.eztcn.user.eztcn.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.eztcn.user.eztcn.listener.TelListener;

/**
 * @title 电话接听广播
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class PhoneBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TelephonyManager telM = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		telM.listen(new TelListener(context),
				PhoneStateListener.LISTEN_CALL_STATE);

	}

}

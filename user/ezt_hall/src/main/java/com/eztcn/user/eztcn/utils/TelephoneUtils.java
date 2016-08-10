/**
 * 
 */
package com.eztcn.user.eztcn.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author Liu Gang
 * 
 *         2015年10月9日 下午7:01:54
 * 
 */
public class TelephoneUtils {
	public static String gainUUid(Context context) {
		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String tmDevice="";
//		String tmSerial, tmPhone, androidId;
		tmDevice = String.valueOf(tm.getDeviceId());
//		tmSerial = "" + tm.getSimSerialNumber();
//		androidId = ""
//				+ android.provider.Settings.Secure.getString(
//						context.getContentResolver(),
//						android.provider.Settings.Secure.ANDROID_ID);
		return tmDevice;
	}

}

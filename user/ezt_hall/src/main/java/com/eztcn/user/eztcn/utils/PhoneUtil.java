package com.eztcn.user.eztcn.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;

/**
 * @title 电话相关操作工具类
 * @describe
 * @author ezt
 * @created 2014年12月25日
 */
public class PhoneUtil {

	public static String TAG = PhoneUtil.class.getSimpleName();

	/**
	 * 挂断电话
	 * 
	 * @param context
	 */
	public static void endCall(Context context) {
		try {
			Object telephonyObject = getTelephonyObject(context);
			if (null != telephonyObject) {
				Class telephonyClass = telephonyObject.getClass();

				Method endCallMethod = telephonyClass.getMethod("endCall");
				endCallMethod.setAccessible(true);

				endCallMethod.invoke(telephonyObject);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private static Object getTelephonyObject(Context context) {
		Object telephonyObject = null;
		try {
			// 初始化iTelephony
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// Will be used to invoke hidden methods with reflection
			// Get the current object implementing ITelephony interface
			Class telManager = telephonyManager.getClass();
			Method getITelephony = telManager
					.getDeclaredMethod("getITelephony");
			getITelephony.setAccessible(true);
			telephonyObject = getITelephony.invoke(telephonyManager);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return telephonyObject;
	}

	/**
	 * 通过反射调用的方法，接听电话，该方法只在android 2.3之前的系统上有效。
	 * 
	 * @param context
	 */
	private static void answerRingingCallWithReflect(Context context) {
		try {
			Object telephonyObject = getTelephonyObject(context);
			if (null != telephonyObject) {
				Class telephonyClass = telephonyObject.getClass();
				Method endCallMethod = telephonyClass
						.getMethod("answerRingingCall");
				endCallMethod.setAccessible(true);

				endCallMethod.invoke(telephonyObject);
				// ITelephony iTelephony = (ITelephony) telephonyObject;
				// iTelephony.answerRingingCall();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 伪造一个有线耳机插入，并按接听键的广播，让系统开始接听电话。
	 * 
	 * @param context
	 */
	private static void answerRingingCallWithBroadcast(Context context) {
//		AudioManager localAudioManager = (AudioManager) context
//				.getSystemService(Context.AUDIO_SERVICE);
//		// 判断是否插上了耳机
//		boolean isWiredHeadsetOn = localAudioManager.isWiredHeadsetOn();
//		if (!isWiredHeadsetOn) {
//            //4.1以上系统限制了部分权限， 使用三星4.1版本测试提示警告：Permission Denial: not allowed to send broadcast android.intent.action.HEADSET_PLUG from pid=1324, uid=10017
//            //这里需要注意一点，发送广播时加了权限“android.permission.CALL_PRIVLEGED”，则接受该广播时也需要增加该权限。但是4.1以上版本貌似这个权限只能系统应用才可以得到。
//            // 测试的时候，自定义的接收器无法接受到此广播，后来去掉了这个权限，设为NULL便可以监听到了。
//			Intent headsetPluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);//高版本把这个广播的权限给了系统
//			headsetPluggedIntent.putExtra("state", 1);
//			headsetPluggedIntent.putExtra("microphone", 0);
//			headsetPluggedIntent.putExtra("name", "");
//			context.sendBroadcast(headsetPluggedIntent);
//
//			Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
//			meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//			context.sendOrderedBroadcast(meidaButtonIntent,"android.permission.CALL_PRIVILEGED");
//
//            Intent headsetUnpluggedIntent = new Intent(Intent.ACTION_HEADSET_PLUG);
//			headsetUnpluggedIntent.putExtra("state", 0);
//			headsetUnpluggedIntent.putExtra("microphone", 0);
//			headsetUnpluggedIntent.putExtra("name", "");
//			context.sendBroadcast(headsetUnpluggedIntent);
//
//		} else {
//			Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
//			KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
//			meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
//			context.sendOrderedBroadcast(meidaButtonIntent, null);
//		}

        //只发送这个耳机的按键广播就可以自动接听电话了
        Intent meidaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_HEADSETHOOK);
        meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(meidaButtonIntent,null);
	}

	/**
	 * 接听电话
	 * 
	 * @param context
	 */
	public static void answerRingingCall(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) { // 2.3或2.3以上系统
			answerRingingCallWithBroadcast(context);
		} else {
			answerRingingCallWithReflect(context);
		}
	}

	/**
	 * 打电话
	 * 
	 * @param context
	 * @param phoneNumber
	 */
	public static void callPhone(Context context, String phoneNumber) {
		if (!TextUtils.isEmpty(phoneNumber)) {
			try {
				Intent callIntent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + phoneNumber));
				context.startActivity(callIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 拨电话
	 * 
	 * @param context
	 * @param phoneNumber
	 */
	public static void dialPhone(Context context, String phoneNumber) {
		if (!TextUtils.isEmpty(phoneNumber)) {
			try {
				Intent callIntent = new Intent(Intent.ACTION_DIAL,
						Uri.parse("tel:" + phoneNumber));
				context.startActivity(callIntent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

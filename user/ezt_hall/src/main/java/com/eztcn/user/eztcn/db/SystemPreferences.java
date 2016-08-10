package com.eztcn.user.eztcn.db;

import android.content.SharedPreferences.Editor;

import com.eztcn.user.eztcn.BaseApplication;

/**
 * @title 系统配置文件
 * @describe
 * @author ezt
 * @created 2014年11月10日
 */
public class SystemPreferences {

	/**
	 * 保存数据到 系统配置文件
	 * 
	 * @param key
	 * @param value
	 */
	public static void save(String key, Object value) {
		try {
			Editor editor = BaseApplication.getInstance().getPreferences()
					.edit();
			if (value instanceof String)
				editor.putString(key, (String) value);
			else if (value instanceof Boolean)
				editor.putBoolean(key, (Boolean) value);
			else if (value instanceof Long)
				editor.putLong(key, (Long) value);
			else
				editor.putInt(key, (Integer) value);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @param value
	 */
	public static void remove(String key) {
		if (BaseApplication.getInstance().getPreferences() != null) {
			Editor editor = BaseApplication.getInstance().getPreferences()
					.edit();
			editor.remove(key).commit();
			editor.commit();
		}
	}

	/**
	 * 读取配置信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getString(key, "");
	}

	/**
	 * 读取配置信息
	 * 
	 * @param key
	 * @param defValue
	 *            默认值
	 * @return
	 */
	public static String getString(String key, String defValue) {
		String value = "";
		if (BaseApplication.getInstance().getPreferences() != null) {
			value = BaseApplication.getInstance().getPreferences()
					.getString(key, defValue);
		}
		return value;
	}

	public static Boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public static Boolean getBoolean(String key, boolean defValue) {
		boolean value = false;
		if (BaseApplication.getInstance().getPreferences() != null) {
			value = BaseApplication.getInstance().getPreferences()
					.getBoolean(key, defValue);
		}
		return value;
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int getInt(String key, int defValue) {
		int value = 0;
		if (BaseApplication.getInstance().getPreferences() != null) {
			value = BaseApplication.getInstance().getPreferences()
					.getInt(key, defValue);
		}
		return value;
	}

	public static long getLong(String key) {
		return getLong(key, 0);
	}

	public static long getLong(String key, long defValue) {
		long value = 0;
		if (BaseApplication.getInstance().getPreferences() != null) {
			value = BaseApplication.getInstance().getPreferences()
					.getLong(key, defValue);
		}
		return value;
	}

}

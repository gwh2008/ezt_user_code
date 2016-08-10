package com.zbar.lib;

/**
 * @title zbar调用类
 * @describe
 * @author ezt
 * @created 2014年12月2日
 */
public class ZbarManager {

	static {
		System.loadLibrary("zbar");
	}
	public native String decode(byte[] data, int width, int height,
			boolean isCrop, int x, int y, int cwidth, int cheight);
}

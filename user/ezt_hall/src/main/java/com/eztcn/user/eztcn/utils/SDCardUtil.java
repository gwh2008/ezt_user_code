package com.eztcn.user.eztcn.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * @title SDCard 工具
 * @describe
 * @author ezt
 * @created 2014年11月17日
 */
public class SDCardUtil {

	/**
	 * 判断SD卡是否可用
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 取SD卡路径不带/
	 * 
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		if (isSDCardEnable()) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	/**
	 * 获得目录
	 * 
	 * @param path
	 * @return
	 */
	public static String getDirectory(String path) {
		String dir = getSDPath() + "/" + path;
		String substr = dir.substring(0, 4);
		if (substr.equals("/mnt")) {
			dir = dir.replace("/mnt", "");
		}
		File cacheFile = new File(dir);
		if (!cacheFile.exists()) { // 如果文件不存在
			cacheFile.mkdirs(); // 创建文件
		}

		return dir;
	}

	/**
	 * 计算SDcard 的可用空间，SD卡的剩余空间小于某个值返回false，如果有足够的空间，则返回true
	 * 
	 * @param sizeMb
	 * @return
	 */
	public static boolean isAvaiableSpace(int sizeMb) {
		boolean ishasSpace = false;
		if (isSDCardEnable()) {
			long availableSpare = getAvaiableSpace();
			if (availableSpare > sizeMb) {
				ishasSpace = true;
			}
		}
		return ishasSpace;
	}

	/**
	 * 获取SD卡的可用空间
	 * 
	 * @return
	 */
	public static long getAvaiableSpace() {
		if (isSDCardEnable()) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);
			return availableSpare;
		}
		return -1;
	}


}
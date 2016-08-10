package com.eztcn.user.eztcn.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import xutils.BitmapUtils;
import android.content.Context;
import android.widget.Toast;

import com.eztcn.user.eztcn.bean.SearchKeyword;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.CommonUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * @title 设置页面相关项处理
 * @describe
 * @author ezt
 * @created 2014年12月29日
 */
public class SettingManager {

	/**
	 * 清除缓存
	 */
	public static void clearCache(final Context context, boolean isSetting) {

		/**
		 * 清除全站搜索历史关键字
		 */
		EztDb.getInstance(context).delDataWhere(new SearchKeyword(), null);

		/**
		 * 删除本地EztUser目录下的图片
		 */
		CommonUtil.clearSdCardCache();

		/**
		 * 清除列表图片缓存
		 */
//		FinalBitmap.create(context).clearCache();
		new BitmapUtils(context).clearCache();
		/**
		 * 清除文字缓存
		 */
		CacheUtils.get(context).clear();
		/**
		 * 清除网页图片加载缓存
		 */
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)
				.memoryCacheSize(CommonUtil.getMemoryCacheSize(context))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);
		ImageLoader.getInstance().clearMemoryCache();
		/**
		 * 移除选择医院、科室操作数据
		 */
		SystemPreferences.remove(EZTConfig.KEY_DEPT_ID);
//		SystemPreferences.remove(EZTConfig.KEY_DEPT_WB_ID);

		SystemPreferences.remove(EZTConfig.KEY_STR_DEPT);
//		SystemPreferences.remove(EZTConfig.KEY_STR_WB_DEPT);

		SystemPreferences.remove(EZTConfig.KEY_SELECT_DEPT_POS);
//		SystemPreferences.remove(EZTConfig.KEY_SELECT_WB_DEPT_POS);

		SystemPreferences.remove(EZTConfig.KEY_SELECT_AREA_POS);
//		SystemPreferences.remove(EZTConfig.KEY_SELECT_AREA_WB_POS);

		SystemPreferences.remove(EZTConfig.KEY_SELECT_HOS_POS);
//		SystemPreferences.remove(EZTConfig.KEY_SELECT_HOS_WB_POS);

		SystemPreferences.remove(EZTConfig.KEY_SELECT_N_POS);
//		SystemPreferences.remove(EZTConfig.KEY_SELECT_N_WB_POS);
//		SystemPreferences.remove(EZTConfig.KEY_DF_WB_SELECT_DEPT_POS);

		SystemPreferences.remove(EZTConfig.KEY_STR_CITY);
//		SystemPreferences.remove(EZTConfig.KEY_STR_WB_CITY);

		SystemPreferences.remove(EZTConfig.KEY_CITY_ID);
//		SystemPreferences.remove(EZTConfig.KEY_CITY_WB_ID);

		SystemPreferences.remove(EZTConfig.KEY_STR_N);
//		SystemPreferences.remove(EZTConfig.KEY_STR_WB_N);

		SystemPreferences.remove(EZTConfig.KEY_HOS_ID);
//		SystemPreferences.remove(EZTConfig.KEY_WB_HOS_ID);

		SystemPreferences.remove(EZTConfig.KEY_HOS_NAME);
//		SystemPreferences.remove(EZTConfig.KEY_WB_HOS_NAME);

		SystemPreferences.remove(EZTConfig.KEY_DF_DEPT_ID);
//		SystemPreferences.remove(EZTConfig.KEY_DF_WB_DEPT_ID);

		SystemPreferences.remove(EZTConfig.KEY_DF_STR_DEPT);
//		SystemPreferences.remove(EZTConfig.KEY_DF_WB_STR_DEPT);

		SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT2_POS);
//		SystemPreferences.remove(EZTConfig.KEY_DF_WB_SELECT_DEPT2_POS);

		SystemPreferences.remove(EZTConfig.KEY_DF_SELECT_DEPT_POS);
//		SystemPreferences.remove(EZTConfig.KEY_DF_WB_SELECT_DEPT2_POS);
		SystemPreferences.remove(EZTConfig.KEY_DF_IS_DAY_REG);

		SystemPreferences.remove(EZTConfig.KEY_HAVE_MSG);

		SystemPreferences.remove(EZTConfig.KEY_TOTAL);

		if (isSetting) {
			Toast.makeText(context, "清除成功", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 格式化单位
	 *
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return "0.0MB";
		}

		double megaByte = kiloByte / 1024;
		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 获取缓存大小
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getCacheSize(Context context) throws Exception {
		// 外部File
		File outsideCacheFile = context.getExternalCacheDir();// 外部Cache
		return getFormatSize(getFolderSize(outsideCacheFile));
	}

	/**
	 * 获取文件
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 *             Context.getExternalCacheDir()
	 *             -->SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	 *             Context.getExternalFilesDir() -->
	 *             SDCard/Android/data/你的应用的包名/files/目录，一般放一些长时间保存的数据
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}
	/**
	 * 解析版本信息
	 */
	public static Map<String, Object> parseVersion(String t) {
		Map<String, Object> map = new HashMap<String, Object>();
		SoftVersion version;
		try {
			JSONObject json = new JSONObject(t);
			boolean bool = json.getBoolean("flag");
			map.put("flag", bool);
			map.put("msg", json.getString("detailMsg"));
			if (!bool) {
				return map;
			}
			if (json.isNull("data")) {
				return map;
			}
			JSONObject data = json.getJSONObject("data");
			version = new SoftVersion();
			if (!data.isNull("vuNumber")) {
				version.setVersionName(data.getString("vuNumber"));
			}
			if (!data.isNull("vuSn")) {
				version.setVersionNum(data.getInt("vuSn"));
			}
			if (!data.isNull("vuCreateTime")) {
				version.setTime(data.getString("vuCreateTime"));
			}
			if (!data.isNull("vuDownloadUrl")) {
				version.setUrl(data.getString("vuDownloadUrl"));
			}
			if (!data.isNull("vuContent")) {
				version.setContent(data.getString("vuContent"));
			}
			if (!data.isNull("isMandatory")) {
				version.setForce(data.getInt("isMandatory"));
			}
			map.put("version", version);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
}

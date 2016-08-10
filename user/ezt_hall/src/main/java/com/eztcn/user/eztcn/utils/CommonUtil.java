package com.eztcn.user.eztcn.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;

import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @author ezt
 * @title 常用工具类
 * @describe
 * @created 2014年11月12日
 */
public class CommonUtil {

    public static final String ENCODING = "UTF-8";

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 以byte[]写文件
     *
     * @param bytes
     * @param path
     * @return
     */
    public static void writeFile(byte[] bytes, File file) {

        try {
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(bytes);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到SD卡中
     *
     * @param bitmap
     * @param path
     * @param fileName
     * @return
     */
    public static File saveImgToSdcard(Bitmap bitmap, String path,
                                       String fileName) {
        File destFile = null;
        if (bitmap == null) {
            return null;
        } else {
            destFile = new File(path, fileName);
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                bitmap.compress(CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                destFile = null;
                e.printStackTrace();
            }
        }
        return destFile;
    }

    /**
     * 清除所有缓存(删除文件)
     */
    public static void clearSdCardCache() {

        if (SDCardUtil.isSDCardEnable()) {

            // 清除图片
            String dirPath = SDCardUtil.getDirectory(EZTConfig.RES_SAVE_DIR
                    + File.separator + EZTConfig.DIR_SD_CARD_IMG);

            File dir = new File(dirPath);

            File[] files = dir.listFiles();
            if (null != files)
                for (File file : files) {
                    file.delete();
                }

            // 清除语音

        }
    }

    /**
     * 获得图片的缩略图
     *
     * @param imagePath
     * @return
     */
    public static Bitmap getThumbnail(String imagePath) {
        int maxH = 200;// 最大高度
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap photoBit = BitmapFactory.decodeFile(imagePath, options); // 此时返回bm为空
        // 计算缩放比
        int be = (int) (options.outHeight / (float) maxH);
        int ys = options.outHeight % maxH;// 求余数
        float fe = ys / (float) maxH;
        if (fe >= 0.5)
            be = be + 1;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;

        // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        photoBit = BitmapFactory.decodeFile(imagePath, options);
        // return new BitmapDrawable(context.getResources(), photoBit);
        return photoBit;
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            // camera驱动挂掉,处理
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }

        return canUse;
    }

    /**
     * 获取内存大小
     *
     * @param context
     * @return
     */
    public static int getMemoryCacheSize(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
            // limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }
        return memoryCacheSize;
    }


}
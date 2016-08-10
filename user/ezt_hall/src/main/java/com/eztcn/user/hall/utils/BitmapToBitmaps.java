package com.eztcn.user.hall.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lizhipeng
 * @Data: 16/6/21 下午3:43
 * @Description: 图片垂直方向截取成若干段
 */
public class BitmapToBitmaps {

    /**
     * 设置显示的图片
     * @param context
     * @param resource
     * @param view
     * @param number
     * @return
     */
    public static List<Bitmap> setBitmap(Context context, int resource, View view, int number) {
        int viewWidth = view.getWidth();
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;   //只去读图片的附加信息,不去解析真实的位图
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        int picWidth = opts.outWidth;// 得到图片宽度
        int picHeight = opts.outHeight;// 得到图片高度
        opts.outHeight = picHeight * viewWidth / picWidth;
        opts.outWidth = viewWidth;
        opts.inJustDecodeBounds = false;//真正的去解析位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource, opts);
        //新图片的宽高
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int itemHeight = bitmapHeight / number;
        List<Bitmap> list = new ArrayList<>();
        for (int i = 0; i < number; i++){
            Bitmap bit = Bitmap.createBitmap(bitmap,0,itemHeight * i,bitmapWidth,itemHeight);
            list.add(bit);
        }
        if (!bitmap.isRecycled()){
            bitmap.recycle();
        }
        return list;
    }

    /**
     * 根据宽度进行压缩
     * @param options
     * @param reqWidth
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {

        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {

            final int halfWidth = width / 2;

            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}

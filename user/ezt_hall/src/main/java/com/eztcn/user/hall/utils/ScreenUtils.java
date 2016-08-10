package com.eztcn.user.hall.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ScreenUtils {
    public static DisplayMetrics gainDM(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}

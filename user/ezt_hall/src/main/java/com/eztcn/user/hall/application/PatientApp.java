
package com.eztcn.user.hall.application;

import android.app.Application;

import com.eztcn.user.hall.common.Constant;

import org.xutils.x;


/**
 * @Author: lizhipeng
 * @Data: 16/5/23 下午3:21
 * @Description:
 */

public class PatientApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initXutils();
    }


/**
     * 初始化xutils
     */

    private void initXutils() {
        x.Ext.init(this);
        x.Ext.setDebug(Constant.isDebug); // 开启debug会影响性能
    }
}


package com.eztcn.user.hall.activity.loginSetting;

import android.os.Bundle;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;

/**
 * Created by zll on 2016/6/6.
 * 关于医指通界面
 */
public class AboutEZTActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true,"关于我们",null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_about_ezt;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}

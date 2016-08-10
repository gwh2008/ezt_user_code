package com.eztcn.user.hall.activity;

import android.os.Bundle;

import com.eztcn.user.R;

/**
 * Created by zll on 2016/6/3.
 * 消息详情界面
 */
public class MessageDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true,"消息详情",null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_message_details;
    }
/**
 * 初始化view控件

 */
    @Override
    protected void initView() {

    }

    /**
     * 初始化data
     */
    @Override
    protected void initData() {

    }
}

package com.eztcn.user.hall.activity;

import com.eztcn.user.R;

/**
 * Created by lx on 2016/6/3.
 * 我的药品详情界面
 */
public class MyDrugDetailsActivity extends  BaseActivity {

    @Override
    protected int preView() {
        return R.layout.new_activity_my_drug_details;
    }

    @Override
    protected void initView() {
        loadTitleBar(true, "预约详情", null);

    }

    @Override
    protected void initData() {

    }
}

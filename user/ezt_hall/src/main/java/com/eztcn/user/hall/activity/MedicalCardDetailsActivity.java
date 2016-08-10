package com.eztcn.user.hall.activity;

import com.eztcn.user.R;

/**
 * Created by lx on 2016/7/22.
 * 医疗卡详情界面。
 */
public class MedicalCardDetailsActivity extends  BaseActivity {

    @Override
    protected int preView() {
        return R.layout.new_activity_medicalcard_details;
    }

    @Override
    protected void initView() {

        loadTitleBar(true, "龙卡服务介绍", null);
    }

    @Override
    protected void initData() {

    }
}

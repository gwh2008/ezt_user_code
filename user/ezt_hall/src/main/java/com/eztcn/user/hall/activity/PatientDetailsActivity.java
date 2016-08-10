package com.eztcn.user.hall.activity;

import com.eztcn.user.R;

/**
 * Created by lx on 2016/6/2.
 * 就诊人详情界面。
 */
public class PatientDetailsActivity extends BaseActivity {

    @Override
    protected int preView() {
        return R.layout.new_activity_patient_details;
    }

    @Override
    protected void initView() {
        loadTitleBar(true,"就诊人详情",null);

    }

    @Override
    protected void initData() {

    }
}

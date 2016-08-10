package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.OrderRegistrationActivity;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;


/**
 * Created by zll on 2016/6/1.
 * 这个是复印病历预约结果页面
 */
public class CopyCaseSubscribeResultActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_order_registered, ll_order_check, ll_big_famous_doctor, ll_order_bed, ll_order_drug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "预约结果", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_copy_case_subscribe_result;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        ll_order_registered = (LinearLayout) findViewById(R.id.ll_order_registered);
        ll_order_check = (LinearLayout) findViewById(R.id.ll_order_check);
        ll_big_famous_doctor = (LinearLayout) findViewById(R.id.ll_big_famous_doctor);
        ll_order_bed = (LinearLayout) findViewById(R.id.ll_order_bed);
        ll_order_drug = (LinearLayout) findViewById(R.id.ll_order_drug);
        ll_order_registered.setOnClickListener(this);
        ll_order_check.setOnClickListener(this);
        ll_big_famous_doctor.setOnClickListener(this);
        ll_order_bed.setOnClickListener(this);
        ll_order_drug.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_order_registered:
                // intent.setClass(mContext, OrderRegistrationActivity.class);
                break;
            case R.id.ll_order_check:
                // intent.setClass(mContext,OrderCheckActivity.class);
                break;
            case R.id.ll_big_famous_doctor:
                intent.setClass(mContext, BigFamousDoctorActivity.class);
                break;
            case R.id.ll_order_bed:
                // intent.setClass(mContext, OrderBedActivity.class);
                break;
            case R.id.ll_order_drug:
                intent.setClass(mContext, OrderDrugActivity.class);
                break;
        }

    }
}

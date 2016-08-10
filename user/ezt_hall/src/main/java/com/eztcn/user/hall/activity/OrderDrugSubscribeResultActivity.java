package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.BigDoctorList30Activity;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;

/**
 * Created by zll on 2016/5/31.
 * 预约药品的預約結果
 */
public class OrderDrugSubscribeResultActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_order_registered;//预约挂号
    private LinearLayout ll_order_check; //预约检查
    private LinearLayout ll_copy_case; //复印病历
    private LinearLayout ll_big_famous_doctor;//大牌名医
    private LinearLayout ll_order_bed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "预约结果", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_order_drug_subscribe_result;
    }

    @Override
    protected void initView() {
        ll_order_registered = (LinearLayout) findViewById(R.id.ll_order_registered);
        ll_order_check = (LinearLayout) findViewById(R.id.ll_order_check);
        ll_big_famous_doctor = (LinearLayout) findViewById(R.id.ll_big_famous_doctor);
        ll_copy_case= (LinearLayout) findViewById(R.id.ll_copy_case);
        ll_order_bed= (LinearLayout) findViewById(R.id.ll_order_bed);
        ll_order_registered.setOnClickListener(this);
        ll_order_check.setOnClickListener(this);
        ll_big_famous_doctor.setOnClickListener(this);
        ll_copy_case.setOnClickListener(this);
        //ll_order_bed.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_order_registered:
                //intent.setClass(mContext, OrderRegisteredActivity.class);
                break;
            case R.id.ll_order_check:
                // intent.setClass(mContext,OrderCheckActivity.class);
                break;
            case R.id.ll_big_famous_doctor:
                intent.setClass(mContext, BigFamousDoctorActivity.class);
                break;
            case R.id.ll_copy_case:
                intent.setClass(mContext, CopyCaseOrderActivity.class);
                break;
            case R.id.ll_order_bed:
                //intent.setClass(mContext,OrderBedActivity.class);
                break;
        }
        startActivity(intent);
    }
}

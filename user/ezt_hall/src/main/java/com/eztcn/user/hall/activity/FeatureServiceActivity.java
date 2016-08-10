package com.eztcn.user.hall.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;

/**
 * Created by zll on 2016/5/31.
 * 特色服务
 */
public class FeatureServiceActivity extends BaseActivity implements View.OnClickListener {

    private Context context=FeatureServiceActivity.this;
    private RelativeLayout rl_order_drug;
   // private RelativeLayout rl_order_cases;
    private RelativeLayout rl_order_bed;
    private  RelativeLayout rl_order_check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int preView() {

        return R.layout.new_activity_feature_service;
    }
    /**
     * 初始化view控件
     */
    @Override
    protected void initView() {
        loadTitleBar(true, "特色服务", null);
        rl_order_drug = (RelativeLayout) findViewById(R.id.rl_order_drug);
      //  rl_order_cases = (RelativeLayout) findViewById(R.id.rl_order_cases);
        rl_order_bed= (RelativeLayout) findViewById(R.id.rl_order_bed);
        rl_order_check= (RelativeLayout) findViewById(R.id.rl_order_check);
        setOnClickListener();
    }

    private void setOnClickListener() {
        rl_order_drug.setOnClickListener(this);
       // rl_order_cases.setOnClickListener(this);
        rl_order_bed.setOnClickListener(this);
        rl_order_check.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_order_drug:
                intent.setClass(context, OrderDrugActivity.class);
                break;
           /* case R.id.rl_order_cases:
                intent.setClass(context,OrderCopyCaseActivity.class);
                break;*/

            case R.id.rl_order_bed:

                intent.setClass(context,OrderBedNoticeActivity.class);
                break;
               case R.id.rl_order_check:

                   intent.setClass(context,ChoiceHosActivity.class);
                   intent.putExtra("isChangedCity", false);
                   intent.putExtra("isOrderCheck", true);
                  break;
        }
        startActivity(intent);
    }
}

package com.eztcn.user.hall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by zll on 2016/5/31.
 * 预约药品订单
 */
public class OrderDrugGoodsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_health_type;
    private RelativeLayout rl_order_time;
    private TextView orderTimeTv;
    private TextView costTypeTv;
    private AlertDialog.Builder reportDialog;
    private AlertDialog.Builder builder;
    private int payWay = 0;// 缴费方式 0：自费 1：普通医保2：门特医保3：门大医保
    private int time = 0;
    private Button btn_order_commit;


    private Context context = OrderDrugGoodsActivity.this;
    // 自定义的弹出框类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "预约药品", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_order_drug_goods;
    }

    @Override
    protected void initView() {
        rl_health_type = (RelativeLayout) findViewById(R.id.rl_health_type);
        rl_order_time = (RelativeLayout) findViewById(R.id.rl_order_time);
        orderTimeTv = (TextView) findViewById(R.id.orderTimeTv);
        costTypeTv = (TextView) findViewById(R.id.costTypeTv);
        btn_order_commit = (Button) findViewById(R.id.btn_order_commit);
        rl_health_type.setOnClickListener(this);
        rl_order_time.setOnClickListener(this);
        btn_order_commit.setOnClickListener(this);

    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_health_type:
                showSelectDialog();
                break;
            case R.id.rl_order_time:
                setWheelData();
                break;
            case R.id.btn_order_commit:
                intent.setClass(mContext, OrderDrugSubscribeResultActivity.class);
                break;
        }
        startActivity(intent);
    }


    /**
     * 预约药品中预约时间的弹窗
     */

    private void setWheelData() {


    }


    /**
     * 选择医保类型的弹窗
     */
    private void showSelectDialog() {
        final String[] payName = {"自费", "医保", "取消"};
        builder = new AlertDialog.Builder(this);
        builder.setItems(payName, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                payWay = which;
                costTypeTv.setText(payName[which]);
            }
        });
        builder.create().show();
    }


}

package com.eztcn.user.hall.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.utils.ToastUtils;

/**
 * Created by zll on 2016/6/1.]
 * 复印病历预约单
 */
public class CopyCaseOrderActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_choose_time;
    private Button btn_commit;
    private TextView show_date;


    private Context context = CopyCaseOrderActivity.this;
    private AlertDialog.Builder builder;
    private int time = 0;// 时间段 0：上午 1：下午

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "复印病历预约单", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_copy_case_order;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        ll_choose_time = (LinearLayout) findViewById(R.id.ll_choose_time);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        show_date = (TextView) findViewById(R.id.show_date);
        setOnClickListener();

    }

    private void setOnClickListener() {
        ll_choose_time.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        show_date.setOnClickListener(this);

    }


    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.show_date:
                showSelectDialog();
                break;
            case R.id.btn_commit:
                intent.setClass(CopyCaseOrderActivity.this, CopyCaseSubscribeResultActivity.class);
                startActivity(intent);
                break;
        }


    }

    /**
     * 选择时间段的弹窗
     */
    private void showSelectDialog() {
        final String[] times = {"上午", "下午", "取消"};
        builder = new AlertDialog.Builder(this);

        builder.setItems(times, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                time = which;
                show_date.setText(times[which]);
            }
        });

        builder.create().show();
    }


}

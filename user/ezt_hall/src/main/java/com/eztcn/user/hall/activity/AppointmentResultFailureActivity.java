package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.utils.ToastUtils;


/**
 * 预约失败页面
 *
 * @author 蒙
 */
public class AppointmentResultFailureActivity extends BaseActivity implements View.OnClickListener{

    private TextView right_top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_appointment_result_failure;
    }

    public void initView() {
        right_top=loadTitleBar(true, "预约结果", null);
    }
    @Override
    protected void initData() {
        TextView error_msg=(TextView)findViewById(R.id.error_msg);
        error_msg.setText(getIntent().getStringExtra("msg"));
    }

    @Override
    protected void onBack() {
        super.onBack();
        startActivity(new Intent(mContext,SelectAppointmentTimeActivity.class));
    }

    @Override
    public void onClick(View v) {
    }
}

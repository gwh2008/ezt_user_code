package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.model.ResultResponse.HospitalDataResponse;


/**
 * 医院首页面
 *
 * @author 蒙
 */
public class HospitalSimpleIntroActivity extends BaseActivity implements View.OnClickListener{

    private HospitalDataResponse dataResponse;
    private TextView phoneNum;
    private TextView address;
    private TextView intro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_hospital_simple_intro;
    }

    public void initView() {
        phoneNum=(TextView)findViewById(R.id.phone_num);
        address=(TextView)findViewById(R.id.address);
        intro=(TextView)findViewById(R.id.intro);
    }
    @Override
    protected void initData() {
        dataResponse=(HospitalDataResponse) getIntent().getSerializableExtra("hospital");
        loadTitleBar(true,dataResponse.getEhName(),null);
        phoneNum.setText(dataResponse.getEhTel());
        address.setText(dataResponse.getEhAddress());
        intro.setText(dataResponse.getRemark());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_activity_appointment_main_select:
                break;
        }
    }

}

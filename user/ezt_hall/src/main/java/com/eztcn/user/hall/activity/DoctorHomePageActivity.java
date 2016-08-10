package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.utils.ToastUtils;


/**
 * 医生首页面
 *
 * @author 蒙
 */
public class DoctorHomePageActivity extends BaseActivity implements View.OnClickListener{
    private TextView good_at_content;
    private boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true,"医生主页",null);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_doctorhomepage;
    }

    public void initView() {

        good_at_content=(TextView) findViewById(R.id.new_activity_doctor_home_good_at_content);
        findViewById(R.id.new_activity_doctor_home_open_text).setOnClickListener(this);
        findViewById(R.id.new_activity_doctor_home_rank).setOnClickListener(this);
    }
    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_activity_doctor_home_open_text:
                if (isOpen){
                    good_at_content.setMaxLines(2);
                    isOpen=false;
                }else{
                    good_at_content.setMaxLines(5000);
                    isOpen=true;
                }
                break;
            case R.id.new_activity_doctor_home_rank:
                ToastUtils.shortToast(mContext,"排行页面");
                break;
        }
    }

}

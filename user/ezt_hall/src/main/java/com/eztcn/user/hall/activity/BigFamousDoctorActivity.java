package com.eztcn.user.hall.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.eztcn.user.R;

/**
 * Created by lx on 2016/6/1.
 * 大牌名医列表界面。
 */
public class BigFamousDoctorActivity extends BaseActivity {

    private Context context=BigFamousDoctorActivity.this;
   private RecyclerView big_famous_doctor_recycleView;
    @Override
    protected int preView() {
        return R.layout.new_activity_big_famous_doctor;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}

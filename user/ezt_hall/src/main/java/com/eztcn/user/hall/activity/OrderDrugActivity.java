package com.eztcn.user.hall.activity;
import android.os.Bundle;
import com.eztcn.user.R;


/**
 * Created by zll on 2016/5/31.
 * 预约药品
 */
public class OrderDrugActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "天津", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_order_drug;
    }

    /**
     * 初始化view控件
     */
    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}

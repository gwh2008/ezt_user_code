package com.eztcn.user.hall.activity.dragonCard;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;

/**
 * Created by lx on 2016/6/4.
 * 我的龙卡服务详情界面。
 */
public class DragonCardServiceDetailsActivity extends BaseActivity{

    @Override
    protected int preView() {
        return R.layout.new_activity_dragon_service_details;
    }

    @Override
    protected void initView() {
        loadTitleBar(true,"服务详情",null);

    }

    @Override
    protected void initData() {

    }
}

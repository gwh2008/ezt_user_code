package com.eztcn.user.hall.activity.loginSetting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.mine.PWDModifyActivity;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.utils.Constant;

/**
 * Created by lx on 2016/6/12.
 * 账号管理界面。
 */
public class AccountManageActivity  extends BaseActivity{

    private Context context=AccountManageActivity.this;
    private LinearLayout modify_tel_ll,modify_password_ll;

    @Override
    protected int preView() {
        return R.layout.new_activity_account_manage;
    }

    @Override
    protected void initView() {
        loadTitleBar(true,"账号管理","");
        modify_tel_ll= (LinearLayout) this.findViewById(R.id.modify_tel_ll);
        modify_password_ll= (LinearLayout) this.findViewById(R.id.modify_password_ll);
        setOnClickListener();
    }

    /**
     * 点击事件。
     */
    private void setOnClickListener() {
        modify_tel_ll.setOnClickListener(modify_tel_llOnClickListener);
        modify_password_ll.setOnClickListener(modify_password_llOnClickListener);
    }

    @Override
    protected void initData() {

    }

    /**
     * 修改手机号
     */
    View.OnClickListener modify_tel_llOnClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            if(BaseApplication.patient==null){
                hintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            startActivity(new Intent(context,ChangeMobileActivity.class));
        }
    };
    /**
     * 修改密码。
     */

    View.OnClickListener  modify_password_llOnClickListener=new View.OnClickListener(){


        @Override
        public void onClick(View v) {

            if(BaseApplication.patient==null){
                hintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            startActivity(new Intent(context,PWDModifyActivity.class));
        }
    };

}

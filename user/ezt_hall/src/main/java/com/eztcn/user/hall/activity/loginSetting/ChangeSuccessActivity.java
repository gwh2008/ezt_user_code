package com.eztcn.user.hall.activity.loginSetting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by zll on 2016/6/4.
 * 更改成功页面
 */
public class ChangeSuccessActivity extends BaseActivity {
    private TextView time;
    private int recLen = 3;//总的秒数
    Timer timer = new Timer();
    private Context context=ChangeSuccessActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "更改成功", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_change_success;
    }

    @Override
    protected void initView() {
        time = (TextView) findViewById(R.id.time);
        timer.schedule(timertask, 1000, 1000);

    }
    TimerTask timertask = new TimerTask() {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    time.setText("" + recLen + "S");
                    if (recLen == 0) {
                        timer.cancel();
                        time.setVisibility(View.GONE);
                        try{
                            Tools.exitApp(context);
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finish();
                            //清除账户信息。
                        }catch (Exception e){
                            e.printStackTrace();
                            ToastUtils.shortToast(context,e.toString());
                        }
                    }
                }
            });
        }

    };
    @Override
    protected void initData() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}

package com.eztcn.user.hall.activity.loginSetting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.activity.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lx on 2016/6/29.
 * 注册成功的结果界面。
 */
public class RegisterResultActivity extends BaseActivity {

    private Context context=RegisterResultActivity.this;
    private Button complete_user_info_bt;
    private TextView time;
    private int recLen = 3;//总的秒数
    private Timer timer=null;

    @Override
    protected int preView() {
        return R.layout.new_register_result_success;
    }

    @Override
    protected void initView() {
        loadTitleBar(true, "注册结果",null);
        complete_user_info_bt= (Button) this.findViewById(R.id.complete_user_info_bt);
        time = (TextView) findViewById(R.id.time);
        complete_user_info_bt.setOnClickListener(complete_user_info_btOnClickListener);
        timer = new Timer();
    }

    @Override
    protected void initData() {
        timer.schedule(timertask, 1000, 1000);
    }
    /**
     * 完善资料资料按钮点击事件。
     */
    View.OnClickListener complete_user_info_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent complete_intent=new Intent(context,CompletePersonalInfoActivity.class);
            complete_intent.putExtra("register",1);//标志是注册传递过来的。
            startActivity(complete_intent);
            RegisterResultActivity.this.finish();
        }
    };

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
                        Intent complete_intent=new Intent(context,MainActivity.class);
                        complete_intent.putExtra("register",1);//标志是注册传递过来的。
                        startActivity(complete_intent);
                        RegisterResultActivity.this.finish();
                    }
                }
            });

        }

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
    }
}

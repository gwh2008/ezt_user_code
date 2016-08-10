package com.eztcn.user.hall.activity.loginSetting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.mine.UserLoginActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.customView.CustomProgressToast;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.utils.Constant;

import java.util.Map;

import xutils.http.RequestParams;
/**
 * Created by lx on 2016/6/4.
 * 意见反馈界面。
 */
public class FeedbackActivity extends BaseActivity implements IHttpResult {

    private Context context=FeedbackActivity.this;
    private TextView submit;
    private EditText content;
    @Override
    protected int preView() {
        return R.layout.new_activity_feedback;
    }

    @Override
    protected void initView() {
        submit= loadTitleBar(true,"意见反馈","提交");
        content= (EditText) this.findViewById(R.id.content);
        setOnClickListener();
    }

    /**
     * 点击事件。
     */
    private void setOnClickListener() {
        submit.setOnClickListener(submitClickListener);
    }

    @Override
    protected void initData() {

    }

    /**
     * 提交事件。
     */
    View.OnClickListener submitClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (content.getText().toString() == null
                    || content.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "请输入您的建议!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            sendSuggestion();
        }
    };

    /**
     * 提交反馈意见数据。
     */
    private void sendSuggestion() {
        if (BaseApplication.patient == null) {
            HintToLogin(Constant.LOGIN_COMPLETE);
            return;
        }
        RequestParams params=new RequestParams();
        params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
        params.addBodyParameter("efContent", content.getText().toString());
        new UserImpl().sendSuggestion(params, this);
        showProgressDialog("数据加载");
    }


    @Override
    public void result(Object... object) {
        dismissProgressDialog();

        String msg;
        if (object == null) {
            return;
        }
        Object[] obj = object;
        Integer taskID = (Integer) obj[0];
        if (taskID == null) {
            return;
        }
        boolean status = (Boolean) obj[1];
        if (!status) {
            Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
            return;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) obj[2];
        if (map == null || map.size() == 0) {
            return;
        }
        boolean flag = (Boolean) map.get("flag");
        if (flag) {
            msg = "您的意见已发送";
            finish();
        } else {
            msg = map.get("msg").toString();
        }
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }
    /**
     * 登录提醒
     */
    public void HintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("提示")
                .setMessage("你还未登录！")
                .setCancelable(false)
                .setNegativeButton("去登录",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                startActivityForResult(new Intent(context,
                                        LoginActivity.class), requestCode);
                            }
                        }).setPositiveButton("取消", null);

        AlertDialog dialog = builder.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });
        dialog.show();
    }


}

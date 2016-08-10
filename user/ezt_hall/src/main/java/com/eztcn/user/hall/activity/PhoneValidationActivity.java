package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.CommitAppointmentDataRequest;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 验收手机页面
 *
 * @author 蒙
 */
public class PhoneValidationActivity extends BaseActivity implements View.OnClickListener,
        ResultSubscriber.OnResultListener {

    private Button commit;
    private String phoneNum;
    private TextView phoneNum_TextView, time_TextView, getCode_TextView;
    private LinearLayout getCode_LinearLayout;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "验证手机", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_phone_validation;
    }

    public void initView() {
        commit = (Button) findViewById(R.id.new_activity_phone_validate_commit);
        commit.setOnClickListener(this);
        phoneNum_TextView = (TextView) findViewById(R.id.new_activity_phone_validate_phoneNum);
        time_TextView = (TextView) findViewById(R.id.new_activity_phone_validate_time);
        getCode_TextView = (TextView) findViewById(R.id.new_activity_phone_validate_getCode);
        getCode_LinearLayout = (LinearLayout) findViewById(R.id.linearLayout_getCode);
        getCode_LinearLayout.setOnClickListener(this);
        getCode_LinearLayout.setEnabled(false);
        editText = (EditText) findViewById(R.id.new_activity_phone_validate_editText);
    }

    private CommitAppointmentDataRequest dataRequest;
    @Override
    protected void initData() {
        dataRequest=(CommitAppointmentDataRequest) getIntent().getSerializableExtra("data");
        phoneNum = FileUtils.getStringBySp(mContext, "account");
        phoneNum_TextView.setText("已经向" + phoneNum.substring(0, 3) + "****" + phoneNum.substring(8) + "发送了短信验证码");
        sendCode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_activity_phone_validate_commit:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtils.shortToast(mContext, "请输入验证码");
                    return;
                }
                dataRequest.setCode(editText.getText().toString());
                startActivity(new Intent(mContext, AppointmentIngActivity.class)
                        .putExtra("data",dataRequest));
                break;
            case R.id.linearLayout_getCode:
                sendCode();
                break;
        }
    }

    /**
     * 发送手机验证码
     */
    public void sendCode() {
        showProgressDialog("正在发送验证码...");
        getCode_LinearLayout.setEnabled(false);
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("phone", phoneNum);//手机号

        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).sendCode(request.getFormMap(params), 111, this);
    }



    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//已经发送验证码
                time_TextView.setVisibility(View.VISIBLE);
                time_TextView.setText("60");
                getCode_TextView.setText("秒后重新发送");
                getCode_TextView.setTextColor(getResources().getColor(R.color.text_color_gray));
                time = 60;
                handler.postDelayed(runnable, 1000);
            }
        } else {
            ToastUtils.shortToast(mContext, response.getDetailMsg());
            if (requestType == 111) {//发送验证码
                getCode_LinearLayout.setEnabled(true);
            }
        }
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        if (requestType == 111) {//已经发送验证码
            getCode_LinearLayout.setEnabled(true);
        }
    }


    private int time = 60;
    private Handler handler = new Handler();
    /**
     * 倒计时
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            time_TextView.setText(time + "");
            if (time > 0) {
                handler.postDelayed(this, 1000);
            } else if (time == 0) {
                getCode_TextView.setText("重新发送");
                getCode_TextView.setTextColor(getResources().getColor(R.color.border_line));
                getCode_LinearLayout.setEnabled(true);
                time_TextView.setVisibility(View.GONE);
            }
        }
    };
}

package com.eztcn.user.hall.activity.loginSetting;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.impl.ChAcImpl;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import xutils.http.RequestParams;
/**
 * Created by LX on 2016/6/6.
 * 设置手机号界面
 */
public class SetNewMobileActivity extends BaseActivity implements  ResultSubscriber.OnResultListener {

    private Context context=SetNewMobileActivity.this;
    private  String TAG="SetNewMobileActivity";
    private Button     confirm_modify;
    private EditText   ed_new_account, verify_code_ed, pwd_ed, confirm_password_ed;
    private ImageView  delete_account;
    private TextView   getCode_tx;
    private final int GET_VERIFY_CODE=4;
    private  final  int CONFIRM_MODIFY=5;
    private  final  int IS_REGISTER=6;
    private  CountDownTimer timer=null;
    private  Subscriber isRegisterSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "设置手机号", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_set_new_mobile;
    }

    @Override
    protected void initView() {
        confirm_modify = (Button) findViewById(R.id.confirm_modify);
        ed_new_account = (EditText) findViewById(R.id.ed_new_account);
        verify_code_ed = (EditText) findViewById(R.id.verify_code_ed);
        pwd_ed = (EditText) findViewById(R.id.pwd_ed);
        confirm_password_ed = (EditText) findViewById(R.id.confirm_password_ed);
        getCode_tx = (TextView) findViewById(R.id.getCode_tx);
        getCode_tx.setOnClickListener(getCode_txOnClickListener);
        confirm_modify = (Button) findViewById(R.id.confirm_modify);
        delete_account = (ImageView) findViewById(R.id.delete_account);
        delete_account.setOnClickListener(delete_accountOnClickListener);
        confirm_modify.setOnClickListener(confirm_modifyOnClickListener);
    }
    @Override
    protected void initData() {

    }
    /**
     * 、清除账号
     * @param v
     */
    View.OnClickListener delete_accountOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ed_new_account.setText("");
        }
    };
    /**
     * 获取验证码。
     */
    View.OnClickListener getCode_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isRegister();
        }
    };

    /**
     * 判断手机号码是否注册、
     */
    private void isRegister() {

        if (TextUtils.isEmpty(ed_new_account.getText().toString())) {
            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtil.isPhone(ed_new_account.getText().toString())) {
            Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        //先验证手机号码是否注册然后，在看 验证码是否发送。
        Map<String,String> params=new HashMap<>();
        params.put("mobile",ed_new_account.getText().toString().trim());
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        isRegisterSubscriber= helper.isRegister(params,IS_REGISTER,this);
    }

    /**
     * 确认修改。
     */
    View.OnClickListener confirm_modifyOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(judgeParams()){
                confirmModify();
            }
        }
    };
    /**
     * 调取更改接口。
     */
    private void confirmModify() {

        Map<String,String> params=new HashMap<>();
        params.put("userId", BaseApplication.patient.getUserId()+"");
        params.put("checkCode",verify_code_ed.getText().toString().trim());
        params.put("mobileNum",ed_new_account.getText().toString().trim());
        params.put("paasword", MD5.getMD5ofStr(pwd_ed.getText().toString()));
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.setNewMobile(map,CONFIRM_MODIFY,this);
        showProgressDialog("请求中");

    }
    /**
     * 获取验证码。
     */
    private void getSecurityCode() {

        Map<String,String> params=new HashMap<>();
        params.put("mobile",ed_new_account.getText().toString().trim());
        params.put("eztCode", MD5.getMD5ofStr("ezt" + ed_new_account.getText().toString()));
        params.put("hosId", "0");
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.getChangeMobileVerifyCode(map,GET_VERIFY_CODE,this);
    }
    /**
     * 修改手机号
     */
    public boolean  judgeParams() {

        if (TextUtils.isEmpty(ed_new_account.getText().toString())) {
            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!StringUtil.isPhone(ed_new_account.getText().toString())) {
            Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(verify_code_ed.getText().toString())) {
            Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (verify_code_ed.getText().toString().length() != 6) {
            Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pwd_ed.getText().toString())) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pwd_ed.getText().toString().trim().length() < 6) {
            Toast.makeText(mContext, "请输入6~20位的密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(confirm_password_ed.getText().toString().trim() )){
            ToastUtils.shortToast(context,"请输入确认密码");
            return false;
        }

        if(!confirm_password_ed.getText().toString().trim().equals(pwd_ed.getText().toString().trim())){
            ToastUtils.shortToast(context,"两次输入密码不一样");
            return false;
        }
        return  true;
    }
    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {
        Log.i(TAG,"onCompleted");
    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        Log.i(TAG,"onError");
        ToastUtils.shortToast(context,"服务器错误");
    }

    @Override
    public void onNext(IModel t, int requestType) {

        Log.i(TAG,"onNext");

        dismissProgressDialog();

        Response response= (Response) t;

        int number=Integer.valueOf(response.getNumber());

        switch (requestType){

            case  GET_VERIFY_CODE:
                // TODO: 2016/6/25

                if(number==2000){
                    ToastUtils.shortToast(context,"验证码已经发送...");
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
            break;

            case CONFIRM_MODIFY:

                if(number==2002){
                    ToastUtils.shortToast(context,"新的手机号修改成功...");
                    startActivity(new Intent(context,ChangeSuccessActivity.class));
                    SetNewMobileActivity.this.finish();
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
            break;

            case  IS_REGISTER:

                if(number==2000){

                    ToastUtils.shortToast(context,"手机号码已经注册");

                }else if(number==4003){
                    //手机号码不存在。
                    timer = new CountDownTimer(60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    getCode_tx.setClickable(false);
                    getCode_tx.setText(millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    getCode_tx.setClickable(true);
                    getCode_tx.setText("获取验证码");
                }
            };
            timer.start();
            getSecurityCode();
                    return;
                }else if(number==5004){
                    //参数不对。
                    ToastUtils.shortToast(context,"传递参数不对");
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
            break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
        }
        if(isRegisterSubscriber!=null){
            if(isRegisterSubscriber.isUnsubscribed()){
           isRegisterSubscriber.unsubscribe();
            }
        }
    }
}


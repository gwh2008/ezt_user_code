package com.eztcn.user.hall.activity.loginSetting;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.text.Editable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by LX on 2016/6/6.
 * 找回密码界面
 */
public class FindBackPasswordActivity extends BaseActivity  implements  ResultSubscriber.OnResultListener {

    private Context  context=FindBackPasswordActivity.this;
    private String   TAG="FindBackPasswordActivity";
    private EditText ed_phone, verify_code_ed,new_pwd_ed,confirm_password_ed;
    private TextView getCode_tx;
    private Button   confirm_bt;
    private final int GET_VERIFY_CODE=4;
    private final int CONFIRM_VERIFY_CODE=5;
    private final int CONFIRM_MODIFY=6;
    private  CountDownTimer timer=null;
    private String userId="";
    private  final  int IS_REGISTER=7;
    private  Subscriber isRegisterSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "找回密码",null);
    }
    @Override
    protected int preView() {
        return R.layout.new_activity_find_back_password;
    }

    @Override
    protected void initView() {
                ed_phone= (EditText) this.findViewById(R.id.ed_phone);
                verify_code_ed= (EditText) this.findViewById(R.id.verify_code_ed);
                new_pwd_ed= (EditText) this.findViewById(R.id.new_pwd_ed);
                confirm_password_ed= (EditText) this.findViewById(R.id.confirm_password_ed);
                getCode_tx= (TextView) this.findViewById(R.id.getCode_tx);
                confirm_bt= (Button) this.findViewById(R.id.confirm_bt);
                getCode_tx.setOnClickListener(getCode_txOnClickListener);
                confirm_bt.setOnClickListener(confirm_btOnClickListener);
    }
    @Override
    protected void initData() {
        if(BaseApplication.patient!=null){
            ed_phone.setText(BaseApplication.patient.getEpMobile());
            ed_phone.setFocusable(false);
        }
    }
    /**
     * 获取验证码
     */
    View.OnClickListener getCode_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isRegister();
        }
    };

    /**
     * 调取接口看 当前的手机号码是否被注册。
     */
    private void isRegister() {

        String tel=ed_phone.getText().toString();
        if (TextUtils.isEmpty(tel)){
            ToastUtils.shortToast(context,"手机号码不能为空");
          return;
        }
        if(tel.length()!=11){
            ToastUtils.shortToast(context,"手机号码不正确");
            return;
        }
          //先验证手机号码是否注册然后，在看 验证码是否发送。
            Map<String,String> params=new HashMap<>();
            params.put("mobile",ed_phone.getText().toString().trim());
            HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
            isRegisterSubscriber= helper.isRegister(params,IS_REGISTER,this);
    }
    /**
     * 获取验证码。
     */
    private void getVerifyCode() {

        Map<String,String> params=new HashMap<>();
        params.put("mobile",ed_phone.getText().toString().trim());
        params.put("eztCode", MD5.getMD5ofStr("ezt" + ed_phone.getText().toString()));
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.getForgetPasswordVerifyCode(map,GET_VERIFY_CODE,this);
    }
    /**
     * 确认修改
     */
    View.OnClickListener confirm_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(judgeParams()){
                verifyCode();
            }
        }
    };
    /**
     * 确认修改的接口调取。
     */
    private void confirmModify() {

        String password=MD5.getMD5ofStr(new_pwd_ed.getText().toString().trim());
        Map<String,String> params=new HashMap<>();
        params.put("userid", userId+"");
        params.put("checkCode", verify_code_ed.getText().toString().trim());
        params.put("checkType","mobile");
        params.put("password", password);
        params.put("confirmPassword",password);
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.confirmModifyPassword(map,CONFIRM_MODIFY,this);
    }

    /**
     * 验证验证码。
     */
    private void verifyCode() {

        Map<String,String> params=new HashMap<>();
        params.put("userId", userId+"");
        params.put("checkCode",verify_code_ed.getText().toString().trim());
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.VerifyForgetPasswordVerifyCode(map,CONFIRM_VERIFY_CODE,this);

    }
    /**
     * 确认修改
     */
    private boolean judgeParams() {

        if (TextUtils.isEmpty(verify_code_ed.getText().toString())) {
            Toast.makeText(mContext, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (verify_code_ed.getText().toString().length() != 6) {
            Toast.makeText(mContext, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(new_pwd_ed.getText().toString())) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (new_pwd_ed.getText().toString().trim().length() < 6) {
            Toast.makeText(mContext, "请输入6~20位的密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(confirm_password_ed.getText().toString().trim() )){
            ToastUtils.shortToast(context,"请输入确认密码");
            return false;
        }
        if(!new_pwd_ed.getText().toString().trim().equals(confirm_password_ed.getText().toString().trim())){
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

    }

    @Override
    public void onError(int requestType) {

        ToastUtils.shortToast(context,"服务器错误");

    }

    @Override
    public void onNext(IModel t, int requestType) {

        dismissProgressDialog();

        Response response= (Response) t;
        int number=Integer.valueOf(response.getNumber());
        switch (requestType){

            case  GET_VERIFY_CODE:
                if(number==2000){
                    ToastUtils.shortToast(context,"验证码已经发送");
                    double id= (double) response.getData();
                    Double dID=new Double(id);
                    Integer  user_id=dID.intValue();
                    userId=user_id+"";
                }
            break;
            case CONFIRM_VERIFY_CODE:

                if(number==2000){
                    confirmModify();
                }
            break;
            case CONFIRM_MODIFY:

                if(number==2002){
                    ToastUtils.shortToast(context,"密码修改成功...");
                    startActivity(new Intent(context,ChangeSuccessActivity.class));
                    FindBackPasswordActivity.this.finish();
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
            break;
            case  IS_REGISTER:

                if(number==2000){
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
                    getVerifyCode();
                }else if(number==4003){
                    //手机号码不存在。
                    ToastUtils.shortToast(context,"手机号码不存在,请注册");
                    return;
                }else if(number==5004){
                    //参数不对。
                    ToastUtils.shortToast(context,"传递参数不对...");
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

package com.eztcn.user.hall.activity.loginSetting;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.DataResponse;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.TabEntity;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;
import xutils.http.RequestParams;
/**
 * Created by lx on 2016/6/6.
 * 注册界面。
 */
public class RegisterActivity extends BaseActivity implements IHttpResult ,ResultSubscriber.OnResultListener {

    private Context context = RegisterActivity.this;
    private ImageView account_clear_image;
    private CheckBox accept_agreement;
    private LinearLayout register_back;
    private TextView get_verify_code;
    private TextView login_tx;
    private TextView accept_user_agreement_tx;
    private EditText account_tx;
    private EditText verify_code_ed;
    private EditText set_password_ed;
    private EditText config_password_ed;
    private Button register_next;
    private UserImpl userImpl;
    private Timer timer;
    private TimerTask timerTask;
    private int runTime;// 验证码倒计时
    private int isAggrement = 0;//是否同意用户协议。
    private final int REGISTER = 5;
    private Subscriber registerSubscriber;


    @Override
    protected int preView() {
        return R.layout.new_activity_register;
    }

    @Override
    protected void initView() {
        account_clear_image = (ImageView) this.findViewById(R.id.account_clear_image);
        accept_agreement = (CheckBox) this.findViewById(R.id.accept_agreement);
        register_back = (LinearLayout) this.findViewById(R.id.register_back);
        get_verify_code = (TextView) this.findViewById(R.id.get_verify_code);
        login_tx = (TextView) this.findViewById(R.id.login_tx);
        account_tx = (EditText) this.findViewById(R.id.account_tx);
        verify_code_ed = (EditText) this.findViewById(R.id.verify_code_ed);
        set_password_ed = (EditText) this.findViewById(R.id.set_password_ed);
        config_password_ed = (EditText) this.findViewById(R.id.config_password_ed);
        register_next = (Button) this.findViewById(R.id.register_next);
        accept_user_agreement_tx = (TextView) this.findViewById(R.id.accept_user_agreement_tx);
        setViewOnClickListener();
        setRegisterButtonDisable();
        userImpl = new UserImpl();
    }

    /**
     * 设置注册按钮不可以用。
     */
    private void setRegisterButtonDisable() {

        accept_agreement.setBackgroundResource(R.drawable.new_un_checked_icon);
        isAggrement = 0;
        register_next.setTextColor(context.getResources().getColor(R.color.agreement_not_receive_text_color));
        register_next.setBackgroundColor(context.getResources().getColor(R.color.agreement_not_receive_background_color));
        register_next.setEnabled(false);

    }

    /**
     * 设置点击事件。
     */
    private void setViewOnClickListener() {

        account_clear_image.setOnClickListener(account_clear_imageOnClickListener);
        get_verify_code.setOnClickListener(get_verify_codeOnClickListener);
        register_next.setOnClickListener(register_nextOnClickListener);
        login_tx.setOnClickListener(login_txOnClickListener);
        register_back.setOnClickListener(register_backOnClickListener);
        accept_agreement.setOnCheckedChangeListener(accept_agreementOnCheckedChangeListener);
        accept_user_agreement_tx.setOnClickListener(accept_user_agreement_txOnClickListener);
        account_tx.addTextChangedListener(account_txTextChangedListener);
    }

    @Override
    protected void initData() {

    }

    /**
     * 选择同意用户协议
     */
    CompoundButton.OnCheckedChangeListener accept_agreementOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {
                accept_agreement.setBackgroundResource(R.drawable.new_auto_login_checked_icon);
                isAggrement = 1;
                register_next.setTextColor(context.getResources().getColor(R.color.colorWhite));
                register_next.setBackgroundResource(R.drawable.new_contact_service_bt_bg);
                accept_user_agreement_tx.setTextColor(context.getResources().getColor(R.color.border_line));
                register_next.setEnabled(true);
            } else {
                accept_agreement.setBackgroundResource(R.drawable.new_un_checked_icon);
                isAggrement = 0;
                register_next.setTextColor(context.getResources().getColor(R.color.agreement_not_receive_text_color));
                register_next.setBackgroundColor(context.getResources().getColor(R.color.agreement_not_receive_background_color));
                accept_user_agreement_tx.setTextColor(context.getResources().getColor(R.color.colorDark));
                register_next.setEnabled(true);
                register_next.setEnabled(false);
            }
        }
    };
    /**
     * 查看用户协议。
     */
    View.OnClickListener accept_user_agreement_txOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context, StatementActivity.class));
        }
    };
    /**
     * 手机号的输入监听事件。
     */
    TextWatcher account_txTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String account = s.toString();

            if (account.length() != 11) {
                get_verify_code.setTextColor(context.getResources().getColor(R.color.agreement_not_receive_text_color));
                get_verify_code.setEnabled(false);
                return;
            }
            get_verify_code.setTextColor(context.getResources().getColor(R.color.border_line));
            get_verify_code.setEnabled(true);
        }
    };
    /**
     * 获取验证码事件
     */

    View.OnClickListener get_verify_codeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            judgePhone();
        }
    };

    /**
     * 判断手机号
     */
    private void judgePhone() {

        if (TextUtils.isEmpty(account_tx.getText().toString())) {
            ToastUtils.shortToast(context, "手机号码不能为空");
            return;
        }
        if (!StringUtil.isPhone(account_tx.getText().toString())) {
            Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", account_tx.getText().toString());
        userImpl.verifyPhone(params, this);
        get_verify_code.setEnabled(false);
        showProgressDialog("数据请求中...");
    }

    /**
     * 下一步点击事件
     */
    View.OnClickListener register_nextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (isAggrement == 0) {

                ToastUtils.shortToast(context, "请选择同意用户协议");

                return;
            } else {
                if (judgeParams()) {
                    userRegister();
                }
            }

        }
    };

    /**
     * 判断参数是否正确
     */
    public boolean judgeParams() {

        if (TextUtils.isEmpty(account_tx.getText().toString())) {
            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!StringUtil.isPhone(account_tx.getText().toString())) {
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
        if (TextUtils.isEmpty(set_password_ed.getText().toString())) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (set_password_ed.getText().toString().length() < 6
                || set_password_ed.getText().toString().length() > 20) {
            Toast.makeText(mContext, "密码长度必须为6~20位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(config_password_ed.getText().toString())) {
            Toast.makeText(mContext, "请输入确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!set_password_ed.getText().toString()
                .equals(config_password_ed.getText().toString())) {
            Toast.makeText(mContext, "前后密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 注册
     */
    public void userRegister() {

     /*   RequestParams params=new RequestParams();
        params.addBodyParameter("mobile", account_tx.getText().toString());
        params.addBodyParameter("password", MD5.getMD5ofStr(set_password_ed.getText().toString()));
        params.addBodyParameter("confirmPassword",
                MD5.getMD5ofStr(config_password_ed.getText().toString()));
        params.addBodyParameter("vcode", verify_code_ed.getText().toString());
        params.addBodyParameter("V", "Y");// 参数(Y/N) 扫描二维码下载注册用到（活动）
        userImpl.userRegister(params, this);
        showProgressDialog("注册中...");*/

        Map<String, String> params = new HashMap<>();
        params.put("mobile", account_tx.getText().toString());
        params.put("password", MD5.getMD5ofStr(set_password_ed.getText().toString()));

        params.put("confirmPassword",
                MD5.getMD5ofStr(config_password_ed.getText().toString()));
        params.put("vcode", verify_code_ed.getText().toString());
        params.put("V", "Y");// 参数(Y/N) 扫描二维码下载注册用到（活动）
        HTTPHelper helper = HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        registerSubscriber = helper.Register(params, REGISTER, this);
        showProgressDialog("注册中...");
        register_next.setEnabled(false);
    }

    /**
     * 登录
     */
    View.OnClickListener login_txOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context, LoginActivity.class));
        }
    };
    /**
     * 关掉界面。
     */
    View.OnClickListener register_backOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    /**
     * 清除账号。
     */
    View.OnClickListener account_clear_imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            account_tx.setText("");
            account_tx.setHint("请输入手机号");
        }
    };

    /**
     * affirmReg
     * 获取验证码
     */
    public void getSecurity() {

        RequestParams params = new RequestParams();
        params.addBodyParameter("mobile", account_tx.getText().toString());
        params.addBodyParameter("eztCode", MD5.getMD5ofStr("ezt" + account_tx.getText().toString()));
        userImpl.getSecurityCode(params, this);
        showProgressDialog("获取验证码...");
    }

    /**
     * 验证码倒计时
     */
    public void codeTimer() {
        get_verify_code.setEnabled(false);
        runTime = 60;
        timerTask = new TimerTask() {

            @Override
            public void run() {
                runTime--;
                handler.sendEmptyMessage(runTime);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 关闭计时器
     */
    public void cancelTimerTask() {
        get_verify_code.setEnabled(true);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int num = msg.what;
            if (num == 0) {
                get_verify_code.setText("获取验证码");
                cancelTimerTask();
                return;
            }
            get_verify_code.setText(num + "s");
        }

    };

    /**
     * 数据返回结果。
     *
     * @param object
     */
    @Override
    public void result(Object... object) {
        dismissProgressDialog();
        if (object == null) {
            Toast.makeText(getApplicationContext(), "注册异常", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String msg;
        Object[] obj = object;
        Integer taskID = (Integer) obj[0];
        boolean status = (Boolean) obj[1];
        if (!status) {
            Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
            get_verify_code.setEnabled(true);
            return;
        }
        switch (taskID) {
            case HttpParams.VERIFY_PHONE:// 验证手机是否已注册
                Map<String, Object> verifyMap = (Map<String, Object>) obj[2];
                if (verifyMap == null || verifyMap.size() == 0) {
                    get_verify_code.setEnabled(true);
                    return;
                }
                boolean valid = (Boolean) verifyMap.get("flag");
                if (valid) {
                    getSecurity();
                } else {
                    get_verify_code.setEnabled(true);
                    Toast.makeText(getApplicationContext(),
                            verifyMap.get("msg") + "", Toast.LENGTH_SHORT).show();
                }
                break;
            case HttpParams.GET_SECURITYCODE:// 获取手机验证码
                get_verify_code.setEnabled(true);
                Map<String, Object> map = (Map<String, Object>) obj[2];
                if (map == null || map.size() == 0) {
                    Toast.makeText(getApplicationContext(), "发送异常",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean flag = (Boolean) map.get("flag");
                if (flag) {
                    msg = "短信已发送，请注意查收...";
                    codeTimer();
                } else {
                    msg = (String) map.get("msg");
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
                break;
            case HttpParams.USER_REG:// 注册
                Map<String, Object> regMap = (Map<String, Object>) obj[2];
                if (regMap == null || regMap.size() == 0) {
                    Toast.makeText(getApplicationContext(), "注册异常",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean regFlag = (Boolean) regMap.get("flag");
                if (regFlag) {
                    msg = "恭喜您，注册成功，请你选择登录或完善资料";
                    startActivity(new Intent(context, CompletePersonalInfoActivity.class));
                    RegisterActivity.this.finish();
                } else {
                    msg = (String) regMap.get("msg");
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
    /**
     * 保存用户信息。
     */
    private void saveUserInfo(UserBean user,PatientBean patient) {
        if(user!=null){
            FileUtils.saveObject(context,"user",user);
        }
        if(patient!=null){
            FileUtils.saveObject(context,"patient",patient);
        }
        String password = com.eztcn.user.hall.utils.MD5.getMD5ofStr(set_password_ed.getText().toString());
        FileUtils.saveStringBySp(context, "account", account_tx.getText().toString().trim());//保存账户。
        FileUtils.saveStringBySp(context, "password", password);//保存密码本地用于自动登录。
        BaseApplication.user=user;//初始化静态
        BaseApplication.patient=patient;//初始化静态

    }

    @Override
    protected void onDestroy() {
        cancelTimerTask();
        super.onDestroy();

    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

        dismissProgressDialog();
        register_next.setEnabled(true);

    }

    @Override
    public void onError(int requestType) {

        dismissProgressDialog();
        register_next.setEnabled(true);
        ToastUtils.shortToast(context,"服务器错误");
    }

    @Override
    public void onNext(IModel t, int requestType) {

        dismissProgressDialog();
        register_next.setEnabled(true);
        switch (requestType) {

            case REGISTER:
                dismissProgressDialog();
                register_next.setEnabled(true);
                Response response = (Response) t;
                int number = Integer.valueOf(response.getNumber());
                if (number == 2001) {
                    DataResponse data = (DataResponse) response.getData();
                    UserBean user = data.getUserbean();
                    PatientBean patient = data.getPatientBean();
                    saveUserInfo(user, patient);
                    Intent re_Intent=new Intent();
                    re_Intent.setClass(context,RegisterResultActivity.class);
                    try {
                        startActivity(re_Intent);
                    }catch (Exception e){
                        Log.i("start--error",e.toString());
                    }
                    RegisterActivity.this.finish();
                    ToastUtils.shortToast(context,"注册成功");
                    break;
                }else if(number==4001){
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }else if(number==5001){

                    ToastUtils.shortToast(context,response.getDetailMsg());
                }else if(number==5006){
                    ToastUtils.shortToast(context,response.getDetailMsg());

                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
        }
    }
    @Override
    protected void disDialogCallBack() {
        super.disDialogCallBack();
        if(registerSubscriber!=null){

            if(!registerSubscriber.isUnsubscribed()){
                registerSubscriber.unsubscribe();
            }
            register_next.setEnabled(true);
        }
    }
}

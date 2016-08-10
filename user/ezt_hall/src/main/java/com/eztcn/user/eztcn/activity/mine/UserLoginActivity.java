package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.utils.FileUtils;

/**
 * @author ezt
 * @title 用户登录
 * @describe
 * @created 2014年12月26日
 */
public class UserLoginActivity extends FinalActivity implements IHttpResult, TextWatcher {

    @ViewInject(R.id.login)//, click = "onClick"
    private Button login;
    @ViewInject(R.id.register)//, click = "onClick"
    private Button register;
    @ViewInject(R.id.forgetPwd)//, click = "onClick"
    private TextView forgetPwd;
    @ViewInject(R.id.account)
    private EditText account;
    @ViewInject(R.id.password)
    private EditText password;
    @ViewInject(R.id.automatic)
    private ImageView automatic;// 自动登录
    @ViewInject(R.id.automatic_layout)
    private LinearLayout automatic_layout;// 自动登录

    @ViewInject(R.id.accountDel)//, click = "onClick" 原代码无响应函数
    private ImageView accountDel;
    @ViewInject(R.id.pwdDel)//, click = "onClick" 原代码无响应函数
    private ImageView pwdDel;
    @ViewInject(R.id.login_close_image)//close
    private ImageView login_close_image;

    private boolean autoMaticLogin;// 自动登录状态
    private String encryptPwd;
    private String strAccount;// 账号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        ViewUtils.inject(UserLoginActivity.this);
//		register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//		forgetPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        init();
    }

    public void init() {
        autoMaticLogin = SystemPreferences
                .getBoolean(EZTConfig.KEY_IS_AUTO_LOGIN);
        account.setText(SystemPreferences.getString(EZTConfig.KEY_ACCOUNT));
        account.setSelection(account.length());
        strAccount = account.getText().toString();
        if (autoMaticLogin) {
            encryptPwd = SystemPreferences.getString(EZTConfig.KEY_PW);
            password.setText(encryptPwd);
        }
//		automatic.setChecked(autoMaticLogin);
        if (autoMaticLogin) {
            automatic.setImageResource(R.drawable.auto_login_checked_icon);
        } else {
            automatic.setImageResource(R.drawable.auto_login_uncheced_gray_icon);
        }
        /*automatic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				autoMaticLogin = isChecked;
			}
		});*/
        automatic_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (autoMaticLogin) {
                    automatic.setImageResource(R.drawable.auto_login_uncheced_gray_icon);
                    autoMaticLogin = false;
                } else {
                    automatic.setImageResource(R.drawable.auto_login_checked_icon);
                    autoMaticLogin = true;
                }
            }
        });
        account.addTextChangedListener(this);
    }

    /**
     * 判断自动登录状态
     */
    public void judgeAutoMatic() {
/*
//		if (autoMaticLogin) {
			SystemPreferences.save(EZTConfig.KEY_PW, encryptPwd);
//		} else {
//			SystemPreferences.remove(EZTConfig.KEY_PW);
//		}
		SystemPreferences.save(EZTConfig.KEY_ACCOUNT, account.getText()
				.toString());
		SystemPreferences.save(EZTConfig.KEY_IS_AUTO_LOGIN, autoMaticLogin);*/

        if (autoMaticLogin) {
            SystemPreferences.save(EZTConfig.KEY_PW, encryptPwd);
            SystemPreferences.save(EZTConfig.KEY_ACCOUNT, account.getText()
                    .toString());
            SystemPreferences.save(EZTConfig.KEY_IS_AUTO_LOGIN, autoMaticLogin);
        }

    }

    @OnClick(R.id.login)
    private void loginClick(View v) {
        Intent intent = new Intent();
        intent = null;
        if (judgeParams()) {
            userLogin();
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.register)
    private void registerClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, UserRegisterActivity.class);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.forgetPwd)
    private void forgetPwdClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, ForgetPasswordActivity.class);
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.login_close_image)
    private void CloseLoginClick(View v) {

        UserLoginActivity.this.finish();
    }

//	public void onClick(View v) {
//		Intent intent = new Intent();
//		switch (v.getId()) {
//		case R.id.login:
//			intent = null;
//			if (judgeParams()) {
//				userLogin();
//			}
//			break;
//		case R.id.register:
//			intent.setClass(this, UserRegisterActivity.class);
//			break;
//		case R.id.forgetPwd:
//			intent.setClass(this, ForgetPasswordActivity.class);
//			break;
//		}
//		if (intent != null) {
//			startActivity(intent);
//			finish();
//		}
//	}

    /**
     * 登录
     */
    public void userLogin() {
        showProgressToast();
        encryptPwd = password.getText().toString();
        if (encryptPwd == null || encryptPwd.length() <= 20) {
            encryptPwd = MD5.getMD5ofStr(password.getText().toString());
        }

//		HashMap<String, Object> params = new HashMap<String, Object>();
        RequestParams params = new RequestParams();
        params.addBodyParameter("username", account.getText().toString());
        params.addBodyParameter("password", encryptPwd);
        UserImpl impl = new UserImpl();
        impl.userLogin(params, this);
        login.setEnabled(false);
    }

    /**
     * 提交推送cid 和uid
     *
     * @param userid
     */
    private void setClientid(String userid) {
        UserImpl impl = new UserImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
        RequestParams params = new RequestParams();
        params.addBodyParameter("userid", userid);
        params.addBodyParameter("cid", SystemPreferences.getString(EZTConfig.KEY_CID));
        impl.setClientid(params, this);
    }

    /**
     * 获取最新预约信息
     */
    private void getRegregisterNew() {
        RegistratioImpl api = new RegistratioImpl();
        RequestParams params = new RequestParams();
        params.addBodyParameter("patientId", BaseApplication.patient.getId()+"");
        api.getRegregisterNew(params, this);
    }

    /**
     * 判断参数是否有效
     */
    public boolean judgeParams() {
        if (TextUtils.isEmpty(account.getText().toString())) {
            Toast.makeText(mContext, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        // if (!StringUtil.isPhone(account.getText().toString())) {
        // Toast.makeText(mContext, "手机号码格式有误", Toast.LENGTH_SHORT).show();
        // return false;
        // }
        if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (encryptPwd == null) {
            if (password.getText().toString().length() < 6
                    || password.getText().toString().length() > 20) {
                Toast.makeText(mContext, "密码长度必须为6~20位", Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }
        if (!BaseApplication.getInstance().isNetConnected) {
            Toast.makeText(mContext, getString(R.string.network_hint),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    int callbackNum = 0;// 回调次数

    @Override
    public void result(Object... object) {
        Integer taskID = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];
        switch (taskID) {
            case HttpParams.USER_LOGIN:
                login.setEnabled(true);
                if (isSuc) {
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (map == null || map.size() == 0) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.service_error),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean flag = (Boolean) map.get("flag");
                    if (flag) {
                        judgeAutoMatic();
                        UserLoginActivity.this.setResult(222);
                        setClientid(BaseApplication.patient
                                .getUserId() + "");
                        getRegregisterNew();
                        callbackNum++;

                    } else {
                        hideProgressToast();
                        Toast.makeText(getApplicationContext(),
                                map.get("msg").toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    hideProgressToast();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.service_error), Toast.LENGTH_SHORT)
                            .show();
                }

                break;

            case HttpParams.GET_REGREGISTER_NEW:// 获取最新预约信息
                callbackNum++;
                break;
        }
        if (callbackNum == 2) {
            hideProgressToast();
            finish();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!TextUtils.isEmpty(password.getText().toString())
                && !strAccount.equals(s.toString())) {
            password.setText("");
            autoMaticLogin = false;
//			automatic.setChecked(autoMaticLogin);
            automatic.setImageResource(R.drawable.auto_login_normal_icon);
        } else if (strAccount.equals(s.toString())
                && !strAccount.equals(s.toString())) {
            password.setText(encryptPwd);
            autoMaticLogin = true;
            automatic.setImageResource(R.drawable.auto_login_normal_icon);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}

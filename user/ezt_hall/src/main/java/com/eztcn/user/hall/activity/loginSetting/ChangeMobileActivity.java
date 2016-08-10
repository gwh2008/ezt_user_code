package com.eztcn.user.hall.activity.loginSetting;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.impl.ChAcImpl;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LX on 2016/6/4.
 * 修改手机号界面.
 */
public class ChangeMobileActivity extends BaseActivity implements ResultSubscriber.OnResultListener {

    private Context context=ChangeMobileActivity.this;
    private String TAG="ChangeMobileActivity";
    private TextView telephone_tx;
    private EditText password_tx;
    private EditText id_number_tx;
    private Button  next_step_bt;
    private final  int MODIFY_MOBILE_VERIFY=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "更改手机号", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_change_account;
    }
    /**
     * 初始化控件
     * @return
     */
    @Override
    protected void initView() {

        telephone_tx= (TextView) this.findViewById(R.id.telephone_tx);
        password_tx= (EditText) this.findViewById(R.id.password_tx);
        id_number_tx= (EditText) this.findViewById(R.id.id_number_tx);
        next_step_bt= (Button) this.findViewById(R.id.next_step_bt);
        next_step_bt.setOnClickListener(next_step_btOnClickListener);
        setData();
    }
    @Override
    protected void initData() {

    }
    /**
     * 设置已知数据。
     */
    private void setData() {

        PatientBean patient= (PatientBean) FileUtils.getObject(context,"patient");
        telephone_tx.setText(patient.getEpMobile()+"");
    }
    /**
     * 下一步点击事件。
     */
    View.OnClickListener next_step_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(judgeParams()){
                changeMobile();
            }
        }
    };
    /**
     * 调取更改手机号的接口。
     */
    private void changeMobile() {

        showProgressDialog("数据提交中...");
        String password = MD5.getMD5ofStr(password_tx.getText().toString());
        Map<String,String> params=new HashMap<>();
        params.put("mobile", BaseApplication.patient.getEpMobile());
        params.put("password",password);
        params.put("idCard",id_number_tx.getText().toString().trim());
        params.put("idnoType","1");
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.modifyMobileVerify(map,MODIFY_MOBILE_VERIFY,this);
        next_step_bt.setEnabled(false);
    }
    /**
     * 判断参数。
     */
    private boolean judgeParams() {

        String password=password_tx.getText().toString().trim();
        if(TextUtils.isEmpty(password)){
            ToastUtils.shortToast(context,"请输入密码");
            return  false;
        }
        if (password_tx.getText().toString().length() < 6
                || password_tx.getText().toString().length() > 20) {
            ToastUtils.shortToast(context, "密码长度必须为6~20位");
            return false;
        }

        String ic = id_number_tx.getText().toString().trim();
        if (TextUtils.isEmpty(ic)) {
            ToastUtils.shortToast(context,"身份证不能为空");
            return false;
        }
        if (!StringUtil.validateCard(ic)) {
            ToastUtils.shortToast(context,"身份证格式有误");
            return false;
        }

        if(BaseApplication.patient.getEpPid()==null){
            ToastUtils.shortToast(context,"请完善个人信息");
            return false;
        }
        return true;
    }

    @Override
    public void onStart(int requestType) {
        Log.i(TAG,"onStart");

    }

    @Override
    public void onCompleted(int requestType) {
        dismissProgressDialog();
        next_step_bt.setEnabled(true);
        Log.i(TAG,"onStart");

    }

    @Override
    public void onError(int requestType) {
        dismissProgressDialog();
        next_step_bt.setEnabled(true);
        ToastUtils.shortToast(context,"error---");
        Log.i(TAG,"onStart");

    }

    @Override
    public void onNext(IModel t, int requestType) {

        dismissProgressDialog();
        next_step_bt.setEnabled(true);
        switch (requestType) {
            //修改手机号验证。
            case MODIFY_MOBILE_VERIFY:

                Response response= (Response) t;

                int number=Integer.valueOf(response.getNumber());

                if(number==2000){
                    ToastUtils.shortToast(context,"数据验证成功...");
                    startActivity(new Intent(context,SetNewMobileActivity.class));
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
                break;
        }
    }
}

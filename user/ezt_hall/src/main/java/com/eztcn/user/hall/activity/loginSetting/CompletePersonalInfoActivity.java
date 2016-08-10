package com.eztcn.user.hall.activity.loginSetting;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.activity.MainActivity;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.impl.ChAcImpl;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.CharacterTool;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.IdVerification;
import com.eztcn.user.hall.utils.MD5;
import com.eztcn.user.hall.utils.ToastUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import rx.observers.Subscribers;
import xutils.http.RequestParams;
/**
 * Created by Lx on 2016/6/6.
 * 完善个人信息界面
 */
public class CompletePersonalInfoActivity extends BaseActivity  implements  ResultSubscriber.OnResultListener {

    private Context context=CompletePersonalInfoActivity.this;
    private String TAG="CompletePersonalInfoActivity";
    private Button complete_next_bt;
    private EditText ed_name,id_number_ed,ed_mail;
    private TextView improveLater=null;
    private final  int COMPLETE_INFO=4;
    private  int Register_tag=0;

    @Override
    protected int preView() {
        return R.layout.new_activity_complete_personal_info;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        improveLater= loadTitleBar(true, "完善个人信息", "稍后完善");
        complete_next_bt = (Button) findViewById(R.id.complete_next_bt);
        ed_name  = (EditText) findViewById(R.id.ed_name);
        id_number_ed = (EditText) findViewById(R.id.id_number_ed);
        ed_mail = (EditText) findViewById(R.id.ed_mail);
        complete_next_bt.setOnClickListener(complete_next_btOnClickListener);
        improveLater.setOnClickListener(improveLaterOnClickListener);
        getIntentData();
    }

    /**
     * 获取传递过来的数据。
     */
    private void getIntentData() {

        Intent intent_data=getIntent();
        if(intent_data!=null){
            Register_tag=intent_data.getIntExtra("register",0);
        }
    }
    @Override
    protected void initData() {

    }
    /**
     * 稍后完善。
     */
    View.OnClickListener  improveLaterOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context, MainActivity.class));
        }
    };
    /**
     * 下一步
     * @param v
     */
    View.OnClickListener  complete_next_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (judgeParams()){
                completeInfo();
            }
        }
    };
    /**
     * 完善信息接口调取。
     */
    private void completeInfo() {
        showProgressDialog("数据提交中...");
        Map<String,String> params=new HashMap<>();
        params.put("userId",BaseApplication.patient.getUserId()+"");
        params.put("name",ed_name.getText().toString().trim());
        params.put("idno",id_number_ed.getText().toString().trim());
        if(!TextUtils.isEmpty(ed_mail.getText().toString().trim())){
            params.put("email",ed_mail.getText().toString().trim());
        }
        params.put("idnoType","1");
        params.put("sex","0");
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.completeInfo(map,COMPLETE_INFO,this);
        complete_next_bt.setEnabled(false);
    }
    /**
     * 判断参数。
     */
    public boolean judgeParams() {

        String n = ed_name.getText().toString();
        if (TextUtils.isEmpty(n)) {
            ToastUtils.shortToast(context,"姓名不能为空");
            return false;
        }
        if(n.length() < 2 || n.length() > 4||(!CharacterTool.isChineseCharacter(n))){
            ToastUtils.shortToast(context,"请输入正确的姓名");
            return false;
        }
        String ic = id_number_ed.getText().toString().trim();
        if (TextUtils.isEmpty(ic)) {
            ToastUtils.shortToast(context,"身份证不能为空");
            return false;
        }
       /* if (!StringUtil.validateCard(ic)) {
            ToastUtils.shortToast(context,"身份证格式有误");
            return false;
        } */
        try {
            if (!"该身份证有效".equals(IdVerification.IDCardValidate(ic))) {
                Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
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

        Log.i(TAG,"onError");
        dismissProgressDialog();
        complete_next_bt.setEnabled(true);
    }
    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        complete_next_bt.setEnabled(true);
        switch (requestType) {
            case  COMPLETE_INFO:
                Response response= (Response) t;
                int number= Integer.valueOf(response.getNumber());
                if(number==2001){
                    ToastUtils.shortToast(context,"完善成功...");
                    refreshPersonData();
                    if(Register_tag==1){  //标志是注册传递过来的。
                        startActivity(new Intent(context,MainActivity.class));
                    }
                    CompletePersonalInfoActivity.this.finish();
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
            break;
        }
    }
    /**
     * 刷新个人本地的数据。
     */
    private void refreshPersonData() {

        UserBean user= (UserBean) FileUtils.getObject(context,"user");
        PatientBean patient= (PatientBean) FileUtils.getObject(context,"patient");
        patient.setEpPid(id_number_ed.getText().toString().trim());
        patient.setEpName(ed_name.getText().toString().trim());
        user.setEuEmail(ed_mail.getText().toString().trim());
        FileUtils.saveObject(context,"user",user);
        FileUtils.saveObject(context,"patient",patient);
        BaseApplication.patient= (PatientBean) FileUtils.getObject(context,"patient");
    }
}

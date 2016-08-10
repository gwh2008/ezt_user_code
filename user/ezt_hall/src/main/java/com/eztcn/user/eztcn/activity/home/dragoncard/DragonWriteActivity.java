
package com.eztcn.user.eztcn.activity.home.dragoncard;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JudgeUtils;
import com.eztcn.user.eztcn.utils.MD5;
import com.eztcn.user.eztcn.utils.StringUtil;
import com.eztcn.user.hall.common.DragonStatusSingle;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Response;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

/**
 * @author Liu Gang
 *         <p/>
 *         2015年11月26日 上午10:13:08
 */
public class DragonWriteActivity extends FinalActivity implements IHttpResult {// implements
    // OnClickListener

    @ViewInject(R.id.nameEt)
    private EditText nameEt;
    @ViewInject(R.id.sexTv)
    private TextView sexTv;
    @ViewInject(R.id.sexLayout)
    private View sexLayout;
    @ViewInject(R.id.idcardEt)
    private EditText idcardEt;
    @ViewInject(R.id.dragonCardEt)
    private TextView dragonCardEt;
    @ViewInject(R.id.mobileEt)
    private EditText mobileEt;
    @ViewInject(R.id.yzmEt)
    private EditText yzmEt;
    @ViewInject(R.id.yzmLayout)
    private View yzmLayout;
    @ViewInject(R.id.bindDragonCardBtn)
    private Button bindDragonCardBtn;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragonawrite);
        ViewUtils.inject(DragonWriteActivity.this);
        loadTitleBar(true, "我的龙卡", null);
        judgeUser();
    }

    private void judgeUser() {

        if (StringUtils.isNotBlank(BaseApplication.patient.getEpHiid())) {// 已经认证
            setDisAbleAll();
        } else {
            setDisAble();
        }
    }

    @OnClick(R.id.sexLayout)
    public void sexLayoutClick(View v) {
        initSex();
    }

    /**
     * 初始化缴费类型
     */
    public void initSex() {
        final String[] payName = {"男", "女"};
        Builder builder = new Builder(this);
        builder.setItems(payName, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sex = which;
                sexTv.setText(sex == 0 ? "男" : "女");
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.yzmLayout)
    public void yzmLayoutClick(View v) {// 获取验证码
        // TODO 后台接口 获取龙卡验证码
        if (StringUtils.isBlank(mobileEt.getText().toString())) {
            toast("请输入手机号", Toast.LENGTH_SHORT);
            return;
        }
        if (!JudgeUtils.isMobile(mobileEt.getText().toString())) {
            toast("请正确输入手机号", Toast.LENGTH_SHORT);
            return;
        }
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("mobile", mobileEt.getText().toString());
//        // 用户端忘记密码获取验证码 eztcode md5加密
//        params.addBodyParameter("eztCode",
//                MD5.getMD5ofStr("ezt" + mobileEt.getText().toString()));
        Log.i("验证码","mobile=="+mobileEt.getText().toString()+"==eztCode=="+MD5.getMD5ofStr("ezt" + mobileEt.getText().toString()));
//        new EztServiceCardImpl().sendSMS(params, this);
//        v.setEnabled(false);

        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobileEt.getText().toString());
        map.put("eztCode",MD5.getMD5ofStr("ezt" + mobileEt.getText().toString()));
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DRAGON).postDragonAuth(map, 1001, new ResultSubscriber.OnResultListener() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onCompleted(int requestType) {

            }

            @Override
            public void onError(int requestType) {

            }

            @Override
            public void onNext(IModel t, int requestType) {
                Response<String> response = (Response<String>) t;

                Log.i("验证码","====="+response.toString());
            }
        });

    }

    @OnClick(R.id.bindDragonCardBtn)
    public void bindDragonCardBtnClick(View v) {// 绑定龙卡
        if (judgeParams()) {
            RequestParams params = new RequestParams();
            params.addBodyParameter("bankCardId", dragonCardEt.getText()
                    .toString());
            params.addBodyParameter("custName", nameEt.getText().toString());
            params.addBodyParameter("custId", idcardEt.getText().toString());
            params.addBodyParameter("phone", mobileEt.getText().toString());
            params.addBodyParameter("sex", sexTv.getText().toString().equals("男") ? "0" : "1");
            params.addBodyParameter("uid",
                    String.valueOf(BaseApplication.patient.getUserId()));
            params.addBodyParameter("pfid", "355");
            params.addBodyParameter("checkCode", yzmEt.getText().toString());
            EztServiceCardImpl api = new EztServiceCardImpl();
            api.authentication(params, this);
            showProgressToast();
        }
    }

    private boolean judgeParams() {
        if (StringUtils.isBlank(nameEt.getText().toString())) {
            toast("请输入姓名", Toast.LENGTH_SHORT);
            return false;
        }
        if (StringUtils.isBlank(sexTv.getText().toString())) {
            toast("请选择性别", Toast.LENGTH_SHORT);
            return false;
        }
        if (StringUtils.isBlank(idcardEt.getText().toString())) {
            toast("请输入身份证号", Toast.LENGTH_SHORT);
            return false;
        }
        if (!StringUtil.validateCard(idcardEt.getText().toString())) {
            toast("请输入合法身份证号", Toast.LENGTH_SHORT);
            return false;
        }
        if (StringUtils.isBlank(dragonCardEt.getText().toString())) {
            toast("请输入卡号", Toast.LENGTH_SHORT);
            return false;
        }
        if (dragonCardEt.getText().toString().length() != 19) {
            toast("卡号需为19位", Toast.LENGTH_SHORT);
            return false;
        }

        if (StringUtils.isBlank(mobileEt.getText().toString())) {
            toast("请输入手机号", Toast.LENGTH_SHORT);
            return false;
        }
        if (!JudgeUtils.isMobile(mobileEt.getText().toString())) {
            toast("请正确输入手机号", Toast.LENGTH_SHORT);
            return false;
        }

        if (StringUtils.isBlank(yzmEt.getText().toString())) {
            toast("请输入验证码", Toast.LENGTH_SHORT);
            return false;
        }
        return true;

    }

    private void setDisAble() {
        PatientBean patientBean = BaseApplication.patient;
        nameEt.setText(patientBean.getEpName());
		nameEt.setEnabled(false);


        idcardEt.setText(patientBean.getEpPid());
		idcardEt.setEnabled(false);
    }

    private void setDisAbleAll() {
        PatientBean patientBean = BaseApplication.patient;

        nameEt.setText(patientBean.getEpName());
        nameEt.setBackgroundColor(getResources().getColor(R.color.light_gray));
		nameEt.setEnabled(false);

        sexTv.setText(patientBean.getEpSex() == 0 ? "男" : "女");
        sexTv.setBackgroundColor(getResources()
                .getColor(R.color.light_gray));
        sexLayout.setClickable(false);

        idcardEt.setText(patientBean.getEpPid());
        idcardEt.setBackgroundColor(getResources().getColor(R.color.light_gray));
		idcardEt.setEnabled(false);

        mobileEt.setText(patientBean.getEpMobile());
        mobileEt.setBackgroundColor(getResources().getColor(R.color.light_gray));
        mobileEt.setEnabled(false);
    }

    private void failed() {
        // View viewContent = View.inflate(mContext, R.layout.ordercheck_dialog,
        // null);
        Builder b = new Builder(mContext);
        b.setTitle("未绑定成功！");
        b.setMessage("您绑定的信息与龙卡开卡信息不匹配，请核对后在进行绑定！");
        b.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    private void success() {
        // View viewContent = View.inflate(mContext, R.layout.ordercheck_dialog,
        // null);
//        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DRAGON_SUCCESS).getDragonSuccess(BaseApplication.patient.getUserId() + "", 2001, new ResultSubscriber.OnResultListener() {
//            @Override
//            public void onStart(int requestType) {
//            }
//
//            @Override
//            public void onCompleted(int requestType) {
//            }
//
//            @Override
//            public void onError(int requestType) {
//            }
//
//            @Override
//            public void onNext(IModel t, int requestType) {
//            }
//        });
        Builder b = new Builder(mContext);
        b.setTitle("绑定成功！");
        b.setMessage("恭喜您，已经成为龙卡专享会员！");
        b.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DragonStatusSingle.getInstance().setOpenDragon(true);
                Intent intent = new Intent(getApplicationContext(),
                        ActivityDragonCard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                // 结束二选一界面
                finisActivity(DragonToActive30Activity.class);
                finish();
            }
        }).create().show();
    }

    @Override
    public void result(Object... object) {

        hideProgressToast();

        int type = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];
        if (isSuc) {

            switch (type) {
                case HttpParams.CCB_INfO_SEND_SMS: {// 获取找回密码验证码
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (isSuc) {// 成功
                        boolean flag = (Boolean) map.get("flag");
                        if (flag) {// 成功
                            yzmLayout.setEnabled(true);
                            Toast.makeText(DragonWriteActivity.this,
                                    "发送成功，请注意查收您的短信！", Toast.LENGTH_SHORT).show();
                        } else {
                            yzmLayout.setEnabled(true);
                            Toast.makeText(DragonWriteActivity.this,
                                    map.get("msg").toString(), Toast.LENGTH_SHORT)
                                    .show();
                        }

                    } else {
                        Toast.makeText(DragonWriteActivity.this,
                                object[3].toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                default: {// 激活成功与否的判断
                    Map<String, Object> map = (Map<String, Object>) object[2];
                    if (map != null && map.size() != 0) {
                        boolean flag = (Boolean) map.get("flag");
                        if (map.containsKey("msg")) {
                            String info = map.get("msg").toString();
                            Toast.makeText(getApplicationContext(), info,
                                    Toast.LENGTH_SHORT).show();
                        }
                        if (flag) {
                            success();
                        } else
                            // 激活失败
                            failed();
                    }
                }
            }

        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.service_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}

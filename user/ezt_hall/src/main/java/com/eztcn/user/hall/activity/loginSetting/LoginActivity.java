package com.eztcn.user.hall.activity.loginSetting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.mine.PWDModifyActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.activity.BaseActivity;
import com.eztcn.user.hall.activity.MainActivity;
import com.eztcn.user.hall.common.DragonStatusSingle;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.exception.MapException;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.DataResponse;
import com.eztcn.user.hall.model.DragonStatusResponse;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.UserBean;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.MD5;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import xutils.http.RequestParams;
/**
 * Created by lx on 2016/6/6.
 * 登录界面。
 */
public class LoginActivity extends BaseActivity implements ResultSubscriber.OnResultListener,IHttpResult {

    private static final int GET_DRAGON_STATE = 10001;//获取龙卡信息

    private Context context=LoginActivity.this;
    private TextView contact_customer_service_tx;
    private EditText login_account;
    private EditText login_password;
    private Button   login_bt;
    private TextView forget_password_tx;
    private TextView register_account;
    private LinearLayout login_back;
    private String password="";
    private String account="";
    private final int LOGIN_TYPE=0;//登录
    private AlertDialog.Builder customerServiceDialog;
    private Subscriber subscriber;


    @Override
    protected int preView() {
        return R.layout.new_activity_login;
    }

    @Override
    protected void initView() {

          login_back= (LinearLayout) this.findViewById(R.id.login_back);
          login_account= (EditText) this.findViewById(R.id.login_account);
          login_password= (EditText) this.findViewById(R.id.login_password);
          login_bt= (Button) this.findViewById(R.id.login_bt);
          forget_password_tx= (TextView) this.findViewById(R.id.forget_password_tx);
          register_account= (TextView) this.findViewById(R.id.register_account);
          contact_customer_service_tx= (TextView) this.findViewById(R.id.contact_customer_service_tx);
          configView();
          setOnClickListener();
    }

    /**
     * 设置点击事件。
     */

    private void setOnClickListener() {
        login_back.setOnClickListener(login_backOnClickListener);
        login_bt.setOnClickListener(login_btOnClickListener);
        forget_password_tx.setOnClickListener(forget_password_txOnClickListener);
        register_account.setOnClickListener(register_accountOnClickListener);
    }

    /**
     * 配置TextView
     */
    private void configView() {

            contact_customer_service_tx.setText(getClickableSpan());
            contact_customer_service_tx.setMovementMethod(LinkMovementMethod.getInstance());
    }
    @Override
    protected void initData() {

    }
    private SpannableString getClickableSpan() {
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomerServiceDialog();
            }
        };

        SpannableString spanableInfo = new SpannableString("遇到问题？请联系我们的客服");
        //设置突出显示的文字开始与结束位置
        int start = 11;
        int end = 13;
        //设置点击事件
        spanableInfo.setSpan(new Clickable(l), start, end, Spanned.SPAN_MARK_POINT);
        spanableInfo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.border_line)), start, end,
                Spanned.SPAN_COMPOSING);
        return spanableInfo;
    }

    /**
     * 客服对话框。
     */
    private void showCustomerServiceDialog() {
        {
            customerServiceDialog = new AlertDialog.Builder(context,R.style.dialog);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.new_item_customer_service, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            final AlertDialog dialog = customerServiceDialog.create();
            // 默认在中间，下面3行是从底部弹出的动画，
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            // 设置 对话框显示横向充满屏幕。。
            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
            android.view.WindowManager.LayoutParams p = dialog.getWindow()
                    .getAttributes(); // 获取对话框当前的参数值
            //  p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
            p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.5
            dialog.getWindow().setAttributes(p); // 设置生效
            dialog.setContentView(view);
            RelativeLayout customer_service_telephone = (RelativeLayout) view.findViewById(R.id.customer_service_telephone);
            RelativeLayout cancel_rl = (RelativeLayout) view.findViewById(R.id.cancel_rl);

            customer_service_telephone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + getString(R.string.customerService_hotLine)));
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            cancel_rl.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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
        ToastUtils.shortToast(context,"网络请求异常");
    }

    @Override
    public void onNext(IModel t, int requestType) {


        switch(requestType){
            //登录
            case LOGIN_TYPE:
                dismissProgressDialog();
                login_bt.setEnabled(true);
                Response response=(Response) t;

                int number=Integer.valueOf(response.getNumber());

                if(number==2000){
                    DataResponse data= (DataResponse) response.getData();
                    UserBean user=data.getUserbean();
                    PatientBean patient=data.getPatientBean();
                    saveUserInfo(user,patient);

                    setResult(Constant.LOGIN_COMPLETE);
                    boolean isSkipMain=FileUtils.getBooleanBySp(context,"isSkipMain");

                    if(!isSkipMain){
                        startActivity(new Intent(context, MainActivity.class));
                        FileUtils.saveBooleanBySp(context,"isSkipMain",true);
                    }

                    Map<String,String> map = new HashMap<>();
                    map.put("uid",String.valueOf(patient.getUserId()));
                    HTTPHelper.getInstance(HTTPHelper.URL_TYPE.DRAGON).postDragonStatus2(map, GET_DRAGON_STATE, this);

                       LoginActivity.this.finish();
                }else{
                    ToastUtils.shortToast(context,response.getDetailMsg());
                }
                break;
            case GET_DRAGON_STATE:
                Response<DragonStatusResponse> dragonStatusResponse = (Response<DragonStatusResponse>) t;
                DragonStatusSingle dragonSingle = DragonStatusSingle.getInstance();
                if ("2000".equals(dragonStatusResponse.getNumber())){
                    dragonSingle.setOpenDragon(true);
                    DragonStatusResponse dragonResponse = dragonStatusResponse.getData();
                    dragonSingle.setActiveDate(dragonResponse.getActiveDate());
                    dragonSingle.setBankCardId(dragonResponse.getBankCardId());
                    dragonSingle.setCustId(dragonResponse.getCustId());
                    dragonSingle.setCustName(dragonResponse.getCustName());
                    dragonSingle.setEndDate(dragonResponse.getEndDate());
                    dragonSingle.setGuideNum(dragonResponse.getGuideNum());
                    dragonSingle.setId(dragonResponse.getId());
                    dragonSingle.setLeadNum(dragonResponse.getLeadNum());
                    dragonSingle.setOpencard(dragonResponse.getOpencard());
                    dragonSingle.setPayFlag(dragonResponse.getPayFlag());
                    dragonSingle.setPfId(dragonResponse.getPfId());
                    dragonSingle.setPhone(dragonResponse.getPhone());
                    dragonSingle.setRemindNum(dragonResponse.getRemindNum());
                    dragonSingle.setSex(dragonResponse.getSex());
                    dragonSingle.setUid(dragonResponse.getUid());
                    dragonSingle.setUname(dragonResponse.getUname());
                }else {
                    dragonSingle.setOpenDragon(false);
                }
                break;
            default:
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
        FileUtils.saveStringBySp(context,"account",account);//保存账户。
        FileUtils.saveStringBySp(context,"password",password);//保存密码本地用于自动登录。
        BaseApplication.user=user;//初始化静态
        BaseApplication.patient=patient;//初始化静态

    }
    class Clickable extends ClickableSpan implements View.OnClickListener {
        private final View.OnClickListener mListener;

        public Clickable(View.OnClickListener l) {
            mListener = l;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
    /**
     * 判断参数是否有效
     */
    public boolean judgeParams() {
        if (TextUtils.isEmpty(login_account.getText().toString())) {
            ToastUtils.shortToast(context,"手机号码不能为空");
            return false;
        }
        if(!Tools.isPhone(login_account.getText().toString().trim())){
            ToastUtils.shortToast(context,"手机号码格式有误");
            return false;
        }
        if (TextUtils.isEmpty(login_password.getText().toString())) {
            ToastUtils.shortToast(context,"密码不能为空");
            return false;
        }
            if (login_password.getText().toString().length() < 6
                    || login_password.getText().toString().length() > 20) {
                ToastUtils.shortToast(context, "密码长度必须为6~20位");
                return false;
            }
        return true;
    }
    /**
     * 关掉界面
     */
    View.OnClickListener login_backOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginActivity.this.finish();
        }
    };
    /**
     * 登录按钮。
     */
    View.OnClickListener login_btOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if( judgeParams()){
                try {
                    userLogin();
                } catch (MapException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 忘记密码。
     */
    View.OnClickListener forget_password_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context,FindBackPasswordActivity.class));
        }
    };
    /**
     * 注册界面
     */
   View.OnClickListener   register_accountOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(context,RegisterActivity.class));

        }
    };
    /**
     * 调取登录接口。
     */
    private void userLogin() throws MapException {

        showProgressDialog("登录中...");
        account=login_account.getText().toString().trim();
        password = login_password.getText().toString().trim();
        password = MD5.getMD5ofStr(login_password.getText().toString());
        Map<String,String> params=new HashMap<>();
        params.put("username",account);
        params.put("password",password);
        Request request=new Request();
        Map<String,String> map=request.getFormMap(params);
        if(map==null) {
                throw new MapException("map为null");
        }
       HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        subscriber= helper.postLogin(map,LOGIN_TYPE,this);
        login_bt.setEnabled(false);
    }
    /**
     * 原来的登录接口。
     */
    private void userLogin2(){

        showProgressDialog("登录中...");
        account=login_account.getText().toString().trim();
        password = login_password.getText().toString().trim();
        password = MD5.getMD5ofStr(login_password.getText().toString());

            RequestParams params=new RequestParams();
            params.addBodyParameter("username", account);
            params.addBodyParameter("password", password);
            UserImpl impl = new UserImpl();
            impl.userLogin(params, this);
            login_bt.setEnabled(false);
    }
    /**
     * 网络返回的数据结果。
     * @param object
     */
    @Override
    public void result(Object... object) {
        Integer taskID = (Integer) object[0];
        boolean isSuc = (Boolean) object[1];
        switch (taskID) {
            case HttpParams.USER_LOGIN:
                login_bt.setEnabled(true);
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

                    } else {
                      dismissProgressDialog();
                        Toast.makeText(getApplicationContext(),
                                map.get("msg").toString(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                   dismissProgressDialog();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.service_error), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }
    @Override
    protected void disDialogCallBack() {
        super.disDialogCallBack();
        if(subscriber!=null){

            if(!subscriber.isUnsubscribed()){
                subscriber.unsubscribe();
            }
        }
        login_bt.setEnabled(true);
    }
}

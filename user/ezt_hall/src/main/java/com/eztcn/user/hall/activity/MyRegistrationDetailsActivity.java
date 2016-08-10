package com.eztcn.user.hall.activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.MyDialog;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.BackValidateDialog;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;
import java.util.Map;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;

/**
 * Created by lx on 2016/6/3.
 * 我的预约挂号详情界面。
 */
public class MyRegistrationDetailsActivity extends BaseActivity implements IHttpResult, BackValidateDialog.CodeSure {

    private ImageView top_right;
    private String temp_regId, temp_pfId;
    private String TAG = "MyRegistrationDetailsActivity";
    private Record_Info record_info;//包含当前页面数据的实体类
    private TextView patient_name;
    private TextView patient_phone;
    private TextView id_card_number;
    private TextView doctor_name;
    private TextView dept_name;
    private TextView hospital_name;
    private TextView pay_way;
    private TextView registration_fee;
    private TextView registration_time;
    private TextView my_registration_hint;
    private Button cancel_order_bt;
    private BackValidateDialog validateDialog;
    private LinearLayout cancel_ll;
    private  int timeType=0;

    @Override
    protected int preView() {
        return R.layout.new_activity_my_registration_details;
    }

    @Override
    protected void initView() {
//        top_right=loadTitleBar(true,"预约详情",R.drawable.new_share_icon);
//        top_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.shortToast(mContext,"分享");
//            }
//        });

        loadTitleBar(true, "预约详情", null);
        patient_name = (TextView) this.findViewById(R.id.patient_name);
        patient_phone = (TextView) this.findViewById(R.id.patient_phone);
        id_card_number = (TextView) this.findViewById(R.id.id_card_number);
        doctor_name = (TextView) this.findViewById(R.id.doctor_name);
        dept_name = (TextView) this.findViewById(R.id.dept_name);
        hospital_name = (TextView) this.findViewById(R.id.hospital_name);
        pay_way = (TextView) this.findViewById(R.id.pay_way);
        registration_fee = (TextView) this.findViewById(R.id.registration_fee);
        registration_time = (TextView) this.findViewById(R.id.registration_time);
        cancel_order_bt = (Button) this.findViewById(R.id.cancel_order_bt);
        cancel_order_bt.setOnClickListener(cancel_order_btOnClickListener);
        cancel_ll = (LinearLayout) this.findViewById(R.id.cancel_ll);
        my_registration_hint = (TextView) this.findViewById(R.id.my_registration_hint);

    }

    @Override
    protected void initData() {
        record_info = (Record_Info) getIntent().getSerializableExtra("data");

        if(getIntent()!=null){
            timeType=getIntent().getIntExtra("timeType",0);
        }
        setData();
    }

    /**
     * 设置展示数据。
     */
    private void setData() {

        patient_name.setText(record_info.getPatientName());
        patient_phone.setText(record_info.getPhone());
        id_card_number.setText(record_info.getIdCard());
        doctor_name.setText(record_info.getDoctorName());
        dept_name.setText(record_info.getDept());
        hospital_name.setText(record_info.getHospital());
        pay_way.setText("到院缴费");

       int free= (int) record_info.getRegDeptDocedFree();

        if (free == 0) {
            registration_fee.setText("按医院标准收费");
        } else if (-1 == record_info.getRegDeptDocedFree()) {
            registration_fee.setText("按医院标准收费");
        } else {
            registration_fee.setText("¥"+free );
        }
        if(timeType==0){
            registration_time.setText(record_info.getRegTime());
        }else{
            String beginTime = record_info.getBeginTime();
            String endTime = record_info.getEndTime();
            String date = record_info.getDate();
            try {
                String time = date.substring(0, date.indexOf(" "))
                        + " "
                        + beginTime.substring(beginTime.indexOf(" ") + 1,
                        beginTime.lastIndexOf(":"))
                        + "-"
                        + endTime.substring(endTime.indexOf(" ") + 1,
                        endTime.lastIndexOf(":"));
                registration_time.setText(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (record_info.getRrStatus() == 4) {
            my_registration_hint.setText("您已取消了预约，当月最多可以取消预约两次，感谢您使用医指通平台，祝您身体健康！");
            cancel_ll.setVisibility(View.GONE);
        }
        if (record_info.getRrStatus() == 3 || record_info.getRrStatus() == 0) {
            my_registration_hint.setText("您已经成功预约，请在就诊当日准时到院就诊！");
            cancel_ll.setVisibility(View.VISIBLE);
        }else if(record_info.getRrStatus()==7){

            my_registration_hint.setText("您在就诊时间内未就诊，已造成爽约。当月爽约3次将无法使用医指通平台的预约挂号服务");
            cancel_ll.setVisibility(View.GONE);
        }else  if(record_info.getRrStatus()==6){
            my_registration_hint.setText(" 您已成功到院就诊，感谢您使用医指通平台，祝您就诊顺利!");
            cancel_ll.setVisibility(View.GONE);

        }else {
            cancel_ll.setVisibility(View.GONE);
        }
        if ("successPage".equals(getIntent().getStringExtra("from"))) {//如果是预约成功页面过来的，就让取消按钮消失，不让用户取消
            cancel_ll.setVisibility(View.GONE);
        }
    }

    /**
     * 取消挂号
     */
    View.OnClickListener cancel_order_btOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelRegistration(record_info.getPlatformId() + "", record_info.getId() + "");
        }
    };

    /**
     * 取消挂号的费用。
     */
    private void cancelRegistration(final String pfId, final String regId) {

        final MyDialog dialog = new MyDialog(mContext, "确定", "取消", "取消订单", null);
        dialog.setDialogCancle(new MyDialog.DialogCancle() {

            @Override
            public void dialogCancle() {
                temp_regId = "";
                temp_pfId = "";
                dialog.dissMiss();
            }
        });

        dialog.setDialogSure(new MyDialog.DialogSure() {

            @Override
            public void dialogSure() {
                temp_regId = regId;
                temp_pfId = pfId;
                validateDialog = new BackValidateDialog(
                        MyRegistrationDetailsActivity.this, record_info.getId(),
                        BaseApplication.patient != null ? BaseApplication.patient.getUserId() : 0);
                validateDialog.setSure(MyRegistrationDetailsActivity.this);
                validateDialog.show();
                dialog.dissMiss();

            }
        });
        dialog.show();
    }

    @Override
    public void result(Object... object) {
        dismissProgressDialog();
        if (object == null) {
            return;
        }
        Integer taskID = (Integer) object[0];
        if (taskID == null) {
            return;
        }
        boolean flag = false;
        if (object[1] != null) {
            flag = (Boolean) object[1];
        }
        if (!flag) {
            if (taskID == HttpParams.BACKNUMBER) {
                // 退号后移除短信验证时间点信息2015-12-31
                SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
                        + temp_regId);
            }
            if (object[2] != null) {
                Toast.makeText(getApplicationContext(), object[2].toString(),
                        Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (taskID == HttpParams.BACKNUMBER) {
            String msg;
            Map<String, Object> map = (Map<String, Object>) object[2];
            boolean f = false;
            if (null != map && null != map.get("flag"))
                f = (Boolean) map.get("flag");
            if (f) {
                msg = "退号成功";
                // 退号后移除短信验证时间点信息2015-12-31
                SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"+ temp_regId);
                WhereBuilder b = WhereBuilder.b("msgId", "=", record_info.getId());
                EztDb.getInstance(mContext).delDataWhere(new MessageAll(), b); // 退号删除相应的预约提醒消息
                my_registration_hint.setText("您已取消了预约，当月最多可以取消预约两次，感谢您使用医指通平台，祝您身体健康！");
                cancel_ll.setVisibility(View.GONE);

                setResult(Constant.REFRESH_MY_REGISTRATION_NUM);
                finish();
            } else {
                // 退号后移除短信验证时间点信息2015-12-31
                SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME + "_"
                        + temp_regId);
                if (null != map.get("msg"))
                    msg = map.get("msg").toString();
                else
                    msg = "退号失败，请您稍后再试";
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    @Override
    public void onSureClick(String valideCode) {

        if (valideCode.isEmpty()) {
            Toast.makeText(MyRegistrationDetailsActivity.this, "请输入验证码",
                    Toast.LENGTH_SHORT).show();
        } else {
            validateDialog.dismiss();
            if (null != BaseApplication.patient) {
                RegistratioImpl impl = new RegistratioImpl();
                RequestParams params = new RequestParams();
                params.addBodyParameter("regId", temp_regId);
                params.addBodyParameter("pfId", temp_pfId);
                params.addBodyParameter("mobile",
                        BaseApplication.patient.getEpMobile());
                params.addBodyParameter("code", valideCode);
                params.addBodyParameter("userId",
                        "" + BaseApplication.patient.getUserId());// 2015-12-30
                // 退号时候要求加
                // 用户id
                impl.backNumber(params, MyRegistrationDetailsActivity.this);
                showProgressDialog("退号中...");
            } else {
                hintToLogin(Constant.LOGIN_COMPLETE);
            }
        }
    }


}

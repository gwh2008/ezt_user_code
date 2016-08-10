package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.CommonBottomDialogListener;
import com.eztcn.user.hall.model.CommitAppointmentDataRequest;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AttentionDoctorResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorDatasOneLevelResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.model.ResultResponse.MyPatientResponse;
import com.eztcn.user.hall.model.ResultResponse.WeekDatasResponse;
import com.eztcn.user.hall.model.ResultResponse.WeekTimesCountDataResponse;
import com.eztcn.user.hall.utils.CommonBottomDialog;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 确认预约页面
 *
 * @author 蒙
 */
public class ConfirmAppointmentActivity extends BaseActivity implements View.OnClickListener
        , ResultSubscriber.OnResultListener {

    private Button commit;
    private boolean isSpecialHospital = true;//该医院是否是天津肿瘤医院
    private TextView health_care_text, pay_type_text, patient_name;//页面下面的相关控件
    private TextView hospital_name, department_name, doctor_name, money, time;//页面上面的医生信息的相关控件
    private EditText healthCare_cardNumber, medical_cardNumber;//就诊卡号和医保卡号
    private LinearLayout linearLayout_healthCare_cardNumber, linearLayout_medical_cardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "确认预约", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_confirm_appointment;
    }

    public void initView() {
        commit = (Button) findViewById(R.id.new_activity_confirm_appointment_commit);
        commit.setOnClickListener(this);
        findViewById(R.id.new_confirm_appointment_select_person).setOnClickListener(this);
        findViewById(R.id.new_activity_confirm_appointment_healthCare_status).setOnClickListener(this);
        findViewById(R.id.new_activity_confirm_appointment_pay_type).setOnClickListener(this);
        health_care_text = (TextView) findViewById(R.id.new_activity_confirm_appointment_healthCare_status_text);
        pay_type_text = (TextView) findViewById(R.id.new_activity_confirm_appointment_pay_type_text);
        linearLayout_healthCare_cardNumber = (LinearLayout) findViewById(R.id.new_activity_confirm_appointment_healthCare_cardNumber);
        linearLayout_medical_cardNumber = (LinearLayout) findViewById(R.id.new_activity_confirm_appointment_medical_cardNumber);

        hospital_name = (TextView) findViewById(R.id.new_confirm_appointment_select_hospital_name);
        department_name = (TextView) findViewById(R.id.new_confirm_appointment_select_department_name);
        doctor_name = (TextView) findViewById(R.id.new_confirm_appointment_select_doctor_name);
        money = (TextView) findViewById(R.id.new_confirm_appointment_select_money);
        time = (TextView) findViewById(R.id.new_confirm_appointment_select_time);
        patient_name = (TextView) findViewById(R.id.new_confirm_appointment_select_patient_name);
        healthCare_cardNumber = (EditText) findViewById(R.id.new_activity_confirm_appointment_healthCare_cardNumber_text);
        medical_cardNumber = (EditText) findViewById(R.id.new_activity_confirm_appointment_medical_cardNumber_text);
    }

    private DoctorListResponse doctorData;//上个页面传过来的医生的一些信息
    private WeekTimesCountDataResponse weekTimesCountDataResponse;//上个页面传过来的的时间段实体类

    @Override
    protected void initData() {
        isSpecialHospital = Constant.IS_SPECIAL_HOSPITAL;

        doctorData = (DoctorListResponse) getIntent().getSerializableExtra("doctorData");
        weekTimesCountDataResponse = (WeekTimesCountDataResponse) getIntent().getSerializableExtra("timeData");

        hospital_name.setText(doctorData.getHospitalName());
        department_name.setText(doctorData.getDptName());
        doctor_name.setText(doctorData.getDocName());
        if (TextUtils.isEmpty(weekTimesCountDataResponse.getRegisterFee())||"0".equals(weekTimesCountDataResponse.getRegisterFee())) {
            money.setText("按医院标准费用");
        } else {
            money.setText("¥" + weekTimesCountDataResponse.getRegisterFee());
        }

        time.setText(weekTimesCountDataResponse.getRegDate().substring(5, 10) + " " +
                SelectAppointmentTimeActivity.numberToWeek(weekTimesCountDataResponse.getRegDateWeek())
                + " " + weekTimesCountDataResponse.getStartDate().substring(11, 16)
                + "-" + weekTimesCountDataResponse.getEndDate().substring(11, 16));
    }

    /**
     * 获取就诊人信息
     */
    public void getPatientInfo() {
        showProgressDialog("正在加载就诊人。。");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("userId", ((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getPatientList(request.getFormMap(params), 111, this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.new_activity_confirm_appointment_commit:
                //以下全是必填项，需要进行输入校验
                if (patient_name.getText().equals("请选择") || health_care_text.getText().equals("请选择")) {
                    ToastUtils.shortToast(mContext, "请完善预约信息");
                    return;
                }
                if (isSpecialHospital) {
                    if (health_care_text.getText().equals("复诊")) {
                        //对就诊卡号进行格式校验
                        String match="^[0-9]{8}$";
                        Pattern pattern = Pattern.compile(match);
                        Matcher matcher = pattern.matcher(healthCare_cardNumber.getText().toString());
                        boolean result = matcher.matches();
                        if (!result){
                            ToastUtils.shortToast(mContext, "请输入8位数字的就诊卡号");
                            return;
                        }
                    }
                    if (pay_type_text.getText().equals("医保") && TextUtils.isEmpty(medical_cardNumber.getText().toString())) {
                        ToastUtils.shortToast(mContext, "请输入医保卡号");
                        return;
                    }
                }

                startActivity(new Intent(mContext, PhoneValidationActivity.class)
                        .putExtra("data", structureCommitData()));
                break;
            case R.id.new_confirm_appointment_select_person://获取就诊人信息
                getPatientInfo();
                break;
            case R.id.new_activity_confirm_appointment_healthCare_status://就诊状态
                if (isSpecialHospital) {
                    CommonBottomDialog.specialShowCommonBottomDialog(this, new CommonBottomDialogListener() {
                        @Override
                        public void itemClick(int index) {
                            if (index == 0) {
                                health_care_text.setText("初诊");
                                linearLayout_healthCare_cardNumber.setVisibility(View.GONE);
                            } else {
                                health_care_text.setText("复诊");
                                linearLayout_healthCare_cardNumber.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    List<String> datas = new ArrayList<>();
                    datas.add("初诊（未在医院就诊过）");
                    datas.add("复诊（曾在医院就诊过）");
                    CommonBottomDialog.showCommonBottomDialog(this, datas, new CommonBottomDialogListener() {
                        @Override
                        public void itemClick(int index) {
                            if (index == 0) {
                                health_care_text.setText("初诊");
                            } else {
                                health_care_text.setText("复诊");
                            }
                        }
                    });
                }
                break;
            case R.id.new_activity_confirm_appointment_pay_type://付费方式
                List<String> datas = new ArrayList<>();
                datas.add("自费");
                datas.add("医保");
                CommonBottomDialog.showCommonBottomDialog(this, datas, new CommonBottomDialogListener() {
                    @Override
                    public void itemClick(int index) {
                        if (index == 0) {
                            pay_type_text.setText("自费");
                        } else {
                            pay_type_text.setText("医保");
                        }
                        if (isSpecialHospital && index == 1) {
                            linearLayout_medical_cardNumber.setVisibility(View.VISIBLE);
                        } else {
                            linearLayout_medical_cardNumber.setVisibility(View.GONE);
                        }
                    }
                });
                break;
        }
    }

    /**
     * @return
     */
    public CommitAppointmentDataRequest structureCommitData() {
        CommitAppointmentDataRequest dataRequest = new CommitAppointmentDataRequest();
        dataRequest.setPfId("350");
        dataRequest.setSourcePfId(Constant.PLAT_ID);
        dataRequest.setCartoonNum("");
        dataRequest.setCardNum("");
        dataRequest.setIp("");
        dataRequest.setRegMark("401");
        dataRequest.setOperateUserId(((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");
        dataRequest.setPoolId(weekTimesCountDataResponse.getPoolId());
        dataRequest.setMobile(FileUtils.getStringBySp(mContext, "account"));
        dataRequest.setIdno(patient_list.get(index_patient).getPatient().getEpPid());
        dataRequest.setPatientId(patient_list.get(index_patient).getFamily().getPatientId()+"");
        if (pay_type_text.getText().equals("自费")) {
            dataRequest.setPayWay("0");
        } else {
            dataRequest.setPayWay("1");
        }
        if (health_care_text.getText().equals("初诊")) {
            dataRequest.setIsFirst("0");
        } else {
            dataRequest.setIsFirst("1");
        }
        if (!TextUtils.isEmpty(medical_cardNumber.getText().toString())) {
            dataRequest.setMedicareCardNum(medical_cardNumber.getText().toString());
        } else {
            dataRequest.setMedicareCardNum("");
        }
        if (!TextUtils.isEmpty(healthCare_cardNumber.getText().toString())) {
            dataRequest.setVisitCardNum(healthCare_cardNumber.getText().toString());
        } else {
            dataRequest.setVisitCardNum("");
        }
        return dataRequest;
    }

    private List<MyPatientResponse> patient_list;//就诊人数据源
    private int index_patient = -1;//就诊人的选中项索引

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//获取就诊人信息
                patient_list = (List<MyPatientResponse>) response.getData();
                if (patient_list == null) {
                    patient_list = new ArrayList<>();
                }
                if (patient_list.size() == 0) {//就诊人列表为空时只显示新建就诊人
                    List<String> datas = new ArrayList<>();
                    datas.add("新建就诊人");
                    CommonBottomDialog.showCommonBottomDialog(this, datas, new CommonBottomDialogListener() {
                        @Override
                        public void itemClick(int index) {
                            startActivity(new Intent(mContext, AddCasesPatientActivity.class));
                        }
                    });
                } else if (patient_list.size() < 3) {//就诊人列表不够三个时将新建就诊人显示出来

                    //构建弹出框数据源
                    List<String> datas = new ArrayList<>();
                    for (int i = 0; i < patient_list.size(); i++) {
                        datas.add(patient_list.get(i).getPatient().getEpName());
                    }
                    datas.add("新建就诊人");

                    CommonBottomDialog.showCommonBottomDialog(this, datas, new CommonBottomDialogListener() {
                        @Override
                        public void itemClick(int index) {
                            if (index == patient_list.size()) {
                                startActivity(new Intent(mContext, AddCasesPatientActivity.class));
                            } else {
                                index_patient = index;
                                patient_name.setText(patient_list.get(index).getPatient().getEpName());

                                //如果就诊人有医保卡号，将医保卡号显示到医保卡的输入位置，并且不可编辑
                                if (!TextUtils.isEmpty(patient_list.get(index).getPatient().getEpHiid())){
                                    medical_cardNumber.setText(patient_list.get(index).getPatient().getEpHiid());
                                    medical_cardNumber.setEnabled(false);
                                }
                            }

                        }
                    });
                } else {//就诊人是三个时不显示新建就诊人按钮
                    List<String> datas = new ArrayList<>();
                    for (int i = 0; i < patient_list.size(); i++) {
                        datas.add(patient_list.get(i).getPatient().getEpName());
                    }
                    CommonBottomDialog.showCommonBottomDialog(this, datas, new CommonBottomDialogListener() {
                        @Override
                        public void itemClick(int index) {
                            index_patient = index;
                            patient_name.setText(patient_list.get(index).getPatient().getEpName());
                            //如果就诊人有医保卡号，将医保卡号显示到医保卡的输入位置，并且不可编辑
                            if (!TextUtils.isEmpty(patient_list.get(index).getPatient().getEpHiid())){
                                medical_cardNumber.setText(patient_list.get(index).getPatient().getEpHiid());
                                medical_cardNumber.setEnabled(false);
                            }
                        }
                    });

                }
            }
        } else {
            ToastUtils.shortToast(mContext, response.getMessage());
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

    }
}

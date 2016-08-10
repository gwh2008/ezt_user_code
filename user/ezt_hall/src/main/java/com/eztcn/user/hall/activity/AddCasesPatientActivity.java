package com.eztcn.user.hall.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.MyPatientResponse;
import com.eztcn.user.hall.utils.CharacterTool;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.IdVerification;
import com.eztcn.user.hall.utils.MD5;
import com.eztcn.user.hall.utils.ToastUtils;
import com.eztcn.user.hall.utils.Tools;
import com.yljt.cascadingmenu.CascadingMenuPopWindow;
import com.yljt.cascadingmenu.DBhelper;
import com.yljt.cascadingmenu.interfaces.CascadingMenuViewOnSelectListener;
import com.yljt.model.Area;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * 添加就诊人页面
 * @author 蒙and LX
 */
public class AddCasesPatientActivity extends BaseActivity implements View.OnClickListener ,ResultSubscriber.OnResultListener{

    private Context context=AddCasesPatientActivity.this;
    private TextView right_top;
    private TextView patient_sex;
    private EditText patient_name_tx;
    private EditText health_insurance_number;
    private EditText patient_id_number;
    private TextView health_insurance_location;
    private EditText patient_mobile;
    private TextView get_code_hint_tx;
    private EditText verification_code;
    private RelativeLayout health_insurance_location_rl;
    private RelativeLayout choose_sex_rl;
    private AlertDialog.Builder builder;
    private ArrayList<Area> provinceList;
    // 两级联动菜单数据
    private CascadingMenuPopWindow cascadingMenuPopWindow = null;
    private DBhelper dBhelper;
    private String TAG="AddCasesPatientActivity";
    private  final int GET_VERIFY_CODE=0;
    private  final  int VERIFY_CODE=1;
    private  final int ADD_PATIENT=2;
    private  final int MODIFY_PATIENT=3;
    private  int  submit_status=0;//当前信息的状态
    private  int  sex_tag=0;//性别的标志。
    private String province_id="";
    private String  city_id="";
    private  String county_id="";
    private String patient_id="";//就诊人的id，用于修改就诊人信息。
    private MyPatientResponse myPatientResponse;//就诊人的bean。
    private  CountDownTimer timer=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int preView() {
        return R.layout.new_activity_add_cases_patient;
    }

    public void initView() {
          right_top=loadTitleBar(true, "新建就诊人", "保存");
          patient_sex= (TextView) this.findViewById(R.id.patient_sex);
          patient_name_tx= (EditText) this.findViewById(R.id.patient_name_tx);
          health_insurance_number= (EditText) this.findViewById(R.id.health_insurance_number);
          patient_id_number= (EditText) this.findViewById(R.id.patient_id_number);
          health_insurance_location= (TextView) this.findViewById(R.id.health_insurance_location);
          patient_mobile= (EditText) this.findViewById(R.id.patient_mobile);
          get_code_hint_tx= (TextView) this.findViewById(R.id.get_code_hint_tx);
          verification_code= (EditText) this.findViewById(R.id.verification_code);
          health_insurance_location_rl= (RelativeLayout) this.findViewById(R.id.health_insurance_location_rl);
          choose_sex_rl= (RelativeLayout) this.findViewById(R.id.choose_sex_rl);
          right_top.setOnClickListener(savePatientInfoOnClickListener);
          get_code_hint_tx.setOnClickListener(get_code_hint_txOnClickListener);
          health_insurance_location_rl.setOnClickListener(health_insurance_location_rlOnClickListener);
          choose_sex_rl.setOnClickListener(choose_sex_rlOnClickListener);
          patient_mobile.addTextChangedListener(patient_mobileTextChangedListener);
          setConfig();

    }
    /**
     * 设置一些数据配置。
     */
    private void setConfig() {
        //向三级menu添加地区数据
        dBhelper = new DBhelper(this);
        provinceList = dBhelper.getProvince();
        builder = new AlertDialog.Builder(this);
        Intent IntentData=getIntent();
        if(IntentData!=null){
            patient_id=IntentData.getStringExtra("patient_id");
            submit_status=IntentData.getIntExtra("submit_status",0);
            myPatientResponse= (MyPatientResponse) IntentData.getSerializableExtra("MyPatientResponse");
        }
        setPatientData();
    }
    /**
     * 设置原始的就诊人的信息。
     */
    private void setPatientData() {
        if(submit_status==1){
            //处于修改的状态。
            if(myPatientResponse!=null){

                patient_name_tx.setText(myPatientResponse.getPatient().getEpName());
                if("0".equals(myPatientResponse.getPatient().getEpSex())){
                    patient_sex.setText("男");
                    sex_tag=0;
                }else{
                    patient_sex.setText("女");
                    sex_tag=1;
                }
                patient_id_number.setText(myPatientResponse.getPatient().getEpPid());
                patient_mobile.setText(myPatientResponse.getPatient().getEpMobile());

                //如果就诊人是自己的话。就不让他修改相关信息。
                if(myPatientResponse.getFamily().getPatientId()==BaseApplication.patient.getId()){
                    patient_name_tx.setFocusable(false);
                    choose_sex_rl.setClickable(false);
                    patient_id_number.setFocusable(false);
                    patient_mobile.setFocusable(false);
                }
                health_insurance_number.setText(myPatientResponse.getPatient().getEpHiid());
                province_id=myPatientResponse.getPatient().getEpNativeProvince();
                city_id=myPatientResponse.getPatient().getEpNativecity();
                county_id=myPatientResponse.getPatient().getEpNativecounty();

                if(province_id!=null&&!"".equals(province_id)){
                    String provinceName= dBhelper.getProvinceName(province_id).getName();
                    String cityName=dBhelper.getCityName(city_id).getName();
                    String countyName=dBhelper.getCountyName(county_id).getName();
                    health_insurance_location.setText(provinceName+cityName+countyName);
                }
                ///进行省市区的查询，进行查询。
                patient_id_number.setEnabled(false);
            }
        }
    }

    @Override
    protected void initData() {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_confirm_appointment_select_person:
                break;
        }
    }

    /**
     *
     */
    TextWatcher patient_mobileTextChangedListener=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String mobile=s.toString().trim();

            if (mobile.length()!=11) {
                get_code_hint_tx.setTextColor(context.getResources().getColor(R.color.agreement_not_receive_text_color));
                get_code_hint_tx.setEnabled(false);
                return ;
            }
            get_code_hint_tx.setTextColor(context.getResources().getColor(R.color.border_line));
            get_code_hint_tx.setEnabled(true);
        }
    };
    // 级联菜单选择回调接口
    class NMCascadingMenuViewOnSelectListener implements
            CascadingMenuViewOnSelectListener {

        @Override
        public void getValue(Area area) {

            String code=area.getCode();
            String pcode=area.getPcode();
            Log.i("ppparea---------", pcode);//city的id
            Log.i("area---------", code);//区的id,
            //Todo 通过cityid反查询，province_id.
            Area city_bean=dBhelper.getCityName(pcode);

            Area province_bean=dBhelper.getProvinceName(city_bean.getPcode());

          //  Toast.makeText(context, "city_name-----"+city_bean.getName(), Toast.LENGTH_SHORT).show();

          //  Toast.makeText(context, "province_id-----"+city_bean.getPcode(), Toast.LENGTH_SHORT).show();

          //  Toast.makeText(context, "province_name-----"+province_bean.getName(), Toast.LENGTH_SHORT).show();

            //通过city的id查询市的名字，和省的名字以及省份的code。
            health_insurance_location.setText(province_bean.getName()+city_bean.getName()+area.getName());

            province_id=city_bean.getPcode();
            city_id=city_bean.getCode();
            county_id=area.getCode();

        }
    }
    /**
     * 地区的展示。
     */
    private void showPopMenu() {
        if (cascadingMenuPopWindow == null) {
            cascadingMenuPopWindow = new CascadingMenuPopWindow(
                    getApplicationContext(), provinceList);
            cascadingMenuPopWindow
                    .setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
            cascadingMenuPopWindow.showAsDropDown(health_insurance_location_rl, 5, 5);
        } else if (cascadingMenuPopWindow != null
                && cascadingMenuPopWindow.isShowing()) {
            cascadingMenuPopWindow.dismiss();
        } else {
            cascadingMenuPopWindow.showAsDropDown(health_insurance_location_rl, 5, 5);
        }
    }
    /**
     * 性别的选择
     */
    View.OnClickListener choose_sex_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String[] arrays = { "男", "女" };
            showSelector(arrays,1);
        }
    };
    /**
     * 社保所在地的选择。
     */
    View.OnClickListener health_insurance_location_rlOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showPopMenu();
        }
    };
    /**
     * 保存就诊人信息。
     */
    //医保卡号和医保所在地是选填，其余都是必填。
    View.OnClickListener savePatientInfoOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(judgeParams()){
                if(verification_code.getText().toString().length()==0){
                    ToastUtils.shortToast(context,"请输入验证码");
                }else{
                    verificationCode();
                }
            }
        }
    };
    /**
     * 获取验证码。
     */
    View.OnClickListener get_code_hint_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String phone=patient_mobile.getText().toString().trim();
            if(TextUtils.isEmpty(phone)){
                ToastUtils.shortToast(context,"请输入手机号");
                return;
            }
            if(phone.length()!=11){
                ToastUtils.shortToast(context,"请输入11为手机号");
                return ;
            }
            if(!Tools.isPhone(phone)){
                ToastUtils.shortToast(context,"请输入正确手机号");
                return;
            }
             timer = new CountDownTimer(60*1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    get_code_hint_tx.setClickable(false);
                    get_code_hint_tx.setText(millisUntilFinished / 1000 + "s");
                }
                @Override
                public void onFinish() {
                    get_code_hint_tx.setClickable(true);
                    get_code_hint_tx.setText("获取验证码");
                }
            };
            timer.start();
            getVerificationCode();
        }
    };
    /**
     * 选择性别窗口*/

    public void showSelector(String[] arrays, final int type) {
        builder.setItems(arrays, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    if (which == 0) {
                        patient_sex.setText("男");
                        sex_tag=0;
                    } else {
                        patient_sex.setText("女");
                        sex_tag=1;
                    }
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    /**
     * 获取验证码接口。
     */
    private void getVerificationCode() {

        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap();
        map.put("mobile",patient_mobile.getText().toString());
        map.put("hosId",0+"");
        String eztCode= MD5.MD5_32_16("ezt"+patient_mobile.getText().toString(),32);
        map.put("eztCode",eztCode);
        Request request=new Request();
        Map<String,String> params= request.getFormMap(map);
        helper.getVerificationCode(params,GET_VERIFY_CODE,this);

    }
    /**
     * 验证验证码接口。
     */
    private void verificationCode() {

        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap();
        map.put("mobile",patient_mobile.getText().toString());
        map.put("flag","A1");
        map.put("vcode",verification_code.getText().toString());
        Request request=new Request();
        Map<String,String> params= request.getFormMap(map);
        helper.VerifySmsCode(params,VERIFY_CODE,this);

    }
    /**
     * 判断参数。
     */
    private boolean judgeParams() {

        String name=patient_name_tx.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtils.shortToast(context,"请输入姓名");
            return false;
        }

        if(name.length() < 2 || name.length() > 4||(!CharacterTool.isChineseCharacter(name))){
            ToastUtils.shortToast(context,"请输入正确的姓名");
            return false;
        }
        if(patient_sex.getText().toString().length()==0){
            ToastUtils.shortToast(context,"请选择性别");
            return false;
        }
        String ic = patient_id_number.getText().toString();
        if (TextUtils.isEmpty(ic)) {
            Toast.makeText(mContext, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
          /*  if (!StringUtil.validateCard(ic)) {
                Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
                return false;
            }*/
        try {
            if (!"该身份证有效".equals(IdVerification.IDCardValidate(ic))) {
                Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(patient_mobile.getText().toString().length()==0){
            ToastUtils.shortToast(context,"请输入手机号");
            return false;
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
    }

    @Override
    public void onNext(IModel t, int requestType) {
        Log.i(TAG,"onNext");

        Response response= (Response) t;
       int number=Integer.valueOf( response.getNumber());

        switch (requestType){

            case GET_VERIFY_CODE :
                if(number==2000){
                    ToastUtils.shortToast(context,"验证码已经发送");
                }else{
                    ToastUtils.shortToast(context,"发送失败..."+response.getDetailMsg());
                }
            break;
            case VERIFY_CODE :
                if(number==2000){
                    //验证成功。
                    SavePatientInfo();
                }else{
                    ToastUtils.shortToast(context,"验证码验证失败"+response.getDetailMsg());
                }
            break;
            case ADD_PATIENT :

                if(number==2001){
                    ToastUtils.shortToast(context,"数据保存成功");
                    setResult(Constant.ADD_PATIENT_COMPLETE);
                    finish();
                }else{
                    ToastUtils.shortToast(context,"保存失败"+ response.getDetailMsg());

                }
            break;
            case  MODIFY_PATIENT:
                if(number==2002){
                    ToastUtils.shortToast(context,"就诊人信息修改成功");
                    setResult(Constant.ADD_PATIENT_COMPLETE);
                    finish();
                }else{
                    ToastUtils.shortToast(context,"修改失败..."+response.getDetailMsg());
                }
                break;
        }
    }
    /**
     * 保存就诊人信息。
     */
    private void SavePatientInfo() {

        // TODO: 2016/6/18  调试增加和修改就诊人信息接口。

        //默认是添加。
        if(submit_status==1){
            //modify
          modifyPatient();

        }else if(submit_status==0){
            //add
            addPatient();
        }
    }
    /**
     * 增加就诊人。
     */
    private void addPatient() {

        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap();
        map.put("userId", BaseApplication.patient.getUserId()+"");
        map.put("name",patient_name_tx.getText().toString().trim());
        map.put("sex",sex_tag+"");
        map.put("idno",patient_id_number.getText().toString().trim());
        map.put("idnoType","1");
        map.put("mobile",patient_mobile.getText().toString().trim());
       if(health_insurance_number.getText().toString().trim().length()!=0){
           map.put("hiid",health_insurance_number.getText().toString().trim());
       }
        if(health_insurance_location.getText().toString().trim().length()!=0){
            map.put("epNativeprovince",province_id);
            map.put("epNativecity",city_id);
            map.put("epNativecounty",county_id);
        }
        Request request=new Request();
        Map<String,String> params= request.getFormMap(map);
        helper.addPatientInfo(params,ADD_PATIENT,this);
    }
    /**
     * 修改就诊人信息。
     */
    private void modifyPatient() {

        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap();
        map.put("userId", BaseApplication.patient.getUserId()+"");
        map.put("id",patient_id);
        map.put("name",patient_name_tx.getText().toString().trim());
        map.put("sex",sex_tag+"");
        map.put("idno",patient_id_number.getText().toString().trim());
        map.put("idnoType","1");
        map.put("mobile",patient_mobile.getText().toString().trim());
        if(health_insurance_number.getText().toString().trim().length()!=0){
            map.put("hiid",health_insurance_number.getText().toString().trim());
        }
        if(health_insurance_location.getText().toString().trim().length()!=0){
            map.put("epNativeprovince",province_id);
            map.put("epNativecity",city_id);
            map.put("epNativecounty",county_id);
        }
        Request request=new Request();
        Map<String,String> params= request.getFormMap(map);
        helper.modifyPatientInfo(params,MODIFY_PATIENT,this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(timer!=null){
            timer.cancel();
        }
    }
}

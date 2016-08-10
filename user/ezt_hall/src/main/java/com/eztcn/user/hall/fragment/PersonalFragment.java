package com.eztcn.user.hall.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.dragoncard.ActivityDragonCard;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.hall.activity.MyAttentionActivity;
import com.eztcn.user.hall.activity.MyCaseCopyListActivity;
import com.eztcn.user.hall.activity.MyDrugListActivity;
import com.eztcn.user.hall.activity.MyServiceActivity;
import com.eztcn.user.hall.activity.PatientManagerActivity;
import com.eztcn.user.hall.activity.PersonInfoActivity;
import com.eztcn.user.hall.activity.dragonCard.MyDragonCardActivity;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.activity.loginSetting.RegisterActivity;
import com.eztcn.user.hall.activity.loginSetting.SettingActivity;
import com.eztcn.user.hall.common.DragonStatusSingle;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.ToastUtils;

/**
 * Created by lx on 2016/5/29.
 * 我的fragment
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private ImageView setting_icon;
    private ImageView personal_head;
    private TextView personal_name;
    private TextView personal_tel;
    private ImageView person_info_label;
    private TextView login_tx;
    private TextView register_tx;
    private TextView my_attention_num;

    public static PersonalFragment getInstance() {
        PersonalFragment fragment = new PersonalFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.new_fragment_personal, null);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }
    /**
     * 初始化View控件
     */
    private void initView(View view) {
        setting_icon= (ImageView) view.findViewById(R.id.setting_icon);
        personal_head= (ImageView) view.findViewById(R.id.personal_head);
        personal_name= (TextView) view.findViewById(R.id.personal_name);
        personal_tel= (TextView) view.findViewById(R.id.personal_tel);
        person_info_label= (ImageView) view.findViewById(R.id.person_info_label);
        login_tx= (TextView) view.findViewById(R.id.login_tx);
        register_tx= (TextView) view.findViewById(R.id.register_tx);
        my_attention_num= (TextView) view.findViewById(R.id.my_attention_num);
        view.findViewById(R.id.my_registration_rl).setOnClickListener(this);
        view.findViewById(R.id.my_reservation_drug_rl).setOnClickListener(this);
        view.findViewById(R.id.patient_manager_rl).setOnClickListener(this);
        view.findViewById(R.id.my_photo_copying_case_rl).setOnClickListener(this);
        view.findViewById(R.id.my_attention_rl).setOnClickListener(this);
        view.findViewById(R.id.dragonCard_image).setOnClickListener(this);
        view.findViewById(R.id.my_reservation_service_rl).setOnClickListener(this);
        setOnClickListener();
        if (BaseApplication.patient != null){
            if(!TextUtils.isEmpty(BaseApplication.patient.getEpPic())){
                GlideUtils.intoRound(getContext(), HTTPHelper.BASE_PATH_IMG_AVATAR + BaseApplication.patient.getEpPic(),0,personal_head);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_registration_rl://我的挂号页面
                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
             //   startActivity(new Intent(mContext, MyRegistrationListActivity.class));
                startActivity(new Intent(mContext, MyRecordActivity.class));
                break;
            case R.id.my_reservation_drug_rl://预约药品页面
                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
                startActivity(new Intent(mContext, MyDrugListActivity.class));
                break;
            case R.id.patient_manager_rl://就诊人管理页面

                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
                startActivity(new Intent(mContext, PatientManagerActivity.class));

                //跳转到以前的就诊人管理页面，以后要改
               // startActivity(new Intent(mContext, FamilyMemberActivity.class));
                break;

            case R.id.my_photo_copying_case_rl://复印病历页面
                startActivity(new Intent(mContext, MyCaseCopyListActivity.class));
                break;
            case R.id.my_attention_rl://我的关注页面

                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
                 startActivityForResult(new Intent(mContext, MyAttentionActivity.class),Constant.REFRESH_MY_ATTENTION_NUM);
                break;
            case R.id.dragonCard_image://龙卡页面
                if (BaseApplication.patient != null){
                    if (DragonStatusSingle.getInstance().isOpenDragon()){
                        startActivity(new Intent(mContext, ActivityDragonCard.class));
                    }else {
                        startActivity(new Intent(mContext, MyDragonCardActivity.class));
                    }
                }else {
                    hintToLogin(Constant.LOGIN_COMPLETE);
                }

                //跳转到以前的龙卡详情页面，以后要改
//                startActivity(new Intent(mContext, ActivityDragonCard.class));
                break;
            case R.id.my_reservation_service_rl://预约服务页面
                if(BaseApplication.patient==null){
                    hintToLogin(Constant.LOGIN_COMPLETE);
                    return;
                }
                startActivity(new Intent(mContext, MyServiceActivity.class));
                break;
        }
    }
    /**
     * 设置点击事件
     */
    private void setOnClickListener() {
        setting_icon.setOnClickListener(setting_iconOnClickListener);
        personal_head.setOnClickListener(personal_headOnClickListener);
        login_tx.setOnClickListener(login_txOnClickListener);
        register_tx.setOnClickListener(register_txOnClickListener);
    }

    @Override
    protected void onFragmentFirstResume() {

    }

    /**
     * 进行当前界面的数据的设置和更新。
     */
    @Override
    public void onResume() {
        super.onResume();


        // TODO: 2016/6/14  获取我的关注数，进行数据更新。
    }
    /**
     * 未登录的显示。
     */
    private void unLoginShow() {

        personal_name.setVisibility(View.GONE);
        personal_tel.setVisibility(View.GONE);
        person_info_label.setVisibility(View.GONE);
        login_tx.setVisibility(View.VISIBLE);
        register_tx.setVisibility(View.VISIBLE);
    }
    /**
     * login后的显示。
     */
    private void loginShow() {

        BaseApplication.patient= (PatientBean) FileUtils.getObject(getContext(),"patient");
        PatientBean patient=BaseApplication.patient;
        personal_name.setVisibility(View.VISIBLE);
        personal_tel.setVisibility(View.VISIBLE);
        if(patient.getEpName()==null){
            personal_name.setText("");
        }else{
            personal_name.setText(patient.getEpName()+"");
        }
        if(patient.getEpMobile()==null){
            personal_tel.setText("");
        }else{
            personal_tel.setText(patient.getEpMobile()+"");
        }
        person_info_label.setVisibility(View.VISIBLE);
        login_tx.setVisibility(View.GONE);
        register_tx.setVisibility(View.GONE);
    }
    /**
     * 刷新我的关注数量
     */
    public void refreshMyAttentionNum(int attentionNum){


        if(attentionNum==0){
            my_attention_num.setText("");
        }else {
            my_attention_num.setText(attentionNum+"");
        }
    }
    /**
     * 刷新个人数据
     *
     */
    public void refreshPersonData(){
        if(BaseApplication.patient!=null){
            loginShow();
        }else{
            unLoginShow();
        }
    }
    /**
     * 设置
     */
    View.OnClickListener  setting_iconOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(getActivity(), SettingActivity.class));
        }
    };
    /**
     * 个人信息
     */
    View.OnClickListener personal_headOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(BaseApplication.patient==null){
                hintToLogin(Constant.LOGIN_COMPLETE);
                return;
            }
            startActivityForResult(new Intent(getActivity(),PersonInfoActivity.class),Constant.REFRESH_PERSON_PAGE_INFO);
        }
    };
    /**
     * 登录
     */
    View.OnClickListener login_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivityForResult(new Intent(getActivity(), LoginActivity.class),Constant.LOGIN_COMPLETE);
        }
    };
    /**
     * 注册
     */
    View.OnClickListener  register_txOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(getActivity(), RegisterActivity.class));
        }
    };

    /**
     * 登录提示。
     */
    public void hintToLogin(final int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.item_login_dialog, null);
        TextView login = (TextView) dialogView.findViewById(R.id.dialog_login);
        TextView cancel = (TextView) dialogView
                .findViewById(R.id.dialog_cancel);
        dialog.show();
        dialog.setContentView(dialogView);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(getActivity(),LoginActivity.class), requestCode);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
                return false;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Constant.LOGIN_COMPLETE){
            refreshPersonData();
        }else if(requestCode==Constant.REFRESH_PERSON_PAGE_INFO){
            refreshPersonData();
        }else if(requestCode==Constant.REFRESH_MY_ATTENTION_NUM){

            //率先

        }
    }

}

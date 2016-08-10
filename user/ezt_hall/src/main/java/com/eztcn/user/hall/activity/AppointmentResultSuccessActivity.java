package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.BigDoctorList30Activity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.AttentionDoctorResponse;
import com.eztcn.user.hall.model.ResultResponse.CommitAppointmentSucessResponse;
import com.eztcn.user.hall.model.ResultResponse.DoctorDatasOneLevelResponse;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 预约成功页面
 *
 * @author 蒙
 */
public class AppointmentResultSuccessActivity extends BaseActivity implements View.OnClickListener
        , ResultSubscriber.OnResultListener{

    private TextView right_top;
    private TextView detail;
    private ImageView headpic1;
    private ImageView headpic2;
    private ImageView headpic3;
    private ImageView headpic4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_appointment_result;
    }

    public void initView() {
        right_top=loadTitleBar(false, "预约结果", "首页");
        right_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(mContext,MainActivity.class));
            }
        });
        headpic1=(ImageView)findViewById(R.id.new_activity_appointment_result_head1);
        headpic2=(ImageView)findViewById(R.id.new_activity_appointment_result_head2);
        headpic3=(ImageView)findViewById(R.id.new_activity_appointment_result_head3);
        headpic4=(ImageView)findViewById(R.id.new_activity_appointment_result_head4);
        detail=(TextView)findViewById(R.id.new_activity_appointment_result_detail);
        detail.setOnClickListener(this);

        findViewById(R.id.linearLayout_famous_doctor).setOnClickListener(this);
        findViewById(R.id.linearLayout_bed).setOnClickListener(this);
        findViewById(R.id.linearLayout_check).setOnClickListener(this);
        findViewById(R.id.linearLayout_drug).setOnClickListener(this);
        findViewById(R.id.linearLayout_copy_info).setOnClickListener(this);
    }
    CommitAppointmentSucessResponse sucessResponse;
    @Override
    protected void initData() {
        sucessResponse=(CommitAppointmentSucessResponse) getIntent().getSerializableExtra("data");

        getGuidePic();

    }
    /**
     * 获取助手头像
     */
    public void getGuidePic(){
        Request request=new Request();
        Map<String, String> params = new HashMap<>();
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getGuidePic(request.getFormMap(params), 111, this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.new_activity_appointment_result_detail://预约详情页面
                Record_Info record_info=new Record_Info();
                record_info.setBeginTime(sucessResponse.getBeginTime());
                record_info.setDept(sucessResponse.getDptName());
                record_info.setDeptId(Integer.parseInt(sucessResponse.getDptId()));
                record_info.setId(Integer.parseInt(sucessResponse.getRegId()));
                record_info.setEndTime(sucessResponse.getEndTime());
                record_info.setDiscribe(sucessResponse.getContent());
                record_info.setDoctorName(sucessResponse.getEdName());
                record_info.setDoctorLevel(Integer.parseInt(sucessResponse.getEdLevel()));
                record_info.setHospital(sucessResponse.getEhName());
                record_info.setPhone(sucessResponse.getMobile());
                record_info.setPhoto(sucessResponse.getPic());
                record_info.setPatientName(sucessResponse.getEpName());
                record_info.setIdCard(sucessResponse.getEpPid());
                record_info.setPayType("自费".equals(sucessResponse.getPayWay())?0:1);
                record_info.setRegTime(sucessResponse.getRegTime());
                record_info.setPlatformId(355);//手机端安卓端
                record_info.setDate("");

                try {
                    record_info.setRegDeptDocedFree(Double.parseDouble(sucessResponse.getEdFree()));
                }catch (Exception e){
                    record_info.setRegDeptDocedFree(-1);
                }

                record_info.setDoctorId(Integer.parseInt(sucessResponse.getDoctorId()));
                record_info.setLeaveSign(false);

                startActivity(new Intent(mContext,MyRegistrationDetailsActivity.class)
                        .putExtra("data",record_info).putExtra("from","successPage"));//预约详情页面需要的数据实体类
                break;
            case R.id.linearLayout_famous_doctor://大牌名医
                intent = new Intent(mContext, BigDoctorList30Activity.class);
                intent.putExtra("isChangedCity", false);//目前先默认没有选择新的城市，需要修改
                startActivity(intent);
                break;
            case R.id.linearLayout_bed://预约病床
                intent=new Intent(mContext,OrderBedNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.linearLayout_check://预约检查
                intent=new Intent(mContext,ChoiceHosActivity.class);
                intent.putExtra("isChangedCity", false);
                intent.putExtra("isOrderCheck", true);
                startActivity(intent);
                break;
            case R.id.linearLayout_drug://预约药品
                intent=new Intent(mContext,OrderDrugActivity.class);
                startActivity(intent);
                break;
            case R.id.linearLayout_copy_info:
                break;
        }
    }
    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response= (Response) t;
        if (response.isFlag()){
            if (requestType==111){//获取助手头像
                ArrayList<String> datas=(ArrayList<String>)response.getData();
                GlideUtils.intoRound(mContext,datas.get(0),0,headpic1
                        ,R.drawable.new_default_head_pic);
                GlideUtils.intoRound(mContext,datas.get(1),0,headpic2
                        ,R.drawable.new_default_head_pic);
                GlideUtils.intoRound(mContext,datas.get(2),0,headpic3
                        ,R.drawable.new_default_head_pic);
                GlideUtils.intoRound(mContext,datas.get(3),0,headpic4
                        ,R.drawable.new_default_head_pic);
            }
        }else{
            ToastUtils.shortToast(mContext,response.getMessage());
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

    @Override
    protected void onBack() {//不回调父类的该方法，阻止系统销毁该页面
    }

}

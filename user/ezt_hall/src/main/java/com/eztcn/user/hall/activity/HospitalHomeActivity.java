package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.ordercheck.ChoiceCheckItemActivity;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.HospitalDataResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 医院首页面
 *
 * @author 蒙
 */
public class HospitalHomeActivity extends BaseActivity implements View.OnClickListener, ResultSubscriber.OnResultListener {

    private ImageView hospital_pic;//医院图片
    private ImageView is_open_bed;//代表是否开通病床的图片
    private ImageView is_open_check;//代表是否开通检查的图片
    private ImageView is_open_drug;//代表是否开通药品的图片
    private TextView remainCount;//可预约量
    private TextView allCount;//总放号量

    private HospitalListResponse hospitalListResponse;//上个页面传过来的医院数据
    private HospitalDataResponse dataResponse = new HospitalDataResponse();//加载网络的医院全部数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_hospital_home;
    }

    public void initView() {
        findViewById(R.id.new_activity_hospital_home_intro).setOnClickListener(this);
        hospital_pic = (ImageView) findViewById(R.id.new_activity_hospital_home_hospital_pic);
        is_open_bed = (ImageView) findViewById(R.id.new_activity_hospital_home_is_open_bed);
        is_open_check = (ImageView) findViewById(R.id.new_activity_hospital_home_is_open_check);
        is_open_drug = (ImageView) findViewById(R.id.new_activity_hospital_home_is_open_drug);
        remainCount = (TextView) findViewById(R.id.new_activity_hospital_home_free_count);
        allCount = (TextView) findViewById(R.id.new_activity_hospital_home_appoint_count);


        findViewById(R.id.linearLayout_appointment).setOnClickListener(this);


    }

    @Override
    protected void initData() {
        hospitalListResponse = (HospitalListResponse) getIntent().getSerializableExtra("hospital");
        loadTitleBar(true, hospitalListResponse.getEhName(), null);
        remainCount.setText(hospitalListResponse.getRpNum());
        allCount.setText(hospitalListResponse.getRpMainNum());

        //图片地址需要按照这个规则进行拼接
        GlideUtils.into(mContext, HTTPHelper.HOSPITAL_PIC + "hosView" + hospitalListResponse.getId() + ".jpg", hospital_pic, R.drawable.new_hospital_default);

        getHospitalData();
    }

    /**
     * 获取医院的数据
     */
    public void getHospitalData() {
        showProgressDialog("正在加载...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("hospitalId", hospitalListResponse.getId());
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).getHospitalData(request.getFormMap(params), 111, this);

    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                dataResponse = (HospitalDataResponse) response.getData();
                if (dataResponse == null) {
                    dataResponse = new HospitalDataResponse();
                }

                //根据接口的数据，判断开通图片是否显示
                if ("0".equals(dataResponse.getEhIsOpenBed())) {
                    is_open_bed.setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.linearLayout_bed).setOnClickListener(this);
                }
                if ("0".equals(dataResponse.getEhIsOpenOrderCheck())) {
                    is_open_check.setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.linearLayout_check).setOnClickListener(this);
                }
                if ("0".equals(dataResponse.getSubscribeDrug())) {
                    is_open_drug.setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.linearLayout_drug).setOnClickListener(this);
                }
            }
        } else {
            ToastUtils.shortToast(mContext, response.getDetailMsg());
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        Hospital hospital;
        switch (v.getId()) {
            case R.id.new_activity_hospital_home_intro:
                startActivity(new Intent(mContext, HospitalSimpleIntroActivity.class)
                        .putExtra("hospital", dataResponse));
                break;
            case R.id.linearLayout_appointment:
                finish();
                break;
            case R.id.linearLayout_check://预约检查
                intent = new Intent(mContext, ChoiceCheckItemActivity.class);
                hospital = new Hospital();
                hospital.setId(Integer.parseInt(hospitalListResponse.getId()));
                hospital.sethName(hospitalListResponse.getEhName());
                intent.putExtra("hospital", hospital);
                startActivity(intent);
                break;
            case R.id.linearLayout_bed://预约病床
                intent = new Intent(mContext, ChoiceDeptByHosActivity.class);
                hospital = new Hospital();
                hospital.setId(Integer.parseInt(hospitalListResponse.getId()));
                intent.putExtra("hospital", hospital);
                intent.putExtra("isOrderBed", true);
                startActivity(intent);
                break;
            case R.id.linearLayout_drug://预约药品

                break;
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

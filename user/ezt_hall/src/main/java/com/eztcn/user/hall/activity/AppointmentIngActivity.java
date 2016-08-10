package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ant.liao.GifView;
import com.eztcn.user.R;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.CommitAppointmentDataRequest;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.CommitAppointmentSucessResponse;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 正在预约页面
 *
 * @author 蒙
 */
public class AppointmentIngActivity extends BaseActivity implements View.OnClickListener,
        ResultSubscriber.OnResultListener{

    private GifView gifView;
    private CommitAppointmentDataRequest dataRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTitleBar(true, "预约进度", null);
    }

    @Override
    protected int preView() {
        return R.layout.new_activity_isappointmenting;
    }

    public void initView() {
        gifView=(GifView) findViewById(R.id.gifView);
        gifView.setGifImageType(GifView.GifImageType.COVER);
        gifView.setGifImage(R.drawable.new_isappointmenting);
        gifView.setDrawingCacheEnabled(false);
    }
    @Override
    protected void initData() {
        dataRequest=(CommitAppointmentDataRequest) getIntent().getSerializableExtra("data");
        commitCode();
    }
    /**
     *
     */
    public void commitCode() {

        Request request = new Request();
        Map<String, String> params = new HashMap<>();
        params.put("operateUserId",dataRequest.getOperateUserId());
        params.put("poolId",dataRequest.getPoolId());
        params.put("pfId",dataRequest.getPfId());
        params.put("sourcePfId",dataRequest.getSourcePfId());
        params.put("cartoonNum",dataRequest.getCartoonNum());
        params.put("cardNum",dataRequest.getCardNum());
        params.put("ip",dataRequest.getIp());
        params.put("regMark",dataRequest.getRegMark());
        params.put("mobile",dataRequest.getMobile());
        params.put("idno",dataRequest.getIdno());
        params.put("patientId",dataRequest.getPatientId());
        params.put("payWay",dataRequest.getPayWay());
        params.put("isFirst",dataRequest.getIsFirst());
        params.put("epHiid",dataRequest.getMedicareCardNum());
        params.put("visitCardNum",dataRequest.getVisitCardNum());
        params.put("code",dataRequest.getCode());

        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).validationPhoneCode(request.getFormMap(params), 111, this);

    }
    @Override
    public void onNext(IModel t, int requestType) {
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {
                CommitAppointmentSucessResponse sucessResponse=(CommitAppointmentSucessResponse) response.getData();
                startActivity(new Intent(mContext,AppointmentResultSuccessActivity.class)
                        .putExtra("data",sucessResponse));
            }
        } else {
            if (response.getNumber().equals("5031")){//验证码错误
                ToastUtils.shortToast(mContext,"请重新输入验证码");
            }else {//其他失败
                startActivity(new Intent(mContext,AppointmentResultFailureActivity.class)
                        .putExtra("msg",response.getDetailMsg()));
            }
        }
        finish();
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {

    }

    @Override
    public void onError(int requestType) {
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_activity_confirm_appointment_commit:
                break;
        }
    }
}

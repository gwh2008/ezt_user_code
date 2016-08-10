package com.eztcn.user.hall.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.adapter.MyAttentionAdapter;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.config.Config;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.CommonDialogOkListener;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.MyHotDoctorRequest;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.model.ResultResponse.MyAttentionResponse;
import com.eztcn.user.hall.model.bean.HosDeptDocBean;
import com.eztcn.user.hall.model.bean.MyAttention;
import com.eztcn.user.hall.utils.CommonDialog;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by lx on 2016/6/3.
 * 我的关注界面。
 */
public class MyAttentionActivity extends BaseActivity implements ResultSubscriber.OnResultListener, PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener {

    private Context context = MyAttentionActivity.this;
    private PullToRefreshListView my_attention_listView;
    private int pageSize = Config.PAGE_SIZE;//每页的数量
    private int page = 1;//当前页
    private String TAG = "MyAttentionActivity";
    private final int MY_ATTENTION = 0;
    private MyAttentionAdapter attentionAdapter;
    Subscriber mSubscriber;
    private List<MyAttention> my_Attention_list;
    private ImageView no_data_image;


    @Override
    protected int preView() {
        return R.layout.new_activity_my_attention;
    }

    @Override
    protected void initView() {
        loadTitleBar(true, "我的关注", null);
        my_attention_listView = (PullToRefreshListView) this.findViewById(R.id.my_attention_listView);
        no_data_image= (ImageView) this.findViewById(R.id.no_data_image);
        my_attention_listView.setEmptyView(no_data_image);
        setAdapterConfig();
    }

    /**
     * 设置adapter.
     */
    private void setAdapterConfig() {
        attentionAdapter = new MyAttentionAdapter(this);
        my_attention_listView.setAdapter(attentionAdapter);
        my_attention_listView.setCanLoadMore(true);
        my_attention_listView.setCanRefresh(true);
        my_attention_listView.setAutoLoadMore(true);
        my_attention_listView.setMoveToFirstItemAfterRefresh(false);
        my_attention_listView.setDoRefreshOnUIChanged(false);
        my_attention_listView.setOnLoadListener(this);
        my_attention_listView.setOnRefreshListener(this);

        my_attention_listView.setOnItemClickListener(my_attention_listViewOnItemClickListener);

    }

    @Override
    protected void initData() {
        getMyAttentionData();
    }

    /**
     * 获取我的关注医生列表。
     */
    private void getMyAttentionData() {

        showProgressDialog("加载中...");
        HTTPHelper helper = HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap<>();
        map.put("page",page+"");
        map.put("rowsPerPage",pageSize+"");
        map.put("userId",BaseApplication.patient.getUserId()+"");
        map.put("ecType","1");
        mSubscriber = helper.postMyAttentionList(map, MY_ATTENTION, this);
    }

    /**
     * 请求数据结果。
     *
     * @param requestType
     */
    @Override
    public void onStart(int requestType) {
        Log.i(TAG, "start");
    }

    @Override
    public void onCompleted(int requestType) {
        Log.i(TAG, "onCompleted");
        dismissProgressDialog();
    }

    @Override
    public void onError(int requestType) {
        Log.i(TAG, "onError----mya");
        dismissProgressDialog();
    }

    @Override
    public void onNext(IModel t, int requestType) {
        Log.i(TAG, "onNext");
        switch (requestType) {

            case MY_ATTENTION:
                Response<MyAttentionResponse> response = (Response<MyAttentionResponse>) t;
                MyAttentionResponse attention = response.getData();
                List<MyAttention> attention_list = attention.getRows();
                page = attention.getPage();//当前页数。
                List<MyAttention> myAttentionData = new ArrayList<>();
                if (page == 1) {
                    my_attention_listView.setAutoLoadMore(false);
                    my_attention_listView.onRefreshComplete();
                    //在第一页。
                    if (attention_list != null && attention_list.size() != 0) {

                        myAttentionData = attention_list;
                        my_Attention_list = attention_list;

                    } else {
                        ToastUtils.shortToast(context, "暂无数据...");
                    }
                } else {
                    //加载更多。
                    myAttentionData = attentionAdapter.getList();
                    if (myAttentionData == null || myAttentionData.size() <= 0)
                        myAttentionData = attention_list;
                    else
                        myAttentionData.addAll(attention_list);
                    my_attention_listView.onLoadMoreComplete();
                }
                attentionAdapter.setList(myAttentionData);
                my_Attention_list = myAttentionData;
                break;
            case 222:
                Response response2 = (Response) t;
                if ("2000".equals(response2.getNumber())) {
                    //信息已经完善了
                    HosDeptDocBean deptDocBean=my_Attention_list.get(index).getEztHosDeptDocBean();
                    //对天津市肿瘤医院进行标记
                    if ("1".equals(deptDocBean.getEhIsneedPatientCard())){
                        Constant.IS_SPECIAL_HOSPITAL=true;
                    }else{
                        Constant.IS_SPECIAL_HOSPITAL=false;
                    }
                    //构建选择预约时间需要的数据模型
                    DoctorListResponse doctorListResponse=new DoctorListResponse();
                    doctorListResponse.setDocId(deptDocBean.getDocId()+"");
                    doctorListResponse.setDocName(deptDocBean.getEdName());
                    doctorListResponse.setLevelId(deptDocBean.getEdLevel()+"");
                    doctorListResponse.setLevelName(deptDocBean.getEdLevelName());
                    doctorListResponse.setHospitalId(deptDocBean.getHid()+"");
                    doctorListResponse.setHospitalName(deptDocBean.getEhName());
                    doctorListResponse.setDeptId(deptDocBean.getDeptId()+"");
                    doctorListResponse.setDptName(deptDocBean.getDptame());
                    doctorListResponse.setGoodAt(deptDocBean.getEdGoodat());
                    doctorListResponse.setOrderNum(0);
                    doctorListResponse.setDocPic(Config.DOC_PHOTO+deptDocBean.getEdPic());
                    doctorListResponse.setDocSex(deptDocBean.getEdSex()+"");
                   /* startActivity(new Intent(mContext,SelectAppointmentTimeActivity.class)
                            .putExtra("hospital",doctorListResponse));*/
                    startActivityForResult(new Intent(mContext,SelectAppointmentTimeActivity.class)
                            .putExtra("hospital",doctorListResponse), com.eztcn.user.hall.utils.Constant.DOCTOR_MAIN_PAGE);

                } else {//信息没有完善
                    CommonDialog.showCommonDialog(MyAttentionActivity.this,
                            "请先完善您的个人信息", "知道了", new CommonDialogOkListener() {
                                @Override
                                public void onClick() {
                                    startActivity(new Intent(mContext, CompletePersonalInfoActivity.class));//去完善个人信息页面
                                }
                            }, false);
                }
                break;


        }
    }
    private int index=-1;
    /**
     * 访问接口判断用户信息是否完善
     */
    public void validationPersonalInfo() {
       // showProgressDialog("验证信息中...");
        Request request = new Request();
        Map<String, String> params = new HashMap<>();

        params.put("userId", ((PatientBean) FileUtils.getObject(mContext, "patient")).getUserId() + "");//用户id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).validationPersonalInfo(request.getFormMap(params), 222, this);
    }

    /**
     * item的点击事件。
     */
    PullToRefreshListView.OnItemClickListener my_attention_listViewOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            index=position-1;
            validationPersonalInfo();
        }
    };

    /**
     * 加载更多。
     */
    @Override
    public void onLoadMore() {
        my_attention_listView.setAutoLoadMore(false);
        page++;
        getMyAttentionData();
    }

    /**
     * 下拉刷新。
     */

    @Override
    public void onRefresh() {
        page = 1;
        my_attention_listView.setAutoLoadMore(true);
        getMyAttentionData();
    }

    @Override
    protected void onBack() {
        super.onBack();
        dismissProgressDialog();
        setResult(com.eztcn.user.hall.utils.Constant.REFRESH_MY_ATTENTION_NUM);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== com.eztcn.user.hall.utils.Constant.DOCTOR_MAIN_PAGE){
            getMyAttentionData();
        }
    }
}

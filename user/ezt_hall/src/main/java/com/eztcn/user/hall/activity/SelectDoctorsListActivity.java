package com.eztcn.user.hall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.common.Constant;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.interfaces.CommonDialogOkListener;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.Request;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.DoctorListResponse;
import com.eztcn.user.hall.model.ResultResponse.HospitalListResponse;
import com.eztcn.user.hall.utils.CommonDialog;
import com.eztcn.user.hall.utils.FileUtils;
import com.eztcn.user.hall.utils.GlideUtils;
import com.eztcn.user.hall.utils.LogUtils;
import com.eztcn.user.hall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 选择医生页面
 *
 * @author 蒙
 */
public class SelectDoctorsListActivity extends BaseActivity implements View.OnClickListener ,
        ResultSubscriber.OnResultListener{
    private ListView listView;
    private Adapter adapter = new Adapter();
    private TextView right_top;//右上角的筛选按钮
    private String registerType="";//号源类型
    private String docLevel="";//医生级别
    private ArrayList<DoctorListResponse> datas=new ArrayList<>();

    private ImageView search_hospital_failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int preView() {
        return R.layout.new_activity_selectdoctors;
    }

    public void initView() {
        right_top = loadTitleBar(true, "选择医生", "筛选");
        right_top.setTextColor(getResources().getColor(R.color.border_line));
        right_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectDoctorRuleActivity.class);
                startActivityForResult(intent, 111);
            }
        });
        listView = (ListView) findViewById(R.id.listView);

        search_hospital_failure=(ImageView)findViewById(R.id.search_hospital_failure);
    }
private int index_listview=-1;
    @Override
    protected void initData() {
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index_listview=position;
                if (null==FileUtils.getObject(mContext,"patient")){//如果没有登录，就去登录页面
                    FileUtils.saveBooleanBySp(mContext,"isSkipMain",true);//登录成功后回到这个页面，不跳到主页
                    startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                validationPersonalInfo();
            }
        });

        getDoctorListData(docLevel,registerType,"0");

        listView.setEmptyView(search_hospital_failure);

    }

    @Override
    public void onClick(View v) {
    }

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null || convertView.getTag() == null) {
                convertView = getLayoutInflater().inflate(R.layout.new_select_doctors_list_item, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_name);
                holder.good_at = (TextView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_good_at);
                holder.department_name = (TextView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_department_name);
                holder.head = (ImageView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_headPic);
                holder.isCan_appointment = (ImageView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_isCan_appointment);
                holder.doctor_type = (ImageView) convertView.findViewById(R.id.new_activity_select_doctors_list_item_doctor_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DoctorListResponse doctorListResponse=datas.get(position);
            holder.name.setText(TextUtils.isEmpty(doctorListResponse.getDocName())?"普通医生":doctorListResponse.getDocName());
            holder.department_name.setText(doctorListResponse.getDptName());
            holder.good_at.setText((TextUtils.isEmpty(doctorListResponse.getGoodAt())?"":"擅长："+doctorListResponse.getGoodAt()));
            if ("0".equals(doctorListResponse.getDocSex())){//性别为男
                GlideUtils.intoRound(SelectDoctorsListActivity.this,doctorListResponse.getDocPic(),0, holder.head
                            ,R.drawable.new_default_doctor_head_man);

            }else{//性别为女
                GlideUtils.intoRound(SelectDoctorsListActivity.this,doctorListResponse.getDocPic(),0, holder.head
                            ,R.drawable.new_default_doctor_head_women);
            }
            if (doctorListResponse.getOrderNum()>0){//是否约满
                holder.isCan_appointment.setImageResource(R.drawable.new_appotinment_logo);
            }else{
                holder.isCan_appointment.setImageResource(R.drawable.new_appointment_not_logo);
            }

            //医生级别的加载
            if (doctorListResponse.getLevelName().contains("主治")){
                holder.doctor_type.setImageResource(R.drawable.new_attending_doctor_orange);
            }else if (doctorListResponse.getLevelName().contains("副")){
                holder.doctor_type.setImageResource(R.drawable.new_chief_doctor_second_orange);
            }else if (doctorListResponse.getLevelName().contains("主任")){
                holder.doctor_type.setImageResource(R.drawable.new_chief_doctor_orange);
            }

            return convertView;
        }

        class ViewHolder {
            private TextView name;//医生名字
            private ImageView head;//医生头像
            private ImageView doctor_type;//医生级别
            private ImageView isCan_appointment;//是否约满
            private TextView department_name;//科室名称
            private TextView good_at;//擅长
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==111){//筛选页面返回
            registerType=data.getStringExtra("registerType");
            docLevel=data.getStringExtra("docLevel");
            getDoctorListData(docLevel,registerType,"1");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取医生列表数据
     * @param docLevel 医生级别
     * @param registerType 号源类型
     * @param accessType 是否默认
     */
    public void getDoctorListData(String docLevel,String registerType,String accessType){
        showProgressDialog("正在加载医生...");
        Request request=new Request();
        Map<String, String> params = new HashMap<>();
        params.put("accessType",accessType);//是否显示默认
        params.put("docLevel",docLevel);//医生级别
        params.put("registerType",registerType);//号源类别
        params.put("deptId",getIntent().getStringExtra("departmentId"));//科室id
        params.put("pfId", Constant.PLAT_ID);//平台id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.PASS).getDoctorListData(request.getFormMap(params), 111, this);

    }

    /**
     * 访问接口判断用户信息是否完善
     */
    public void validationPersonalInfo(){
        showProgressDialog("验证信息中...");
        Request request=new Request();
        Map<String, String> params = new HashMap<>();

        params.put("userId",((PatientBean)FileUtils.getObject(mContext,"patient")).getUserId()+"");//用户id
        HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC).validationPersonalInfo(request.getFormMap(params), 222, this);
    }

    @Override
    public void onNext(IModel t, int requestType) {
        dismissProgressDialog();
        Response response = (Response) t;
        if (response.isFlag()) {
            if (requestType == 111) {//获取列表数据
                datas = (ArrayList<DoctorListResponse>) response.getData();
                if (datas==null){
                    datas=new ArrayList<>();
                }
                adapter.notifyDataSetChanged();
            }
            if (requestType == 222) {//个人信息的验证结果
                if ("2000".equals(response.getNumber())){//信息已经完善了
                    startActivity(new Intent(mContext, SelectAppointmentTimeActivity.class)
                            .putExtra("hospital",datas.get(index_listview)));
                }else{//信息没有完善
                    CommonDialog.showCommonDialog(SelectDoctorsListActivity.this,
                            "请先完善您的个人信息", "知道了", new CommonDialogOkListener() {
                        @Override
                        public void onClick() {
                            startActivity(new Intent(mContext, CompletePersonalInfoActivity.class));//去完善个人信息页面
                        }
                    },false);
                }
            }
        } else {
            ToastUtils.shortToast(mContext, response.getDetailMsg());
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

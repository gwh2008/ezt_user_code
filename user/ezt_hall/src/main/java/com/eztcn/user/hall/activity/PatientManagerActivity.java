package com.eztcn.user.hall.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.hall.activity.loginSetting.CompletePersonalInfoActivity;
import com.eztcn.user.hall.adapter.PatientAdapter;
import com.eztcn.user.hall.common.ResultSubscriber;
import com.eztcn.user.hall.http.HTTPHelper;
import com.eztcn.user.hall.model.IModel;
import com.eztcn.user.hall.model.Response;
import com.eztcn.user.hall.model.ResultResponse.MyPatientResponse;
import com.eztcn.user.hall.utils.Constant;
import com.eztcn.user.hall.utils.ToastUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Subscriber;
/**
 * Created by lx on 2016/6/2.
 * 就诊人管理界面。
 */
public class PatientManagerActivity extends BaseActivity implements  ResultSubscriber.OnResultListener{


    private Context context=PatientManagerActivity.this;
    private ListView   my_patient_listView;
    private ImageView  top_right;
    private Subscriber mSubscriber;
    private final int MY_PATIENT_LIST=0;
    private String TAG="PatientManagerActivity";
    private PatientAdapter patientAdapter;
    private List<MyPatientResponse> patient_list;
    private AlertDialog.Builder delete_Dialog;
    private final int  DELETE_PATIENT=3;
    private  int position=-1;

    @Override
    protected int preView() {
        return R.layout.new_activity_patient_manager;
    }
    @Override
    protected void initView() {
        top_right=loadTitleBar(true,"就诊人管理",R.drawable.new_add_icon);
        top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("submit_status",0);
                intent.setClass(context,AddCasesPatientActivity.class);
                startActivityForResult(intent, Constant.ADD_PATIENT_COMPLETE);
            }
        });
        my_patient_listView= (ListView) this.findViewById(R.id.my_patient_listView);
        my_patient_listView.setOnItemClickListener(my_patient_listViewOnItemClickListener);
        my_patient_listView.setOnItemLongClickListener(my_patient_listViewOnItemLongClickListener);
    }
    @Override
    protected void initData() {
        patientAdapter=new PatientAdapter(this);
        my_patient_listView.setAdapter(patientAdapter);
        getMyPatient();
    }
    /**
     * 获取我的就诊人列表。
     */
    private void getMyPatient() {

        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        Map<String,String> map=new HashMap();
        map.put("userId",BaseApplication.patient.getUserId()+"");
        mSubscriber=helper.getPatientList(map,MY_PATIENT_LIST,this);
        showProgressDialog("数据加载中...");
    }

    @Override
    public void onStart(int requestType) {

    }

    @Override
    public void onCompleted(int requestType) {
        Log.i(TAG,"onCompleted");
        dismissProgressDialog();
    }

    @Override
    public void onError(int requestType) {
        Log.i(TAG,"onError");
        dismissProgressDialog();
    }

    @Override
    public void onNext(IModel t, int requestType) {

        dismissProgressDialog();
        switch(requestType){

            case MY_PATIENT_LIST:
                Response response= (Response) t;
                patient_list= (List<MyPatientResponse>) response.getData();
                if(patient_list==null||patient_list.size()==0){
                    ToastUtils.shortToast(context,"暂无数据");
                }else{
                    int user_id=BaseApplication.patient.getUserId();
                 /*  for (int i=0;i<patient_list.size();i++){
                       if(user_id==patient_list.get(i).getPatient().getUserId()){
                           patient_list.remove(i);
                           break;
                       }
                   }*/
                    if(patient_list.size()==0){

                        ToastUtils.shortToast(context,"暂无数据");
                    }else{
                        patientAdapter.setList(patient_list);
                    }
                }
            break;

            case  DELETE_PATIENT:
                //删除就诊人。
                Response response2= (Response) t;
                boolean flag=response2.isFlag();
                if(flag){

                    if(position!=-1){
                        patient_list.remove(position);
                        patientAdapter.notifyDataSetChanged();
                        ToastUtils.shortToast(context,"删除成功...");
                    }
                }else{
                    ToastUtils.shortToast(context,response2.getDetailMsg());
                }
            break;
        }
    }
    AdapterView.OnItemClickListener  my_patient_listViewOnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


              if(TextUtils.isEmpty(BaseApplication.patient.getEpPid())){
                    ToastUtils.shortToast(context,"请完善个人信息");
                    startActivity(new Intent(context, CompletePersonalInfoActivity.class));
                    return;
                }
            Intent intent=new Intent();
            intent.putExtra("patient_id",patient_list.get(position).getFamily().getId()+"");
            intent.putExtra("submit_status",1);
            intent.putExtra("MyPatientResponse", patient_list.get(position));
            intent.setClass(context,AddCasesPatientActivity.class);
            startActivityForResult(intent, Constant.ADD_PATIENT_COMPLETE);

        }
    };
    /**
     * 长按item删除
     */

    AdapterView.OnItemLongClickListener  my_patient_listViewOnItemLongClickListener=new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            showDeleteDialog(position);
            return true;
        }
    };
    /**
     * 弹出删除对话框。
     */
    private void showDeleteDialog(final  int position) {

        delete_Dialog = new AlertDialog.Builder(context, R.style.dialog);
        LayoutInflater Inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = Inflater.inflate(R.layout.new_item_delete_patient_dialog, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final AlertDialog dialog = delete_Dialog.create();
        // 默认在中间，下面3行是从底部弹出的动画，
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
        // 设置 对话框显示横向充满屏幕。。
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow()
                .getAttributes(); // 获取对话框当前的参数值
        //  p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.5
        // p.alpha=0.0f;
        dialog.getWindow().setAttributes(p); // 设置生效
        dialog.setContentView(view);
        TextView delete = (TextView) view.findViewById(R.id.delete);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.getBackground().setAlpha(230);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BaseApplication.patient.getId()==patient_list.get(position).getFamily().getPatientId()){
                    ToastUtils.shortToast(context,"自己不可以删除");
                    return;
                }else{
                    deletePatient(position);
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 删除就诊人的接口。
     */
    private void deletePatient(int position) {

        Map<String,String> map=new HashMap<>();
        map.put("userId",BaseApplication.patient.getUserId()+"");
        map.put("id",patient_list.get(position).getFamily().getId()+"");
        HTTPHelper helper= HTTPHelper.getInstance(HTTPHelper.URL_TYPE.MC);
        helper.deletePatient(map,DELETE_PATIENT,this);
        this.position=position;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Constant.ADD_PATIENT_COMPLETE){
            getMyPatient();
        }
    }
}

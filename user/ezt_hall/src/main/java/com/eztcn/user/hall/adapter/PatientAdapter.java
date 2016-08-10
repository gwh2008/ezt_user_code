package com.eztcn.user.hall.adapter;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.hall.model.ResultResponse.MyPatientResponse;

import java.util.Calendar;

/**
 * Created by LX on 2016/6/17.
 * 我的就诊人的adapter。
 */
public class PatientAdapter extends BaseArrayListAdapter<MyPatientResponse> {

    private Activity context;

    public PatientAdapter(Activity context) {
        super(context);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= context.getLayoutInflater().inflate(R.layout.new_item_patient_manager,null);
            viewHolder.colorized_view=convertView.findViewById(R.id.colorized_view);
            viewHolder.id_number_tx= (TextView) convertView.findViewById(R.id.id_number_tx);
            viewHolder.patient_age= (TextView) convertView.findViewById(R.id.patient_age);
            viewHolder.patient_name= (TextView) convertView.findViewById(R.id.patient_name);
            viewHolder.patient_sex= (ImageView) convertView.findViewById(R.id.patient_sex);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        MyPatientResponse patient_manager= mList.get(position);

        viewHolder.patient_name.setText(patient_manager.getPatient().getEpName());
        if(TextUtils.isEmpty(patient_manager.getPatient().getEpAge())&& BaseApplication.patient.getEpPid()!=null){

            String year=getBirthDay(BaseApplication.patient.getEpPid());
            int age=   getAge(year);
            viewHolder.patient_age.setText(age+"");
        }else if(!TextUtils.isEmpty(patient_manager.getPatient().getEpAge())){
            viewHolder.patient_age.setText(patient_manager.getPatient().getEpAge()+"");
        }else {
            viewHolder.patient_age.setText("");
        }
        if("0".equals(patient_manager.getPatient().getEpSex())){
            //男
            viewHolder.patient_sex.setImageResource(R.drawable.new_boy_icon);
            viewHolder.colorized_view.setBackgroundColor(mContext.getResources().getColor(R.color.blue_color));
        }else{
            viewHolder.patient_sex.setImageResource(R.drawable.new_girl_icon);
            viewHolder.colorized_view.setBackgroundColor(mContext.getResources().getColor(R.color.new_red));
        }
        viewHolder.id_number_tx.setText(patient_manager.getPatient().getEpPid());

        return convertView;
    }
    /**
     * 计算年龄
     */
    private String getBirthDay(String idNumber) {

        if(idNumber.length()!=18){
            return "";
        }
        return  idNumber.substring(6,10)+"";
    }

    private int getAge(String year){

        Calendar a=Calendar.getInstance();
       int currentYear= a.get(Calendar.YEAR);
        int yy=Integer.valueOf(year);
        return currentYear-yy;
    }
    static class ViewHolder{

        TextView patient_name;
        TextView patient_age;
        TextView id_number_tx;
        ImageView patient_sex;
        View colorized_view;
    }
}

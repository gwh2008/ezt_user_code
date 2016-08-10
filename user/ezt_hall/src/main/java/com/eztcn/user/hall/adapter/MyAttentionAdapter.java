package com.eztcn.user.hall.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.hall.config.Config;
import com.eztcn.user.hall.model.bean.MyAttention;
import com.eztcn.user.hall.utils.GlideUtils;

/**
 * Created by lx on 2016/6/16.
 * 我的关注的adapter.
 *
 */
public class MyAttentionAdapter extends BaseArrayListAdapter<MyAttention>{


    public MyAttentionAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mContext.getLayoutInflater().inflate(
                    R.layout.new_item_my_attention, null);

            holder.doctor_head = (ImageView) convertView
                    .findViewById(R.id.doctor_head);// 医生头像
            holder.doctor_name = (TextView) convertView
                    .findViewById(R.id.doctor_name);// 医生名称
            holder.doctor_label = (TextView) convertView
                    .findViewById(R.id.doctor_label);// 医生职位
            holder.doctor_department = (TextView) convertView
                    .findViewById(R.id.doctor_department);//医院加科室

             holder.doctor_be_good_at_skill = (TextView) convertView
                    .findViewById(R.id.doctor_be_good_at_skill);//医生擅长。

              holder.be_good_at_tx = (TextView) convertView
                    .findViewById(R.id.be_good_at_tx);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyAttention attention_doc = mList.get(position);
        switch (attention_doc.getEztHosDeptDocBean().getEdSex()){

            case 0:
                GlideUtils.intoRound(mContext, Config.DOC_PHOTO+attention_doc.getEztHosDeptDocBean().getEdPic(),0,holder.doctor_head,R.drawable.new_default_doctor_head_man);
            break;
            case  1:
                GlideUtils.intoRound(mContext, Config.DOC_PHOTO+attention_doc.getEztHosDeptDocBean().getEdPic(),0,holder.doctor_head,R.drawable.new_default_doctor_head_women);
            break;

        }
        GlideUtils.intoRound(mContext, Config.DOC_PHOTO+attention_doc.getEztHosDeptDocBean().getEdPic(),0,holder.doctor_head);

        if(attention_doc!=null&&attention_doc.getEztHosDeptDocBean()!=null){
            holder.doctor_name.setText(attention_doc.getEztHosDeptDocBean().getEdName());
            holder.doctor_label.setText(attention_doc.getEztHosDeptDocBean().getEdLevelName());
            holder.doctor_department.setText(attention_doc.getEztHosDeptDocBean().getEhName()+attention_doc.getEztHosDeptDocBean().getDptame());

            if(attention_doc.getEztHosDeptDocBean().getEdGoodat()==null){
                holder.be_good_at_tx.setVisibility(View.GONE);
                holder.doctor_be_good_at_skill.setVisibility(View.GONE);
            }else {
                holder.be_good_at_tx.setVisibility(View.VISIBLE);
                holder.doctor_be_good_at_skill.setVisibility(View.VISIBLE);
                holder.doctor_be_good_at_skill.setText(attention_doc.getEztHosDeptDocBean().getEdGoodat());
            }
        }
        return convertView;
    }

    static class ViewHolder {

        TextView doctor_name,doctor_label,doctor_department,doctor_be_good_at_skill,be_good_at_tx;
        ImageView doctor_head;
    }

}

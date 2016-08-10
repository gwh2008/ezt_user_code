package com.eztcn.user.eztcn.adapter;

import com.eztcn.user.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 我的预约的药品的adapter。
 * @author LX
 *@date2016-3-29 @time下午2:45:19
 */
public class MyOrderDrugAdapter extends BaseArrayListAdapter<Object> {

	private Context context;
	public MyOrderDrugAdapter(Activity context) {
		super(context);
		this.context=context;
	}
	class ViewHolder{
		ImageView order_drug_hospital_image;
		TextView doctor_name;
		TextView doctor_tel;
		TextView drug_type;
		TextView drug_effect;
		TextView hospital_name;
		TextView drug_order_status;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewholder;
		
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_my_order_drug, null);
			viewholder=new ViewHolder();
			viewholder.order_drug_hospital_image=(ImageView) convertView.findViewById(R.id.order_drug_hospital_image);
			viewholder.doctor_name=(TextView) convertView.findViewById(R.id.doctor_name);
			viewholder.doctor_tel=(TextView) convertView.findViewById(R.id.doctor_tel);
			viewholder.drug_type=(TextView) convertView.findViewById(R.id.drug_type);
			viewholder.drug_effect=(TextView) convertView.findViewById(R.id.drug_effect);
			viewholder.hospital_name=(TextView) convertView.findViewById(R.id.hospital_name);
			viewholder.drug_order_status=(TextView) convertView.findViewById(R.id.drug_order_status);
			convertView.setTag(viewholder);
		}else{
			viewholder=(ViewHolder) convertView.getTag();
		}
		
		String text="jkajka";
		viewholder.order_drug_hospital_image.setImageDrawable(null);
		viewholder.doctor_name.setText(text);
		
		viewholder.doctor_tel.setText(text);
		viewholder.drug_type.setText(text);
		viewholder.drug_effect.setText(text);
		viewholder.hospital_name.setText(text);
		viewholder.drug_order_status.setText(text);
		
		
		return convertView;
	}

}

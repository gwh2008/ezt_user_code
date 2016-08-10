package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Vaccine;

public class BabyVaccineAdapter extends BaseArrayListAdapter<Vaccine>{
	private  Activity mContext;
	
	public BabyVaccineAdapter(Activity context) {
		super(context);
		mContext=context;
	}
	private ViewHold holder=null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(null==convertView){
			holder=new ViewHold();
			convertView=View.inflate(mContext, R.layout.item_vaccine, null);
			holder.tv_vac_name=(TextView) convertView.findViewById(R.id.tv_vac_name);
			holder.tv_vac_time=(TextView) convertView.findViewById(R.id.tv_vac_time);
			holder.tv_vac_times=(TextView) convertView.findViewById(R.id.tv_vac_times);
			holder.tv_vac_use=(TextView) convertView.findViewById(R.id.tv_vac_use);
			holder.tv_vac_part=(TextView) convertView.findViewById(R.id.tv_vac_part);
			convertView.setTag(holder);
		}else{
			holder=(ViewHold) convertView.getTag();
		}
		Vaccine vaccine=mList.get(position);
		holder.tv_vac_name.setText(vaccine.getVac_name());
		holder.tv_vac_time.setText(vaccine.getVac_time());
		holder.tv_vac_times.setText(vaccine.getVac_times());
		holder.tv_vac_use.setText(vaccine.getVac_use());
		holder.tv_vac_part.setText(vaccine.getVac_part());
		return convertView;
	}
	private class ViewHold{
		private TextView tv_vac_time; 
		private TextView tv_vac_name;
		private TextView tv_vac_times;
		private TextView tv_vac_use;
		private TextView tv_vac_part;
	}

}

package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;

/**
 * @title 快速挂号adapter
 * @describe
 * @author ezt
 * @created 2015年1月4日
 */
public class QuickAppointListAdapter extends BaseArrayListAdapter<Doctor> {

//	private FinalBitmap fb;
private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	private int flag = 0;// 0为关注医生，1为推荐医生

	public QuickAppointListAdapter(Activity context, int flag) {
		super(context);
		this.flag = flag;
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
	}

	static class ViewHolder {
		TextView tvDocName, tvPos, tvHos, tvDept, tvRate;
		RoundImageView imgHead;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_attent_doc, null);
			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.item_attent_img);// 医生头像
			holder.tvDocName = (TextView) convertView
					.findViewById(R.id.item_attent_name);// 医生名称
			holder.tvPos = (TextView) convertView
					.findViewById(R.id.item_attent_pos);// 医生职位
			holder.tvHos = (TextView) convertView
					.findViewById(R.id.item_attent_hos_name);// 所属医院
			holder.tvDept = (TextView) convertView
					.findViewById(R.id.item_attent_dept_name);// 所属科室
			holder.tvRate = (TextView) convertView
					.findViewById(R.id.item_doc_rate);// 预约率（推荐医生用到）

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Doctor doc = mList.get(position);
		holder.tvDocName.setText(doc.getDocName());
		holder.tvPos.setText(doc.getDocPosition());
		holder.tvHos.setText(doc.getDocHos());
		holder.tvDept.setText(doc.getDocDept());
		switch (flag) {
		case 1:// 推荐医生
//			holder.tvRate.setVisibility(View.VISIBLE);
//			holder.tvRate.setText(doc.getDocRate());
			break;

		default:
			break;
		}

		final String imgurl = EZTConfig.DOC_PHOTO + doc.getDocHeadUrl();
//		fb.display(holder.imgHead, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.imgHead, imgurl);
		return convertView;
	}

}

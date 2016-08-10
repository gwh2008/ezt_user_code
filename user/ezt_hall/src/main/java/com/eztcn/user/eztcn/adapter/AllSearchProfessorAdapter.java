package com.eztcn.user.eztcn.adapter;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;

/**
 * @title 全站搜索专家adapter
 * @describe
 * @author ezt
 * @created 2014年12月27日
 */
public class AllSearchProfessorAdapter extends BaseArrayListAdapter<Doctor> {
	private BitmapUtils bitmapUtils;
	public AllSearchProfessorAdapter(Activity context) {
		super(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
//		fb = FinalBitmap.create(context);
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	    bitmapUtils.configDefaultLoadingImage(defaultBit);
	    bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		
	}

	static class ViewHolder {
		TextView tvPName, tvPDept, tvPLevel, tvPHos;

		RoundImageView imgHead;
	}

//	private FinalBitmap fb;
	private Bitmap defaultBit;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_attent_doc, null);
  
			holder.tvPName = (TextView) convertView
					.findViewById(R.id.item_attent_name);// 专家名称
			holder.tvPDept = (TextView) convertView
					.findViewById(R.id.item_attent_dept_name);// 所属科室

			holder.tvPLevel = (TextView) convertView
					.findViewById(R.id.item_attent_pos);// 专家等级

			holder.tvPHos = (TextView) convertView
					.findViewById(R.id.item_attent_hos_name);// 所属医院

			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.item_attent_img);// 头像

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Doctor doc = mList.get(position);

		holder.tvPName.setText(TextUtils.isEmpty(doc.getDocName()) ? "专家名称"
				: doc.getDocName());
		holder.tvPDept.setText(TextUtils.isEmpty(doc.getDocDept()) ? "所属科室"
				: doc.getDocDept());
		holder.tvPLevel.setText(TextUtils.isEmpty(doc.getDocLevel()) ? "专家级别"
				: doc.getDocLevel());
		holder.tvPHos.setText(TextUtils.isEmpty(doc.getDocHos()) ? "所属医院" : doc
				.getDocHos());
		
		if(!TextUtils.isEmpty(doc.getDocHeadUrl())){
			final String imgurl = EZTConfig.DOC_PHOTO + doc.getDocHeadUrl();
//			fb.display(holder.imgHead, imgurl, defaultBit, defaultBit);
			bitmapUtils.display(holder.imgHead, imgurl);
		}
		return convertView;
	}
}

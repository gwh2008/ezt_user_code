package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.DoctorList30Activity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

/**
 * 3.0版本医生列表适配器
 * 
 * @title 医生列表adapter
 * @describe
 * @author ezt
 * @created 2016年03月03日
 */
public class DoctorListAdapter30 extends BaseArrayListAdapter<Doctor> implements
		IHttpResult {
	private final int WARING = 0;// 2015-12-18 对接中

	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	public boolean isChoice = false;// 标记是否为选择医生，如果是，出诊表、电话医生按钮不可点击

	private HashMap<Integer, Boolean> isShow = new HashMap<Integer, Boolean>();// 保存出诊表显示状态
	// private HashMap<Integer, VisitFormAdapter> isShowData = new
	// HashMap<Integer, VisitFormAdapter>();// 保存出诊表显示数据

	// 停诊bug 解决所需
	private ArrayList<Pool> poolist;//
	private Boolean isBigDoc;

	public DoctorListAdapter30(Activity context, boolean isBigDoc) {
		super(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		resetStateValue();
		this.isBigDoc = isBigDoc;
	}

//	tvDeptName
	static class ViewHolder {
		TextView tvDocName, tvPos, tvHosName, canRegView;
		RoundImageView imgHead;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_doctorlist30, null);

			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.doctor_img);// 医生头像

			holder.tvDocName = (TextView) convertView
					.findViewById(R.id.doc_name);// 医生名称

			holder.tvPos = (TextView) convertView
					.findViewById(R.id.doc_position);// 医生职位
//			holder.tvDeptName = (TextView) convertView
//					.findViewById(R.id.doc_dept_name);// 所属科室
			holder.tvHosName = (TextView) convertView
					.findViewById(R.id.doc_hos_name);// 所属医院
			holder.canRegView = (TextView) convertView
					.findViewById(R.id.canRegView);// 所属医院

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Doctor doc = mList.get(position);
		if (null != doc) {
			holder.tvPos.setVisibility(View.VISIBLE);
			holder.tvDocName.setText(doc.getDocName() == null ? "医生名称" : doc
					.getDocName());
			holder.tvPos.setText(doc.getDocPosition() == null ? "医生职位" : doc
					.getDocPosition());

//			holder.tvDeptName.setText(doc.getDocDept());
			holder.tvHosName.setText(doc.getDocHos()+" "+doc.getDocDept());
		}

		if (isBigDoc) {
			holder.tvHosName.setVisibility(View.VISIBLE);
//			holder.tvDeptName.setVisibility(View.VISIBLE);
		} else {

			String strChoice2Name = ((DoctorList30Activity) mContext).tvChoice2
					.getText().toString();
			String strChoice3Name = ((DoctorList30Activity) mContext).tvChoice3
					.getText().toString();

			if (((DoctorList30Activity) mContext).fromType == 1) {// 医院进入
				if ("选择医院".equals(strChoice2Name)
						&& "选择科室".equals(strChoice3Name)
						|| (!"选择医院".equals(strChoice2Name) && !"选择科室"
								.equals(strChoice3Name))) {// 未选中，默认显示
					holder.tvHosName.setVisibility(View.VISIBLE);
//					holder.tvDeptName.setVisibility(View.VISIBLE);
				} else if (!"选择医院".equals(strChoice2Name)
						&& "选择科室".equals(strChoice3Name)) {
					holder.tvHosName.setText(doc.getDocDept());
					holder.tvHosName.setVisibility(View.VISIBLE);
//					holder.tvDeptName.setVisibility(View.GONE);
				} else if ("选择医院".equals(strChoice2Name)
						&& !"选择科室".equals(strChoice3Name)) {
					holder.tvHosName.setVisibility(View.VISIBLE);
//					holder.tvDeptName.setVisibility(View.GONE);
				}

			} else {// 科室进入
				if ("选择科室".equals(strChoice2Name)) {
					holder.tvHosName.setVisibility(View.VISIBLE);
//					holder.tvDeptName.setVisibility(View.VISIBLE);
				} else {
					holder.tvHosName.setVisibility(View.VISIBLE);
//					holder.tvDeptName.setVisibility(View.GONE);
				}

			}

		}
		if (doc.getEhDockingStatus() != WARING) {// 若是非对接医院
			if (doc.getIsHaveNum() == 1) {
				setVisable(holder.canRegView, position);
			} else {
				setNot(holder.canRegView, position);
			}
		} else {
			setNot(holder.canRegView, position);
		}

		final String imgurl = EZTConfig.DOC_PHOTO + doc.getDocHeadUrl();
		// fb.display(holder.imgHead, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.imgHead, imgurl);
		return convertView;
	}

	/***
	 * 设置预约按钮为灰底灰字
	 * 
	 * @param regView
	 * @param position
	 */
	@SuppressLint("NewApi")
	private void setNot(TextView regView, int position) {
//		regView.setBackground(mContext.getResources().getDrawable(
//				R.drawable.conners_grey));
        regView.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.drawable.conners_grey));
		// regView.setTextColor(mContext.getResources().getColor(
		// R.color.dark_gray2));
		regView.setTextColor(mContext.getResources().getColor(
				android.R.color.white));
		regView.setText("约满");
		isShow.put(position, false);
	}

	/***
	 * 设置预约按钮为蓝底白字
	 * 
	 * @param regView
	 * @param position
	 */
	@SuppressLint("NewApi")
	private void setVisable(TextView regView, int position) {
//		regView.setBackground(mContext.getResources().getDrawable(
//				R.drawable.conners_green));
        regView.setBackgroundDrawable(mContext.getResources().getDrawable(
                R.drawable.conners_green));
		regView.setTextColor(mContext.getResources().getColor(
				android.R.color.white));
		regView.setText("预约");
		isShow.put(position, true);
	}

	/**
	 * 获取号池资料
	 */
	private void getPools(int pos) {

		String deptid = mList.get(pos).getDocDeptId();
		String doctorid = mList.get(pos).getId();
		RequestParams params = new RequestParams();
		params.addBodyParameter("deptid", deptid);
		params.addBodyParameter("doctorid", doctorid);
		params.addBodyParameter("isExist", 1 + "");// 默认查询预约未满的号池信息

		HospitalImpl impl = new HospitalImpl();
		impl.getDocPool(params, this);
	}

	/**
	 * 处理号池信息
	 */
	private void dealWithPoolData() {
		List<PoolTimes> ptList;

		for (int i = 0; i < poolist.size(); i++) {

			ptList = poolist.get(i).getTimeList();
			if (ptList == null || ptList.size() == 0) {
				continue;
			}
			for (int j = 0; j < ptList.size(); j++) {
				if (!ptList.get(j).isRemains()) {
					continue;
				} else {
					poolist.get(i).setRemain(1);
					break;
				}
			}
		}

	}

	@Override
	public void result(Object... object) {

		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_DOC_POOL:// 获取号池
			if (isSuc) {// 成功
				poolist = (ArrayList<Pool>) object[2];
				dealWithPoolData();

			}
			break;
		}

	}

	public boolean getVisJump(int position) {
        try{
            return isShow.get(position);
        }catch (Exception e){
            return false;
        }

	}

	/**
	 * 重置状态值
	 */
	public void resetStateValue() {
		isShow.clear();
		// isShowData.clear();
	}
	
	
	

}

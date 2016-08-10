package com.eztcn.user.eztcn.adapter;
import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.PhoneDoctorTimeActivity;
import com.eztcn.user.eztcn.activity.home.PromptlyCallActivity;
import com.eztcn.user.eztcn.activity.mine.RechargeActivity;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.hall.utils.Constant;
/**
 * @title 医生列表adapter
 * @describe
 * @author ezt
 * @created 2014年12月10日
 */
public class TelDoctorListAdapter extends BaseArrayListAdapter<Doctor>
		implements OnClickListener {
	private int WARNING = 0;// 2015-12-21*医院对接中的状态
	private double eztCurrency;

	// private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	public TelDoctorListAdapter(Activity context) {
		super(context);
		// fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(
				mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);

	}

	static class ViewHolder {
		TextView tvDocName, tvPos, tvHosDept, tvPrice, tvUnit,
				doc_visit_form_tv, orderCall;
		LinearLayout layoutImmediate, layoutAppoint;
		ImageView imgEvaluate;
		RoundImageView imgHead;

	}

	public void setUserCurrency(double eztCurrency) {
		this.eztCurrency = eztCurrency;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Doctor doctor;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_tel_doclist, null);

			holder.tvDocName = (TextView) convertView
					.findViewById(R.id.tel_doc_name);// 医生名称
			holder.tvPos = (TextView) convertView
					.findViewById(R.id.tel_doc_position);// 医生职位

			holder.tvHosDept = (TextView) convertView
					.findViewById(R.id.tel_doc_hos_dept);// 所属医院、科室
			holder.tvPrice = (TextView) convertView
					.findViewById(R.id.tel_doc_price);// 开通价格
			holder.tvUnit = (TextView) convertView
					.findViewById(R.id.tel_doc_unit);// 单位费用

			holder.layoutImmediate = (LinearLayout) convertView
					.findViewById(R.id.tel_doc_immediate_bt);// 立即通话
			holder.doc_visit_form_tv = (TextView) convertView
					.findViewById(R.id.doc_visit_form_tv);
			holder.layoutAppoint = (LinearLayout) convertView
					.findViewById(R.id.tel_doc_appoint_bt);// 预约通话
			holder.orderCall = (TextView) convertView
					.findViewById(R.id.orderCall);
			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.tel_doctor_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		doctor = mList.get(position);
		holder.layoutAppoint.setTag(position);
		holder.layoutImmediate.setTag(position);
		holder.layoutImmediate.setOnClickListener(this);
		holder.layoutAppoint.setOnClickListener(this);
		holder.tvDocName.setText(doctor.getDocName());
		holder.tvPos.setText(EztDictionaryDB.getInstance(mContext)
				.getLabelByTag("doctorLevel", doctor.getDocLevel() + ""));
		holder.tvHosDept.setText(doctor.getDocHos() + "  "
				+ doctor.getDocDept());
		double d = doctor.getFees();
		holder.tvPrice.setText((int) Math.floor(d) + " 医通币");
		holder.tvUnit.setText("(" + doctor.getMinTime() + "分钟)");

		final String imgurl = EZTConfig.DOC_PHOTO + doctor.getDocHeadUrl();
		// fb.display(holder.imgHead, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.imgHead, imgurl);
		// 判断电话医生
		Drawable rightDrawable;

		// 2015-12-21 电话医生 医院对接中不得通话

		int ehDockingStatus = doctor.getEhDockingStatus();
		if (ehDockingStatus == WARNING) {// 2015-12-21 电话医生 医院对接中不得通话
			rightDrawable = mContext.getResources().getDrawable(
					R.drawable.ic_tel_gray);
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
					rightDrawable.getMinimumHeight());
			holder.doc_visit_form_tv.setCompoundDrawables(rightDrawable, null,
					null, null);
			holder.doc_visit_form_tv.setCompoundDrawablePadding(5);
			holder.doc_visit_form_tv.setTextColor(mContext.getResources()
					.getColor(R.color.dark_gray2));
			holder.layoutImmediate
					.setBackgroundResource(android.R.color.transparent);
			holder.layoutImmediate.setEnabled(false);

			rightDrawable = mContext.getResources().getDrawable(
					R.drawable.ic_tel_gray);
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
					rightDrawable.getMinimumHeight());
			holder.orderCall.setCompoundDrawables(rightDrawable, null, null,
					null);
			holder.orderCall.setCompoundDrawablePadding(5);
			holder.orderCall.setTextColor(mContext.getResources().getColor(
					R.color.dark_gray2));
			holder.layoutAppoint
					.setBackgroundResource(android.R.color.transparent);
			holder.layoutAppoint.setEnabled(false);

		} else {// 2015-12-21 电话医生 医院对接中不得通话

			int ynONline = doctor.getCallDoctorYnOnline();
			int ynAppointment = doctor.getCallDoctorYnAppointment();
			if (ynONline == 1) {// 是否在线
				rightDrawable = mContext.getResources().getDrawable(
						R.drawable.ic_tel);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				holder.doc_visit_form_tv.setCompoundDrawables(rightDrawable,
						null, null, null);
				holder.doc_visit_form_tv.setCompoundDrawablePadding(5);
				holder.doc_visit_form_tv.setTextColor(mContext.getResources()
						.getColor(R.color.light_main_color));
				holder.layoutImmediate
						.setBackgroundResource(R.drawable.selector_listitem_bg);
				holder.layoutImmediate.setEnabled(true);
			} else {
				rightDrawable = mContext.getResources().getDrawable(
						R.drawable.ic_tel_gray);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				holder.doc_visit_form_tv.setCompoundDrawables(rightDrawable,
						null, null, null);
				holder.doc_visit_form_tv.setCompoundDrawablePadding(5);
				holder.doc_visit_form_tv.setTextColor(mContext.getResources()
						.getColor(R.color.dark_gray2));
				holder.layoutImmediate
						.setBackgroundResource(android.R.color.transparent);
				holder.layoutImmediate.setEnabled(false);
			}

			if (ynAppointment == 1) {// 是否可预约
				rightDrawable = mContext.getResources().getDrawable(
						R.drawable.ic_tel);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				holder.orderCall.setCompoundDrawables(rightDrawable, null,
						null, null);
				holder.orderCall.setCompoundDrawablePadding(5);
				holder.orderCall.setTextColor(mContext.getResources().getColor(
						R.color.light_main_color));
				holder.layoutAppoint
						.setBackgroundResource(R.drawable.selector_listitem_bg);
				holder.layoutAppoint.setEnabled(true);
			} else {
				rightDrawable = mContext.getResources().getDrawable(
						R.drawable.ic_tel_gray);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				holder.orderCall.setCompoundDrawables(rightDrawable, null,
						null, null);
				holder.orderCall.setCompoundDrawablePadding(5);
				holder.orderCall.setTextColor(mContext.getResources().getColor(
						R.color.dark_gray2));
				holder.layoutAppoint
						.setBackgroundResource(android.R.color.transparent);
				holder.layoutAppoint.setEnabled(false);
			}
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();

		switch (v.getId()) {
		case R.id.tel_doc_immediate_bt:// 立即通话
			if (BaseApplication.patient == null) {
				((FinalActivity) mContext).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			mContext.startActivity(new Intent(mContext,
					PromptlyCallActivity.class).putExtra("type", 1)
					.putExtra("info", mList.get(pos))
					.putExtra("currency", eztCurrency));
			break;

		case R.id.tel_doc_appoint_bt:// 预约咨询
			if (BaseApplication.patient == null) {
				((FinalActivity) mContext).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			if (eztCurrency < mList.get(pos).getFees()) {
				hidCharge(eztCurrency);
				return;
			}
			mContext.startActivity(new Intent(mContext,
					PhoneDoctorTimeActivity.class).putExtra("type", 1)
					.putExtra("info", mList.get(pos)));
			break;
		}
	}
	/**
	 * 提示充值
	 */
	public void hidCharge(double money) {
		int m = (int) Math.floor(money);
		View view = View.inflate(mContext, R.layout.balancehint, null);
		TextView tv = (TextView) view.findViewById(R.id.balance);
		tv.setText("医通币：" + m + "个");
		AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
		ab.setTitle("温馨提示");
		ab.setView(view);
		ab.setPositiveButton("充值", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, RechargeActivity.class);
				mContext.startActivity(intent);
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}
}

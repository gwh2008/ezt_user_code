package com.eztcn.user.eztcn.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.DoctorListActivity;
import com.eztcn.user.eztcn.activity.home.PhoneDoctorActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.HorizontalListView;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.MapUtil;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 医生列表adapter
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class DoctorListAdapter extends BaseArrayListAdapter<Doctor> implements
		OnClickListener, IHttpResult {
	private final int WARING=0;//2015-12-18 对接中


//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;
	private onTelDoctorClickListener listener;
	public boolean isChoice = false;// 标记是否为选择医生，如果是，出诊表、电话医生按钮不可点击

	private HashMap<Integer, Boolean> isShow = new HashMap<Integer, Boolean>();// 保存出诊表显示状态
	private HashMap<Integer, VisitFormAdapter> isShowData = new HashMap<Integer, VisitFormAdapter>();// 保存出诊表显示数据
	
	
	//停诊bug 解决所需
	private ArrayList<Pool> poolist;//

	private ViewHolder selHolder;
	private int selPos;


	public DoctorListAdapter(Activity context) {
		super(context);
		
//		fb = FinalBitmap.create(context);
		defaultBit = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.default_doc_img);
		
		bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
	        bitmapUtils.configDefaultLoadingImage(defaultBit);
		    bitmapUtils.configDefaultLoadFailedImage(defaultBit);

	}

	static class ViewHolder {
		TextView tvDocName, tvPos, tvRanking, tvVisitForm, tvDeptName,
				tvHosName, phoneDoctor;
		LinearLayout layoutVisitForm, layoutTel;
		RoundImageView imgHead;
		HorizontalListView lvVisitForm;
		View horLine;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_doctorlist, null);

			holder.tvDocName = (TextView) convertView
					.findViewById(R.id.doc_name);// 医生名称
			holder.tvPos = (TextView) convertView
					.findViewById(R.id.doc_position);// 医生职位
			holder.tvRanking = (TextView) convertView
					.findViewById(R.id.doc_ranking);// 排行榜
			holder.tvVisitForm = (TextView) convertView
					.findViewById(R.id.doc_visit_form_tv);// 出诊按钮

			holder.imgHead = (RoundImageView) convertView
					.findViewById(R.id.doctor_img);// 医生头像

			holder.layoutVisitForm = (LinearLayout) convertView
					.findViewById(R.id.doc_visit_form_layout);

			holder.layoutTel = (LinearLayout) convertView
					.findViewById(R.id.doc_tel_layout);// 电话医生按钮

			holder.lvVisitForm = (HorizontalListView) convertView
					.findViewById(R.id.doc_visitform_hor_lv);// 出诊表

			holder.tvDeptName = (TextView) convertView
					.findViewById(R.id.doc_dept_name);// 所属科室
			holder.tvHosName = (TextView) convertView
					.findViewById(R.id.doc_hos_name);// 所属医院
			holder.phoneDoctor = (TextView) convertView
					.findViewById(R.id.phoneDoctor);// 电话医生
			holder.horLine = convertView.findViewById(R.id.doc_visitform_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!isChoice) {// 为选择医生，不可点击
			holder.layoutVisitForm.setOnClickListener(this);
			holder.lvVisitForm.setTag(position);
			holder.layoutVisitForm.setTag(holder);
			holder.layoutTel.setOnClickListener(this);
			holder.layoutTel.setTag(position);
		}

		/**
		 * 恢复所标记的显示状态
		 */
		if (isShow.get(position) != null) {
			holder.lvVisitForm
					.setVisibility(isShow.get(position) == true ? View.VISIBLE
							: View.GONE);
		} else {
			holder.lvVisitForm.setVisibility(View.GONE);
		}

		/**
		 * 恢复所填充的值
		 */
		if (isShowData.get(position) != null) {
			holder.lvVisitForm.setAdapter(isShowData.get(position));
		}

		Doctor doc = mList.get(position);
		holder.tvDocName.setText(doc.getDocName() == null ? "医生名称" : doc
				.getDocName());
		holder.tvPos.setText(doc.getDocPosition() == null ? "医生职位" : doc
				.getDocPosition());
		// holder.tvRate.setText(doc.getDocRate() == null ? "0" : StringUtil
		// .getTwoRadixPoint(Double.parseDouble(doc.getDocRate()) * 100)
		// + "%");
		holder.tvRanking.setText(doc.getDocRanking() == null ? "5星" : doc
				.getDocRanking());
		holder.tvDeptName.setText(doc.getDocDept());
		holder.tvHosName.setText(doc.getDocHos());

		String strChoice2Name = ((DoctorListActivity) mContext).tvChoice2
				.getText().toString();
		String strChoice3Name = ((DoctorListActivity) mContext).tvChoice3
				.getText().toString();

		if (((DoctorListActivity) mContext).fromType == 1) {// 医院进入
			if ("选择医院".equals(strChoice2Name)
					&& "选择科室".equals(strChoice3Name)
					|| (!"选择医院".equals(strChoice2Name) && !"选择科室"
							.equals(strChoice3Name))) {// 未选中，默认显示
				holder.tvHosName.setVisibility(View.VISIBLE);
				holder.tvDeptName.setVisibility(View.VISIBLE);
			} else if (!"选择医院".equals(strChoice2Name)
					&& "选择科室".equals(strChoice3Name)) {
				holder.tvHosName.setText(doc.getDocDept());
				holder.tvHosName.setVisibility(View.VISIBLE);
				holder.tvDeptName.setVisibility(View.GONE);
			} else if ("选择医院".equals(strChoice2Name)
					&& !"选择科室".equals(strChoice3Name)) {
				holder.tvHosName.setVisibility(View.VISIBLE);
				holder.tvDeptName.setVisibility(View.GONE);
			}

		} else {// 科室进入
			if ("选择科室".equals(strChoice2Name)) {
				holder.tvHosName.setVisibility(View.VISIBLE);
				holder.tvDeptName.setVisibility(View.VISIBLE);
			} else {
				holder.tvHosName.setVisibility(View.VISIBLE);
				holder.tvDeptName.setVisibility(View.GONE);
			}

		}

		String str = "";
		// 2015-12-18 医院对接语

		if (doc.getEhDockingStatus() == WARING) {
			str = "出诊表";
			Drawable draw1 = mContext.getResources().getDrawable(
					R.drawable.ic_visit_form_gray);
			draw1.setBounds(0, 0, draw1.getMinimumWidth(),
					draw1.getMinimumHeight());
			holder.tvVisitForm.setCompoundDrawables(draw1, null, null, null);

			holder.tvVisitForm.setTextColor(mContext.getResources().getColor(
					R.color.dark_gray2));
			holder.layoutVisitForm
					.setBackgroundResource(android.R.color.transparent);
			holder.layoutVisitForm.setEnabled(false);
		} else {
			if (doc.getIsHaveNum() == 1) {// 有号
				str = "出诊表";
				holder.layoutVisitForm
						.setBackgroundResource(R.drawable.selector_listitem_bg);
				holder.layoutVisitForm.setEnabled(true);
				holder.tvVisitForm.setTextColor(mContext.getResources()
						.getColor(R.color.light_main_color));

				Drawable draw1 = mContext.getResources().getDrawable(
						R.drawable.ic_visit_form);
				draw1.setBounds(0, 0, draw1.getMinimumWidth(),
						draw1.getMinimumHeight());
				holder.tvVisitForm
						.setCompoundDrawables(draw1, null, null, null);

			} else if (doc.getIsHaveNum() == 0) {// 预约已满
				str = "预约已满";
				Drawable draw1 = mContext.getResources().getDrawable(
						R.drawable.ic_visit_form_red);
				draw1.setBounds(0, 0, draw1.getMinimumWidth(),
						draw1.getMinimumHeight());
				holder.tvVisitForm
						.setCompoundDrawables(draw1, null, null, null);
				holder.tvVisitForm.setTextColor(Color.RED);
				holder.layoutVisitForm
						.setBackgroundResource(R.drawable.selector_listitem_bg);
				holder.layoutVisitForm.setEnabled(false);
			} else {// 未放号
				str = "出诊表";

				Drawable draw1 = mContext.getResources().getDrawable(
						R.drawable.ic_visit_form_gray);
				draw1.setBounds(0, 0, draw1.getMinimumWidth(),
						draw1.getMinimumHeight());
				holder.tvVisitForm
						.setCompoundDrawables(draw1, null, null, null);

				holder.tvVisitForm.setTextColor(mContext.getResources()
						.getColor(R.color.dark_gray2));

				holder.layoutVisitForm
						.setBackgroundResource(android.R.color.transparent);
				holder.layoutVisitForm.setEnabled(false);
			}

		}
		// 判断电话医生
		Drawable rightDrawable;
		int ynONline = doc.getCallDoctorYnOnline();
		int ynAppointment = doc.getCallDoctorYnAppointment();
		if (ynONline == 1 || ynAppointment == 1) {
			rightDrawable = mContext.getResources().getDrawable(
					R.drawable.ic_tel2);
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
					rightDrawable.getMinimumHeight());
			holder.phoneDoctor.setCompoundDrawables(rightDrawable, null, null,
					null);
			holder.phoneDoctor.setCompoundDrawablePadding(5);
			holder.phoneDoctor.setTextColor(mContext.getResources().getColor(
					R.color.light_main_color));
			holder.layoutTel
					.setBackgroundResource(R.drawable.selector_listitem_bg);
		} else {
			rightDrawable = mContext.getResources().getDrawable(
					R.drawable.ic_tel2_gray);
			rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
					rightDrawable.getMinimumHeight());
			holder.phoneDoctor.setCompoundDrawables(rightDrawable, null, null,
					null);
			holder.phoneDoctor.setCompoundDrawablePadding(5);
			holder.phoneDoctor.setTextColor(mContext.getResources().getColor(
					R.color.dark_gray2));
			holder.layoutTel.setBackgroundResource(android.R.color.transparent);
			holder.layoutTel.setEnabled(false);
		}

		holder.tvVisitForm.setText(str);
		final String imgurl = EZTConfig.DOC_PHOTO + doc.getDocHeadUrl();
//		fb.display(holder.imgHead, imgurl, defaultBit, defaultBit);
		bitmapUtils.display(holder.imgHead, imgurl);
		return convertView;
	}
	
	//sunstar add 处理停诊显示问题

	public void showVisitForm(ViewHolder holder,int pos) {

		String deptid = mList.get(pos).getDocDeptId();
		String doctorid = mList.get(pos).getId();
		String docName = mList.get(pos).getDocName();
		String hosName = mList.get(pos).getDocHos();
		String hospitalId = mList.get(pos).getDocHosId();
		double distance = MapUtil.calculatePath(mList.get(pos).getHosLat(),
				mList.get(pos).getHosLon());
		// 判断是否是深圳儿童医院，并且距离是否>30公里
		if (mList.get(pos).getDocHosId().equals("83")
				&& distance > 30 * 1000) {
			hintLargeDistance();
			return;
		}
		if (holder.lvVisitForm.getVisibility() == View.GONE) {
			isShow.put(pos, true);
			holder.horLine.setVisibility(View.VISIBLE);
			holder.lvVisitForm.setVisibility(View.VISIBLE);
			VisitFormAdapter adapter = new VisitFormAdapter(mContext,
					doctorid, hospitalId, deptid, docName, hosName);
			holder.lvVisitForm.setAdapter(adapter);
			if(null!=mList&&mList.size()>0){
				adapter.setList(mList.get(pos).getPools());
				// 计算lvVisitForm高度
				View listItem = adapter.getView(0, null, holder.lvVisitForm);
				listItem.measure(0, 0); // 计算子项View 的宽高
				int item_height = listItem.getMeasuredHeight();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, item_height);
				holder.lvVisitForm.setLayoutParams(params);
				isShowData.put(pos, adapter);
			}
			
		

		} else {
			isShow.put(pos, false);
			holder.lvVisitForm.setVisibility(View.GONE);
			holder.horLine.setVisibility(View.GONE);
		}
	}
	
	public void showVisitForm2(ViewHolder holder,int pos) {

		String deptid = mList.get(pos).getDocDeptId();
		String doctorid = mList.get(pos).getId();
		String docName = mList.get(pos).getDocName();
		String hosName = mList.get(pos).getDocHos();
		String hospitalId = mList.get(pos).getDocHosId();
		double distance = MapUtil.calculatePath(mList.get(pos).getHosLat(),
				mList.get(pos).getHosLon());
		// 判断是否是深圳儿童医院，并且距离是否>30公里
		if (mList.get(pos).getDocHosId().equals("83")
				&& distance > 30 * 1000) {
			hintLargeDistance();
			return;
		}
		if (holder.lvVisitForm.getVisibility() == View.GONE) {
			if(null!=poolist&&poolist.size()>0){
				isShow.put(pos, true);
				holder.horLine.setVisibility(View.VISIBLE);
				holder.lvVisitForm.setVisibility(View.VISIBLE);
				VisitFormAdapter adapter = new VisitFormAdapter(mContext,
						doctorid, hospitalId, deptid, docName, hosName);
				holder.lvVisitForm.setAdapter(adapter);
				adapter.setList(poolist);

				// 计算lvVisitForm高度
				View listItem = adapter.getView(0, null, holder.lvVisitForm);
				listItem.measure(0, 0); // 计算子项View 的宽高
				int item_height = listItem.getMeasuredHeight();
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, item_height);
				holder.lvVisitForm.setLayoutParams(params);

				isShowData.put(pos, adapter);
			}else{
//				Toast.makeText(mContext, mContext.getString(R.string.sorry_pool_wrong),Toast.LENGTH_SHORT).show();
			}
		} else {
			isShow.put(pos, false);
			holder.lvVisitForm.setVisibility(View.GONE);
			holder.horLine.setVisibility(View.GONE);
		}
	}
	

	/**
	 * 获取号池资料
	 */
	private void getPools(int pos) {
		
		String deptid = mList.get(pos).getDocDeptId();
		String doctorid = mList.get(pos).getId();
	
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("deptid", deptid);
//		params.put("doctorid", doctorid);
//		params.put("isExist", 1 + "");// 默认查询预约未满的号池信息
		
		
		
		RequestParams params=new RequestParams();
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
				}
				else
				{
					poolist.get(i).setRemain(1);
					break;
				}
			}
		}
		
		// ptList = null;
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
			showVisitForm2(selHolder,selPos);
//			this.showVisitForm(selHolder,selPos);
			
		} else {
//			tvDate.setVisibility(View.GONE);
//			tvTime.setVisibility(View.GONE);
//			tvReTime.setVisibility(View.VISIBLE);
//			Toast.makeText(getApplicationContext(), object[3].toString(),
//					Toast.LENGTH_SHORT).show();
		}

		break;
		}
		
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.doc_visit_form_layout:// 出诊表
			selHolder = (ViewHolder) v.getTag();
			selPos = (Integer) selHolder.lvVisitForm.getTag();
			if(selHolder.lvVisitForm.getVisibility() == View.GONE){
				getPools(selPos);
			}else{
				isShow.put(selPos, false);
				selHolder.lvVisitForm.setVisibility(View.GONE);
				selHolder.horLine.setVisibility(View.GONE);
			}
			break;

		case R.id.doc_tel_layout:// 电话医生
			if (BaseApplication.patient == null) {
				((FinalActivity) mContext).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			int p = (Integer) v.getTag();
			mContext.startActivity(new Intent(mContext,
					PhoneDoctorActivity.class).putExtra("doc", mList.get(p)));
			// listener.telDoctorClick(p);
			break;

		}
	}

	/**
	 * 重置状态值
	 */
	public void resetStateValue() {
		isShow.clear();
		isShowData.clear();
	}

	/**
	 * 电话医生按钮点击事件
	 */
	public void setOnTelDoctorListener(onTelDoctorClickListener listener) {
		this.listener = listener;
	}

	/**
	 * @title 电话医生
	 * @describe
	 * @author ezt
	 * @created 2015年2月12日
	 */
	public interface onTelDoctorClickListener {

		public void telDoctorClick(int position);
	}

	/**
	 * 温馨提示（针对儿童医院，距离>30公里）
	 */
	public void hintLargeDistance() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage("您的行程已超出30公里外，将错过就诊时间，请预约其他时间段")
				.setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();
	}
}

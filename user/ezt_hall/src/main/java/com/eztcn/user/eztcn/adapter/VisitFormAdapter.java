package com.eztcn.user.eztcn.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.DoctorListActivity;
import com.eztcn.user.eztcn.activity.home.OrderRegistrationActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 出诊时间表adapter
 * @describe
 * @author ezt
 * @created 2014年12月19日
 */
public class VisitFormAdapter extends BaseArrayListAdapter<Pool> implements
		OnClickListener {

	private String docId, hosId, deptId, docName, hosName;

	public VisitFormAdapter(Activity context, String docId, String hosId,
			String deptId, String docName, String hosName) {
		super(context);
		this.docId = docId;
		this.hosId = hosId;
		this.deptId = deptId;
		this.docName = docName;
		this.hosName = hosName;
	}

	static class ViewHolder {
		TextView tvDate, tvWeek, tvState;
	}

	private String changeDate(String dateStr){
		//2016-01-14
		String[] dateArray=dateStr.split("-");//按-分组
		String monthStr=dateArray[1];
		String dayStr=dateArray[2];
		//"1月14日" 01--->1月
		
		if(monthStr.startsWith("0")){
			monthStr=monthStr.replaceFirst("0","");
		}
		monthStr=monthStr+"月";
		
		if(dayStr.startsWith("0")){
			dayStr=dayStr.replaceFirst("0","");
		}
		dayStr=dayStr+"日";
		return monthStr+dayStr;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mContext.getLayoutInflater().inflate(
					R.layout.item_visit_form, null);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.item_visit_form_date);// 日期
			holder.tvWeek = (TextView) convertView
					.findViewById(R.id.item_visit_form_week);// 星期
			holder.tvState = (TextView) convertView
					.findViewById(R.id.item_visit_form_state);// 状态
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// mList 为 0 bug

		if (mList.size() != 0) {
			Pool pool = mList.get(position);
			//获取号池
			boolean bspformat = false;
			if (!TextUtils.isEmpty(pool.getDate())) {
				//号池非空
				int idx = pool.getDate().indexOf(":");
				if (idx < 0) {
					bspformat = true;

					String dateStr=pool.getDate();
					String tempDateStr=changeDate(dateStr);
					holder.tvDate.setText(tempDateStr);
				} else {
					//2016-01-14
					String str = pool.getDate().substring(0, idx - 3);
					String tempDateStr=changeDate(str);
					holder.tvDate.setText(tempDateStr);
				}

			} else {
				holder.tvDate.setText("日期");
			}
			holder.tvWeek.setText(TextUtils.isEmpty(pool.getDateWeek()) ? "星期"
					: pool.getDateWeek());
			if (!bspformat) {
				if (!TextUtils.isEmpty(pool.getDateWeek())) {
					String strWeek = "";
					switch (Integer.parseInt(pool.getDateWeek())) {
					case 1:
						strWeek = "星期一";
						break;

					case 2:
						strWeek = "星期二";
						break;
					case 3:
						strWeek = "星期三";
						break;

					case 4:
						strWeek = "星期四";
						break;

					case 5:
						strWeek = "星期五";
						break;

					case 6:
						strWeek = "星期六";
						break;

					case 7:
						strWeek = "星期日";
						break;
					}
					holder.tvWeek.setText(strWeek);
				} else {
					holder.tvWeek.setText("暂无星期");
				}
			}
			holder.tvState.setTag(position);
			int remainType = pool.getRemain();

			if (remainType == 1) {
				holder.tvState.setOnClickListener(this);
				holder.tvState.setText("预约");
				holder.tvState.setTextColor(mContext.getResources().getColor(
						R.color.main_color));
				holder.tvState
						.setBackgroundResource(R.drawable.selector_border_small_white);

			} else if (remainType == -1) {
				holder.tvState.setOnClickListener(null);
				holder.tvState.setText("未放号");
				holder.tvState.setTextColor(Color.RED);
				holder.tvState
						.setBackgroundResource(android.R.color.transparent);
			} else {
				holder.tvState.setOnClickListener(null);
				holder.tvState.setText("约满");
				holder.tvState.setTextColor(Color.RED);
				holder.tvState
						.setBackgroundResource(android.R.color.transparent);
			}
			holder.tvState.setPadding(ResourceUtils.dip2px(mContext,
					ResourceUtils.getXmlDef(mContext, R.dimen.medium_margin)),
					ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
							mContext, R.dimen.small_margin)), ResourceUtils
							.dip2px(mContext, ResourceUtils.getXmlDef(mContext,
									R.dimen.medium_margin)), ResourceUtils
							.dip2px(mContext, ResourceUtils.getXmlDef(mContext,
									R.dimen.small_margin)));

		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		Pool pool = mList.get(pos);
		if (pool.getRemain() == 1) {

			if (BaseApplication.getInstance().isNetConnected) {
				if (BaseApplication.patient == null) {// 登录
					((DoctorListActivity) mContext).HintToLogin(Constant.LOGIN_COMPLETE);
					return;
				}
				if (!judgeUserInfo()) {
					hintPerfectInfo("请完善个人信息再进行挂号!", 1);

				} else if (TextUtils.isEmpty(BaseApplication.patient
						.getEpMobile())) {
					hintPerfectInfo("请完善个人手机号！", 2);
				} else {
					String pdate = pool.getDate();
					if (pdate.contains(" ")) {
						pdate = pdate.substring(0, pdate.indexOf(" "));
					}

					mContext.startActivity(new Intent(mContext,
							OrderRegistrationActivity.class)
							.putExtra("pos", pos)
							.putExtra("hosId", hosId)
							.putExtra("poolDate", pdate)
							.putExtra("deptid", deptId)
							.putExtra("doctorid", docId)
							.putExtra("hosName", hosName)
							.putExtra("docName", docName)
                            .putExtra("poolList",(Serializable) mList));
				}

			} else {
				Toast.makeText(mContext,
						mContext.getString(R.string.network_hint),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, "预约已满", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 提醒完善信息
	 * 
	 * @param hintInfo
	 * @param type
	 *            1为跳转完善信息页面，2为绑定个人手机号
	 */
	private void hintPerfectInfo(String hintInfo, final int type) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hintInfo).setCancelable(false)
				.setNegativeButton("完善", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (type == 1) {
							mContext.startActivity(new Intent(mContext,
									AutonymAuthActivity.class));
						} else {
							mContext.startActivity(new Intent(mContext,
									ModifyPhoneActivity.class));
						}

					}
				}).setPositiveButton("取消", null);

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

	/**
	 * 判断用户信息完善度
	 */
	public boolean judgeUserInfo() {
		if (TextUtils.isEmpty(BaseApplication.patient.getEpHiid())) {
			return false;
		}
		return true;
	}
}

package com.eztcn.user.eztcn.fragment;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import xutils.http.RequestParams;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.AttentDocActivity;
import com.eztcn.user.eztcn.activity.mine.FamilyMemberActivity;
import com.eztcn.user.eztcn.activity.mine.HelpActivity30;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.eztcn.activity.mine.MyServiceActivity30;
import com.eztcn.user.eztcn.activity.mine.SettingActivity;
import com.eztcn.user.eztcn.activity.mine.UserInfoActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.bean.OrderCheck;
import com.eztcn.user.eztcn.impl.ValideImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.TelephoneUtils;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.utils.Constant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class UserFragmentNew30 extends FinalFragment implements
		OnClickListener, IHttpResult {
	private PatientBean patientBean;
	private Activity mActivity;
	private View rootView;// 缓存Fragment view
	private FrameLayout user_setting_framelayout;
	private RelativeLayout user_attention_jump, user_case_jump;
	private RelativeLayout user_help_jump, user_invite_code_jump;
	private RelativeLayout user_service_jump;
	private RelativeLayout user_info_relayout;
	private RelativeLayout unlogin_status_layout;
	private LinearLayout unlogin_layout;
	private TextView user_name;
	private TextView user_telphone;
	private ImageView user_head_icon;
	private EditText guideDocNum;
	private Dialog invite_dialog;
	/**
	 * 管理就诊人
	 */
	private View user_managemen_patient_jump;
	/**
	 * 优惠券
	 */
	private View user_coupon_jump;
	private View user_consultation_jump;

	public static UserFragmentNew30 newInstance() {
		UserFragmentNew30 fragment = new UserFragmentNew30();
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_user_new30, null);
			WindowManager manager = (WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
			// qrCodeWidth = (int) (dm.widthPixels * 0.7);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		// setTitleBar(rootView);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		WindowManager manager = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		initView(getView(), dm);
		init();
	}

	private void init() {
		if (patientBean != null) {
			user_info_relayout.setVisibility(View.VISIBLE);
			unlogin_status_layout.setVisibility(View.GONE);
			unlogin_layout.setVisibility(View.VISIBLE);
			user_name.setText(patientBean.getEpName());
			user_telphone.setText(patientBean.getEpMobile());
			int uSex = patientBean.getEpSex();
			user_head_icon.setImageResource(R.drawable.userdefault);
		} else {
			user_info_relayout.setVisibility(View.GONE);
			unlogin_status_layout.setVisibility(View.VISIBLE);
			unlogin_layout.setVisibility(View.GONE);
		}
	}

	private void initView(View view, DisplayMetrics dm) {

		user_setting_framelayout = (FrameLayout) view
				.findViewById(R.id.user_setting_framelayout);
		user_setting_framelayout.setOnClickListener(this);
		user_attention_jump = (RelativeLayout) view
				.findViewById(R.id.user_attention_jump);
		user_attention_jump.setOnClickListener(this);
		user_help_jump = (RelativeLayout) view
				.findViewById(R.id.user_help_jump);
		user_help_jump.setOnClickListener(this);
		user_service_jump = (RelativeLayout) view
				.findViewById(R.id.user_service_jump);
		user_service_jump.setOnClickListener(this);
		user_info_relayout = (RelativeLayout) view
				.findViewById(R.id.user_info_relayout);
		user_info_relayout.setOnClickListener(this);
		user_case_jump = (RelativeLayout) view
				.findViewById(R.id.user_case_jump);
		user_case_jump.setOnClickListener(this);
		user_invite_code_jump = (RelativeLayout) view
				.findViewById(R.id.user_invite_code_jump);
		user_invite_code_jump.setOnClickListener(this);
		unlogin_status_layout = (RelativeLayout) view
				.findViewById(R.id.unlogin_status_layout);
		unlogin_status_layout.setOnClickListener(this);
		user_name = (TextView) view.findViewById(R.id.user_name);
		user_telphone = (TextView) view.findViewById(R.id.user_telphone);
		user_head_icon = (ImageView) view.findViewById(R.id.user_head_icon);
		// 管理就诊人
		user_managemen_patient_jump = view
				.findViewById(R.id.user_managemen_patient_jump);
		user_managemen_patient_jump.setOnClickListener(this);

		user_coupon_jump = view.findViewById(R.id.user_coupon_jump);
		user_coupon_jump.setOnClickListener(this);
		user_consultation_jump = view.findViewById(R.id.user_consultation_jump);
		user_consultation_jump.setOnClickListener(this);
		unlogin_layout=(LinearLayout) view.findViewById(R.id.unlogin_layout);
		patientBean = BaseApplication.patient;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case 1: {
			if (patientBean != null) {
				intent.setClass(getActivity(), MyRecordActivity.class);
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		case R.id.user_setting_framelayout:
			intent.setClass(mActivity, SettingActivity.class);
			break;

		case R.id.user_attention_jump:

			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			} else {
				intent.setClass(mActivity, AttentDocActivity.class);
				break;
			}
		case R.id.user_help_jump:
			intent.setClass(mActivity, HelpActivity30.class);
			break;
		case R.id.user_service_jump:
			if (BaseApplication.patient == null) {

				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			} else {
				intent.setClass(mActivity, MyServiceActivity30.class);
				break;
			}
		case R.id.user_info_relayout:
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			} else {
				intent.setClass(mActivity, UserInfoActivity.class);
				break;
			}
		case R.id.user_case_jump:
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			} else {
				intent.setClass(mActivity, MyMedicalRecordListActivity.class);
				break;
			}
		case R.id.user_invite_code_jump:
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			} else {
				inviteDialog();
				return;
			}

		case R.id.unlogin_status_layout:

			intent.setClass(mActivity, LoginActivity.class);
			break;
		case R.id.user_managemen_patient_jump: {
			if (patientBean != null) {
				intent.setClass(getActivity(), FamilyMemberActivity.class);
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}

			break;
		}
		case R.id.user_coupon_jump: {// 优惠券
			intent = null;
			Toast.makeText(getActivity(), getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
		}
			break;

		case R.id.user_consultation_jump: {// 我的咨询
			intent = null;
			Toast.makeText(getActivity(), getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
		}
			break;
		}
		if (null != intent) {
			startActivity(intent);
		}
	}

	private void inviteDialog() {

		invite_dialog = new Dialog(getActivity());
		invite_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View dialogView = inflater
				.inflate(R.layout.dialog_invite_code_30, null);
		Button sure = (Button) dialogView.findViewById(R.id.sure);
		Button cancel = (Button) dialogView.findViewById(R.id.cancel);
		guideDocNum = (EditText) dialogView
				.findViewById(R.id.input_invite_code_ed);
		invite_dialog.show();
		try {
			int dividerID = getActivity().getResources().getIdentifier(
					"android:id/titleDivider", null, null);
			View divider = invite_dialog.findViewById(dividerID);
			divider.setBackgroundColor(Color.TRANSPARENT);
		} catch (Exception e) {
			// 上面的代码，是用来去除Holo主题的蓝色线条
			e.printStackTrace();
		}
		invite_dialog.setContentView(dialogView);
		sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sureDialog(judgeGuideDoc(guideDocNum));
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				invite_dialog.dismiss();
			}
		});
		invite_dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (invite_dialog != null) {
						invite_dialog.dismiss();
					}
				}
				return false;
			}
		});
	}

	private void sureDialog(boolean isEmpty) {

		if (isEmpty) {
			ValideImpl valideImpl = new ValideImpl();
			RequestParams params = new RequestParams();
			params.addBodyParameter("extensionCode", guideDocNum.getText()
					.toString());
			String uuid = TelephoneUtils.gainUUid(getActivity());
			if (null != uuid && !"".equals(uuid)) {
				// params.put("equipmentCode", uuid);
				params.addBodyParameter("equipmentCode", uuid);
				valideImpl.valideGuideDoc(params, UserFragmentNew30.this);
				((FinalActivity) getActivity()).showProgressToast();
			}
		} else {
			guideDocNum.setText("");
			// guideDocNum.requestFocus();
		}

	}

	private boolean judgeGuideDoc(EditText guideDocNum) {

		String code = guideDocNum.getText().toString();
		if (StringUtils.isEmpty(code)) {
			Toast.makeText(getActivity(), "请输入邀请码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtils.isNumeric(code)) {
			Toast.makeText(getActivity(), "邀请码格式有误，请重新输入", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (code.length() != 6) {
			Toast.makeText(getActivity(), "邀请码应为6位，请重新输入", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}

	public void HintToLogin(final int requestCode) {
		AlertDialog.Builder builder = new Builder(getActivity());
		final AlertDialog dialog = builder.create();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View dialogView = inflater.inflate(R.layout.item_login_dialog, null);
		TextView login = (TextView) dialogView.findViewById(R.id.dialog_login);
		TextView cancel = (TextView) dialogView
				.findViewById(R.id.dialog_cancel);
		dialog.show();
		dialog.setContentView(dialogView);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				startActivityForResult(new Intent(getActivity(),LoginActivity.class), requestCode);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new Gson().fromJson("", OrderCheck.class);
				Gson g=new Gson();
//				new TypeToken<List<Order>>() {}.getType();
				g.fromJson("", new TypeToken<List<Order>>(){}.getType());
			}
		});
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

	}

	@Override
	public void result(Object... object) {

		((FinalActivity) getActivity()).hideProgressToast();
		if (object == null || object.equals("")) {
			return;
		}
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		if (type == null) {
			return;
		}
		switch (type) {
		case HttpParams.GUIDE_DOCNUM: {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (isSuc) {// 服务器验证通过 返回信息
				isSuc = (Boolean) map.get("flag");
				if (isSuc) {// 返回正常信息
					String tipStr = "您的邀请码提交成功！";
					if (map.containsKey("data")) {
						tipStr = String.valueOf(map.get("data"));
					}
					if (map.containsKey("msg")) {
						tipStr = String.valueOf(map.get("msg"));
					}
					Toast.makeText(getActivity(), tipStr, Toast.LENGTH_SHORT)
							.show();
				} else {// 返回错误信息
					String tipStr = String.valueOf(map.get("msg"));
					Toast.makeText(getActivity(), tipStr, Toast.LENGTH_SHORT)
							.show();
				}

			} else {// 服务器异常
				Toast.makeText(getActivity(), String.valueOf(object[3]),
						Toast.LENGTH_LONG).show();
			}
			guideDocNum.clearFocus();
			if (null != invite_dialog && invite_dialog.isShowing()) {
				invite_dialog.dismiss();
			}
		}
			break;
		}

	}

}

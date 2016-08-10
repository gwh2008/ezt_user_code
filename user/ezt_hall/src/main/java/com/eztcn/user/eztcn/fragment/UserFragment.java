package com.eztcn.user.eztcn.fragment;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.activity.mine.FamilyMemberActivity;
import com.eztcn.user.eztcn.activity.mine.MemberCenterActivity;
import com.eztcn.user.eztcn.activity.mine.MyCollectionActivity;
import com.eztcn.user.eztcn.activity.mine.MyHealthCardActivity;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.eztcn.activity.mine.MyOrderListActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.eztcn.activity.mine.MyWalletActivity;
import com.eztcn.user.eztcn.activity.mine.NewMyRecordActivity;
import com.eztcn.user.eztcn.activity.mine.SettingActivity;
import com.eztcn.user.eztcn.activity.mine.UserInfoActivity;
import com.eztcn.user.eztcn.activity.mine.UserRegisterActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.MemberLevel;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.ValideImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JsonUtil;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.eztcn.utils.TelephoneUtils;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.model.PatientBean;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;
/**
 * @title 我
 * @describe
 * @author ezt
 * @created 2014年12月23日
 */
public class UserFragment extends FinalFragment implements OnClickListener,
		DialogCancle, DialogSure, IHttpResult {
	private View rootView;// 缓存Fragment view

	private View baseView;
	private View loginView;
	private TextView register;
	private TextView login;
	private RelativeLayout userinfo;// 用户信息
	private LinearLayout familyPerson;// 家庭成员
	private RelativeLayout sdaDiagnose;// 就医问诊
	private RelativeLayout layoutRoute;// 就医行程
	private RelativeLayout myWallet;// 我的钱包
	private RelativeLayout myWatch;
	// private RelativeLayout memberCenter;// 会员中心
	private RelativeLayout healthbook;// 健康本
	private RelativeLayout setting;// 设置
	// private LinearLayout layoutCollect;// 关注医生
	// private LinearLayout hosCollect;// 收藏医院
	private RoundImageView userPhoto;
	private ImageView imgSetting;// 设置
	private TextView userName;
	private TextView memberlevel;// vip等级
	private TextView userPhone;
	// private TextView attentionCount;
	private ImageView imgQrCode;//
	// private ImageView imgDiDi;//滴滴快车入口图标
	private Bitmap qrCodeBitmap;// 二维码图片
	private PopupWindow pWindow;

	private PatientBean patientBean;
	private Intent intent;
	public TextView right_txt;// 城市
	private int qrCodeWidth;// 二维码图片大小
	// 2015 11 23 导医卡号
	private RelativeLayout guideDoc;
	/**
	 * 邀请码
	 */
	private TextView guideDocNum;
	private MyDialog dialog;
	/**
	 * 是否展现我的手表功能2.4.x的版本无需展示
	 */
	private boolean showWatch = false;
	/**
	 * 2016-1-8 去除 收藏医院 和 关注医生 替代它的是 我的收藏
	 */
	private View myCollection;

	private View readyRegistedLayout;
	private TextView registedNumTV;

	private View notPayLayout;
	private TextView notPayNumTV;

	private View notUseLayout;
	private TextView notUseNumTV;
	private Activity mActivity;

	public static UserFragment newInstance() {
		UserFragment fragment = new UserFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_user, null);
			WindowManager manager = (WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
			qrCodeWidth = (int) (dm.widthPixels * 0.7);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		setTitleBar(rootView);
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
		if (right_txt != null) {
			right_txt
					.setText(TextUtils.isEmpty(BaseApplication.selectCity) ? "选择城市"
							: BaseApplication.selectCity);
		}
	}

	public void init() {
		if (patientBean != null) {
			userName.setText(patientBean.getEpName());
			userPhone.setText(patientBean.getEpMobile());
			int uSex = patientBean.getEpSex();
			if (uSex == 0) {
				userPhoto.setImageResource(R.drawable.userman);
			} else {
				userPhoto.setImageResource(R.drawable.userwomen);
			}
			createQRCode(EZTConfig.QRCodeUrl + "?userName="
					+ patientBean.getEpName());// 生成二维码图片
		}
	}
	/**
	 * 设置会员等级
	 */
	public void setMemberLevel(int levelId) {
		int length = SystemPreferences.getInt("mlevel_length", 0);
		String data = "";
		String vip = null;
		MemberLevel mLevel;
		for (int i = 0; i < length; i++) {
			data = SystemPreferences.getString("memberLevel_" + i);
			mLevel = JsonUtil.fromJson(data, MemberLevel.class);
			if (mLevel != null && mLevel.getId() == levelId) {
				vip = mLevel.getEgName();
				break;
			}
		}
		if (TextUtils.isEmpty(vip)) {
			vip = "VIP0";
		}
		memberlevel.setText(vip);
		memberlevel.setBackground(ResourceUtils.setBackgroundColor(
				Color.parseColor("#ffc451"), 6));
	}

	/**
	 * 设置标题栏
	 * @param view
	 */
	public void setTitleBar(View view) {
		right_txt = (TextView) view.findViewById(R.id.right_btn);
		TextView title = (TextView) view.findViewById(R.id.title_tv);
		TextView back = (TextView) view.findViewById(R.id.left_btn);
		back.setVisibility(View.GONE);
		title.setText("我");
		right_txt.setVisibility(View.VISIBLE);
		Drawable rightDrawable = getResources().getDrawable(
				R.drawable.position_icon);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		right_txt.setCompoundDrawables(rightDrawable, null, null, null);
		right_txt.setCompoundDrawablePadding(5);
		right_txt.setOnClickListener(this);
	}

	/**
	 * 初始化页面
	 * 
	 * @param view
	 */
	public void initView(View view, DisplayMetrics dm) {
		imgSetting = (ImageView) view.findViewById(R.id.img5);// 设置
		if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_UPDATE)
				&& SystemPreferences.getBoolean(EZTConfig.KEY_IS_SETTING, true)) {
			imgSetting.setImageResource(R.drawable.ic_user_setting_point);
		} else {
			imgSetting.setImageResource(R.drawable.ic_user_setting);
		}
		loginView = view.findViewById(R.id.userlogin);
		baseView = view.findViewById(R.id.userBase);

		// 滴滴快车
		// imgDiDi = (ImageView) view.findViewById(R.id.didi_img);
		// imgDiDi.setOnClickListener(this);

		myWatch = (RelativeLayout) view.findViewById(R.id.myWatch);
		if (showWatch) {
			myWatch.setVisibility(View.VISIBLE);
		} else {
			myWatch.setVisibility(View.GONE);
		}

		patientBean = BaseApplication.patient;
		if (patientBean != null) {// 初始化用户信息
			loginView.setVisibility(View.GONE);
			baseView.setVisibility(View.VISIBLE);
			myWatch.setOnClickListener(this);
			imgQrCode = (ImageView) view.findViewById(R.id.userDimension);
			imgQrCode.setOnClickListener(this);

			userPhoto = (RoundImageView) view.findViewById(R.id.userPhoto);
			userName = (TextView) view.findViewById(R.id.userName);
			memberlevel = (TextView) view.findViewById(R.id.memberlevel);
			userPhone = (TextView) view.findViewById(R.id.userPhone);
			userinfo = (RelativeLayout) view.findViewById(R.id.userinfo);
			familyPerson = (LinearLayout) view.findViewById(R.id.familyPerson);
			userinfo.setOnClickListener(this);
			familyPerson.setOnClickListener(this);
			myCollection = view.findViewById(R.id.myCollection);
			myCollection.setOnClickListener(this);

			readyRegistedLayout = view.findViewById(R.id.readyRegistedLayout);
			registedNumTV = (TextView) view.findViewById(R.id.registedNumTV);
			readyRegistedLayout.setOnClickListener(this);

			notPayLayout = view.findViewById(R.id.notPayLayout);
			notPayNumTV = (TextView) view.findViewById(R.id.notPayNumTV);
			notPayLayout.setOnClickListener(this);

			notUseLayout = view.findViewById(R.id.notUseLayout);
			notUseNumTV = (TextView) view.findViewById(R.id.notUseNumTV);
			notUseLayout.setOnClickListener(this);
		} else {
			// imgDiDi.setVisibility(View.GONE);//滴滴快车
			myWatch.setVisibility(View.GONE);
			loginView.setVisibility(View.VISIBLE);
			baseView.setVisibility(View.GONE);
			register = (TextView) view.findViewById(R.id.register);
			login = (TextView) view.findViewById(R.id.login);
			login.getLayoutParams().width = dm.widthPixels / 2;
			register.setOnClickListener(this);
			login.setOnClickListener(this);
		}
		guideDoc = (RelativeLayout) view.findViewById(R.id.guideDoc);
		layoutRoute = (RelativeLayout) view.findViewById(R.id.user_doc_route);
		sdaDiagnose = (RelativeLayout) view.findViewById(R.id.sdaDiagnose);
		myWallet = (RelativeLayout) view.findViewById(R.id.myWallet);
		// memberCenter = (RelativeLayout)
		// view.findViewById(R.id.memberCenter);//
		healthbook = (RelativeLayout) view.findViewById(R.id.healthbook);
		setting = (RelativeLayout) view.findViewById(R.id.setting);

		sdaDiagnose.setOnClickListener(this);
		myWallet.setOnClickListener(this);
		// memberCenter.setOnClickListener(this);//
		healthbook.setOnClickListener(this);
		setting.setOnClickListener(this);
		layoutRoute.setOnClickListener(this);
		guideDoc.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {

		// case R.id.didi_img:// 滴滴优惠券
		// intent.setClass(getActivity(), DiDi_CouponActivity.class);
		// break;

		case R.id.login:
			intent.setClass(getActivity(), LoginActivity.class);
			break;
		case R.id.register:
			intent.setClass(getActivity(), UserRegisterActivity.class);
			break;

		case R.id.userinfo:
			intent.setClass(getActivity(), UserInfoActivity.class);
			break;
		case R.id.familyPerson:
			intent.setClass(getActivity(), FamilyMemberActivity.class);
			break;
		case R.id.user_doc_route:// 就医行程
			Toast.makeText(getActivity(), getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			intent = null;
			// intent.setClass(getActivity(), SeeDocRouteActivity.class);
			break;

		case R.id.sdaDiagnose:// 我的记录
			if (patientBean != null) {
				intent.setClass(getActivity(), MyRecordActivity.class);
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.myWallet:// 我的钱包
			if (patientBean != null) {
				intent.setClass(getActivity(), MyWalletActivity.class);
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.myWatch: {// 我的手表
			if (patientBean != null) {
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
		}
			break;
		case R.id.memberCenter:// 会员中心
			if (patientBean != null) {
				// intent = null;
				intent.setClass(getActivity(), MemberCenterActivity.class);
				intent.putExtra("memberlevel", memberlevel.getText().toString());
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.healthbook:// 健康本
			if (patientBean != null) {
				// intent.setClass(getActivity(), HealthBookActivity.class);
				intent.setClass(getActivity(),
						MyMedicalRecordListActivity.class);// 暂时直接跳入我的病历
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.setting:// 设置
			SystemPreferences.save(EZTConfig.KEY_IS_SETTING, false);
			intent.setClass(getActivity(), SettingActivity.class);
			break;

		case R.id.right_btn:// 选择城市
			startActivityForResult(
					intent.setClass(getActivity(), ChoiceCityActivity.class), 1);
			intent = null;
			break;
		case R.id.myCollection: {
			// 我的收藏 2016-1-8
			// 操作：点击进入【我的收藏】
			// 说明：把关注医生和收藏医院整合到我的收藏
			if (patientBean != null) {
				intent.setClass(getActivity(), MyCollectionActivity.class);
			} else {
				intent = null;
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		case R.id.userDimension:// 二维码点击(放大)
			intent = null;
			openDimensionCode();
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			break;

		case R.id.guideDoc: {// 点击导医
			intent = null;
			View viewContent = View.inflate(getActivity(),
					R.layout.dc_guide_doctor, null);

			guideDocNum = (TextView) viewContent.findViewById(R.id.guideDocNum);

			dialog = new MyDialog(getActivity(), "确定", "取消", "请输入邀请码",
					viewContent);
			dialog.setDialogSure(this);
			dialog.setDialogCancle(this);
			dialog.show();

		}
			break;

		case R.id.notUseLayout: {
			// 操作：点击进入【我的医管家】
			// 说明：此为线上安卓v2.5.4>我的钱包>我的服务，改为我的医管家
			// 数字为购买激活后未使用的套餐卡数量（未使用且未过期）
			intent.setClass(mActivity, MyHealthCardActivity.class);
		}
			break;
		case R.id.notPayLayout: {
			// 操作：点击进入【我的订单】
			// 说明：此为线上安卓v2.5.4>我的钱包>我的订单
			// 数字为我的订单未支付状态的订单条数
			intent.setClass(mActivity, MyOrderListActivity.class);
		}
			break;
		case R.id.readyRegistedLayout: {
			// 操作：点击进入【预约记录】
			// 说明：说明：此为线上安卓v2.5.4>我的记录
			// 数字为预约挂号已预约条数
			intent.setClass(mActivity, NewMyRecordActivity.class);
		}
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 11) {
			City city = (City) data.getSerializableExtra("city");
			if (right_txt != null && city != null) {
				right_txt.setText(city.getCityName());
				BaseApplication.selectCity = city.getCityName();
			}
		}

	}

	/**
	 * 生成二维码
	 */
	private void createQRCode(String info) {
		try {
			if (!TextUtils.isEmpty(info)) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				qrCodeBitmap = EncodingHandler.createQRCode(info, qrCodeWidth);
				imgQrCode.setImageBitmap(qrCodeBitmap);
			} else {
				Toast.makeText(getActivity(), "Text can not be empty",
						Toast.LENGTH_SHORT).show();
			}

		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示App二维码
	 */
	public void openDimensionCode() {
		View view = LinearLayout.inflate(getActivity(),
				R.layout.dialog_scan_img, null);
		TextView hint = (TextView) view.findViewById(R.id.hint);
		hint.setVisibility(View.VISIBLE);
		ImageView imgScan = (ImageView) view.findViewById(R.id.scan_img);
		imgScan.setImageBitmap(qrCodeBitmap);
		showPopupWindow(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (pWindow != null) {
					pWindow.dismiss();
				}
			}
		});
	}

	private void showPopupWindow(View view) {
		pWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		ColorDrawable color_bg = new ColorDrawable(0xb0000000);
		pWindow.setBackgroundDrawable(color_bg);
		pWindow.setAnimationStyle(R.style.PopupAnimation);
		pWindow.update();
		pWindow.setFocusable(true);
		// 设置点击其他地方 就消失
		pWindow.setOutsideTouchable(true);
	}

	private boolean judgeGuideDoc() {
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

	@Override
	public void dialogSure() {
		if (judgeGuideDoc()) {
			ValideImpl valideImpl = new ValideImpl();
			// HashMap<String, Object> params = new HashMap<String, Object>();
			// params.put("extensionCode", guideDocNum.getText().toString());
			RequestParams params = new RequestParams();
			params.addBodyParameter("extensionCode", guideDocNum.getText()
					.toString());

			String uuid = TelephoneUtils.gainUUid(getActivity());
			if (null != uuid && !"".equals(uuid)) {
				// params.put("equipmentCode", uuid);
				params.addBodyParameter("equipmentCode", uuid);
				valideImpl.valideGuideDoc(params, UserFragment.this);
				((FinalActivity) getActivity()).showProgressToast();
			}
		} else {
			guideDocNum.setText("");
			// guideDocNum.requestFocus();
		}

	}

	@Override
	public void dialogCancle() {
		guideDocNum.clearFocus();
		dialog.dissMiss();
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
			dialog.dissMiss();
		}
			break;
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((FinalActivity) getActivity()).hideProgressToast();
	}

}

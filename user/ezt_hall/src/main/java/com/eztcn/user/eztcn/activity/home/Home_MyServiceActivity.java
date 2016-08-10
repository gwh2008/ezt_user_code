/**
 * 
 */
package com.eztcn.user.eztcn.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.AttentDocActivity;
import com.eztcn.user.eztcn.activity.mine.AttentHosActivity;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.activity.mine.FamilyMemberActivity;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.eztcn.activity.mine.MyWalletActivity;
import com.eztcn.user.eztcn.activity.mine.SettingActivity;
import com.eztcn.user.eztcn.activity.mine.UserInfoActivity;
import com.eztcn.user.eztcn.activity.mine.UserRegisterActivity;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.hall.activity.loginSetting.LoginActivity;
import com.eztcn.user.hall.model.PatientBean;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

/**
 * @author EZT
 * 
 */
public class Home_MyServiceActivity extends FinalActivity implements
		OnClickListener {
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
	private RelativeLayout healthbook;// 健康本
	private RelativeLayout setting;// 设置
	private LinearLayout layoutCollect;// 关注医生
	private LinearLayout hosCollect;// 收藏医院
	private RoundImageView userPhoto;
	private ImageView imgSetting;// 设置
	private TextView userName;
	private TextView userPhone;
	private TextView attentionCount;
	private ImageView imgQrCode;//
	private Bitmap qrCodeBitmap;// 二维码图片
	private PopupWindow pWindow;

	//private EztUser eztUser;
	private PatientBean patientBean;

	private Intent intent;
	public TextView right_txt;// 城市
	private int qrCodeWidth;// 二维码图片大小
	private final int GONE = View.GONE;
	private final int VISIBLE = View.VISIBLE;
	private boolean showWatch = false;
	@ViewInject(R.id.myWatch)
	private RelativeLayout myWatch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_user);
		ViewUtils.inject(Home_MyServiceActivity.this);
		if (showWatch) {
			myWatch.setVisibility(View.VISIBLE);
		} else {
			myWatch.setVisibility(View.GONE);
		}
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		qrCodeWidth = (int) (dm.widthPixels * 0.7);
		setTitleBar(rootView);

	}

	@Override
	public void onResume() {
		super.onResume();
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		initView(dm);
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
	 * 设置标题栏
	 * 
	 * @param view
	 */
	public void setTitleBar(View view) {

		right_txt = (TextView) findViewById(R.id.right_btn);
		TextView title = (TextView) findViewById(R.id.title_tv);
		TextView back = (TextView) findViewById(R.id.left_btn);
		back.setVisibility(VISIBLE);
		back.setOnClickListener(this);
		title.setText("我的服务");
		right_txt.setVisibility(VISIBLE);
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
	public void initView(DisplayMetrics dm) {
		imgSetting = (ImageView) findViewById(R.id.img5);// 设置
		if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_UPDATE)
				&& SystemPreferences.getBoolean(EZTConfig.KEY_IS_SETTING, true)) {
			imgSetting.setImageResource(R.drawable.ic_user_setting_point);
		} else {
			imgSetting.setImageResource(R.drawable.ic_user_setting);
		}
		loginView = findViewById(R.id.userlogin);
		baseView = findViewById(R.id.userBase);
		patientBean = BaseApplication.patient;
		if (patientBean != null) {
			loginView.setVisibility(GONE);
			baseView.setVisibility(VISIBLE);

			imgQrCode = (ImageView) findViewById(R.id.userDimension);
			imgQrCode.setOnClickListener(this);

			userPhoto = (RoundImageView) findViewById(R.id.userPhoto);
			userName = (TextView) findViewById(R.id.userName);
			userPhone = (TextView) findViewById(R.id.userPhone);
//			attentionCount = (TextView) findViewById(R.id.attentionCount);
			userinfo = (RelativeLayout) findViewById(R.id.userinfo);
			familyPerson = (LinearLayout) findViewById(R.id.familyPerson);
			userinfo.setOnClickListener(this);
			familyPerson.setOnClickListener(this);
//			layoutCollect = (LinearLayout) findViewById(R.id.layout_collect_doc);
//			hosCollect = (LinearLayout) findViewById(R.id.layout_collect_hos);
			layoutCollect.setOnClickListener(this);
			hosCollect.setOnClickListener(this);

		} else {
			loginView.setVisibility(VISIBLE);
			baseView.setVisibility(GONE);
			register = (TextView) findViewById(R.id.register);
			login = (TextView) findViewById(R.id.login);
			login.getLayoutParams().width = dm.widthPixels / 2;
			register.setOnClickListener(this);
			login.setOnClickListener(this);

		}

		layoutRoute = (RelativeLayout) findViewById(R.id.user_doc_route);
		sdaDiagnose = (RelativeLayout) findViewById(R.id.sdaDiagnose);
		myWallet = (RelativeLayout) findViewById(R.id.myWallet);
		healthbook = (RelativeLayout) findViewById(R.id.healthbook);
		setting = (RelativeLayout) findViewById(R.id.setting);

		sdaDiagnose.setOnClickListener(this);
		myWallet.setOnClickListener(this);
		healthbook.setOnClickListener(this);
		setting.setOnClickListener(this);
		layoutRoute.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		intent = new Intent();
		switch (v.getId()) {

		case R.id.login:
			intent.setClass(this, LoginActivity.class);
			break;
		case R.id.register:
			intent.setClass(this, UserRegisterActivity.class);
			break;

		case R.id.userinfo:
			intent.setClass(this, UserInfoActivity.class);
			break;
		case R.id.familyPerson:
			intent.setClass(this, FamilyMemberActivity.class);
			break;
		case R.id.user_doc_route:// 就医行程
			Toast.makeText(this, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			intent = null;
			// intent.setClass(this, SeeDocRouteActivity.class);
			break;

		case R.id.sdaDiagnose:// 我的记录
			if (patientBean != null) {
				intent.setClass(this, MyRecordActivity.class);
			} else {
				intent = null;
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.myWallet:// 我的钱包
			if (patientBean != null) {
				intent.setClass(this, MyWalletActivity.class);
			} else {
				intent = null;
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.healthbook:// 健康本
			if (patientBean != null) {
				// intent.setClass(this, HealthBookActivity.class);
				intent.setClass(this, MyMedicalRecordListActivity.class);// 暂时直接跳入我的病历
			} else {
				intent = null;
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.setting:// 设置
			SystemPreferences.save(EZTConfig.KEY_IS_SETTING, false);
			intent.setClass(this, SettingActivity.class);
			break;

		case R.id.right_btn:// 选择城市
			startActivityForResult(
					intent.setClass(this, ChoiceCityActivity.class), 1);
			intent = null;
			break;
		case R.id.userDimension:// 二维码点击(放大)
			intent = null;
			openDimensionCode();
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
			break;

		case R.id.left_btn: {
			intent = null;
			Home_MyServiceActivity.this.finish();
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
				Toast.makeText(this, "Text can not be empty",
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
		View view = LinearLayout.inflate(this, R.layout.dialog_scan_img, null);
		TextView hint = (TextView) findViewById(R.id.hint);
		hint.setVisibility(VISIBLE);
		ImageView imgScan = (ImageView) findViewById(R.id.scan_img);
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

}

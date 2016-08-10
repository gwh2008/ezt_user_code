package com.eztcn.user.eztcn.activity;

import java.util.ArrayList;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.home.InformationFragment;
import com.eztcn.user.eztcn.activity.mine.UserInfoActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.UpManager;
import com.eztcn.user.eztcn.controller.VersionManager;
import com.eztcn.user.eztcn.customView.NoScrollViewPager;
import com.eztcn.user.eztcn.customView.tabview.UITabBottom;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.fragment.DiscoverFragmentNew30;

import com.eztcn.user.eztcn.fragment.HomeFragment30;
import com.eztcn.user.hall.fragment.HomeFragment;

import com.eztcn.user.eztcn.fragment.ServiceFragment30;
import com.eztcn.user.eztcn.fragment.UserFragmentNew30;
import com.eztcn.user.eztcn.impl.SoftUpdateImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

import com.zxing.activity.QrcodeActivity;

/**
 * @title 主activity
 * @describe
 * @author ezt
 * @created 2014年11月24日
 */
public class MainActivity extends FinalActivity implements
		OnPageChangeListener, android.view.View.OnClickListener, IHttpResult {

	@ViewInject(R.id.home_tab_bottom)
	public UITabBottom mUiTabBottom;// 底部导航栏

	@ViewInject(R.id.home_view_page)
	private NoScrollViewPager mUiViewPager;

	// @ViewInject(R.id.guide_layout, click = "onClick")
	// private RelativeLayout layoutGuide;// 关闭引导窗口

	@ViewInject(R.id.info_layout)
	private LinearLayout infoLayout;

	private static MainActivity instance = null;

	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(MainActivity.this);
		initFragments();
		mUiTabBottom.setmViewPager(mUiViewPager);
		mUiViewPager.setAdapter(fragmentAdapter);
		mUiViewPager.setOnPageChangeListener(this);
		instance = this;

		// // 引导框
		// if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_FIRST)) {
		// layoutGuide.setVisibility(View.VISIBLE);
		// }
//		// 个推
//		PushManager.getInstance().initialize(this.getApplicationContext());
		if (BaseApplication.getInstance().isUpdateFirst) {
			checkVersion();
		}

	}

	public static MainActivity getInstance() {
		return instance;
	}

	@Override
	protected void onResume() {
		// int selectIndex = getIntent().getIntExtra("selectIndex", 0);
		// if (selectIndex != 0) {
		// mUiTabBottom.tabIntent(selectIndex);
		// }
		super.onResume();
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragments() {

		fragments.add(HomeFragment30.newInstance());// 首页

		// fragments.add(FDoctorFragment.newInstance());// 找医
		// fragments.add(DiscoverFragment.newInstance());// 发现
		// fragments.add(ENurseHelpFragment.newInstance());// e护帮
		// fragments.add(EManageFragment.newInstance());// e护帮

		fragments.add(ServiceFragment30.newInstance());// 服务

		fragments.add(DiscoverFragmentNew30.newInstance());// 发现

		// fragments.add(SendMedicineFragment.newInstance());// 药给送
		// fragments.add(MsgManageFragment.newInstance());// 消息
		fragments.add(InformationFragment.newInstance());//

		fragments.add(UserFragmentNew30.newInstance());// 我
	}

	FragmentPagerAdapter fragmentAdapter = new FragmentPagerAdapter(
			getSupportFragmentManager()) {

		@Override
		public Fragment getItem(int i) {
			return fragments.get(i);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}
	};

	public void comingMsg() {
		// mUiTabBottom.setTabRedDot(2, true);
	}

	public void noMsg() {
		// mUiTabBottom.setTabRedDot(2, false);
	}

	@Override
	public void onPageScrolled(int pageIndex, float v, int i2) {
		mUiTabBottom.scroll(pageIndex, v);
		if (pageIndex == 3) {// 我频道
			SystemPreferences.save(EZTConfig.KEY_IS_USER, false);
			mUiTabBottom.setTabRedDot(3, false);
		} else if (pageIndex == 2) {// 消息
			mUiTabBottom.setTabRedDot(2, false);
		}
	}

	@Override
	public void onPageSelected(int i) {
		mUiTabBottom.selectTab(i);
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	/**
	 * 检查更新
	 */
	public void checkVersion() {
		// AjaxParams params = new AjaxParams();
		// params.put("type", "1");

		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "1");

		new SoftUpdateImpl().getSoftVersion(params, this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (requestCode == 0 && data != null) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			int type = -1;
			String title = "";
			if (scanResult.contains(EZTConfig.QRCodeUrl + "?userName=")) {
				type = 0;
			} else if (scanResult.length() > "http://".length()
					&& ("http://".equalsIgnoreCase(scanResult.substring(0,
							"http://".length())) || ("https://"
							.equalsIgnoreCase(scanResult.substring(0,
									"https://".length()))))) {// 网址
				type = 1;
				title = "是否用浏览器打开？";

			}
			// vcard格式和mecard均为电子名片格式
			// else if (scanResult.length() > "BEGIN:VCARD".length()
			// && "BEGIN:VCARD".equalsIgnoreCase(scanResult.substring(0,
			// "BEGIN:VCARD".length()))) {
			// // qrcode.setType(2);//
			// scanResult = "联系人" + scanResult;
			// } else if (scanResult.length() > "MECARD".length()
			// && "MECARD".equalsIgnoreCase(scanResult.substring(0,
			// "MECARD".length()))) {
			// scanResult = "联系人"
			// + scanResult
			// .replaceAll(";;", ";")
			// .replaceAll(";", ";\n")
			// .replace("MECARD:",
			// "BEGIN:VCARD\nVERSION:2.1\n")
			// + "\nEND:VCARD";// 将mecard格式变成vcard格式
			// }

			else {
				type = -1;
				scanResult = "二维码格式不正确，请检查图片来源是否可靠或重新扫描！";
			}
			ScanResultDialog(title, type, scanResult);

		}

	}

	/**
	 * 二维码处理跳转
	 */
	private void ScanResultDialog(String title, int type, final String result) {
		if (type == 0) {// 平台跳转
			// EztUser user = JsonUtil.fromJson(result, EztUser.class);
			String splitStr = "userName=";
			int index = result.lastIndexOf(splitStr);
			String strName = result.substring(index + splitStr.length(),
					result.length());
			// Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
			EztUser user = new EztUser();
			user.setUserName(strName);
			Intent intent = new Intent(mContext, UserInfoActivity.class);
			intent.putExtra("userinfo", user);
			intent.putExtra("flag", 1);
			startActivity(intent);

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

			if (type == 1) {// 网址
				builder.setTitle(title);
				builder.setMessage("Url:" + result);
				builder.setPositiveButton("继续访问", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(result)));
					}
				}).setNegativeButton("取消", null).create().show();
			} else {// 其他
				builder.setMessage(result);
				builder.setPositiveButton("重新扫描", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(new Intent(mContext,
								QrcodeActivity.class), 0);
					}
				}).create().show();
			}

		}

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
            return true;//返回true，把事件消费掉，不会继续调用onBackPressed
		}
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_UP) {

            return true;//返回true，把事件消费掉，不会继续调用onBackPressed
        }
		return super.dispatchKeyEvent(event);
	}

    @Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null || object.equals("")) {
			return;
		}
		Integer type = (Integer) object[0];
		if (type == null) {
			return;
		}
		if (type == HttpParams.SOFT_VERSION) {// 获取服务器版本信息
			boolean status = (Boolean) object[1];
			if (!status) {
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> versionMap = (Map<String, Object>) object[2];
			if (versionMap == null || versionMap.size() == 0) {

				return;
			}
			SoftVersion version = (SoftVersion) versionMap.get("version");
			if (null != version) {
				int vNum = VersionManager.getVersionCode(this);
				int force = version.getForce();
				try {
					if (vNum < version.getVersionNum()) {
						UpManager up = new UpManager(getApplicationContext(),
								version);
						up.checkUpdataInfo(force,mContext);
						// BaseApplication.getInstance().isUpdateFirst = false;
					}

				} catch (Exception e) {

				}
			}
			// 初始化我频道图标
			if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_UPDATE)
					&& SystemPreferences
							.getBoolean(EZTConfig.KEY_IS_USER, true)) {
				// mUiTabBottom.setTabRedDot(3, true);
			}

		}

	}

	@Override
	public void onClick(View v) {
		SystemPreferences.save(EZTConfig.KEY_IS_FIRST, false);
		// layoutGuide.setVisibility(View.GONE);
		infoLayout.setClickable(true);
	}

	public ArrayList<Fragment> getFragments() {
		return fragments;
	}

}

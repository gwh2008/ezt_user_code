package com.eztcn.user.eztcn.activity.mine;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.GuidanceActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.UpManager;
import com.eztcn.user.eztcn.controller.VersionManager;
import com.eztcn.user.eztcn.customView.UmengCustomShareBoard;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.SoftUpdateImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 关于医指通
 * @describe
 * @author ezt
 * @created 2015年1月14日
 */
public class AboutEZTActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.versionUpdate)
	private TextView versionUpdate;

	@ViewInject(R.id.versionUpdate_layout)//, click = "onClick"
	private RelativeLayout layoutVersionUpdate;// 检测更新

	@ViewInject(R.id.about_grade_tv)//, click = "onClick"
	private TextView tvGrade;// 评分

	@ViewInject(R.id.about_function_intr_tv)//, click = "onClick"
	private TextView tvFunction;// 功能介绍

	@ViewInject(R.id.about_suggest_tv)//, click = "onClick"
	private TextView tvSuggest;// 建议

	@ViewInject(R.id.about_share_tv)//, click = "onClick"
	private TextView tvShare;// 软件分享

	@ViewInject(R.id.about_vnum_tv)
	private TextView tvVNum;// 版本号

	@ViewInject(R.id.hotLine)//, click = "onClick"
	private TextView hotLine;// 客服热线

	private UpManager up;
	private Builder builder;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutezt);
		ViewUtils.inject(AboutEZTActivity.this);
		loadTitleBar(true, "关于医指通", null);
		String vNum = VersionManager.getVersionName(getApplicationContext());
		tvVNum.setText("门诊大厅" + vNum);
		builder = new AlertDialog.Builder(this);
		// versionUpdate
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (SystemPreferences.getBoolean(EZTConfig.KEY_IS_UPDATE)) {
				Drawable rightDrawable = getResources().getDrawable(
						R.drawable.ic_red_point);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				versionUpdate.setCompoundDrawables(null, null, rightDrawable,
						null);
				versionUpdate.setCompoundDrawablePadding(ResourceUtils.dip2px(
						mContext, ResourceUtils.getXmlDef(mContext,
								R.dimen.medium_margin)));
			} else {
				versionUpdate.setCompoundDrawables(null, null, null, null);
			}
		}
		super.onWindowFocusChanged(hasFocus);
	}

	@OnClick(R.id.versionUpdate_layout)// 检查更新
	private void versionUpdateClick(View v){
		RequestParams params = new RequestParams();
		params.addBodyParameter("type", "1");
		new SoftUpdateImpl().getSoftVersion(params, this);
		showProgressToast();
		versionUpdate.setEnabled(false);
	}
	
	@OnClick(R.id.about_grade_tv)// 评分
	private void about_grade_tvClick(View v){
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@OnClick(R.id.about_function_intr_tv)// 功能介绍
	private void about_function_intr_tvClick(View v){
		intent = new Intent(this, GuidanceActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.about_suggest_tv)// 建议或投诉
	private void about_suggest_tvClick(View v){
		intent = new Intent(this, TicklingContentActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.about_share_tv)//  软件分享
	private void about_share_tvClick(View v){
		// 设置软件分享的内容
		setShareContent(getString(R.string.app_share_info),
				getString(R.string.app_name),
				"http://app.eztcn.com/WHMS_Android/toEztAppDown.ezt", null,
				BitmapFactory.decodeResource(getResources(),
						R.drawable.ezt_window));
		UmengCustomShareBoard shareBoard = new UmengCustomShareBoard(
				mContext, false);
		shareBoard.showAtLocation(mContext.getWindow().getDecorView(),
				Gravity.BOTTOM, 0, 0);
	}
	
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.versionUpdate_layout:// 检查更新
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("type", "1");
//			new SoftUpdateImpl().getSoftVersion(params, this);
//			showProgressToast();
//			versionUpdate.setEnabled(false);
//			break;
//		case R.id.about_grade_tv:// 评分
//			Uri uri = Uri.parse("market://details?id=" + getPackageName());
//			intent = new Intent(Intent.ACTION_VIEW, uri);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			break;

//		case R.id.about_function_intr_tv:// 功能介绍
//			intent = new Intent(this, GuidanceActivity.class);
//			startActivity(intent);
//			break;

//		case R.id.about_suggest_tv:// 建议或投诉
//			intent = new Intent(this, TicklingContentActivity.class);
//			startActivity(intent);
//			break;

//		case R.id.about_share_tv:// 软件分享
//			// 设置软件分享的内容
//			setShareContent(getString(R.string.app_share_info),
//					getString(R.string.app_name),
//					"http://app.eztcn.com/WHMS_Android/toEztAppDown.ezt", null,
//					BitmapFactory.decodeResource(getResources(),
//							R.drawable.ezt_window));
//			UmengCustomShareBoard shareBoard = new UmengCustomShareBoard(
//					mContext, false);
//			shareBoard.showAtLocation(mContext.getWindow().getDecorView(),
//					Gravity.BOTTOM, 0, 0);
//
//			break;
//
//		}
//	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (!versionUpdate.isEnabled()) {
			versionUpdate.setEnabled(true);
		}
		if (object == null || object.equals("")) {
			Toast.makeText(getApplicationContext(), "检查新版本失败",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int type = (Integer) object[0];
		boolean bool = (Boolean) object[1];
		if (!bool) {
			Toast.makeText(getApplicationContext(), "检查新版本失败",
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (type) {
		case HttpParams.POST_ERROR:
			Toast.makeText(getApplicationContext(), "已是最新版本",
					Toast.LENGTH_SHORT).show();

			if (!versionUpdate.isEnabled()) {
				versionUpdate.setEnabled(true);
			}
			break;

		case HttpParams.SOFT_VERSION:// 获取服务器版本信息
			versionUpdate.setEnabled(true);

			if (object[2] == null || object[2] instanceof String) {
				Toast.makeText(getApplicationContext(), "检查新版本失败",
						Toast.LENGTH_SHORT).show();
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> versionMap = (Map<String, Object>) object[2];
			if (versionMap == null || versionMap.size() == 0) {

				return;
			}
			SoftVersion version = (SoftVersion) versionMap.get("version");
			
			int vNum = VersionManager.getVersionCode(this);
			try {
				if (vNum >= version.getVersionNum()) {
					builder.setTitle("提示");
					builder.setMessage("此版本为最新版本，无需更新");
					builder.setPositiveButton("确定", null);
					builder.create().show();
				} else {
					up = new UpManager(AboutEZTActivity.this, version);
//					up.checkUpdataInfo(version.getForce());
					up.checkUpdataInfo(0,mContext);
				}
			} catch (Exception e) {
				builder.setTitle("提示");
				builder.setMessage("此版本为最新版本，无需更新");
				builder.setPositiveButton("确定", null);
				builder.create().show();
			}
			break;
		}

	}
}

package com.eztcn.user.eztcn.customView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.utils.OauthHelper;

/**
 * @title umeng 分享自定义弹出框
 * @describe
 * @author ezt
 * @created 2015年1月26日
 */
public class UmengCustomShareBoard extends PopupWindow implements
		OnClickListener {

	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(EZTConfig.DESCRIPTOR);
	private Activity mActivity;

	public UmengCustomShareBoard(Activity activity, boolean isDiDi) {
		super(activity);
		this.mActivity = activity;
		initView(activity, isDiDi);
	}

	@SuppressWarnings("deprecation")
	private void initView(Context context, boolean isDiDi) {

		View rootView = LayoutInflater.from(context).inflate(
				R.layout.umeng_custom_board, null);

		LinearLayout layoutWeChat = (LinearLayout) rootView
				.findViewById(R.id.wechat);
		LinearLayout layoutCircle = (LinearLayout) rootView
				.findViewById(R.id.wechat_circle);
		LinearLayout layoutQzone = (LinearLayout) rootView
				.findViewById(R.id.qzone);
		LinearLayout layoutSina = (LinearLayout) rootView
				.findViewById(R.id.microBlog_sina);

		layoutWeChat.setOnClickListener(this);
		layoutCircle.setOnClickListener(this);
		layoutQzone.setOnClickListener(this);
		layoutSina.setOnClickListener(this);

		if (isDiDi) {
			layoutCircle.setVisibility(View.INVISIBLE);
			layoutQzone.setVisibility(View.INVISIBLE);
			layoutSina.setVisibility(View.INVISIBLE);
		}
		setContentView(rootView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
		setAnimationStyle(R.style.PopupAnimation);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wechat:// 微信
			performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.wechat_circle:// 朋友圈
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.qzone:// 空间
			performShare(SHARE_MEDIA.QZONE);
			break;

		case R.id.microBlog_sina:// 新浪微博
			performShare(SHARE_MEDIA.SINA);
			break;

		default:
			break;
		}

		dismiss();
	}

	/**
	 * 授权验证
	 * 
	 * @param activity
	 * @param platForm
	 * @param mController
	 * @param handler
	 */
	private void auth(SHARE_MEDIA platForm) {
		if (!OauthHelper.isAuthenticated(mActivity, platForm)) {
			mController.doOauthVerify(mActivity, platForm,
					new UMAuthListener() {
						@Override
						public void onStart(SHARE_MEDIA platform) {
							Toast.makeText(mActivity, "授权开始",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(SocializeException e,
								SHARE_MEDIA platform) {
							Toast.makeText(mActivity, "授权错误",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onComplete(Bundle value,
								SHARE_MEDIA platform) {
							Toast.makeText(mActivity, "授权完成",
									Toast.LENGTH_SHORT).show();
							// 获取相关授权信息或者跳转到自定义的分享编辑页面
							String uid = value.getString("uid");
							share(platform);
						}

						@Override
						public void onCancel(SHARE_MEDIA platform) {
							Toast.makeText(mActivity, "授权取消",
									Toast.LENGTH_SHORT).show();
						}
					});
		}else{
			share(platForm);
		}
	}

	private void performShare(SHARE_MEDIA platform) {
		// 微博、腾讯微博、豆瓣、人人网、QQ在执行分享前需要先进行授权操：
		if (platform != SHARE_MEDIA.WEIXIN
				&& platform != SHARE_MEDIA.WEIXIN_CIRCLE) {
			auth(platform);
		} else {
			share(platform);
		}

		// mController.postShare(mActivity, platform, new SnsPostListener() {
		//
		// @Override
		// public void onStart() {
		//
		// }
		//
		// @Override
		// public void onComplete(SHARE_MEDIA platform, int eCode,
		// SocializeEntity entity) {
		// String showText = platform.toString();
		// if (eCode == StatusCode.ST_CODE_SUCCESSED) {
		// showText += "平台分享成功";
		// } else {
		// showText += "平台分享失败";
		// }
		// Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
		// dismiss();
		// }
		// });
	}

	private void share(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";
				} else {
					showText += "平台分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}
}

package com.eztcn.user.eztcn.controller;

import java.io.File;

import xutils.HttpUtils;
import xutils.exception.HttpException;
import xutils.http.HttpHandler;
import xutils.http.ResponseInfo;
import xutils.http.callback.RequestCallBack;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.bean.SoftVersion;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.utils.AppManager;
import com.igexin.sdk.PushManager;

/**
 * @title 软件更新控制类
 * @describe 检测是否有新版本，下载应用
 * @author ezt
 * @created 2014年10月28日
 */

public class UpManager {

	private Context mContext;
	// private Map map=new HashMap();
	private String updateMsg = "";
	private String apkUrl = "";
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	private static final String savePath = "mnt/sdcard/eztcn/";
//	public static final String saveFileName = savePath + "ezthall.apk";
	public static final String saveFileName = savePath + "eztuser.apk";
	private ProgressBar mProgressBar;
	private TextView currentPosition;
	private static final int DOWN_UPDATA = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_ERROR = 3;
	private static final int DOWN_STOP = 4;
	private static final int DOWN_INSTALL = 5;// 安装
	// private int ERROR_NO;
	private int progress;
	private AppManager appManager;
	private HttpHandler<File> httpHandler;
	private float versionCode;
	private String fileName;

	private int uForce;// 强制更新

	/****** 通知栏 ********************************/
	public NotificationManager mNotificationManager;
	private Notification mNotification;
	private int update_type;

	public UpManager(Context context, SoftVersion version) {
		this.mContext = context;
		apkUrl = version.getUrl();
		versionCode = version.getVersionNum();
		String str = version.getContent();
		str = str.replaceAll("#", "\n");
		fileName = version.getVersionName();
		updateMsg = fileName + "\n" + "更新说明：" + str + "\n" + "发布时间："
				+ version.getTime();
		appManager = AppManager.getAppManager();
	}

	public void checkUpdataInfo(int force,Context context) {
		SystemPreferences.save(EZTConfig.KEY_IS_UPDATE, true);
		this.uForce = force;
		showNoticeDialog(context);
	}

	private void showNoticeDialog(final Context context) {
		String btnName = "以后再说";
		if (uForce == 1) {
			btnName = "退出应用";
		} else {
			btnName = "以后再说";
		}
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("发现新版本！").setMessage(updateMsg);
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				if (judgeDownLoaded(saveFileName)) {
					installApk(context);
				} else {
					showDownloadDialog();
				}
				SystemPreferences.save(EZTConfig.KEY_IS_UPDATE, false);
			}
		}).setNegativeButton(btnName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				BaseApplication.getInstance().isUpdateFirst = true;
				if (uForce == 1) {
					PushManager.getInstance().stopService(mContext);// 停止个推服务
					AppManager.getAppManager().AppExit(mContext);
					dialog.dismiss();

				} else {
					SystemPreferences.save("update_status", false);
					dialog.dismiss();
				}

			}
		}).setCancelable(false);
		noticeDialog = builder.create();
		noticeDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
					if (uForce == 1) {
						PushManager.getInstance().stopService(mContext);// 停止个推服务
						AppManager.getAppManager().AppExit(mContext);
						dialog.dismiss();
					}
				}
				return false;
			}
		});

		noticeDialog.show();
	}

	/**
	 * 更新页面
	 */
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.updataprogressbar, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
		currentPosition = (TextView) view.findViewById(R.id.progress);
		builder.setView(view);
		if (uForce == 0) {
			builder.setPositiveButton("后台更新",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							update_type = 1;
							setUpNotification();
							if (downloadDialog != null) {
								downloadDialog.dismiss();
							}

						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							BaseApplication.getInstance().isUpdateFirst = true;
							if (httpHandler != null) {
//								httpHandler.stop();
								httpHandler.cancel();
							}
							
							update_type = 0;
						}
					}).setCancelable(false);
		}
		downloadDialog = builder.create();
		if (uForce == 1) {
			downloadDialog.setCancelable(false);
		}
		downloadDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (dialog != null) {
					BaseApplication.getInstance().isUpdateFirst = true;
					dialog.dismiss();
				}
				if (uForce == 1) {
					BaseApplication.getInstance().isUpdateFirst = true;
					PushManager.getInstance().stopService(mContext);// 停止个推服务
					AppManager.getAppManager().AppExit(mContext);
					dialog.dismiss();
				}
			}
		});
		downloadDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						BaseApplication.getInstance().isUpdateFirst = true;
						dialog.dismiss();
					}
					if (uForce == 1) {
						BaseApplication.getInstance().isUpdateFirst = true;
						PushManager.getInstance().stopService(mContext);// 停止个推服务
						AppManager.getAppManager().AppExit(mContext);
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		downloadDialog.show();
		downLoadApk();
	}

	/**
	 * 创建通知
	 */
	@SuppressWarnings("deprecation")
	public void setUpNotification() {
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		icon = android.R.drawable.stat_sys_download;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(mContext.getPackageName(),
				R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, fileName + " 正在下载...");
		// 指定个性化视图
		mNotification.contentView = contentView;
		// Intent intent = new Intent(mContext, DownloadListActivity.class);
		// intent.putExtra("name", fileName);
		// intent.putExtra("path", savePath + apkName + ".apk");
		// 下面两句是 在按home后，点击通知栏，返回之前activity 状态;
		// 有下面两句的话，假如service还在后台下载， 在点击程序图片重新进入程序时，直接到下载界面，相当于把程序MAIN 入口改了 - -
		// 是这么理解么。。。
		// intent.setAction(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(0, mNotification);
	}

	Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWN_UPDATA:
				if (update_type == 1) {// 后台更新
					if (progress <= 100) {
						RemoteViews contentview = mNotification.contentView;
						contentview.setTextViewText(R.id.tv_notife_progress,
								progress + "%");
						contentview.setProgressBar(R.id.notife_progressbar,
								100, progress, false);

						// if (progress == 100) {
						// mHandler.sendEmptyMessageDelayed(2, 500);
						// }
						mNotificationManager.notify(0, mNotification);
					}
				} else {
					mProgressBar.setProgress(progress);
					currentPosition.setText(progress + "%");
				}
				break;
			case DOWN_ERROR:
				if (update_type == 1) {// 后台更新
					closeNotify();
				} else {
					downloadDialog.dismiss();
				}
				Toast.makeText(mContext, "下载异常！", Toast.LENGTH_SHORT).show();
				break;
			case DOWN_OVER:
				if (update_type == 1) {// 后台更新
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.contentView = null;
					update_type = 0;
					File apkfile = new File(saveFileName);
					if (!apkfile.exists()) {
						return;
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(
							Uri.parse("file://" + apkfile.toString()),
							"application/vnd.android.package-archive");
					// 更新参数,注意flags要使用FLAG_UPDATE_CURRENT
					PendingIntent contentIntent = PendingIntent.getActivity(
							mContext.getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
//					mNotification.setLatestEventInfo(mContext.getApplicationContext(), "下载完成","文件已下载完毕", contentIntent);
					mNotificationManager.notify(0, mNotification);
					mHandler.sendEmptyMessage(DOWN_INSTALL);
				} else {
					downloadDialog.dismiss();
					installApk(mContext);
				}
				SystemPreferences.save("update_status", true);
				break;
			case DOWN_STOP:
				closeNotify();
				Toast.makeText(mContext, "下载取消！", Toast.LENGTH_SHORT).show();
				break;
			case DOWN_INSTALL:
				// 取消通知
				mNotificationManager.cancel(0);
				installApk(mContext);
				// closeNotify();
				break;
			default:
				break;
			}
		};
	};
	/**
	 * 关闭通知栏
	 */
	public void closeNotify() {
		if (mNotificationManager != null) {
			// mNotification = null;
			update_type = 0;
			mNotificationManager.cancel(0);
		}
	}
	public void downLoadApk() {
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 为防止下载出错误的apk，在下载进度条加载前直接删除apk安装包
		File file_old = new File(UpManager.saveFileName);
		if (file_old.exists()) {
			file_old.delete();
		}

//		FinalHttp http = FinalHttp.getInstance();
		HttpUtils httpUtils=new HttpUtils(EZTConfig.TIMEOUT);
		httpHandler=httpUtils.download(apkUrl, saveFileName, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				mHandler.sendEmptyMessage(DOWN_OVER);
			}
			@Override
			public void onLoading(long count, long current, boolean isUploading) {
				// TODO 自动生成的方法存根
				super.onLoading(count, current, isUploading);
				
				progress = (int) (((float) current / count) * 100);
				mHandler.sendEmptyMessage(DOWN_UPDATA);
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				int errorNo=error.getExceptionCode();
				if (errorNo == 416) {
					installApk(mContext);
					downloadDialog.dismiss();
				} else if (errorNo == 0) {
					mHandler.sendEmptyMessage(DOWN_STOP);
				} else {
					mHandler.sendEmptyMessage(DOWN_ERROR);
				}
			}
		});
	}

	private void installApk(Context context) {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		BaseApplication.getInstance().isUpdateFirst = false;
		Intent i = new Intent(Intent.ACTION_VIEW);
		
		 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //安装打开2015-11-27新设定
		 
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		appManager.currentActivity().startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());//安装打开2015-11-27新设定
	}

	/**
	 * 判断是否已下载
	 */
	public boolean judgeDownLoaded(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}
		PackageManager pkgManager = BaseApplication.getInstance()
				.getPackageManager();
		PackageInfo info = pkgManager.getPackageArchiveInfo(filePath,
				PackageManager.GET_ACTIVITIES);
		if (info == null) {
			return false;
		}
		try {
			if (versionCode == info.versionCode) {
				return true;
			}
		} catch (Exception e) {

		}
		return false;
	}

}

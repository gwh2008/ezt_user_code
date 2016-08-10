package com.zxing.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testandroid.R;
import com.google.zxing.BarcodeFormat;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ScanView;

/**
 * @title 扫描页面
 * @describe zxing 经过修改,解析变为ZBar方式，其他不变
 * @author ezt
 * @created 2014年11月27日
 */
public class QrcodeActivity extends Activity implements Callback,
		OnClickListener {

	private CaptureActivityHandler handler;
	private ScanView scanView;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private TextView cancelScanButton;

	private ImageView btOpenLight;
	private boolean hasSurface;
	private FrameLayout mContainer;
	private TextView tvBack, tvTitle;

	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private boolean isNeedCapture = false;

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qrcode_camera);
		
		CameraManager.init(getApplication());
		mContainer = (FrameLayout) findViewById(R.id.capture_containter);
		scanView = (ScanView) findViewById(R.id.viewfinder_view);// 扫描框
		cancelScanButton = (TextView) this.findViewById(R.id.btn_cancel_scan);
		btOpenLight = (ImageView) this.findViewById(R.id.btn_light);
		tvBack = (TextView) this.findViewById(R.id.left_btn);
		tvTitle = (TextView) this.findViewById(R.id.title_tv);
		tvTitle.setText("二维码扫描");
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		cancelScanButton.setOnClickListener(this);
		btOpenLight.setOnClickListener(this);
		tvBack.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {// surfaceView是否初始化
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 扫描返回结果
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();

		if (result.equals("")) {// 扫描失败
			Toast.makeText(QrcodeActivity.this, "Scan failed!",
					Toast.LENGTH_SHORT).show();
		} else {// 成功
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", result);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);

		}
		QrcodeActivity.this.finish();

	}

	/**
	 * 初始化相机
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			Point point = CameraManager.get().getCameraResolution();
			Rect frame = CameraManager.get().getFramingRect();
			int width = point.y;
			int height = point.x;

			int x = frame.left * width / mContainer.getWidth();
			int y = frame.top * height / mContainer.getHeight();

			int scanViewWidth = frame.right - frame.left;
			int scanViewHeight = frame.bottom - frame.top;

			int cropWidth = scanViewWidth * width / mContainer.getWidth();
			int cropHeight = scanViewHeight * height / mContainer.getHeight();
			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);

		} catch (IOException ioe) {
			Toast.makeText(getApplicationContext(), ioe.getMessage(),
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		} catch (RuntimeException e) {
			// String msg = getResources().getString(R.string.scanner_hint);
			// hintLargeDistance(msg);
			// finish();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ScanView getScanView() {
		return scanView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		scanView.drawViewfinder();

	}

	/**
	 * 初始化扫描成功提示音
	 */
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	/**
	 * 播放声音
	 */
	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * 播放完成
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	boolean flag = true;

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_light) {
			if (getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_CAMERA_FLASH)) {
				if (flag == true) {
					flag = false;
					// 开闪光灯
					CameraManager.get().openLight();
					btOpenLight
							.setImageResource(R.drawable.flash_on_background);
				} else {
					flag = true;
					// 关闪光灯
					CameraManager.get().offLight();
					btOpenLight
							.setImageResource(R.drawable.flash_off_background);
				}
			} else {
				Toast.makeText(this, "当前设备没有闪光灯", Toast.LENGTH_SHORT).show();
			}

		} else {
			QrcodeActivity.this.finish();
		}

	}

}
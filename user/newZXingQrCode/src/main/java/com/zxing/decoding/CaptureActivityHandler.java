package com.zxing.decoding;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.testandroid.R;
import com.google.zxing.BarcodeFormat;
import com.zxing.activity.QrcodeActivity;
import com.zxing.camera.CameraManager;
import com.zxing.view.ScanResultPointCallback;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 */
public final class CaptureActivityHandler extends Handler {

	private static final String TAG = CaptureActivityHandler.class
			.getSimpleName();
	private final QrcodeActivity activity;
	private final DecodeThread decodeThread;
	private State state;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureActivityHandler(QrcodeActivity activity,
			Vector<BarcodeFormat> decodeFormats, String characterSet) {
		this.activity = activity;
		decodeThread = new DecodeThread(activity, decodeFormats, characterSet,
				new ScanResultPointCallback(activity.getScanView()));
		decodeThread.start();
		state = State.SUCCESS;
		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		if(message.what==R.id.auto_focus){
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (state == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			}
		}else if(message.what==R.id.restart_preview){
			Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			
		}else if(message.what==R.id.decode_succeeded){
			Log.d(TAG, "Got decode succeeded message");
			state = State.SUCCESS;
			Bundle bundle = message.getData();

			/***********************************************************************/
//			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
//					.getParcelable(DecodeThread.BARCODE_BITMAP);

			activity.handleDecode((String) message.obj);// 解析成功，回调
//			activity.handleDecode((Result) message.obj, barcode);
			/***********************************************************************/
		}else if(message.what==R.id.decode_failed){
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
		}else if(message.what==R.id.return_scan_result){
			Log.d(TAG, "Got return scan result message");
			activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			activity.finish();
		}else if(message.what==R.id.launch_product_query){
			Log.d(TAG, "Got product query message");
			String url = (String) message.obj;
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
		}
	}

	public void quitSynchronously() {
		state = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
		quit.sendToTarget();
		try {
			decodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(R.id.decode_succeeded);
		removeMessages(R.id.decode_failed);
	}

	private void restartPreviewAndDecode() {
		if (state == State.SUCCESS) {
			state = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(decodeThread.getHandler(),
					R.id.decode);
			CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
			activity.drawViewfinder();
		}
	}

}

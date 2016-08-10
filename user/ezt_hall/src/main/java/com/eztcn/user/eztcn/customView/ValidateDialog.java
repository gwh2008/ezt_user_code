/**
 * 
 */
package com.eztcn.user.eztcn.customView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.config.EZTConfig;

/**
 * @author Liu Gang
 * 
 *         2015年10月10日 上午8:55:47 验证码相关
 */
public class ValidateDialog extends Dialog implements OnClickListener {
	private EditText message_et;
	private ImageView message_iv;
	private Button btn_sure;
	private CodeSure codeSure;
	private Bitmap bitmap;
	private View closeMe;

	public ValidateDialog(Context context, int theam) {
		super(context, theam);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_validcode);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent); 
		message_et = (EditText) findViewById(R.id.message_et);
		message_iv = (ImageView) findViewById(R.id.message_iv);
		message_iv.setVisibility(View.INVISIBLE);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		closeMe = findViewById(R.id.closeMe);
		closeMe.setOnClickListener(this);
		this.setOnDismissListener(dismissListener);
		if (null != bitmap) {
			bitmap.recycle();
            bitmap=null;
		}
		new Thread() {
			@Override
			public void run() {
				// 定义一个URL对象
				URL url;
				InputStream is = null;
				try {
					url = new URL(EZTConfig.GET_VALIDE_CODE + "?phone="+ BaseApplication.patient.getEpMobile());
					// 打开该URL的资源输入流
					is = url.openStream();
					// 从InputStream中解析出图片
					bitmap = BitmapFactory.decodeStream(is);
					// 发送消息
					handler.sendEmptyMessage(0x123);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != is)
							is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123: {
				if (null != bitmap) {
					message_iv.setVisibility(View.VISIBLE);
					message_iv.setImageBitmap(bitmap);
				} else {
					message_iv.setVisibility(View.INVISIBLE);
					Toast.makeText(getContext(), "无法获取验证码", Toast.LENGTH_SHORT)
							.show();
				}
			}
			}
		};
	};

	public void setSure(CodeSure codeSure) {
		this.codeSure = codeSure;
	}

	OnDismissListener dismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			if (null != bitmap) {
				bitmap.recycle();
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure: {
			codeSure.onSureClick(message_et.getText().toString());
		}
			break;
		case R.id.closeMe: {
			this.dismiss();
		}
			break;
		}
	}

	public interface CodeSure {
		void onSureClick(String valideCode);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	// @Override
	// public void result(Object... object) {
	// Boolean flag = (Boolean) object[1];
	// if (flag) {
	// int returnParams = (Integer) object[0];
	// switch (returnParams) {
	// case HttpParams.GET_VALIDATE_CODE: {
	// Map<String, Object> map =(Map<String, Object>) object[2];
	// if(map.containsKey("data")){
	// String imageInStream=String.valueOf(map.get("data"));
	// byte[] byteIcon= Base64.decode(imageInStream,Base64.DEFAULT);
	// // ByteArrayInputStream inputStream=new ByteArrayInputStream(buf);
	// // Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
	// Bitmap bitmap=BitmapFactory.decodeByteArray(byteIcon, 0,
	// byteIcon.length);
	// message_iv.setImageBitmap(bitmap);
	// }else{
	// Toast.makeText(getContext(), "无法获取验证码", Toast.LENGTH_SHORT).show();
	// }
	// }
	// break;
	// }
	// }
	//
	// }
}

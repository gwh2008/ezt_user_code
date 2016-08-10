package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.customView.SelectPicPopupWindow;

/**
 * @title 实名找回密码
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
public class AutonymFoundActivity extends FinalActivity //implements OnClickListener
		 {

	@ViewInject(R.id.submit)//, click = "onClick"
	private Button submit;

	@ViewInject(R.id.idCard_front_layout)//, click = "onClick"
	private FrameLayout layoutIdCard;

	@ViewInject(R.id.medication_front_layout)//, click = "onClick"
	private FrameLayout layoutMedication;

	@ViewInject(R.id.idCard_frontImg)
	private ImageView imgIdCard;// 身份证原件

	@ViewInject(R.id.medication_frontImg)
	private ImageView imgMedication;// 医保卡原件

	@ViewInject(R.id.idCard_front_hint_tv)
	private TextView tvIdCardHint;

	@ViewInject(R.id.medication_front_hint_tv)
	private TextView tvMedicationHint;

	// 自定义的底部弹出框类（用于图片选择）
	private SelectPicPopupWindow menuWindow;

	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LinearLayout.inflate(this, R.layout.autonymfound, null);
		setContentView(view);
		ViewUtils.inject(AutonymFoundActivity.this);
		loadTitleBar(true, "实名找回密码", null);
	}

	@OnClick(R.id.submit)
	private void submitClick(View v){
	}
	
	@OnClick(R.id.idCard_front_layout)// 上传身份证
	private void idCard_front_layoutClick(View v){
		dialogChoicePic(1);
	}
	
	@OnClick(R.id.medication_front_layout)// 上传医保卡
	private void medication_front_layoutClick(View v){
		dialogChoicePic(2);
	}
	
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.submit:
//
//			break;
//		case R.id.idCard_front_layout:// 上传身份证
//			dialogChoicePic(1);
//			break;
//
//		case R.id.medication_front_layout:// 上传医保卡
//			dialogChoicePic(2);
//			break;
//		}
//	}

	/**
	 * 选择图
	 */
	private void dialogChoicePic(int index) {
		menuWindow = new SelectPicPopupWindow(mContext, new ChoiceImgClick(
				index));
		// 显示窗口
		menuWindow.showAtLocation(view, Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	/*
	 * 选择图片
	 */
	public class ChoiceImgClick implements OnClickListener {
		private int index;

		public ChoiceImgClick(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			if (index == 1) {// 身份证
				switch (v.getId()) {
				case R.id.btn_take_photo:// 拍照
					openCamera(11);
					break;
				case R.id.btn_pick_photo:// 从相册选择
					openImgLibrary(22);
					break;
				default:// 取消
					break;
				}

			} else {// 社保
				switch (v.getId()) {
				case R.id.btn_take_photo:// 拍照
					openCamera(111);
					break;
				case R.id.btn_pick_photo:// 从相册选择
					openImgLibrary(222);
					break;
				default:// 取消
					break;
				}
			}

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 拍照或图库返回结果
		if (requestCode == 11 || requestCode == 22) {// 身份证
			Bitmap bitmap = getBitmapFromImage(data);
			if (bitmap == null)
				Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
			else {
				imgIdCard.setImageBitmap(bitmap);
			}
		} else if (requestCode == 111 || requestCode == 222) {// 医保卡
			Bitmap bitmap = getBitmapFromImage(data);
			if (bitmap == null)
				Toast.makeText(mContext, "获取图片失败", Toast.LENGTH_LONG).show();
			else {
				imgMedication.setImageBitmap(bitmap);
			}
		}

	}

}

package com.eztcn.user.eztcn.activity.home;

import java.util.HashMap;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IForeignPatient;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title 中心介绍
 * @describe 宣传图片点击跳入
 * @author ezt
 * @created 2015年3月24日
 */
public class CentreIntroActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.img)
	private ImageView img;

	@ViewInject(R.id.intro)
	private TextView tvIntro;

//	private FinalBitmap fb;
	private BitmapUtils bitmapUtils;
	private Bitmap defaultBit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_centre_intro);
		ViewUtils.inject(CentreIntroActivity.this);
		loadTitleBar(true, "天津医科大学肿瘤医院", null);
//		fb = FinalBitmap.create(this);
		bitmapUtils=new BitmapUtils(CentreIntroActivity.this);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		bitmapUtils.configDefaultLoadingImage(defaultBit);
		bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_info_default);
		initialData();
	}

	/*
	 * 获取数据
	 */
	private void initialData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("infoid", "102");
//		IForeignPatient api = new ForeignPatientImpl();
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("infoid", "102");
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getTumourIntro(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			HashMap<String, Object> map = (HashMap<String, Object>) object[2];
			if (map != null) {
				String imgUrl = (String) map.get("pic");
				String body = (String) map.get("body");
				final String imgurl = EZTConfig.OFFICIAL_WEBSITE + imgUrl;
				bitmapUtils.display(img, imgurl);
//				fb.display(img, imgurl, defaultBit, defaultBit);
				tvIntro.setText(Html.fromHtml(body));
			} else {
				Toast.makeText(mContext, getString(R.string.request_fail),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
		}
		hideProgressToast();

	}

}

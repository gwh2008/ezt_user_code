package com.eztcn.user.eztcn.activity.fdoc;

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
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;

/**
 * @title (外患)病例详情
 * @describe
 * @author ezt
 * @created 2015年3月24日
 */
public class ForeignPatient_RCDetailsActivity extends FinalActivity implements IHttpResult {
	
	
	@ViewInject(R.id.img)
	private ImageView img;

	@ViewInject(R.id.intro)
	private TextView tvIntro;
	
//	private FinalBitmap fb;
	private BitmapUtils fb;
	private Bitmap defaultBit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_r_c_details);
		ViewUtils.inject(ForeignPatient_RCDetailsActivity.this);
		String strName = getIntent().getStringExtra("title");
		String id = getIntent().getStringExtra("id");
		loadTitleBar(true, strName, null);
		fb=new BitmapUtils(ForeignPatient_RCDetailsActivity.this);
//		fb = FinalBitmap.create(this);
		defaultBit = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_info_default);
		initialData(id);
	}
	
	/**
	 * 获取详情
	 * @param id
	 */
	private void initialData(String id){
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("infoid", id);
		RequestParams params=new RequestParams();
		params.addBodyParameter("infoid", id);
//		IForeignPatient api = new ForeignPatientImpl();
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getRecoveryCaseDetail(params, this);
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
				fb.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
				fb.configDefaultLoadingImage(defaultBit);
				fb.configDefaultLoadFailedImage(defaultBit);
				fb.display(img, imgurl);
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

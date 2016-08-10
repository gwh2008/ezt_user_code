package org.fireking.app.imagelib.widget;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.fireking.app.imagelib.R;
import org.fireking.app.imagelib.entity.ImageBean;
import org.fireking.app.imagelib.view.clip.ClipImageLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @title 图片裁剪
 * @describe
 * @author ezt
 * @created 2015年6月30日
 */
public class ClipActivity extends Activity implements OnClickListener {

	private ClipImageLayout mClipImageLayout;
	private TextView tvBack, tvUse;
	public static final int PHOTO_CLIP = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);
		List<ImageBean> images = (List<ImageBean>) getIntent()
				.getSerializableExtra("images");
		String strPath = images.get(0).path;
		Bitmap bip = BitmapFactory.decodeFile(strPath);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImg);
		mClipImageLayout.initialImg(bip);
		tvBack = (TextView) findViewById(R.id.back_tv);
		tvUse = (TextView) findViewById(R.id.use_tv);
		tvBack.setOnClickListener(this);
		tvUse.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == tvUse) {
			Bitmap newBit = mClipImageLayout.clip();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] datas = baos.toByteArray();
			setResult(RESULT_OK, new Intent().putExtra("bitmap", datas));
		}
		finish();

	}

}

/**
 * 
 */
package com.eztcn.user.eztcn.bean.compent;

import java.util.List;

import xutils.BitmapUtils;
import xutils.bitmap.BitmapCommonUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.eztcn.user.R;

/**
 * @author Liu Gang
 * 
 *         2015年11月17日 上午10:56:43 每一项 只有图标 和 name 它代表 具体医生 或者 具体模块
 */
public class ItemCompent extends Compent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name1;
	private String name2;
	private String jumpLink; // 跳转到哪个Activity ep. Class.forName “”
	private List<IntentParams> intentParamList; // 跳转时候用的参数 ep. (key,value)
	private ImageView imageView;
	private String imgUrl;
//	private InputStream is;
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			Bitmap bitmap = (Bitmap) msg.obj;
//			imageView.setImageBitmap(bitmap);
//			if (null != is) {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		};
//	};

	public ItemCompent(final Context context,final String imgUrl, String name1Str, String name2Str,
			String jumpLink, List<IntentParams> intentParamList) {
		imageView=new ImageView(context);
		this.imgUrl=imgUrl;
		final Bitmap loadingBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.default_doc_img);
		this.name1 = name1Str;
		this.name2 = name2Str;
		this.jumpLink = jumpLink;
		this.intentParamList = intentParamList;
		
//		FinalBitmap fbitmap=FinalBitmap.create(context);
		
		BitmapUtils bitmapUtils=new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(context).scaleDown(3));
	    bitmapUtils.configDefaultLoadingImage(loadingBitmap);
	    bitmapUtils.configDefaultLoadFailedImage(loadingBitmap);
		bitmapUtils.display(imageView, imgUrl);	
//		fbitmap.display(imageView, imgUrl, loadingBitmap, loadingBitmap);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					URL url = new URL(imgUrl);
////					 打开该URL的资源输入流
//					is = url.openStream();
//				
//					Bitmap bitmap = BitmapFactory.decodeStream(is);
//					Message handleMsg = handler.obtainMessage();
//					handleMsg.obj = bitmap;
//					handler.sendMessage(handleMsg);
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//
//			}
//		}).start();

	}

	public String getName1() {
		return name1;
	}

	public String getName2() {
		return name2;
	}

	public String getJumpLink() {
		return jumpLink;
	}

	public List<IntentParams> getIntentParamList() {
		return intentParamList;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public String getImgUrl() {
		return imgUrl;
	}
}

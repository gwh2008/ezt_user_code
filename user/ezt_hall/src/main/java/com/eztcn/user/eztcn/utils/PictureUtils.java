package com.eztcn.user.eztcn.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * @title
 * @describe
 * @author ezt
 * @created 2015年6月24日
 */
public class PictureUtils {

	/**
	 * 压锟斤拷图片锟斤拷
	 * 
	 * @param path
	 *            图片路锟斤拷
	 * @param size
	 *            图片锟斤拷锟竭达拷
	 * @return 压锟斤拷锟斤拷锟酵计?
	 * @throws IOException
	 */
	public static Bitmap compressImage(String path, int size)
			throws IOException {
		Bitmap bitmap = null;
		// 取锟斤拷图片
		InputStream is = new FileInputStream(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 锟斤拷锟斤拷锟斤拷锟斤拷?锟斤拷为bitmap锟斤拷锟斤拷锟节达拷占洌伙拷锟铰家恍╋拷锟酵计拷锟斤拷锟较拷锟斤拷锟斤拷锟酵计拷锟叫★拷锟斤拷锟剿碉拷锟斤拷司锟斤拷锟轿拷锟斤拷诖锟斤拷呕锟?
		options.inJustDecodeBounds = true;
		// 通锟斤拷图片锟侥凤拷式锟斤拷取锟斤拷options锟斤拷锟斤拷锟捷ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟絡ava锟侥碉拷址锟斤拷锟斤拷锟斤拷锟斤拷值锟斤拷
		BitmapFactory.decodeStream(is, null, options);
		// 锟截憋拷锟斤拷
		is.close();

		// // 锟斤拷锟窖癸拷锟斤拷锟酵计?
		int i = 0;
		while (true) {
			// 锟斤拷一锟斤拷锟角革拷锟揭拷锟斤拷玫拇锟叫★拷锟绞癸拷锟酵高讹拷锟斤拷锟斤拷锟斤拷
			if ((options.outWidth >> i <= size)
					&& (options.outHeight >> i <= size)) {
				// 锟斤拷锟斤拷取锟斤拷锟斤拷锟斤拷注锟解：锟斤拷锟斤拷一锟斤拷要锟劫次硷拷锟截ｏ拷锟斤拷锟杰讹拷锟斤拷使锟斤拷之前锟斤拷锟斤拷锟斤拷
				is = new FileInputStream(path);
				// 锟斤拷锟斤拷锟斤拷锟斤拷示 锟斤拷锟斤拷傻锟酵计纪计拷募锟斤拷锟街伙拷锟?
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 锟斤拷锟斤拷之前锟斤拷锟斤拷为锟斤拷true锟斤拷锟斤拷锟斤拷要锟斤拷为false锟斤拷锟斤拷锟斤拷痛锟斤拷锟斤拷锟斤拷锟酵计?
				options.inJustDecodeBounds = false;
				options.inPreferredConfig = Config.ARGB_8888;
				// 同时锟斤拷锟矫才伙拷锟斤拷效
				options.inPurgeable = true;
				// 锟斤拷系统锟节存不锟斤拷时锟斤拷图片锟皆讹拷锟斤拷锟斤拷锟斤拷
				options.inInputShareable = true;
				// 锟斤拷锟斤拷Bitmap
				bitmap = BitmapFactory.decodeStream(is, null, options);
				break;
			}
			i += 1;
		}

		return bitmap;
	}

	/**
	 * 锟斤拷锟斤拷图片锟斤拷锟教讹拷锟侥硷拷锟斤拷小
	 * 
	 * @param bm
	 *            锟斤拷要锟斤拷锟脚碉拷图片
	 * @param maxSize
	 *            目锟斤拷锟侥硷拷锟斤拷小锟斤拷锟斤拷位锟斤拷KB
	 * @return
	 */
	public static Bitmap imageZoom(Bitmap bm, double maxSize) {
		// 图片锟斤拷锟斤拷锟斤拷锟秸硷拷 锟斤拷位锟斤拷KB
		// 锟斤拷bitmap锟斤拷锟斤拷锟斤拷锟斤拷锟叫ｏ拷锟斤拷锟斤拷bitmap锟侥达拷小锟斤拷锟斤拷实锟绞讹拷取锟斤拷原锟侥硷拷要锟斤拷
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 锟斤拷锟街节伙拷锟斤拷KB
		double mid = b.length / 1024;
		// 锟叫讹拷bitmap占锟矫空硷拷锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷占锟?锟斤拷锟斤拷锟斤拷锟斤拷压锟斤拷 小锟斤拷锟斤拷压锟斤拷
		if (mid > maxSize) {
			// 锟斤拷取bitmap锟斤拷小 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷小锟侥讹拷锟劫憋拷
			double i = mid / maxSize;
			// 锟斤拷始压锟斤拷 锟剿达拷锟矫碉拷平锟斤拷锟斤拷 锟斤拷锟斤拷锟酵高讹拷压锟斤拷锟斤拷锟斤拷应锟斤拷平锟斤拷锟斤拷
			// 锟斤拷锟街刻度和高度猴拷原bitmap锟斤拷锟斤拷一锟铰ｏ拷压锟斤拷锟斤拷也锟斤到锟斤拷锟斤拷锟斤拷小占锟矫空硷拷拇锟叫?
			bm = zoomImage(bm, bm.getWidth() / Math.sqrt(i), bm.getHeight()
					/ Math.sqrt(i));
		}
		return bm;
	}

	/***
	 * 图片锟斤拷锟斤拷锟脚凤拷锟斤拷
	 * 
	 * @param bgimage
	 *            锟斤拷源图片锟斤拷源
	 * @param newWidth
	 *            锟斤拷锟斤拷锟脚猴拷锟斤拷
	 * @param newHeight
	 *            锟斤拷锟斤拷锟脚猴拷叨锟?
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 锟斤拷取锟斤拷锟酵计拷目锟酵革拷
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 锟斤拷锟斤拷锟斤拷锟斤拷图片锟矫碉拷matrix锟斤拷锟斤拷
		Matrix matrix = new Matrix();
		// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 锟斤拷锟斤拷图片锟斤拷锟斤拷
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 
	 * @Title: rotateImage
	 * @param path
	 * @return void
	 */
	public static int getImageOrientation(String path) {

		try {
			ExifInterface exifInterface = new ExifInterface(path);

			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			return orientation;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ExifInterface.ORIENTATION_NORMAL;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
		Matrix m = new Matrix();
		if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
			m.setRotate(90);
		} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
			m.setRotate(180);
		} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
			m.setRotate(270);
		} else {
			return bitmap;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		try {
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
		} catch (OutOfMemoryError ooe) {

			m.postScale(1, 1);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);

		}
		return bitmap;
	}
}

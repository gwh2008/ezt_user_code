package com.zxing.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @title 生成二维码/一维码图片
 * @describe
 * @author ezt
 * @created 2014年11月27日
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	private static final String CHARSET = "utf-8";

	/**
	 * 将字符串编成二维条码
	 * @param str
	 * @param widthAndHeight
	 * @return
	 * @throws WriterException
	 */
	public static Bitmap createQRCode(String str, int widthAndHeight)
			throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * 将字符串编成一维条码
	 * 
	 * @param str
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap toBarCodeMatrix(String str, Integer width,
			Integer height) {

		if (width == null || width < 200) {
			width = 200;
		}

		if (height == null || height < 50) {
			height = 50;
		}

		try {
			// 文字编码
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, CHARSET);

			BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
					BarcodeFormat.CODE_128, width, height, hints);

			int newWidth = bitMatrix.getWidth();
			int newHeight = bitMatrix.getHeight();
			int[] pixels = new int[newWidth * newHeight];

			for (int y = 0; y < newHeight; y++) {
				for (int x = 0; x < newWidth; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * newWidth + x] = BLACK;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(newWidth, newHeight,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, newWidth, 0, 0, newWidth, newHeight);
			return bitmap;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

package org.fireking.app.imagelib.widget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.fireking.app.imagelib.R;
import org.fireking.app.imagelib.entity.AlbumBean;
import org.fireking.app.imagelib.entity.ImageBean;
import org.fireking.app.imagelib.tools.AlbumHelper;
import org.fireking.app.imagelib.tools.Config;
import org.fireking.app.imagelib.tools.NativeImageLoader;
import org.fireking.app.imagelib.tools.NativeImageLoader.NativeImageCallBack;
import org.fireking.app.imagelib.tools.Uitls;
import org.fireking.app.imagelib.view.MyImageView;
import org.fireking.app.imagelib.view.MyImageView.OnMeasureListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @title 选择本地图库/拍照
 * @describe
 * @author ezt
 * @created 2015年6月30日
 */
public class PicSelectActivity extends FragmentActivity implements
		OnItemClickListener, OnClickListener {

	private static final int PHOTO_GRAPH = 1;
	private static final int SCAN_OK = 0x1001;
	private static boolean isOpened = false;

	private GridView gridView;
	private PicSelectAdapter adapter;
	private TextView album, complete, preView, back;
	private String fileName;// 文件名，路径
	private String dirPath;//
	private PopupWindow popWindow;
	private int selected = 0;
	private int haveSelected = 0;
	private int height = 0;
	private List<AlbumBean> mAlbumBean;
	public static final String IMAGES = "images";
	private boolean isMChoice = true;// 是否多选

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.the_picture_selection);
		isMChoice = getIntent().getBooleanExtra("isMChoice", true);
		// 20160408添加个数
		haveSelected = getIntent().getIntExtra("selectNum", 0);
		back = (TextView) this.findViewById(R.id.back);
		album = (TextView) this.findViewById(R.id.album);
		complete = (TextView) this.findViewById(R.id.complete);
		preView = (TextView) this.findViewById(R.id.preview);

		if (isMChoice) {// 多选
			complete.setVisibility(View.VISIBLE);
		}
		back.setOnClickListener(this);
		preView.setOnClickListener(this);
		complete.setOnClickListener(this);
		album.setOnClickListener(this);
		gridView = (GridView) this.findViewById(R.id.child_grid);
		adapter = new PicSelectAdapter(PicSelectActivity.this, gridView,
				onImageSelectedCountListener, isMChoice, haveSelected);
		gridView.setAdapter(adapter);
		adapter.setOnImageSelectedListener(onImageSelectedListener);
		showPic();
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v == back) {
			finish();
		} else if (v == preView) {

		} else if (v == complete) {
			List<ImageBean> selecteds = getSelectedItem();
			Intent intent = new Intent();
			intent.putExtra(IMAGES, (Serializable) selecteds);
			setResult(RESULT_OK, intent);
			finish();
		} else if (v == album) {
			if (!isOpened && popWindow != null) {
				height = getWindow().getDecorView().getHeight();
				WindowManager.LayoutParams ll = getWindow().getAttributes();
				ll.alpha = 0.3f;
				getWindow().setAttributes(ll);
				popWindow.showAtLocation(findViewById(android.R.id.content),
						Gravity.NO_GRAVITY, 0,
						height - Uitls.dip2px(PicSelectActivity.this, 448));
			} else {
				if (popWindow != null) {
					popWindow.dismiss();
				}
			}

		}

	}

	/**
	 */
	private void takePhoto() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			fileName = getFileName();
			System.out.println(Environment.getExternalStorageDirectory()
					.toString());
			System.out.println(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			dirPath = Environment.getExternalStorageDirectory().getPath()
					+ Config.getSavePath();
			File tempFile = new File(dirPath);
			if (!tempFile.exists()) {
				tempFile.mkdirs();
			}
			File saveFile = new File(tempFile, fileName + ".jpg");
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
			startActivityForResult(intent, PHOTO_GRAPH);
		} else {
			Toast.makeText(PicSelectActivity.this, "未检测到CDcard，拍照不可用!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_GRAPH && resultCode == RESULT_OK) {
			// http://blog.csdn.net/lyltiger/article/details/44202223
			String absPath = dirPath + "/" + fileName + ".jpg";

//			int degree = getBitmapDegree(absPath);
//			Bitmap bm=getSmallBitmap(absPath);
//			Bitmap bitmap = rotaingImageView(degree, bm);
//			String finPath=compressHeadPhoto(bitmap,absPath);
			List<ImageBean> selecteds = new ArrayList<ImageBean>();
//			selecteds.add(new ImageBean(null, 0l, null, finPath, false));
			
			
			selecteds.add(new ImageBean(null, 0l, null, dirPath + "/"
					+ fileName + ".jpg", false));
			
			Intent intent = new Intent();
			intent.putExtra(IMAGES, (Serializable) selecteds);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
		public static Bitmap getSmallBitmap(String filePath) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 480, 800);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;

			return BitmapFactory.decodeFile(filePath, options);
		}
		
		// 计算图片的缩放值
		public static int calculateInSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			return inSampleSize;
		}
	
	

	private String compressHeadPhoto(final Bitmap bm,String absPath) {
		File rotateFile = new File(absPath);
		try {
			bm.compress(Bitmap.CompressFormat.PNG, 70, new FileOutputStream(
					rotateFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return rotateFile.getAbsolutePath();
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
//		Logs.i("startPhotoZoom uri: " + uri);
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html 直接在里面
		 * Ctrl+F 搜：CROP
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的view可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
//		startActivityForResult(intent, SQConstants.AVATAR_C_A_DATA_IMG);
	}

	/**
	 * 图片旋转
	 * 
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	private int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 */
	private String getFileName() {
		StringBuffer sb = new StringBuffer();
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		String[] dictionaries = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
				"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
				"5", "6", "7", "8", "9" };
		sb.append("dzc");
		sb.append(millis);
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			sb.append(dictionaries[random.nextInt(dictionaries.length - 1)]);
		}
		return sb.toString();
	};

	OnImageSelectedCountListener onImageSelectedCountListener = new OnImageSelectedCountListener() {

		@Override
		public int getImageSelectedCount() {
			return selected;
		}
	};

	OnImageSelectedListener onImageSelectedListener = new OnImageSelectedListener() {

		@Override
		public void notifyChecked() {
			selected = getSelectedCount();
			complete.setText("完成(" + (selected + haveSelected) + "/"
					+ Config.limit + ")");
			preView.setText("预览(" + (selected + haveSelected) + "/"
					+ Config.limit + ")");
		}
	};

	private void showPic() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = SCAN_OK;
				msg.obj = AlbumHelper.newInstance(PicSelectActivity.this)
						.getFolders();
				msg.sendToTarget();
			}
		}).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (SCAN_OK == msg.what) {
				mAlbumBean = (List<AlbumBean>) msg.obj;
				if (mAlbumBean != null && mAlbumBean.size() != 0) {
					AlbumBean b = mAlbumBean.get(0);
					adapter.taggle(b);
					popWindow = showPopWindow();
				} else {
					List<ImageBean> sets = new ArrayList<ImageBean>();
					sets.add(new ImageBean());
					AlbumBean b = new AlbumBean("", 1, sets, "");
					adapter.taggle(b);
				}
			}
		};
	};

	/**
	 * @return
	 */
	private int getSelectedCount() {
		int count = 0;
		for (AlbumBean albumBean : mAlbumBean) {
			for (ImageBean b : albumBean.sets) {
				if (b.isChecked == true) {
					count++;
				}
			}
		}
		return count;
	}

	private List<ImageBean> getSelectedItem() {
		int count = 0;
		List<ImageBean> beans = new ArrayList<ImageBean>();
		OK: for (AlbumBean albumBean : mAlbumBean) {
			for (ImageBean b : albumBean.sets) {
				if (b.isChecked == true) {
					beans.add(b);
					count++;
				}
				if ((count + haveSelected) == Config.limit) {
					// if (count == Config.limit) {
					break OK;
				}
			}
		}
		return beans;
	}

	private PopupWindow showPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.the_picture_selection_pop, null);
		final PopupWindow mPopupWindow = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, Uitls.dip2px(PicSelectActivity.this,
						400), true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		ListView listView = (ListView) view.findViewById(R.id.list);
		AlbumAdapter albumAdapter = new AlbumAdapter(PicSelectActivity.this,
				listView);
		listView.setAdapter(albumAdapter);
		albumAdapter.setData(mAlbumBean);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams ll = getWindow().getAttributes();
				ll.alpha = 1f;
				getWindow().setAttributes(ll);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlbumBean b = (AlbumBean) parent.getItemAtPosition(position);
				adapter.taggle(b);
				album.setText(b.folderName);
				mPopupWindow.dismiss();
			}
		});
		return mPopupWindow;
	}

	class AlbumAdapter extends BaseAdapter {
		LayoutInflater inflater;
		List<AlbumBean> albums;
		private Point mPoint = new Point(0, 0);
		ListView mListView;

		public AlbumAdapter(Context context, ListView mListView) {
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mListView = mListView;
		}

		public void setData(List<AlbumBean> albums) {
			this.albums = albums;
		}

		@Override
		public int getCount() {
			return albums == null || albums.size() == 0 ? 0 : albums.size();
		}

		@Override
		public Object getItem(int position) {
			return albums.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(
						R.layout.the_picture_selection_pop_item, null);
				viewHolder.album_count = (TextView) convertView
						.findViewById(R.id.album_count);
				viewHolder.album_name = (TextView) convertView
						.findViewById(R.id.album_name);
				viewHolder.mImageView = (MyImageView) convertView
						.findViewById(R.id.album_image);
				viewHolder.mImageView
						.setOnMeasureListener(new OnMeasureListener() {

							@Override
							public void onMeasureSize(int width, int height) {
								mPoint.set(width, height);
							}
						});
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				viewHolder.mImageView
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}
			final AlbumBean b = (AlbumBean) getItem(position);
			viewHolder.mImageView.setTag(b.thumbnail);

			viewHolder.album_name.setText(b.folderName);
			viewHolder.album_count.setText(b.count + "");

			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
					b.thumbnail, mPoint, new NativeImageCallBack() {

						@Override
						public void onImageLoader(Bitmap bitmap, String path) {
							ImageView mImageView = (ImageView) mListView
									.findViewWithTag(b.thumbnail);
							if (mImageView != null && bitmap != null) {
								mImageView.setImageBitmap(bitmap);
							}
						}
					});
			if (bitmap != null) {
				viewHolder.mImageView.setImageBitmap(bitmap);
			} else {
				viewHolder.mImageView
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}
			return convertView;
		}
	}

	public interface OnImageSelectedListener {
		void notifyChecked();
	}

	public interface OnImageSelectedCountListener {
		int getImageSelectedCount();
	}

	public static class ViewHolder {
		public MyImageView mImageView;
		public TextView album_name;
		public TextView album_count;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			takePhoto();
		} else {
			if (!isMChoice) {
				Intent intent = new Intent();
				List<ImageBean> selecteds = new ArrayList<ImageBean>();
				selecteds.add(adapter.getBean().sets.get(position));
				intent.putExtra(IMAGES, (Serializable) selecteds);
				setResult(RESULT_OK, intent);
				finish();
			}
		}

	}

}

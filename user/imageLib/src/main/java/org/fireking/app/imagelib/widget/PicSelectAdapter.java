package org.fireking.app.imagelib.widget;

import java.io.IOException;

import org.fireking.app.imagelib.R;
import org.fireking.app.imagelib.entity.AlbumBean;
import org.fireking.app.imagelib.entity.ImageBean;
import org.fireking.app.imagelib.tools.Config;
import org.fireking.app.imagelib.tools.NativeImageLoader;
import org.fireking.app.imagelib.tools.NativeImageLoader.NativeImageCallBack;
import org.fireking.app.imagelib.view.MyImageView;
import org.fireking.app.imagelib.view.MyImageView.OnMeasureListener;
import org.fireking.app.imagelib.widget.PicSelectActivity.OnImageSelectedCountListener;
import org.fireking.app.imagelib.widget.PicSelectActivity.OnImageSelectedListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class PicSelectAdapter extends BaseAdapter {

	private Context context;
	private Point mPoint = new Point(0, 0);
	private AlbumBean bean;
	private GridView mGridView;
	private OnImageSelectedListener onImageSelectedListener;
	private OnImageSelectedCountListener onImageSelectedCountListener;
	private boolean isMChoice;// 是否多选

	// public PicSelectAdapter(Context context, GridView mGridView,
	// OnImageSelectedCountListener onImageSelectedCountListener,
	// boolean isMChoice) {
	private int hanveSelected;

	public PicSelectAdapter(Context context, GridView mGridView,
			OnImageSelectedCountListener onImageSelectedCountListener,
			boolean isMChoice, int haveSelected) {
		this.hanveSelected = haveSelected;
		this.context = context;
		this.mGridView = mGridView;
		this.onImageSelectedCountListener = onImageSelectedCountListener;
		this.isMChoice = isMChoice;
	}

	public AlbumBean getBean() {
		return bean;
	}

	public void setBean(AlbumBean bean) {
		this.bean = bean;
	}

	public void taggle(AlbumBean bean) {
		this.bean = bean;
		notifyDataSetChanged();
	}

	public void setOnImageSelectedListener(
			OnImageSelectedListener onImageSelectedListener) {
		this.onImageSelectedListener = onImageSelectedListener;
	}

	@Override
	public int getCount() {
		return bean == null || bean.count == 0 ? 0 : bean.count;
	}

	@Override
	public Object getItem(int position) {
		return bean == null ? null : bean.sets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		final ImageBean ib = (ImageBean) getItem(index);
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.the_picture_selection_item, null);
			viewHolder.mImageView = (MyImageView) convertView
					.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView
					.findViewById(R.id.child_checkbox);
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
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

		viewHolder.mImageView.setTag(ib.path);

		if (index == 0) {
			viewHolder.mImageView.setImageResource(R.drawable.tk_photo);
			viewHolder.mCheckBox.setVisibility(View.GONE);
		} else {

			if (isMChoice == true) {
				viewHolder.mCheckBox.setVisibility(View.VISIBLE);
				viewHolder.mCheckBox
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								int count = onImageSelectedCountListener
										.getImageSelectedCount();
								// if (count == Config.limit && isChecked) {
								if (count + hanveSelected == Config.limit
										&& isChecked) {
									Toast.makeText(context,
											"只能选择" + Config.limit + "张",
											Toast.LENGTH_SHORT).show();
									viewHolder.mCheckBox
											.setChecked(ib.isChecked);
								} else {
									if (!ib.isChecked && isChecked) {
										addAnimation(viewHolder.mCheckBox);
									}
									ib.isChecked = isChecked;
								}
								onImageSelectedListener.notifyChecked();
							}
						});
				if (ib.isChecked) {
					viewHolder.mCheckBox.setChecked(true);
				} else {
					viewHolder.mCheckBox.setChecked(false);
				}
			}
			//
			Bitmap bm =null;
			
			int degree=getBitmapDegree(ib.path);
			if(degree==90){
				bm=toturn(getSmallBitmap(ib.path));
			}else{
			 bm = getSmallBitmap(ib.path);
			}
			Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(
					ib.path, mPoint, new NativeImageCallBack() {

						@Override
						public void onImageLoader(Bitmap bitmap, String path) {
							ImageView mImageView = (ImageView) mGridView
									.findViewWithTag(ib.path);
							
							Bitmap bm =null;
							
							int degree=getBitmapDegree(ib.path);
							if(degree==90){
								bm=toturn(getSmallBitmap(ib.path));
							}else{
							 bm = getSmallBitmap(ib.path);
							}
							
							if (bm != null && mImageView != null) {
								mImageView.setImageBitmap(bm);
							}
						}
					});

			if (bm != null) {
				viewHolder.mImageView.setImageBitmap(bm);
			} else {
				viewHolder.mImageView
						.setImageResource(R.drawable.friends_sends_pictures_no);
			}

		}
		return convertView;
	}

	/**
	 * 
	 * @param view
	 */
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	public static class ViewHolder {
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}
	
	private int getBitmapDegree(String path) {
	    int degree = 0;
	    try {
	        // 从指定路径下读取图片，并获取其EXIF信息
	        ExifInterface exifInterface = new ExifInterface(path);
	        // 获取图片的旋转信息
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
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
	
	public static Bitmap toturn(Bitmap img){
        Matrix matrix = new Matrix();
        matrix.postRotate(+90); /*翻转90度*/
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
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

}

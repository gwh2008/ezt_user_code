package com.eztcn.user.eztcn.activity.discover;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.gestureimage.GestureImageView;
import com.eztcn.user.eztcn.customView.gestureimage.MyViewPager;
import com.eztcn.user.eztcn.utils.CommonUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * @title 资讯详情图片显示
 * @describe
 * @author ezt
 * @created 2015年1月9日
 */
public class ShowWebImageActivity extends FinalActivity
		 {
	@ViewInject(R.id.web_image_viewpager)
	private MyViewPager viewPager;//

	@ViewInject(R.id.text_title)
	private TextView tvTitle;//

	@ViewInject(R.id.text_page)
	private TextView page;// 文章类型

	@ViewInject(R.id.left_btn)
	private TextView tvBack;// 返回按钮

	@ViewInject(R.id.right_btn)
	private TextView tvCommentNum;// 评论数

	public static final String IMAGE_URLS = "image_urls";// 图片地址
	public static final String POSITION = "position";// 选中的下标
	public static final String TITLE = "title";// 标题
	public static final String NUM = "num";// 评论数
	public static final String ID = "id";

	private String[] imageArray;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private int position;
	private GestureImageView[] mImageViews;

	private int count;// 图片数量
	private String title;// 标题
	private String evaluateNum;// 评价数
	private String id;// 文章id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_image_show);
		ViewUtils.inject(ShowWebImageActivity.this);
		getIntentValue();
		imageLoader = ImageLoader.getInstance();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3)
				.memoryCacheSize(CommonUtil.getMemoryCacheSize(this))
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		imageLoader.init(config);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(false).bitmapConfig(Bitmap.Config.RGB_565).build();
		initViews();
	}

	/**
	 * 传过来的值
	 */
	private void getIntentValue() {
		Intent intent = getIntent();
		String urls = intent.getStringExtra(IMAGE_URLS);
		title = intent.getStringExtra(TITLE);// 文章标题
		evaluateNum = intent.getStringExtra(NUM);// 评价数
		id = intent.getStringExtra(ID);
		position = intent.getIntExtra(POSITION, 0);
		imageArray = urls.split(",");
		count = imageArray.length;
	}

	private void initViews() {

		tvTitle.setText(title);
		tvCommentNum.setText(evaluateNum + " 评价");
		if (count <= 1) {
			page.setVisibility(View.GONE);
		} else {
			page.setVisibility(View.VISIBLE);
			page.setText((position + 1) + "/" + count);
		}

		viewPager.setPageMargin(20);
		viewPager.setAdapter(new ImagePagerAdapter(getWebImageViews()));
		viewPager.setCurrentItem(position);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				page.setText((arg0 + 1) + "/" + count);
				mImageViews[arg0].reset();
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private List<View> getWebImageViews() {
		mImageViews = new GestureImageView[count];
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < count; i++) {
			View view = layoutInflater.inflate(R.layout.web_image_item, null);
			final GestureImageView imageView = (GestureImageView) view
					.findViewById(R.id.image);
			final ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id.loading);
			mImageViews[i] = imageView;
			imageLoader.displayImage(
					EZTConfig.OFFICIAL_WEBSITE + imageArray[i], imageView,
					options, new SimpleImageLoadingListener() {

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setVisibility(View.VISIBLE);
						}

					});
			imageView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					finish();
				}
			});
			views.add(view);
		}
		viewPager.setGestureImages(mImageViews);
		return views;
	}

	@Override
	protected void onDestroy() {
		if (mImageViews != null) {
			mImageViews = null;
		}
		// imageLoader.clearMemoryCache();//清除缓存
		super.onDestroy();
	}

	private class ImagePagerAdapter extends PagerAdapter {
		private List<View> views;

		public ImagePagerAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(views.get(position), 0);
			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}
	@OnClick(R.id.right_btn)
	private void evaluateClick(View v){// 评价
		if (Integer.parseInt(evaluateNum) != 0) {
			startActivity(new Intent(mContext,
					InformationCommentListActivity.class)
					.putExtra("id", id).putExtra("commentNum", evaluateNum));
		}
	}
	@OnClick(R.id.left_btn)
	private void back(View v){
		// 返回
		finish();
	}
}

package com.eztcn.user.eztcn.activity.fdoc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.AppointCheckIn_ChoiceAreaActivity;
import com.eztcn.user.eztcn.adapter.HospitalListAdapter;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 医院列表（按医院找）
 * @describe
 * @author ezt
 * @created 2015年4月23日
 */
public class HospitalListActivity extends FinalActivity implements
		OnTouchListener, IHttpResult, OnClickListener {// , OnItemClickListener

	@ViewInject(R.id.title_tv)
	// , click = "onClick"
	private TextView title;
	@ViewInject(R.id.hosList)
	// , itemClick = "onItemClick"
	private ListView hosList;
	private TextView filter;// 筛选

	/**
	 * 广告图
	 */
	private ImageView adsDefault;// 图片未加载出来时显示的图片
	private MyImgScroll adsPager;// 图片容器
	private LinearLayout adsOvalLayout;// 圆点容器

	private String[] noopsyches;
	private View popView;
	private PopupWindow pop;
	private PopupWindowAdapter popAdapter;
	private ListView lvPop;// 下拉选择列表

	private String cityId;
	private String areaId = "";
	private String levelId;// 380为三甲医院

	private int windowWidth;// 屏幕宽度

	private HospitalListAdapter hosAdapter;// 医院adapter

	// private List<Hospital> list;
	// private int currentPage = 1;// 当前页数
	// private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospitallist);
		ViewUtils.inject(HospitalListActivity.this);
		// 2016-1-4 添加城市自动变更
		String cityStr = BaseApplication.selectCity;
		if (!TextUtils.isEmpty(cityStr)) {
			filter = loadTitleBar(true, cityStr, "智能筛选");
		} else
			filter = loadTitleBar(true, "全国", "智能筛选");
		filter.setPadding(10, 5, 10, 5);
		filter.setOnClickListener(this);
		windowWidth = getWindowWidth();
		setTitleIcon(title);
		initAdImage();
		initData();
	}

	public void setTitleIcon(TextView tv) {
		Drawable rightDrawable = getResources().getDrawable(
				R.drawable.arrows_right_white);
		rightDrawable.setBounds(0, 2, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, rightDrawable, null);
		tv.setCompoundDrawablePadding(5);

		// 初始化城市
		String[] cityNames = getResources().getStringArray(
				R.array.appoint_city_name);
		String[] cityIds = getResources().getStringArray(
				R.array.appoint_city_id);
		String cityName = BaseApplication.selectCity;
		if (TextUtils.isEmpty(cityName)) {
			return;
		}
		for (int i = 0; i < cityNames.length; i++) {
			if (cityName.equals(cityNames[i])) {
				title.setText(cityName);
				cityId = cityIds[i];
				break;
			}
		}

	}

	public void initData() {
		hosAdapter = new HospitalListAdapter(this);
		hosList.setAdapter(hosAdapter);
		noopsyches = new String[] { "全 部", "三甲医院" };
		popView = LayoutInflater.from(this).inflate(R.layout.popwindow_choice,
				null);
		lvPop = (ListView) popView.findViewById(R.id.pop_list);
		// lvPop.setOnItemClickListener(this);
		// LinearLayout popLayout = (LinearLayout) popView
		// .findViewById(R.id.pop_layout);
		handler.sendEmptyMessageDelayed(1, 200);
		getHosData(cityId, "", "",false);
	}

	Handler handler = new Handler() {

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pop = new PopupWindow(popView, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT, false);
			pop.setBackgroundDrawable(new BitmapDrawable());
			// 设置点击窗口外边窗口消失
			pop.setOutsideTouchable(true);
			// 设置此参数获得焦点，否则无法点击
			pop.setFocusable(true);
			popAdapter = new PopupWindowAdapter(HospitalListActivity.this);
			lvPop.setAdapter(popAdapter);
		}
	};

	@OnClick(R.id.title_tv)
	private void tiltle_tvClick(View v) {
		Intent intent = new Intent(this,
				AppointCheckIn_ChoiceAreaActivity.class);
		startActivityForResult(intent, 2);
	}

	@Override
	public void onClick(View v) {
		// if (v.getId() == R.id.title_tv) {
		//
		//
		// } else {// 筛选
		if (v == filter) {
			popAdapter.setList(noopsyches);
			pop.showAsDropDown(v, 0, 10);
		}
		// }
	}

	/**
	 * 初始化广告图
	 */
	public void initAdImage() {
		/**
		 * 头部广告图
		 */
		View adsView = LinearLayout.inflate(this, R.layout.item_ads_layout,
				null);
		adsView.setPadding(0, 0, 0, 0);
		adsDefault = (ImageView) adsView.findViewById(R.id.home_loading_img);
		adsPager = (MyImgScroll) adsView.findViewById(R.id.home_img_scroll);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, windowWidth / 9 * 4);
		adsDefault.setLayoutParams(params);
		adsPager.setLayoutParams(params);
		adsOvalLayout = (LinearLayout) adsView
				.findViewById(R.id.home_point_layout);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, ResourceUtils.dip2px(this,
						ResourceUtils.getXmlDef(this,
								R.dimen.ads_point_layout_height)),
				Gravity.BOTTOM | Gravity.RIGHT);
		layoutParams.setMargins(
				0,
				0,
				ResourceUtils.dip2px(this,
						ResourceUtils.getXmlDef(this, R.dimen.large_margin)),
				ResourceUtils.dip2px(this,
						ResourceUtils.getXmlDef(this, R.dimen.medium_margin)));
		adsOvalLayout.setLayoutParams(layoutParams);
		adsPager.setmScrollTime(EZTConfig.SCROLL_TIME);

		hosList.addHeaderView(adsView);
		// hosList.setOnItemClickListener(this);
		hosList.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
		hosList.setAdapter(hosAdapter);
		getAdsList();// 获取广告图数据
	}

	/**
	 * 获取资讯广告图
	 */
	private void getAdsList() {
		ArrayList<String> imgs = new ArrayList<String>();
		String url = "";
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				url = R.drawable.finddoctor_ad_1 + "";
				break;

			case 1:
				url = R.drawable.finddoctor_ad_2 + "";
				break;

			case 2:
				url = R.drawable.finddoctor_ad_3 + "";
				break;
			}
			imgs.add(url);
		}
		adsDefault.setVisibility(View.GONE);
		ArrayList<View> listViews = InitAdsViewPager(imgs);
		adsPager.setmListViews(listViews);
		/* 重新加载初始化 */
		// myPager.stopTimer();
		// myPager.oldIndex = 0;
		// myPager.curIndex = 0;
		// ovalLayout.removeAllViews();
		/*------------*/
		adsPager.start(this, adsOvalLayout);
	}

	/**
	 * 初始化图片
	 * 
	 * @throws FileNotFoundException
	 */
	private ArrayList<View> InitAdsViewPager(ArrayList<String> imgs) {
		ArrayList<View> listViews = new ArrayList<View>();
		for (int i = 0; i < imgs.size(); i++) {
			ImageView imageView = new ImageView(this);
			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// 设置图片点击事件

					if (adsPager.getmListViews() != null) {
						// Toast.makeText(
						// HomeActivity.this,
						// "点击了:"
						// + advList.get(myPager.getCurIndex())
						// .getAdUrl(), Toast.LENGTH_SHORT)
						// .show();
					}
				}
			});
			if (imgs.size() == 0) {// 网络加载图片地址成功
				// finalBitMap.display(imageView, imgs.get(i), defaultBitmap,
				// failBitmap);
			} else {
				imageView.setImageResource(Integer.parseInt(imgs.get(i)));// 本地
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 300);
			imageView.setLayoutParams(params);
			imageView.setScaleType(ScaleType.FIT_XY);
			listViews.add(imageView);
		}
		return listViews;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			adsPager.startTimer();
			adsPager.requestDisallowInterceptTouchEvent(true);
		} else {
			adsPager.stopTimer();
			adsPager.requestDisallowInterceptTouchEvent(false);
		}
		return false;
	}

	/**
	 * 获取医院数据
	 **/
	private void getHosData(String cityIds, String countyId, String levelId,
			boolean isDayRegList) {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		HospitalImpl imple = new HospitalImpl();
		// params.put("cityid", cityIds);
		// if (!TextUtils.isEmpty(countyId)) {
		// params.put("countyid", countyId);
		// }
		// if (!TextUtils.isEmpty(levelId)) {
		// params.put("level", levelId);
		// }
		// // params.put("rowsPerPage", pageSize + "");
		// // params.put("page", currentPage + "");
		RequestParams params = new RequestParams();
		params.addBodyParameter("cityid", cityIds);
		if (!TextUtils.isEmpty(countyId)) {
			params.addBodyParameter("countyid", countyId);
		}
		if (!TextUtils.isEmpty(levelId)) {
			params.addBodyParameter("level", levelId);
		}
		if (isDayRegList) {
			imple.getDayHosList(params, this);
		} else{
			imple.getHospitalList(params, this);
		}
		showProgressToast();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == 2) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				areaId = data.getStringExtra("areaId");
				cityId = data.getStringExtra("cityId");
				String strCity = data.getStringExtra("strCity");
				title.setText(strCity);

			}
			if (cityId != null && areaId != null) {
				// currentPage = 1;// 还原页值
				getHosData(cityId, areaId, levelId,false);
			}
		}
	}

	@OnItemClick(R.id.pop_list)
	private void lvPopItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 1) {// 三甲医院
			levelId = "380";
			filter.setText(noopsyches[position]);
		} else {
			levelId = null;
			filter.setText(noopsyches[position]);
		}
		getHosData(cityId, areaId, levelId,false);
		if (pop != null) {
			pop.dismiss();
		}
	}

	@OnItemClick(R.id.hosList)
	private void hosListItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		Intent intent = new Intent(this, HospitalDetailActivity.class);
		intent.putExtra("hospital", hosAdapter.getList().get(position - 1));
		startActivity(intent);
	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// if (parent.getId() == R.id.pop_list) {
	// // if (position == 1) {// 三甲医院
	// // levelId = "380";
	// // filter.setText(noopsyches[position]);
	// // } else {
	// // levelId = null;
	// // filter.setText(noopsyches[position]);
	// // }
	// // getHosData(cityId, areaId, levelId);
	// // if (pop != null) {
	// // pop.dismiss();
	// // }
	// } else {
	// Intent intent = new Intent(this, HospitalDetailActivity.class);
	// intent.putExtra("hospital", hosAdapter.getList().get(position - 1));
	// startActivity(intent);
	// }
	// }

	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer type = (Integer) object[0];
		if (type == null) {
			return;
		}
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_HOS:// 获取医院列表
			if (isSucc) {
				hosList.setVisibility(View.VISIBLE);
				List<Hospital> list = (ArrayList<Hospital>) object[2];
				hosAdapter.setList(list);
			} else {
				String error = (String) object[3];
				Toast.makeText(this, error + "", Toast.LENGTH_SHORT).show();
			}

			break;
		}
	}
}

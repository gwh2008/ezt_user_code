package com.eztcn.user.eztcn.fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import xutils.BitmapUtils;
import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.activity.home.BigDoctorList30Activity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.ENurseHelpActivity;
import com.eztcn.user.eztcn.activity.home.dragoncard.ActivityDragonCard;
import com.eztcn.user.eztcn.activity.home.dragoncard.DragonToActive30Activity;
import com.eztcn.user.eztcn.activity.home.drug.DrugListActivity;
import com.eztcn.user.eztcn.activity.home.message.ActivityMessageList;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.customView.MyViewPager;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.customView.PullToRefreshView;
import com.eztcn.user.eztcn.customView.PullToRefreshView.OnFooterRefreshListener;
import com.eztcn.user.eztcn.customView.PullToRefreshView.OnHeaderRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.impl.MessageImpl;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.CustomPopupWindow;
import com.eztcn.user.eztcn.utils.FontUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.PicUtils;
import com.eztcn.user.eztcn.utils.ScreenUtils;
/**
 * @title 首页 3.0版本
 * @describe
 * @author LiuGang
 * @created 2016年03月03日
 */
public class HomeFragment30 extends FinalFragment implements OnClickListener,
		IHttpResult,OnFooterRefreshListener,
		OnHeaderRefreshListener {//OnTouchListener
	private PullToRefreshView pullRootView;
	private Activity activity;
	private View rootView;

	private TextView locationTV;

	private RelativeLayout homeImgSearch;

	// 健康头条
	private MyImgScroll msgViewPager;
	private TextView msgTitle;
	private TextView msgPage;

	private CustomPopupWindow popUpWindow;
	private String recordId;
	/**
	 * 消息提醒
	 */
	private ImageView msgTip;
	/**
	 * 健康头条
	 */
	private ImageView msgText;
	private ImageView loadingMsgImg;
	private ImageView dargonIv;
	private ArrayList<Information> infoList;
	private View dargonCardLayout, eHelpLayout, orderRegLayout, bigDocLayout,
			todayRegLayout, orderCheckLayout, orderBedLayout, orderMedLayout,
			onlineQLayout, symCheckTv;
	private LinearLayout imgParent;
	private LinearLayout ovalLayout;
	private boolean isAuto = false;// 是否自动登录
	public static boolean isChangedCity = false;
	private String recordCityStr = "选择城市";
	private DragonCard card;
	private View message_remind_layout;
	private Timer timer;

	public static HomeFragment30 newInstance() {
		HomeFragment30 fragment = new HomeFragment30();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_home30, null);// 缓存Fragment

			initView();
			isAuto = SystemPreferences.getBoolean(EZTConfig.KEY_IS_AUTO_LOGIN,
					false);
			if (isAuto && BaseApplication.patient == null
					&& BaseApplication.getInstance().isFirst) {// 自动登录
				autoLogin();
				BaseApplication.getInstance().isFirst = false;
			}
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void autoLogin() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("username",
				SystemPreferences.getString(EZTConfig.KEY_ACCOUNT));
		params.addBodyParameter("password",
				SystemPreferences.getString(EZTConfig.KEY_PW));

		UserImpl impl = new UserImpl();
		impl.userLogin(params, this);
	}
	/**
	 * 初始化view控件
	 */
	private void initView() {

		pullRootView = (PullToRefreshView) rootView
				.findViewById(R.id.pullRootView);
		pullRootView.setOnFooterRefreshListener(this);
		pullRootView.setOnHeaderRefreshListener(this);

		// 消息提醒Layout

		message_remind_layout = rootView
				.findViewById(R.id.message_remind_layout);
		message_remind_layout.setOnClickListener(this);
		// 消息头条start
		msgText = (ImageView) rootView.findViewById(R.id.homeMsgText);
		msgText.setOnClickListener(this);
		msgTip = (ImageView) rootView.findViewById(R.id.homeMsgTip);

		ovalLayout = (LinearLayout) rootView
				.findViewById(R.id.home_point_layout);// 圆点容器
		ovalLayout.setVisibility(View.VISIBLE);

		msgViewPager = (MyImgScroll) rootView
				.findViewById(R.id.homeMsgViewPager);
		loadingMsgImg = (ImageView) rootView
				.findViewById(R.id.homeMsgLoadingImg);
		// msgViewPager.setVisibility(View.GONE);
		// loadingMsgImg.setVisibility(View.VISIBLE);
		loadingMsgImg.setVisibility(View.GONE);
		msgViewPager.setVisibility(View.VISIBLE);
		msgViewPager.setmScrollTime(EZTConfig.SCROLL_TIME);
		initialViewList();
		Bitmap bitmap1 = PicUtils.zoomImg(R.drawable.newlongka,
				ScreenUtils.gainDM(activity).widthPixels, activity);
		dargonIv = (ImageView) rootView.findViewById(R.id.dargonIv);
		dargonIv.setImageBitmap(bitmap1);
		dargonIv.setOnClickListener(this);

		msgTitle = (TextView) rootView.findViewById(R.id.homeMsgTitle);
		msgPage = (TextView) rootView.findViewById(R.id.homeMsgPage);
		// 消息头条end
		// 全站搜索 satart
		homeImgSearch = (RelativeLayout) rootView
				.findViewById(R.id.homeImgSearch);
		homeImgSearch.setOnClickListener(this);
		// 全站搜索 end
		// 城市定位start
		locationTV = (TextView) rootView.findViewById(R.id.homeLocationTV);
		locationTV.setOnClickListener(this);
		// 城市定位end

		// 龙卡
		dargonCardLayout = rootView.findViewById(R.id.dargonCardLayout);
		dargonCardLayout.setOnClickListener(this);
		// 预约挂号
		orderRegLayout = rootView.findViewById(R.id.orderRegLayout);
		orderRegLayout.setOnClickListener(this);
		// 打牌名医
		bigDocLayout = rootView.findViewById(R.id.bigDocLayout);
		bigDocLayout.setOnClickListener(this);
		// 今天号
		todayRegLayout = rootView.findViewById(R.id.todayRegLayout);
		todayRegLayout.setOnClickListener(this);
		// 月检查
		orderCheckLayout = rootView.findViewById(R.id.orderCheckLayout);
		orderCheckLayout.setOnClickListener(this);
		// 约病床
		orderBedLayout = rootView.findViewById(R.id.orderBedLayout);
		orderBedLayout.setOnClickListener(this);
		// 约药
		orderMedLayout = rootView.findViewById(R.id.orderdrugsTv);
		orderMedLayout.setOnClickListener(this);
		// 在线咨询
		onlineQLayout = rootView.findViewById(R.id.onlineQLayout);
		onlineQLayout.setOnClickListener(this);
		// ehlep
		eHelpLayout = rootView.findViewById(R.id.eHelpLayout);
		eHelpLayout.setOnClickListener(this);

		symCheckTv = rootView.findViewById(R.id.symCheckTv);
		symCheckTv.setOnClickListener(this);
		FontUtils.changeFonts(getActivity(), rootView);
	}
	private int pagerHeight=-1;
	private List<View> loadViews(){
		List<View> viewList = new ArrayList<View>();
		ImageView image0 = new ImageView(activity);
		Bitmap bitmap0 = PicUtils.zoomImg(R.drawable.new_home_circle_stay_in,
				ScreenUtils.gainDM(activity).widthPixels, activity);
		image0.setImageBitmap(bitmap0);
		ImageView image1 = new ImageView(activity);
		Bitmap bitmap1 = PicUtils.zoomImg(R.drawable.new_home_circlr_appointment_check,
				ScreenUtils.gainDM(activity).widthPixels, activity);
		image1.setImageBitmap(bitmap1);
		pagerHeight=bitmap1.getHeight();
		ImageView image2 = new ImageView(activity);
		Bitmap bitmap2 = PicUtils.zoomImg(R.drawable.new_home_circle_appointment_bed,
				ScreenUtils.gainDM(activity).widthPixels, activity);
		image2.setImageBitmap(bitmap2);
		pagerHeight=bitmap2.getHeight();
		viewList.add(image0);
		viewList.add(image1);
		viewList.add(image2);
		return viewList;

	}
	
	private void initialViewList() {
		ArrayList<View> listViews = (ArrayList<View>) loadViews();
		msgViewPager.setScrollble(true);
		msgViewPager.getLayoutParams().height=pagerHeight;
		msgViewPager.setmListViews(listViews);
		/* 重新加载初始化 */
		msgViewPager.stopTimer();
		msgViewPager.oldIndex = 0;
		msgViewPager.curIndex = 0;
		ovalLayout.removeAllViews();
		/*------------*/
		msgViewPager.start(activity, ovalLayout);
	}

	@Override
	public void onResume() {
		super.onResume();
		setCityView();

		View msgRed = rootView.findViewById(R.id.msgRed);
		if (null != BaseApplication.patient) {
			Boolean flag_HaveMsg = SystemPreferences
					.getBoolean(EZTConfig.KEY_HAVE_MSG);
			if (flag_HaveMsg) {
				msgRed.setVisibility(View.VISIBLE);
			} else
				msgRed.setVisibility(View.GONE);
		} else
			msgRed.setVisibility(View.GONE);

	}

	public void setRedVisable() {
		View msgRed = rootView.findViewById(R.id.msgRed);
		msgRed.setVisibility(View.VISIBLE);
	}

	private void setCityView() {

		locationTV
				.setText(TextUtils.isEmpty(BaseApplication.selectCity) ? "选择城市"
						: BaseApplication.selectCity);
	}
	/**
	 * 获取新闻头条
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 11) {
			City city = (City) data.getSerializableExtra("city");
			if (locationTV != null && city != null) {
				BaseApplication.cCity = city;// 给application赋值方便取当前城市
				locationTV.setText(city.getCityName());
				BaseApplication.selectCity = city.getCityName();
				if(null!=BaseApplication.selectCity){
					if (!recordCityStr.equals(BaseApplication.selectCity)) {// 如果数值一样就判断是没有改变医院，如果不一样就是改变了医院
						recordCityStr = BaseApplication.selectCity;
						isChangedCity = true;
					} else {
						isChangedCity = false;
					}
				}else{
					isChangedCity = false;
				}
			}
		}
	}

	/**
	 * 跳转到城市选择界面
	 */
	public void jumToCity() {
		Intent intent = new Intent();
		startActivityForResult(intent.setClass(activity, ChoiceCityActivity.class), 1);
	}

	private void getHealthDragonInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",
				String.valueOf(BaseApplication.patient.getUserId()));
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.judgeDragonBind(params, this);
		((FinalActivity) activity).showProgressToast();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.message_remind_layout: {
			// 消息
			if (null == BaseApplication.patient) {
				((FinalActivity) activity).HintToLogin(0);
				return;
			}
			SystemPreferences.save(EZTConfig.KEY_HAVE_MSG, false);
			startActivity(new Intent(activity, ActivityMessageList.class));

		}
			break;
		case R.id.homeImgSearch: {// 全站搜索
			Intent intent = new Intent(activity, ChoiceHosActivity.class);
			intent.putExtra("isDayRegList", false);
			intent.putExtra("isChangedCity", isChangedCity);
			intent.putExtra("isSearchInit", true);
			startActivity(intent);
		}
			break;
		case R.id.homeLocationTV: {// 选择城市
			jumToCity();
		}
			break;
		case R.id.dargonIv: {// 健康龙卡
			if (null == BaseApplication.patient) {
				((FinalActivity) getActivity()).HintToLogin(0);
				return;
			}
			// getHealthDragonInfo();
			Intent intent = new Intent(activity, ActivityDragonCard.class);
			startActivity(intent);

		}
			break;
		case R.id.eHelpLayout: {// e护帮
			startActivity(new Intent(activity, ENurseHelpActivity.class));
		}
			break;
		case R.id.orderRegLayout: {// 预约挂号
			Intent intent = new Intent(activity, ChoiceHosActivity.class);
			intent.putExtra("isDayRegList", false);
			intent.putExtra("isChangedCity", isChangedCity);
			startActivity(intent);
		}
			break;
		case R.id.bigDocLayout: {// 大牌子名医
			Intent intent = new Intent(activity, BigDoctorList30Activity.class);
			intent.putExtra("isChangedCity", isChangedCity);
			startActivity(intent);

		}
			break;
		case R.id.todayRegLayout: {// 今日挂号
			Intent intent = new Intent(activity, ChoiceHosActivity.class);
			intent.putExtra("isChangedCity", isChangedCity);
			intent.putExtra("isDayRegList", true);
			startActivity(intent);

		}
			break;
		case R.id.orderCheckLayout: {// 预约检查
			Intent intent = new Intent(activity, ChoiceHosActivity.class);
			intent.putExtra("isChangedCity", isChangedCity);
			intent.putExtra("isOrderCheck", true);
			startActivity(intent);
		}
			break;
		case R.id.orderBedLayout: {// 预约病床
			startActivity(new Intent(getActivity(),
					OrderBedNoticeActivity.class));
		}
			break;
		case R.id.orderdrugsTv: {// 预约药品
			Intent intent = new Intent(activity, DrugListActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.onlineQLayout: {// 在线咨询
			Toast.makeText(activity, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
		}
			break;
		case R.id.symCheckTv: {// 症状自查
			startActivity(new Intent(this.getActivity(),
					SymptomSelfActivity.class));
		}
			break;
		default: {
			// 一旦点击进入就消除红点
			SystemPreferences.save(EZTConfig.MSG_RECORDID, recordId);
			setMsgRed(false, 0);
			startActivity(new Intent(activity, InformationActivity.class));
		}
			break;

		}
	}

	@Override
	public void result(Object... object) {
		FinalActivity.getInstance().hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc=false;
		if(object[1]!=null){
			isSuc= (Boolean) object[1];
		}
		switch (type) {
		case HttpParams.USER_LOGIN:// 登录
		{
			if (isSuc) {
				Map<String, Object> mapLogin = (Map<String, Object>) object[2];
				if (mapLogin != null) {
					boolean flag = (Boolean) mapLogin.get("flag");
					if (flag) {
						// 登录成功
						if ((BaseApplication.getInstance()) != null) {
							setClientid(BaseApplication.patient
									.getUserId() + "");
							getRegregisterNew();

							initialData();
						}
					} else {

						Toast.makeText(getActivity(), "login失败了--",
								Toast.LENGTH_SHORT).show();
					}

				}
			}
		}
			break;
		case HttpParams.GET_CCB_INFO_BY_UID: {
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("flag")) {
					if ((Boolean) map.get("flag")) {
						// 绑定卡了
						if (map.containsKey("data")) {
							card = (DragonCard) map.get("data");
							jumToDragonCardInfo();
						}
					} else {
						// 用户信息不存在 未绑定卡
						card = null;
						jumpToDragonActivie();
					}
				}
			}
		}
			break;
		case HttpParams.FIND_TRAORDER_INFO: {

			// 获取服务器版本信息
			boolean status = (Boolean) object[1];
			if (!status) {
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map == null || map.size() == 0) {
				return;
			}
			if (map.containsKey(EZTConfig.KEY_TOTAL)) {// 服务器的total
				Integer saveTotal = null;
				if ("0".equals(SystemPreferences.getString(EZTConfig.KEY_TOTAL)))
					saveTotal = 0;
				int total = Integer.parseInt(map.get("total").toString());
				if ((null == saveTotal && total > 0)
						|| (null != saveTotal && saveTotal < total)) {
					setRedVisable();
					SystemPreferences.save(EZTConfig.KEY_TOTAL,
							map.get("total"));
					SystemPreferences.save(EZTConfig.KEY_HAVE_MSG, true);
				}
			}

		}
			break;
		}
	}

	private void initialData() {
		RequestParams params = new RequestParams();
		MessageImpl impl = new MessageImpl();
		params.addBodyParameter("userId",
				String.valueOf(BaseApplication.patient.getUserId()));
		params.addBodyParameter("page", "1");
		impl.findOrderInfo(params, this);
		FinalActivity.getInstance().showProgressToast();
	}

	private void jumToDragonCardInfo() {
		Intent intent = new Intent();
		intent.setClass(activity, ActivityDragonCard.class);
		startActivity(intent);
	}

	private void jumpToDragonActivie() {
		Intent intent = new Intent();
		intent.setClass(activity, DragonToActive30Activity.class);
		startActivity(intent);
	}

	private void setTitlePage(int index) {
		if (null != infoList) {
			msgTitle.setText(infoList.get(index).getInfoTitle());
			msgPage.setText((index + 1) + "/3");
		}
	}

	private class MsgAdapter extends PagerAdapter {
		List<View> viewList;

		public MsgAdapter(List<View> viewList) {
			this.viewList = viewList;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// 根据传来的key，找到view,判断与传来的参数View arg0是不是同一个视图
			return arg0 == viewList
					.get((int) Integer.parseInt(arg1.toString()));
		}

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			// 把当前新增视图的位置（position）作为Key传过去
			return position;
		}
	};

	/**
	 * 设置红点
	 * 
	 * @param showRed
	 * @param num
	 */
	private void setMsgRed(boolean showRed, int num) {
		if (showRed) {
			msgTip.setVisibility(View.VISIBLE);
		} else {
			msgTip.setVisibility(View.INVISIBLE);
		}
	}


	private void setClientid(String userid) {
		UserImpl impl = new UserImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userid", userid);
		params.addBodyParameter("cid",
				SystemPreferences.getString(EZTConfig.KEY_CID));
		impl.setClientid(params, this);
	}

	/**
	 * 获取最新预约信息
	 */
	private void getRegregisterNew() {
		RegistratioImpl api = new RegistratioImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("patientId",
				"" + BaseApplication.patient.getId());
		api.getRegregisterNew(params, this);
	}

	public TextView getLocationTV() {
		return locationTV;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(FOOTER_REFRESH);
			}
		};
		if (null == timer)
			timer = new Timer();
		timer.schedule(task, 1000);
	}

	private final int HEADER_REFRESH = 12, FOOTER_REFRESH = 13;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HEADER_REFRESH: {
				pullRootView.onHeaderRefreshComplete();
			}
				break;
			case FOOTER_REFRESH: {
				pullRootView.onFooterRefreshComplete();
			}
				break;
			}

		};
	};

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				handler.sendEmptyMessage(HEADER_REFRESH);
			}
		};
		if (null == timer)
			timer = new Timer();
		timer.schedule(task, 1000);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (null != timer) {
			timer.cancel();
			timer = null;
		}
	}
}

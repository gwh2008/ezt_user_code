package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.activity.home.Activity_Dargon;
import com.eztcn.user.eztcn.activity.home.AllSearchActivity;
import com.eztcn.user.eztcn.activity.home.DoctorIndexActivity;
import com.eztcn.user.eztcn.activity.mine.ChoiceCityActivity;
import com.eztcn.user.eztcn.activity.mine.MyCollectionActivity;
import com.eztcn.user.eztcn.adapter.FamousDocAdapter;
import com.eztcn.user.eztcn.adapter.HomeFunctionAdapter;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Function;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.compent.IntentParams;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyViewPager;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.NewsImpl;
import com.eztcn.user.eztcn.utils.CustomPopupWindow;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ScreenUtils;
import com.eztcn.user.hall.utils.Constant;

public class NewHomeFragment extends FinalFragment implements OnClickListener,
		OnItemClickListener, IHttpResult, OnPageChangeListener {
	private Activity activity;
	private View rootView;

	private TextView locationTV;

	private LinearLayout homeImgSearch;

	private ImageView homeRMenuIV;
	// 健康头条
	private MyViewPager msgViewPager;
	private TextView msgTitle;
	private TextView msgPage;
	// 功能模块
	private GridView functionListView;
	private HomeFunctionAdapter functionAdapter;
	private List<Function> functionList;

	// 名医谷
	private TextView homeDocMore;
	// 名医谷数据适配器
	private FamousDocAdapter famDocAdapter;
	private GridView docHorList;

	private CustomPopupWindow popUpWindow;
	private Animation openPopAnimation;
	private Animation closePopAnimation;
	private ListView popListView;
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
	private ArrayList<Information> infoList;
	private ImageView imgCard;

	public static NewHomeFragment newInstance() {
		NewHomeFragment fragment = new NewHomeFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();
		openPopAnimation = AnimationUtils.loadAnimation(activity,
				R.anim.action_rotate_to45);
		closePopAnimation = AnimationUtils.loadAnimation(activity,
				R.anim.action_rotate_to0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (null == rootView) {
			rootView = inflater.inflate(R.layout.fragment_home, null);// 缓存Fragment
			initView();
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		loadNewsMsg();
		return rootView;
	}

	/**
	 * 初始化view控件
	 */
	private void initView() {
		// 消息头条start
		msgText = (ImageView) rootView.findViewById(R.id.homeMsgText);
		msgText.setOnClickListener(this);
		msgTip = (ImageView) rootView.findViewById(R.id.homeMsgTip);
		msgViewPager = (MyViewPager) rootView
				.findViewById(R.id.homeMsgViewPager);
		loadingMsgImg = (ImageView) rootView
				.findViewById(R.id.homeMsgLoadingImg);
		msgViewPager.setVisibility(View.GONE);
		loadingMsgImg.setVisibility(View.VISIBLE);
		msgTitle = (TextView) rootView.findViewById(R.id.homeMsgTitle);
		msgPage = (TextView) rootView.findViewById(R.id.homeMsgPage);
		msgViewPager.setOnPageChangeListener(this);
		// 消息头条end
		// 弹出菜单 start
		homeRMenuIV = (ImageView) rootView.findViewById(R.id.homeRMenuIV);
		homeRMenuIV.setOnClickListener(this);
		// 弹出菜单 end
		// 全站搜索 satart
		homeImgSearch = (LinearLayout) rootView
				.findViewById(R.id.homeImgSearch);
		homeImgSearch.setOnClickListener(this);
		// 全站搜索 end
		// 城市定位start
		locationTV = (TextView) rootView.findViewById(R.id.homeLocationTV);
		locationTV.setOnClickListener(this);
		// 城市定位end
		// 功能列表start
		functionListView = (GridView) rootView
				.findViewById(R.id.homeFunctionHLV);
		LinearLayout.LayoutParams functionParams = (LinearLayout.LayoutParams) functionListView
				.getLayoutParams();
		int margin = functionParams.leftMargin + functionParams.rightMargin;
		functionAdapter = new HomeFunctionAdapter(activity,
				(ScreenUtils.gainDM(activity).widthPixels - margin) / 4);
		functionListView.setAdapter(functionAdapter);

		functionList = new ArrayList<Function>();
		Function reg_function = new Function();
		Bitmap regBtimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_register);
		reg_function.setImageBitmap(regBtimap);

		reg_function
				.setJumpLink("com.eztcn.user.eztcn.activity.fdoc.HospitalListActivity");
		reg_function.setName("预约挂号");
		functionList.add(reg_function);

		Function tel_function = new Function();
		Bitmap teldocBtimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_teldoc);
		tel_function.setImageBitmap(teldocBtimap);
		tel_function.setName("电话医生");
		tel_function
				.setJumpLink("com.eztcn.user.eztcn.activity.home.TelDoctorListActivity");
		functionList.add(tel_function);

		Function check_function = new Function();
		Bitmap regCheckBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_regcheck);
		check_function.setImageBitmap(regCheckBitmap);
		check_function.setName("预约检查");
		check_function.setJumpLink(null);
		functionList.add(check_function);

		Function bed_function = new Function();
		Bitmap bedBtimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_bed);
		bed_function.setImageBitmap(bedBtimap);
		bed_function.setName("预约病床");
		bed_function.setJumpLink(null);
		functionList.add(bed_function);

		functionAdapter.setList(functionList);
		functionListView.setOnItemClickListener(this);
		// 功能列表end
		// 健康你我他start
		imgCard = (ImageView) rootView.findViewById(R.id.homeImgCard);
		imgCard.setOnClickListener(this);
		// 健康你我他end
		// 名医谷start
		homeDocMore = (TextView) rootView.findViewById(R.id.homeDocMore);
		homeDocMore.setOnClickListener(this);
		docHorList = (GridView) rootView.findViewById(R.id.homeDocHorList);
		famDocAdapter = new FamousDocAdapter(activity);
		docHorList.setAdapter(famDocAdapter);
		docHorList.setOnItemClickListener(this);

		List<Doctor> doctorList = new ArrayList<Doctor>();
		Doctor doctorWu = new Doctor();
		doctorWu.setDocName("吴咸中");
		doctorWu.setDocPosition("中国工程院院士");
		doctorWu.setDocGoodAt("心血管科");
		doctorList.add(doctorWu);

		Doctor doctorZhang = new Doctor();
		doctorZhang.setDocName("张伯礼");
		doctorZhang.setDocPosition("中国工程院院士");
		doctorZhang.setDocGoodAt("心血管科");
		doctorList.add(doctorZhang);

		famDocAdapter.setList(doctorList);
		// 名医谷end

	}

	@Override
	public void onResume() {
		super.onResume();
		setRightClickView();
		setCityView();

	}

	private void setCityView() {
		locationTV
				.setText(TextUtils.isEmpty(BaseApplication.selectCity) ? "选择城市"
						: BaseApplication.selectCity);
	}

	/**
	 * 获取新闻头条
	 */
	private void loadNewsMsg() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("listid", "all");
		params.addBodyParameter("start", 1 + "");
		params.addBodyParameter("pagesize", EZTConfig.PAGE_SIZE + "");
		NewsImpl api = new NewsImpl();
		api.getNewsList(params, this);
		FinalActivity.getInstance().showProgressToast();
	}

	/**
	 * 菜单点击设置
	 */
	private void setRightClickView() {
		View popView = View.inflate(activity, R.layout.popwindow_choice, null);
		popListView = (ListView) popView.findViewById(R.id.pop_list);
		PopupWindowAdapter adapter = new PopupWindowAdapter(activity);
		List<String> list = new ArrayList<String>();
		list.add("症状自查");
		list.add("我的收藏");
		adapter.setList(list);
		popListView.setAdapter(adapter);
		popUpWindow = new CustomPopupWindow(popView,
				ScreenUtils.gainDM(activity).widthPixels / 3,
				LayoutParams.WRAP_CONTENT);
		popUpWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				openPopAnimation.cancel();
				homeRMenuIV.startAnimation(closePopAnimation);
				popUpWindow.dismiss();
			}
		});
		popListView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == popListView) {
			popUpWindow.dismiss();
			// 症状自查 或者 我的收藏
			switch (position) {
			case 0: {
				// 症状自查
				startActivity(new Intent(activity, SymptomSelfActivity.class));
			}
				break;
			case 1: {
				// 我的收藏
				if(null==BaseApplication.patient){
					((FinalActivity)activity).HintToLogin(Constant.LOGIN_COMPLETE);
				return;	
				}
				startActivity(new Intent(activity, MyCollectionActivity.class));
			}
				break;
			}
		} else if (parent == docHorList) {// 名医谷跳转到个人医生站
			switch (position) {
			case 0: {
				Intent intent = new Intent(this.activity,
						DoctorIndexActivity.class);
				intent.putExtra("docId", "2442");
				intent.putExtra("deptId", "701");
				intent.putExtra("deptDocId", "2442"); 
				startActivity(intent);
			}
				break;
			case 1: {
				Intent intent = new Intent(this.activity,
						DoctorIndexActivity.class);
				intent.putExtra("docId", "3295");
				intent.putExtra("deptId", "718");
				intent.putExtra("deptDocId", "3295");
				startActivity(intent);
			}
				break;
			}
		} else
			switch (parent.getId()) {
			case R.id.homeFunctionHLV: {// 功能模块列表
				functionAdapter.setTouchPosition(position);
				// //HorizontalListView
				// 单点事件对适配器单点有影响 该方式不可尝试
				functionListView.setSelection(position);
				Function function_temp = functionList.get(position);
				try {
					if (null == function_temp.getJumpLink()) {
						Toast.makeText(activity,
								getString(R.string.function_hint),
								Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(activity,
							Class.forName(function_temp.getJumpLink()));
					if (null != function_temp.getIntentParamList()) {
						List<IntentParams> params = function_temp
								.getIntentParamList();
						for (int i = 0; i < params.size(); i++) {
							intent.putExtra(params.get(i).getKey(),
									params.get(i).getKey());
						}
					}
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
				break;
			}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 11) {
			City city = (City) data.getSerializableExtra("city");
			if (locationTV != null && city != null) {
				locationTV.setText(city.getCityName());
				BaseApplication.selectCity = city.getCityName();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homeRMenuIV: {
			v.setClickable(true);
			closePopAnimation.cancel();
			v.startAnimation(openPopAnimation);
			// 获取点击菜单栏的按钮的坐标
			int[] location = new int[2];
			v.getLocationInWindow(location);
			popUpWindow.showAsDropDown(
					v,
					-ScreenUtils.gainDM(activity).widthPixels + location[0]
							- v.getWidth(), 0);
		}
			break;
		case R.id.homeImgSearch: {// 全站搜索
			startActivity(new Intent(activity, AllSearchActivity.class));
		}
			break;
		case R.id.homeLocationTV: {// 选择城市
			Intent intent = new Intent();
			startActivityForResult(
					intent.setClass(activity, ChoiceCityActivity.class), 1);
		}
			break;
		case R.id.homeImgCard: {// 健康365
			startActivity(new Intent(activity, Activity_Dargon.class));
		}
			break;
		case R.id.homeDocMore: {// 名医谷跳转到医生列表界面 暂时不实现

		}
			break;
		case R.id.homeMsgText: {// 健康头条

		}
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
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_NEWS_LIST:// 获取资讯列表--->头条新闻
		{
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					if ((Boolean) map.get("flag")) {
						if (map.containsKey("message")) {// 2015-12-22 提示信息
							String msgStr = map.get("message").toString();
							Toast.makeText(activity, msgStr, Toast.LENGTH_SHORT)
									.show();
						}
						infoList = (ArrayList<Information>) map.get("infoList");
						String record_id_f = SystemPreferences
								.getString(EZTConfig.MSG_RECORDID);
						if (infoList != null && infoList.size() > 0) {
							recordId = infoList.get(0).getId();// 记录一个id用来显示红点
							if (!recordId.equals(record_id_f)) {// 如果记录的那个id和现在内容id不一致那么就显示红点
								setMsgRed(true, 0);
							}
							List<View> viewList = new ArrayList<View>();
							setTitlePage(0);
							for (int i = 0; i < 3; i++) {
								ImageView container = new ImageView(activity);
								Information infor = infoList.get(i);// 获取一条消息
								String imgUrl = infor.getImgUrl();// 获取图片url
								// 想办法开线程加载图片，这里决定用BitmapUtils
								BitmapUtils utils = new BitmapUtils(activity);
								utils.display(container,
										EZTConfig.OFFICIAL_WEBSITE + imgUrl);
								container.setOnClickListener(this);
								viewList.add(container);
							}
							msgViewPager.setAdapter(new MsgAdapter(viewList));
						}
					}
				}
			}
		}
		}
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

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Log.i(getTag(), "arg0 " + arg0 + " arg1 " + arg1 + " arg2 " + arg2);
		setTitlePage(arg0);
	}

	@Override
	public void onPageSelected(int arg0) {

	}
}

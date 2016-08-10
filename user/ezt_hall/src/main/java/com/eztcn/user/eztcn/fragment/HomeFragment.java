package com.eztcn.user.eztcn.fragment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.EztTestActivity;
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.activity.discover.InformationActivity;
import com.eztcn.user.eztcn.activity.discover.InformationDetailActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.activity.home.Activity_Dargon;
import com.eztcn.user.eztcn.activity.home.AllSearchActivity;
import com.eztcn.user.eztcn.activity.home.DoctorIndexActivity;
import com.eztcn.user.eztcn.activity.home.MsgManageActivity;
import com.eztcn.user.eztcn.activity.home.QuickAppointActivity;
import com.eztcn.user.eztcn.activity.home.TelDoctorListActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Advertise;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.IGroup;
import com.eztcn.user.eztcn.customView.MyImgScroll;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;

/**
 * @title 首页
 * @describe
 * @author ezt
 * @created 2014年12月11日
 */
public class HomeFragment extends FinalFragment implements OnClickListener,
		OnTouchListener, IHttpResult {
	private View rootView;
	private ImageView loadingImg;
	private MyImgScroll myPager;
	private LinearLayout ovalLayout;
	private LinearLayout layoutTransBar;
	private static ImageView imgMsg;
	private IGroup msgLayout;
	private Activity activity;
	private boolean isAuto = false;// 是否自动登录

	private final String ADS_LIST_DATA = "infoListData";// 缓存key-内容
	private CacheUtils mCache;

//	private FinalBitmap fb;
//	private BitmapUtils bitmapUtils;
//	private Bitmap defaultBit;

	// 医生姓名 头像
	private RoundImageView doc_image1, doc_image2, doc_image3, doc_image4;
	private TextView doc_name1, doc_name2, doc_name3, doc_name4;
	private TextView doc_name1_2, doc_name2_2, doc_name3_2, doc_name4_2;
	private ImageView img_fam_doc, img_free_consult, // img_case_con,
			img_mine_server,// img_child_vaccine, img_home_doc,
			img_usurl_question, img_country_reg;

	private TextView tv_child_reg, tv_fam_doc, tv_free_consult, tv_mine_server,
			tv_usurl_question,// tv_child_vaccine, tv_case_con,
			tv_country_reg;// , tv_home_doc
	private ImageView img_child_reg;
	private ImageView imgCard;
	/**
	 * 医生列表
	 */
	private ArrayList<Doctor> docList;

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();
		mCache = CacheUtils.get(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_home1, null);// 缓存Fragment
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			isAuto = SystemPreferences.getBoolean(EZTConfig.KEY_IS_AUTO_LOGIN,
					false);
			initialView(dm);
			if (isAuto && BaseApplication.patient == null
					&& BaseApplication.getInstance().isFirst) {// 自动登录
				// progressBar.setVisibility(View.VISIBLE);// 显示进度条
				Toast.makeText(getActivity(), "在自动登录---", Toast.LENGTH_SHORT).show();
				autoLogin();
				BaseApplication.getInstance().isFirst = false;
			}
			// 加载广告
			loadingImg.setVisibility(View.GONE);
			ArrayList<String> imgs = new ArrayList<String>();
			imgs.add("pic08.jpg");
			imgs.add("pic09.jpg");
			imgs.add("pic05.jpg");
			initialAdsList(imgs);

		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		return rootView;
	}

	@Override
	public void onResume() {
		initialMsgNum();
		ArrayList<String> ads_cache = (ArrayList<String>) mCache
				.getAsObject(ADS_LIST_DATA);
		getDoctorList();
		super.onResume();
	}

	private void initDocView(DisplayMetrics dm) {
		int singleWidth = (int) ((dm.widthPixels - dm.widthPixels * 0.3) / 4);

		// 初始化医生头像
		doc_image1 = (RoundImageView) rootView.findViewById(R.id.doctor_Image1);
		doc_image2 = (RoundImageView) rootView.findViewById(R.id.doctor_Image2);
		doc_image3 = (RoundImageView) rootView.findViewById(R.id.doctor_Image3);
		doc_image4 = (RoundImageView) rootView.findViewById(R.id.doctor_Image4);

		doc_image1.getLayoutParams().width = singleWidth;
		doc_image1.getLayoutParams().height = singleWidth;

		doc_image2.getLayoutParams().width = singleWidth;
		doc_image2.getLayoutParams().height = singleWidth;

		doc_image3.getLayoutParams().width = singleWidth;
		doc_image3.getLayoutParams().height = singleWidth;

		doc_image4.getLayoutParams().width = singleWidth;
		doc_image4.getLayoutParams().height = singleWidth;
		// 初始化医生姓名
		doc_name1 = (TextView) rootView.findViewById(R.id.doc_Name1);
		doc_name2 = (TextView) rootView.findViewById(R.id.doc_Name2);
		doc_name3 = (TextView) rootView.findViewById(R.id.doc_Name3);
		doc_name4 = (TextView) rootView.findViewById(R.id.doc_Name4);

		doc_name1_2 = (TextView) rootView.findViewById(R.id.doc_Name1_2);
		doc_name2_2 = (TextView) rootView.findViewById(R.id.doc_Name2_2);
		doc_name3_2 = (TextView) rootView.findViewById(R.id.doc_Name3_2);
		doc_name4_2 = (TextView) rootView.findViewById(R.id.doc_Name4_2);
		doc_image1.setOnClickListener(this);
		doc_image2.setOnClickListener(this);
		doc_image3.setOnClickListener(this);
		doc_image4.setOnClickListener(this);
	}

	/**
	 * 初始化界面
	 */
	private void initialView(DisplayMetrics dm) {
		// 最下面的图
		imgCard = (ImageView) rootView.findViewById(R.id.imgCard);
		imgCard.setOnClickListener(this);
		initDocView(dm);
		
		View adLayout=rootView.findViewById(R.id.adLayout);
		adLayout.getLayoutParams().height=dm.heightPixels/4;
		msgLayout = (IGroup) rootView.findViewById(R.id.home_layout_message);
		imgMsg = (ImageView) rootView.findViewById(R.id.home_message_img);// 消息箱图标
		layoutTransBar = (LinearLayout) rootView
				.findViewById(R.id.home_img_search);

		LayoutParams layout = layoutTransBar.getLayoutParams();
		layout.width = dm.widthPixels / 2;
		layoutTransBar.setLayoutParams(layout);

		// 初始化9个按钮
		img_child_reg = (ImageView) rootView.findViewById(R.id.child_reg);
		img_fam_doc = (ImageView) rootView.findViewById(R.id.fam_doc);
		img_free_consult = (ImageView) rootView.findViewById(R.id.free_consult);
		img_country_reg = (ImageView) rootView.findViewById(R.id.country_reg);
		img_mine_server = (ImageView) rootView.findViewById(R.id.mine_server);
		img_usurl_question = (ImageView) rootView
				.findViewById(R.id.usurl_question);
		// 初始化9个按钮下的文本view
		tv_child_reg = (TextView) rootView.findViewById(R.id.child_reg_text);
		tv_fam_doc = (TextView) rootView.findViewById(R.id.fam_doc_text);
		tv_free_consult = (TextView) rootView
				.findViewById(R.id.free_consult_text);
		// tv_case_con = (TextView) rootView.findViewById(R.id.case_con_text);
		tv_mine_server = (TextView) rootView
				.findViewById(R.id.mine_server_text);
		tv_usurl_question = (TextView) rootView
				.findViewById(R.id.usurl_question_text);
		tv_country_reg = (TextView) rootView
				.findViewById(R.id.country_reg_text);
		// tv_home_doc = (TextView) rootView.findViewById(R.id.home_doc_text);
		// tv_child_vaccine = (TextView) rootView
		// .findViewById(R.id.child_vaccine_text);
		img_child_reg.setOnTouchListener(this);
		img_fam_doc.setOnTouchListener(this);
		img_free_consult.setOnTouchListener(this);
		img_mine_server.setOnTouchListener(this);
		img_usurl_question.setOnTouchListener(this);
		img_country_reg.setOnTouchListener(this);
		img_child_reg.setOnClickListener(this);
		img_fam_doc.setOnClickListener(this);
		img_free_consult.setOnClickListener(this);
		img_mine_server.setOnClickListener(this);
		img_usurl_question.setOnClickListener(this);
		img_country_reg.setOnClickListener(this);

		loadingImg = (ImageView) rootView.findViewById(R.id.home_loading_img);// 图片未加载出来时显示的图片
		myPager = (MyImgScroll) rootView.findViewById(R.id.home_img_scroll);// 图片容器
		ovalLayout = (LinearLayout) rootView
				.findViewById(R.id.home_point_layout);// 圆点容器
		myPager.setmScrollTime(EZTConfig.SCROLL_TIME);
		layoutTransBar.setOnClickListener(this);
		msgLayout.setOnClickListener(this);
		img_child_reg.setOnClickListener(this);
		img_fam_doc.setOnClickListener(this);
		img_free_consult.setOnClickListener(this);
		// img_case_con.setOnClickListener(this);
		// img_child_vaccine.setOnClickListener(this);
		// img_home_doc.setOnClickListener(this);
		img_mine_server.setOnClickListener(this);
		img_usurl_question.setOnClickListener(this);
		img_country_reg.setOnClickListener(this);

	}
	/**
	 * 获取医生列表 onCreate 调用
	 */
	public void getDoctorList() {

		docList = new ArrayList<Doctor>();
		Doctor doctor1 = new Doctor();
		doctor1.setDeptDocId("6198");
		doctor1.setDocDeptId("806");
		doctor1.setId("6198");
		Bitmap bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_doc_image_1);
		doc_image1.setImageBitmap(bm);
		doc_name1.setText("肿瘤科");
		doc_name1_2.setText("贾英杰");
		docList.add(doctor1);

		Doctor doctor2 = new Doctor();
		doctor2.setDeptDocId("79632");
		doctor2.setDocDeptId("929");
		doctor2.setId("68175");
		Bitmap bm2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_doc_image_2);
		doc_image2.setImageBitmap(bm2);
		doc_name2.setText("特需专家");
		doc_name2_2.setText("石学敏");
		docList.add(doctor2);

		Doctor doctor3 = new Doctor();
		doctor3.setDeptDocId("3295");
		doctor3.setDocDeptId("718");
		doctor3.setId("3295");
		Bitmap bm3 = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_doc_image_3);
		doc_image3.setImageBitmap(bm3);
		doc_name3.setText("心血管科");
		doc_name3_2.setText("张伯礼");
		docList.add(doctor3);

		Doctor doctor4 = new Doctor();
		doctor4.setDeptDocId("5458");
		doctor4.setDocDeptId("1090");
		doctor4.setId("5458");
		Bitmap bm4 = BitmapFactory.decodeResource(getResources(),
				R.drawable.home_doc_image_4);
		doc_image4.setImageBitmap(bm4);
		doc_name4.setText("肾病科");
		doc_name4_2.setText("张大宁");
		docList.add(doctor4);

	}

	/**
	 * 自动登录
	 */
	private void autoLogin() {
		RequestParams params=new RequestParams();
		params.addBodyParameter("username",
				SystemPreferences.getString(EZTConfig.KEY_ACCOUNT));
		params.addBodyParameter("password", SystemPreferences.getString(EZTConfig.KEY_PW));
		
		UserImpl impl = new UserImpl();
		impl.userLogin(params, this);

	}

	/**
	 * 首页广告图赋值
	 */
	private void initialAdsList(ArrayList<String> imgs) {
		ArrayList<View> listViews = loadAdsView(imgs);
		myPager.setmListViews(listViews);
		/* 重新加载初始化 */
		myPager.stopTimer();
		myPager.oldIndex = 0;
		myPager.curIndex = 0;
		ovalLayout.removeAllViews();
		/*------------*/
		myPager.start(activity, ovalLayout);

	}

	/**
	 * 初始化 消息总数
	 */
	public static void initialMsgNum() {
	
		ArrayList<MessageAll> msgList = null;
		if (BaseApplication.patient != null) {// 已登录
			WhereBuilder whereBuilder=WhereBuilder.b("clickState", "=", "0");
			msgList = EztDb.getInstance(BaseApplication.getInstance())
					.queryAll(new MessageAll(), whereBuilder,null);
		} else {
			try {
				WhereBuilder whereBuilder=WhereBuilder.b("msgType", "=", "custom").or("msgType", "=", "停诊通知").and("clickState", "=", "0");
				msgList = EztDb
						.getInstance(BaseApplication.getInstance())
						.queryAll(new MessageAll(),
								whereBuilder,null);
			} catch (Exception e) {

			}

		}
		if (msgList.size() == 0) {// 没有未读消息
			MainActivity.getInstance().noMsg();
			imgMsg.setImageResource(R.drawable.ic_msg);
		} else {
			MainActivity.getInstance().comingMsg();
			imgMsg.setImageResource(R.drawable.ic_msg_point);
		}
	

	}

	private ArrayList<View> loadAdsView(final ArrayList<String> imgsName) {
		ArrayList<View> listViews = new ArrayList<View>();

		for (int i = 0; i < imgsName.size(); i++) {
			ImageView imageView = new ImageView(activity);
			imageView.setOnTouchListener(this);
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {// 设置图片点击事件

					if (myPager.getmListViews() != null) {
						if (imgsName.get(myPager.curIndex).equals("pic07.jpg")) {
							Intent intent = new Intent(getActivity(),
									InformationDetailActivity.class);
							intent.putExtra("infoId", 965 + "");
							startActivity(intent);
						} else if (imgsName.get(myPager.curIndex).equals(
								"pic08.jpg")) {
							// 母亲儿童
							Intent intent = new Intent(getActivity(),
									QuickAppointActivity.class);
							intent.putExtra("isWomenBaby", 1);
							activity.startActivity(intent);
						} else if (imgsName.get(myPager.curIndex).equals(
								"pic09.jpg")) {
							// 医生
							getActivity().startActivity(
									new Intent(getActivity(),
											QuickAppointActivity.class));
						} else if (imgsName.get(myPager.curIndex).equals(
								"pic05.jpg")) {
							// 电话医生
							getActivity().startActivityForResult(
									new Intent(getActivity(),
											TelDoctorListActivity.class), 1);
						}
					}
				}
			});
			Bitmap bitmap = null;
			if (imgsName.get(i).equals("pic07.jpg")) {

			} else if (imgsName.get(i).equals("pic08.jpg")) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic08);
			} else if (imgsName.get(i).equals("pic09.jpg")) {
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic09);
			} else if (imgsName.get(i).equals("pic05.jpg")) {
				// 网络加载图片地址成功
				bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.pic05);
			} else {// 滴滴红包
//				bitmap = BitmapFactory.decodeResource(getResources(),
//						R.drawable.pic_didi);
			}
			if (null != bitmap) {
				imageView.setImageBitmap(bitmap);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 300);
				imageView.setLayoutParams(params);
				imageView.setScaleType(ScaleType.FIT_XY);
				listViews.add(imageView);
			}

		}
		return listViews;
	}

	/**
	 * 初始化图片
	 * 
	 * @throws FileNotFoundException
	 */
	// private ArrayList<View> InitAdsViewPager(final ArrayList<String> imgs) {
	//
	// ArrayList<View> listViews = new ArrayList<View>();
	// for (int i = 0; i < imgs.size(); i++) {
	// ImageView imageView = new ImageView(activity);
	// imageView.setOnTouchListener(this);
	// imageView.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {// 设置图片点击事件
	//
	// if (myPager.getmListViews() != null) {
	// if (imgs.get(myPager.curIndex).equals("pic07.jpg")) {
	// Intent intent = new Intent(getActivity(),
	// InformationDetailActivity.class);
	// intent.putExtra("infoId", 965 + "");
	// startActivity(intent);
	// }
	// }
	// }
	// });
	// // if (imgs.size() == 0) {
	// // 网络加载图片地址成功
	// String url = EZTConfig.HOME_ADS_PIC + imgs.get(i);
	// fb.display(imageView, EZTConfig.HOME_ADS_PIC + imgs.get(i),
	// defaultBit, defaultBit);
	// // }
	// // else {
	// // imageView.setImageResource(Integer.parseInt(imgs.get(i)));// 本地
	// // }
	// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	// LayoutParams.MATCH_PARENT, 300);
	// imageView.setLayoutParams(params);
	// imageView.setScaleType(ScaleType.FIT_XY);
	// listViews.add(imageView);
	// }
	// return listViews;
	// }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.home_img_search:// 全站搜索
			this.activity.startActivity(new Intent(this.activity,
					AllSearchActivity.class));
			break;

		case R.id.home_layout_message:// 消息箱
			this.activity.startActivity(new Intent(this.activity,
					MsgManageActivity.class));
			break;
		case R.id.doctor_Image1: {
			Intent intent = new Intent(this.activity, DoctorIndexActivity.class);
			Doctor doctor = docList.get(0);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("deptId", doctor.getDocDeptId());
			intent.putExtra("deptDocId", doctor.getDeptDocId());
			startActivity(intent);
		}
			break;
		case R.id.doctor_Image2: {
			Intent intent = new Intent(this.activity, DoctorIndexActivity.class);
			Doctor doctor = docList.get(1);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("deptId", doctor.getDocDeptId());
			intent.putExtra("deptDocId", doctor.getDeptDocId());
			startActivity(intent);
		}
			break;
		case R.id.doctor_Image3: {
			Intent intent = new Intent(this.activity, DoctorIndexActivity.class);
			Doctor doctor = docList.get(2);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("deptId", doctor.getDocDeptId());
			intent.putExtra("deptDocId", doctor.getDeptDocId());
			startActivity(intent);
		}
			break;
		case R.id.doctor_Image4: {
			Intent intent = new Intent(this.activity, DoctorIndexActivity.class);
			Doctor doctor = docList.get(3);
			intent.putExtra("docId", doctor.getId());
			intent.putExtra("deptId", doctor.getDocDeptId());
			intent.putExtra("deptDocId", doctor.getDeptDocId());
			startActivity(intent);
		}
			break;
		case R.id.imgCard: {
			// 点击最下面的图
			getActivity().startActivity(
					new Intent(getActivity(), Activity_Dargon.class));
		}
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL: {
			myPager.startTimer();
			if (-1 != v.getId()) {
				resumBtn(v.getId(), false);
			}
		}
			break;
		case MotionEvent.ACTION_UP: {
			myPager.startTimer();
			resumBtn(v.getId(), true);
		}
			break;

		default: {

			myPager.stopTimer();

			switch (v.getId()) {
			case R.id.child_reg: {
				onTouchBtn(v.getId());
			}
				break;
			case R.id.fam_doc: {
				onTouchBtn(v.getId());
			}
				break;
			case R.id.free_consult: {
				onTouchBtn(v.getId());
			}
				break;
			case R.id.country_reg: {
				onTouchBtn(v.getId());
			}
				break;
			// case R.id.child_vaccine: {
			// onTouchBtn(v.getId());
			// }
			// break;
			// case R.id.home_doc: {
			// onTouchBtn(v.getId());
			// }
			// break;
			case R.id.mine_server: {
				onTouchBtn(v.getId());
			}
				break;
			case R.id.usurl_question: {
				onTouchBtn(v.getId());
			}
				break;
			// case R.id.case_con: {
			// onTouchBtn(v.getId());
			// }
			// break;

			}
		}
			break;
		}

		return false;
	}

	/**
	 * 将按钮与文字都变成正常
	 * 
	 * @param btnId
	 * @param jump
	 *            是否跳转
	 */
	private void resumBtn(int btnId, boolean jump) {

		// 处理图片回复效果
		switch (btnId) {
		case R.id.child_reg: {
			// 妇幼挂号
			// img_child_reg.setImageResource(R.drawable.home_child_reg);
			removeFilter(img_child_reg);
			tv_child_reg.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				Intent intent = new Intent(getActivity(),
						QuickAppointActivity.class);
				intent.putExtra("isWomenBaby", 1);
				activity.startActivity(intent);
			}
		}
			break;
		case R.id.fam_doc: {
			// 名医名师
			// img_fam_doc.setImageResource(R.drawable.home_fam_doc);
			removeFilter(img_fam_doc);
			tv_fam_doc.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				getActivity().startActivityForResult(
						new Intent(getActivity(), TelDoctorListActivity.class),
						1);
			}
		}
			break;
		case R.id.free_consult: {
			// 免费咨询
			// img_free_consult.setImageResource(R.drawable.home_free_consult);
			removeFilter(img_free_consult);
			tv_free_consult.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				startActivity(new Intent(this.getActivity(),
						SymptomSelfActivity.class));
			}
		}
			break;
		case R.id.country_reg: {
			// 全国挂号
			// img_country_reg.setImageResource(R.drawable.home_country_reg);
			removeFilter(img_country_reg);
			tv_country_reg.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				getActivity().startActivity(
						new Intent(getActivity(), QuickAppointActivity.class));
			}
		}
			break;
		// case R.id.child_vaccine: {
		// // 宝贝疫苗
		// // img_child_vaccine.setImageResource(R.drawable.home_child_vaccine);
		// removeFilter(img_child_vaccine);
		// tv_child_vaccine.setTextColor(getResources().getColor(
		// R.color.font_unclick_color));
		// if (jump) {
		// // Intent intent = new Intent(activity, SendMedicineActivity.class);
		// Intent intent = new Intent(activity, BabyVaccineActivity.class);
		// activity.startActivity(intent);
		// }
		// }
		// break;

		// case R.id.home_doc: {
		// // 家庭医生
		// // img_home_doc.setImageResource(R.drawable.home_doc);
		// removeFilter(img_home_doc);
		// tv_home_doc.setTextColor(getResources().getColor(
		// R.color.font_unclick_color));
		// if (jump) {
		// getActivity().startActivityForResult(
		// new Intent(getActivity(), TelDoctorListActivity.class),
		// 1);
		// }
		// }
		// break;
		case R.id.mine_server: {
			// 我的服务
			// img_mine_server.setImageResource(R.drawable.home_mine_server);
			removeFilter(img_mine_server);
			tv_mine_server.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				activity.startActivity(new Intent(getActivity(),
						EztTestActivity.class));
			}

		}
			break;
		case R.id.usurl_question: {
			// 常见问题跳转到微资讯
			// img_usurl_question.setImageResource(R.drawable.home_usurl_question);
			removeFilter(img_usurl_question);
			tv_usurl_question.setTextColor(getResources().getColor(
					R.color.font_unclick_color));
			if (jump) {
				activity.startActivity(new Intent(getActivity(),
						InformationActivity.class));
			}
		}
			break;
		}

	}

	/**
	 * 设置滤镜
	 */
	private void setFilter(ImageView image) {
		// 先获取设置的src图片
		Drawable drawable = image.getDrawable();
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = image.getBackground();
		}
		if (drawable != null) {
			// 设置滤镜
			drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
		}
	}

	/**
	 * 清除滤镜
	 */
	private void removeFilter(ImageView image) {
		// 先获取设置的src图片
		Drawable drawable = image.getDrawable();
		// 当src图片为Null，获取背景图片
		if (drawable == null) {
			drawable = image.getBackground();
		}
		if (drawable != null) {
			// 清除滤镜
			drawable.clearColorFilter();
		}
	}

	/**
	 * 点击按钮使得按钮发生颜色改变
	 * 
	 * @param btnId
	 */
	private void onTouchBtn(int btnId) {
		ImageView image = (ImageView) rootView.findViewById(btnId);
		setFilter(image);
		switch (btnId) {
		case R.id.child_reg: {
			tv_child_reg.setTextColor(getResources().getColor(
					R.color.main_color));
		}
			break;
		// case R.id.case_con: {
		// tv_case_con.setTextColor(getResources()
		// .getColor(R.color.main_color));
		// }
		// break;
		case R.id.fam_doc: {
			tv_fam_doc
					.setTextColor(getResources().getColor(R.color.main_color));
		}
			break;
		case R.id.free_consult: {
			tv_free_consult.setTextColor(getResources().getColor(
					R.color.main_color));
		}
			break;
		case R.id.country_reg: {
			tv_country_reg.setTextColor(getResources().getColor(
					R.color.main_color));
		}
			break;
		// case R.id.child_vaccine: {
		// tv_child_vaccine.setTextColor(getResources().getColor(
		// R.color.main_color));
		// }
		// break;
		// case R.id.home_doc: {
		// tv_home_doc.setTextColor(getResources()
		// .getColor(R.color.main_color));
		// }
		// break;
		case R.id.mine_server: {
			tv_mine_server.setTextColor(getResources().getColor(
					R.color.main_color));
		}
			break;
		case R.id.usurl_question: {
			tv_usurl_question.setTextColor(getResources().getColor(
					R.color.main_color));
		}
			break;
		}

	}

	@Override
	public void result(Object... object) {
		if (object == null) {
			return;
		}
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			switch (type) {
			case HttpParams.AD_LIST:// 获取广告图
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null && map.size() != 0) {
					ArrayList<Advertise> adsList = (ArrayList<Advertise>) map
							.get("ad");
					if (adsList != null && adsList.size() != 0) {
						ArrayList<String> ads = new ArrayList<String>();
						for (int i = 0; i < adsList.size(); i++) {
							ads.add(adsList.get(i).getEaHdPic());
						}
						mCache.put(ADS_LIST_DATA, ads);
						initialAdsList(ads);
						loadingImg.setVisibility(View.GONE);
					}
				}
				break;
			case HttpParams.USER_LOGIN:// 登录
				Map<String, Object> mapLogin = (Map<String, Object>) object[2];
				if (mapLogin != null) {
					boolean flag = (Boolean) mapLogin.get("flag");
					if (flag) {// 登录成功
						setClientid(BaseApplication.patient
								.getUserId() + "");
						getRegregisterNew();
					}else{
						
						Toast.makeText(getActivity(), "login失败了--", Toast.LENGTH_SHORT).show();
					}
				}

				break;

			}

		} else {
			Logger.i("首页自动登录", getString(R.string.service_error));
		}
		// progressBar.setVisibility(View.GONE);// 隐藏进度条
	}

	/**
	 * 获取最新预约信息
	 */
	private void getRegregisterNew() {
		RegistratioImpl api = new RegistratioImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("patientId", ""+BaseApplication.patient.getId());
		api.getRegregisterNew(params, this);
	}
	/**
	 * 提交推送cid 和uid
	 * 
	 * @param userid
	 */
	private void setClientid(String userid) {
		UserImpl impl = new UserImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userid", userid);
		params.addBodyParameter("cid", SystemPreferences.getString(EZTConfig.KEY_CID));
		impl.setClientid(params, this);
	}
}

package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.NearHosAdapter;
import com.eztcn.user.eztcn.adapter.QuickAppointListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnLoadMoreListener;
import com.eztcn.user.eztcn.customView.PullToRefreshListView.OnRefreshListener;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.CacheUtils;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 快速预约
 * @describe 首页预约挂号快速进入
 * @author ezt
 * @created 2014年12月9日
 */
public class QuickAppointActivity extends FinalActivity implements
		OnClickListener, OnItemClickListener, IHttpResult, OnLoadMoreListener,
		OnRefreshListener {

	@ViewInject(R.id.quick_hos_layout)//, click = "onClick"
	private RelativeLayout btHos;// 选择医院

	@ViewInject(R.id.quick_dept_layout)//, click = "onClick"
	private RelativeLayout btDept;// 选择科室

	@ViewInject(R.id.quick_find_bt)//, click = "onClick"
	private Button btFind;// 快速找医按钮

	@ViewInject(R.id.home_bt_checkin)//, click = "onClick"
	private LinearLayout home_bt_checkin;// 预约登记

	@ViewInject(R.id.rim_hos_distance_img)//, click = "onClick"
	private ImageView imgDistance;// 离我最近

//	@ViewInject(R.id.quick_recommend_lt, itemClick = "onItemClick")
//	private PullToRefreshListView lv;// 推荐医生列表

	@ViewInject(R.id.textView2)
	private TextView tvText;

	@ViewInject(R.id.quick_hos_tv)
	private TextView tvHos;// 所选的医院

	@ViewInject(R.id.quick_dept_tv)
	private TextView tvDept;// 所选的科室

	@ViewInject(R.id.recommendLayout)
	private LinearLayout recommendLayout;// 推荐医生

	@ViewInject(R.id.topLayout)
	private LinearLayout topLayout;

	private String hosId = null;// 所选医院的id

	private String deptId = null;// 所选科室的id

	// private String deptTypeId = null;// 所选科室类型id

	private QuickAppointListAdapter adapter;

//	private ArrayList<Doctor> docList;

//	private int currentPage = 1;// 当前页数
//	private int pageSize = EZTConfig.PAGE_SIZE;// 每页条数

	private final String QUICK_APPOINT_DATA = "QuickAppointData";// 缓存key
	private CacheUtils mCache;

	/**
	 * 选择离我最近医院
	 */
	private Dialog nearHosChoice;

	private NearHosAdapter nearHosAdapter;

	private ListView ltNearHos;

	private TextView tvNearHosTitle, tvNearHosCancel;

	private int dialogWidth;// 弹出框宽

	private ArrayList<Hospital> nearHosList;// 离我最近的医院

	private boolean isClickNearHos = false;// 是否点击附近医院

	int y = 0;
	private boolean isWB = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_appoint);
		loadTitleBar(true, "预约挂号", null);
		ViewUtils.inject(QuickAppointActivity.this);
		mCache = CacheUtils.get(this);
//		adapter = new QuickAppointListAdapter(this, 1);
//		lv.setAdapter(adapter);
//		lv.setCanLoadMore(true);
//		lv.setCanRefresh(false);
//		lv.setAutoLoadMore(true);
//		lv.setMoveToFirstItemAfterRefresh(false);
//		lv.setDoRefreshOnUIChanged(false);
//		lv.setOnLoadListener(this);
//		// lv.setOnRefreshListener(this);

		// 选择离我最近医院
		dialogWidth = (int) (getWindowWidth() * 0.8);
		nearHosChoice = new Dialog(this, R.style.ChoiceDialog);
		View dialogView = LinearLayout.inflate(this, R.layout.dialog_choice,
				null);
		ltNearHos = (ListView) dialogView.findViewById(R.id.dialog_lt);
		ltNearHos.setDivider(getResources().getDrawable(
				android.R.color.transparent));
		tvNearHosTitle = (TextView) dialogView.findViewById(R.id.title);// 标题
		tvNearHosCancel = (TextView) dialogView.findViewById(R.id.cancel_tv);// 取消
		tvNearHosCancel.setOnClickListener(this);
		nearHosAdapter = new NearHosAdapter(mContext);
		ltNearHos.setAdapter(nearHosAdapter);
		ltNearHos.setOnItemClickListener(this);
		nearHosChoice.setCanceledOnTouchOutside(true);
		nearHosChoice.setContentView(dialogView);
//		if (BaseApplication.getInstance().isNetConnected) {
//			showProgressToast();
//			getRecommendDocData();
//		} else {// 无网络
//			ArrayList<Doctor> docList = (ArrayList<Doctor>) mCache
//					.getAsObject(QUICK_APPOINT_DATA);
//			adapter.setList(docList);
//			if (adapter.getList() != null && adapter.getList().size() != 0) {
//				lv.setVisibility(View.VISIBLE);
//				tvText.setVisibility(View.VISIBLE);
//			} else {
//				Toast.makeText(mContext, getString(R.string.network_hint),
//						Toast.LENGTH_SHORT).show();
//			}
//		}

//		lv.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					y = (int) event.getY();
//					break;
//				case MotionEvent.ACTION_MOVE:
//
//					break;
//				case MotionEvent.ACTION_UP:
//					if (y - event.getY() > 0) {
//						if (lv.getFirstVisiblePosition() > 0)
//							topLayout.setVisibility(View.GONE);
//						return false;
//					}
//					int d = lv.getFirstVisiblePosition();
//					if (lv.getFirstVisiblePosition() < 2) {
//						topLayout.setVisibility(View.VISIBLE);
//					}
//					break;
//				}
//				return false;
//			}
//		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		gainIntent();
		if (!isClickNearHos) {
			initHosDept();
		}
	}

	private void gainIntent() {
		Intent from_inent = getIntent();
		if (!isWB)
			if (null != from_inent
					&& from_inent.getIntExtra("isWomenBaby", 0) == 1) {
				isWB = true;
			} else
				isWB = false;

		if (!isWB) {
			loadTitleBar(true, "全国挂号", null);
		} else
			loadTitleBar(true, "妇幼挂号", null);
	}

	/**
	 * 初始化医院、科室信息
	 */
	public void initHosDept() {
		if (!isWB) {
			hosId = SystemPreferences.getString(EZTConfig.KEY_HOS_ID);
			tvHos.setText(SystemPreferences.getString(EZTConfig.KEY_HOS_NAME));
			deptId = SystemPreferences.getString(EZTConfig.KEY_DEPT_ID);
			tvDept.setText(SystemPreferences.getString(EZTConfig.KEY_STR_DEPT));
		} 
//		else {
//			hosId = SystemPreferences.getString(EZTConfig.KEY_WB_HOS_ID);
//			deptId = SystemPreferences.getString(EZTConfig.KEY_DEPT_WB_ID);
//			tvDept.setText(SystemPreferences
//					.getString(EZTConfig.KEY_STR_WB_DEPT));
//			tvHos.setText(SystemPreferences
//					.getString(EZTConfig.KEY_WB_HOS_NAME));
//		}
	}

	@Override
	public void onBackPressed() {
		hideProgressToast();
		finish();
		super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (BaseApplication.getInstance().isNetConnected) {
			switch (parent.getId()) {
			case R.id.quick_recommend_lt:
				if (BaseApplication.getInstance().isNetConnected) {
					position = position - 1;
					String docId = adapter.getList().get(position).getId();
					String deptId = adapter.getList().get(position)
							.getDocDeptId();
					String deptDocId = adapter.getList().get(position)
							.getDeptDocId();
					Intent intent = new Intent(mContext,
							DoctorIndexActivity.class)
							.putExtra("deptId", deptId)
							.putExtra("docId", docId)
							.putExtra("deptDocId", deptDocId);
					startActivity(intent);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.dialog_lt:// 离我最近医院
				Hospital hos = nearHosAdapter.getList().get(position);
				tvHos.setText(hos.gethName());
				hosId = hos.getId() + "";
				nearHosChoice.dismiss();
				tvDept.setText("选择科室");
				deptId = "";
				isClickNearHos = true;
				break;
			}

		} else {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 获取推荐医生列表
	 */
//	private void getRecommendDocData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("orderLevel", "0");// 是否按职务排行
//		params.put("orderRate", "2");// 是否按预约率排序
//		params.put("orderYnRemain", "2");// 是否有号
//		params.put("orderYnEvaluation", "1");// 按评价高低(
//												// 需求要求首次默认为评价最高，之后按历史就诊相关医生排序)
//		params.put("dcOrderParm", "6");// 1热门2新闻页3名医堂4最受欢迎5妇婴名医6本院推荐
//		params.put("rowsPerPage", pageSize + "");
//		params.put("page", currentPage + "");
//		new HospitalImpl().getRankingDocList(params, this);
//	}

	@OnClick(R.id.rim_hos_distance_img)
	public void rim_hos_distance_imgClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
		

		// 离我最近
				if (BaseApplication.getInstance().lat == 0.0
						&& BaseApplication.getInstance().lon == 0.0) {
					Toast.makeText(mContext, "暂无定位到您的位置！", Toast.LENGTH_SHORT)
							.show();
				} else {
					initialNearHosData();
				}


			
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}
	@OnClick(R.id.quick_hos_layout)
	public void quick_hos_layoutClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
			

		// 选择医院

				Intent intent = new Intent(QuickAppointActivity.this,
						ChoiceHosActivity.class);
				if (isWB)
					intent.putExtra("isWomenBaby", 1);
				startActivity(intent);

				isClickNearHos = false;

			
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}
	@OnClick(R.id.quick_dept_layout)
	public void quick_dept_layoutClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
		

			// 选择科室
				if (!TextUtils.isEmpty(hosId) || "-1".equals(hosId)) {
					Intent intent_ = new Intent(mContext,
							ChoiceDeptByHosActivity.class);
					if (isWB) {
						intent_.putExtra("isWomenBaby", 1);
					}
					startActivityForResult(intent_.putExtra("hosId", hosId)
							.putExtra("hosName", tvHos.getText().toString())
							.putExtra("isNearHos", isClickNearHos), 2);

				} else {
					Toast.makeText(QuickAppointActivity.this, "请选择医生所在医院",
							Toast.LENGTH_SHORT).show();
					return;
				}


			
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}
	@OnClick(R.id.quick_find_bt)
	public void quick_find_btClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
		

		// 快速找医按钮
				if (TextUtils.isEmpty(tvHos.getText().toString())) {
					Toast.makeText(QuickAppointActivity.this, "请选择医生所在的医院",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (TextUtils.isEmpty(deptId)) {
					Toast.makeText(QuickAppointActivity.this, "请选择医生所在的科室",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					if (isClickNearHos) {// 附近医院筛选进入

						Intent intent_to = new Intent(mContext,
								DoctorListActivity.class);
						if (isWB) {
							intent_to.putExtra("isWomenBaby", 1);
						}
						startActivity(intent_to
								.putExtra("type", 1)
								.putExtra("deptName",
										tvDept.getText().toString())
								.putExtra("hosId", hosId)
								.putExtra("deptId", deptId)
								.putExtra("isAllSearch", true)
								.putExtra("hosName", tvHos.getText().toString()));

						// isClickNearHos = false;

					} else {

						Intent intent_to = new Intent(
								QuickAppointActivity.this,
								DoctorListActivity.class);
						if (isWB) {
							intent_to.putExtra("isWomenBaby", 1);
						}
						startActivity(intent_to
								.putExtra("type", 1)
								.putExtra("hosId", hosId)
								.putExtra("deptId", deptId)
								// .putExtra("deptTypeId", deptTypeId)
								.putExtra("hosName", tvHos.getText().toString())
								.putExtra("deptName",
										tvDept.getText().toString()));// type为1为医院找，2为科室找
					}
				}

			
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}
	@OnClick(R.id.home_bt_checkin)
	public void home_bt_checkinClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
			
			// 预约登记
				if (BaseApplication.patient == null) {
					HintToLogin(Constant.LOGIN_COMPLETE);
					return;
				}
				startActivityForResult(new Intent(this,
						AppointCheckInActivity.class), 2);
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onClick(View v) {
		if (BaseApplication.getInstance().isNetConnected) {
			switch (v.getId()) {
			case R.id.cancel_tv:// 离我最近医院取消按钮
				nearHosChoice.dismiss();
				break;
			}
		} else {// 无网络
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.NEAR_HOS_LIST:// 获取离我最近的医院列表
			if (isSuc) {
				nearHosList = (ArrayList<Hospital>) object[2];
				choiceNearHos(nearHosList, "离我最近医院");
			} else {
			}
			break;
		}

	}

	// 加载更多
	@Override
	public void onLoadMore() {
	}

	/**
	 * 获取离我最近的医生
	 */
	private void initialNearHosData() {

		RequestParams params=new RequestParams();
		HospitalImpl api = new HospitalImpl();
		params.addBodyParameter("lat", ""+BaseApplication.getInstance().lat);
		params.addBodyParameter("lng",""+ BaseApplication.getInstance().lon);
		params.addBodyParameter("rowsPerPage",""+ 20);
		params.addBodyParameter("page", ""+1);
		
		api.getNearHosList(params, this);
		showProgressToast();
	}

	/**
	 * 弹出框选择离我最近的医院
	 */
	private void choiceNearHos(ArrayList<Hospital> data, String title) {
		tvNearHosTitle.setText(title);
		nearHosAdapter.setList(data);
		// 获取listview的高度
		int totalHeight = 0;
		for (int i = 0, len = nearHosAdapter.getCount(); i < len; i++) {
			View listItem = nearHosAdapter.getView(i, null, ltNearHos);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int item_height = listItem.getMeasuredHeight()
					+ ltNearHos.getDividerHeight();
			totalHeight += item_height; // 统计所有子项的总高度
		}
		Window dialogWindow = nearHosChoice.getWindow();

		totalHeight = totalHeight
				+ ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
						mContext, R.dimen.dialog_title_bar_size));
		dialogWindow
				.setLayout(
						dialogWidth,
						totalHeight > getWindowHeight() / 4 * 3 ? getWindowHeight() / 4 * 3
								: totalHeight);
		if (!nearHosChoice.isShowing()) {
			nearHosChoice.show();
		} else {
			nearHosChoice.dismiss();
		}
	}

	// 刷新
	@Override
	public void onRefresh() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 2 && resultCode == 22) {
			if (intent != null) {
				deptId = intent.getStringExtra("deptId");
				tvDept.setText(intent.getStringExtra("deptName"));
			}
		}

	}
}

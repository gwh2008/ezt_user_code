package com.eztcn.user.eztcn.activity.mine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.AlreadyRegDetailActivity;
import com.eztcn.user.eztcn.activity.home.EvaluateActivity;
import com.eztcn.user.eztcn.activity.home.PhoneRecordDetailActivity;
import com.eztcn.user.eztcn.activity.home.WriteLetterActivity;
import com.eztcn.user.eztcn.activity.home.drug.MyOrderDrugDetailsActivity;
import com.eztcn.user.eztcn.adapter.AlreadyRegisterAdapter;
import com.eztcn.user.eztcn.adapter.OrderBigInsAdapter;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter;
import com.eztcn.user.eztcn.adapter.Order_RecordAdapter.onRecordClickListener;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.adapter.Telphone_RecordAdapter;
import com.eztcn.user.eztcn.adapter.Telphone_RecordAdapter.onPhoneRecordListener;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.LInsItem;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.bean.OrderRegisterRecord;
import com.eztcn.user.eztcn.bean.PhoneRecordBean;
import com.eztcn.user.eztcn.bean.Record_Info;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.BackValidateDialog;
import com.eztcn.user.eztcn.customView.BackValidateDialog.CodeSure;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.InsReportImpl;
import com.eztcn.user.eztcn.impl.RegistratioImpl;
import com.eztcn.user.eztcn.impl.TelDocImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.activity.AppointmentMainActivity;
import com.eztcn.user.hall.activity.MyRegistrationDetailsActivity;
import com.eztcn.user.hall.common.Constant;

/**
 * @title 我的记录
 * @describe 预约挂号(已登记、已预约、已就诊、爽约、退号)，电话医生(已预约、已通话、已关闭)，图文问诊(已回复、未回复)
 * @author ezt
 * @created 2014年12月10日
 */
public class MyRecordActivity extends FinalActivity implements
		OnItemClickListener, IHttpResult, onRecordClickListener, CodeSure {
	private String temp_regId, temp_pfId;
	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.dataList)
	private ListView dataList;
	@ViewInject(R.id.orderRecord_layout)//, click = "onClick"
	private FrameLayout orderRecord_layout;
	@ViewInject(R.id.orderRecord)
	private TextView orderRecord;// 预约挂号
	@ViewInject(R.id.seeDoctorRecord_layout)//, click = "onClick"
	private FrameLayout seeDoctorRecord_layout;
	@ViewInject(R.id.seeDoctorRecord)
	private TextView seeDoctorRecord;// 远程看病历
	@ViewInject(R.id.phoneDoctorRecord_layout)//, click = "onClick"
	private FrameLayout phoneDoctorRecord_layout;
	@ViewInject(R.id.phoneDoctorRecord)
	private TextView phoneDoctorRecord;// 电话医生
	@ViewInject(R.id.textAskRecord_layout)//, click = "onClick"
	private FrameLayout textAskRecord_layout;

	@ViewInject(R.id.report_layout)//, click = "onClick"
	private FrameLayout report_layout;// 检验报告

	@ViewInject(R.id.textAskRecord)
	private TextView textAskRecord;// 免费提问
	@ViewInject(R.id.textReport)
	private TextView textReport;// 检验报告
	@ViewInject(R.id.all_layout)
	private RelativeLayout allLayout;

	private PopupWindow pWindow;
	private PopupWindowAdapter popAdapter;
	private View popView;
	private ListView itemList;
	private List<Record_Info> list;// 预约挂号
	private List<PhoneRecordBean> telList;// 电话医生
	private Order_RecordAdapter adapter;
	private Telphone_RecordAdapter telAdapter;
	private List<OrderRegisterRecord> regList;

	private AlreadyRegisterAdapter regAdapter;
	/**
	 * 大检验项目列表 2015 - 11 -25
	 */
	private List<LInsItem> bInsItemList;

	private static final int REGISTER_ORDER = 3;// 预约记录
	private static final int REGISTER_TREAT = 6;// 已就诊记录
	private static final int REGISTER_FAILS = 7;// 爽约记录
	private static final int REGISTER_BACKNUM = 4;// 退号记录

	/**
	 * 0:预约挂号，1：电话医生，2：图文问诊 4：检验报告
	 */
	private int clickType = 0;
	private final int TYPEREPORT = 20;
	/**
	 * 0、已预约 1、已登记 2、已就诊 3、爽约 4、已退号 5、电话医生已预约 6、已通话 7、未回复 8、已回复9、电话医生已关闭状态
	 * 20、检验报告
	 */
	private int itemType = 0;

	/**
	 * 进入方式：2、电话医生预约成功 4、预约挂号成功 进入方式：2、电话医生预约成功 3、预约登记成功 4、预约挂号成功
	 */
	private int enterType;
	private BackValidateDialog validateDialog;
	private OrderBigInsAdapter orderBInsAdapter;
	private Context context=MyRecordActivity.this;
    private TextView top_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdadiagnose);
		ViewUtils.inject(MyRecordActivity.this);
        top_right=loadTitleBar(true,"我的预约","添加预约");
        top_right.setTextColor(getResources().getColor(R.color.border_line));
        top_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,AppointmentMainActivity.class)
                        .putExtra("cityData", Constant.cityListDataResponse));
            }
        });
		if (BaseApplication.getInstance().isNetConnected) {
			allLayout.setVisibility(View.VISIBLE);
			title.setText("我的预约");
			initMenuView();
			init();
		} else {
			allLayout.setVisibility(View.GONE);
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
		}

	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			enterType = bundle.getInt("type");
		}
		switch (enterType) {
		case 0:
		case 4:
			setOrderRecord(REGISTER_ORDER);
			break;
		case 2:
			title.setText("预约单");
			clickType = 1;
			itemType = 5;
			updateBottomViewStatus();
			getCallRecord(0);
			break;
		case 3:
			title.setText("已登记");
			clickType = 0;
			itemType = 1;
			updateBottomViewStatus();
			getORRecord();
			break;
		}
	}

	/**
	 * 点击下面的选项卡 清楚列表数据
	 */
	private void clearListData() {
		if (null != list) {
			list.clear();
			adapter.notifyDataSetChanged();
		}
		if (null != telList) {
			telList.clear();
			telAdapter.notifyDataSetChanged();
		}
		if (null != regList) {
			regList.clear();
			regAdapter.notifyDataSetChanged();
		}
		if (null != bInsItemList) {
			bInsItemList.clear();
			orderBInsAdapter.notifyDataSetChanged();
		}
	}
	
	
	@OnClick(R.id.report_layout)// 2015 11 23 检验报告
	private void report_layoutClick(View v){
		clearListData();
		title.setText("检验报告");
		clickType = 4;
		itemType = TYPEREPORT;
		InsReportImpl insReportImpl = new InsReportImpl();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("social_no", "120102195305061727");
		insReportImpl.getPatientMessage(params, this);
		showProgressToast();
		updateBottomViewStatus();
	}
	
	@OnClick(R.id.orderRecord_layout)// 预约挂号
	private void orderRecord_layoutClick(View v){
		clearListData();
		title.setText("预约挂号");
		String[] menuArry = getResources().getStringArray(
				R.array.user_orderRecord);
		popAdapter.setList(menuArry);
		pWindow.showAtLocation(v, Gravity.LEFT | Gravity.BOTTOM, 0,
				v.getHeight());
		clickType = 0;
		updateBottomViewStatus();
	}
	
	@OnClick(R.id.phoneDoctorRecord_layout)// 电话医生
	private void phoneDoctorRecord_layoutClick(View v){
		clearListData();
		title.setText("电话医生");
		String[] menuArry2 = getResources().getStringArray(
				R.array.user_phoneRecord);
		popAdapter.setList(menuArry2);
		pWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.LEFT,
				v.getLeft(), v.getHeight());
		clickType = 1;
		updateBottomViewStatus();
	}
	
	@OnClick(R.id.seeDoctorRecord_layout)// 远程看病历
	private void seeDoctorRecord_layoutClick(View v){
		clearListData();
		title.setText("远程看病历");
		String[] menuArry3 = getResources().getStringArray(
				R.array.user_textAskRecord);
		popAdapter.setList(menuArry3);
		pWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.LEFT,
				v.getLeft(), v.getHeight());
		clickType = 2;
		updateBottomViewStatus();
	}
	
	@OnClick(R.id.textAskRecord_layout)// 免费提问
	private void textAskRecord_layoutClick(View v){
		clearListData();
		title.setText("免费提问");
		String[] menuArry4 = getResources().getStringArray(
				R.array.user_textAskRecord);
		popAdapter.setList(menuArry4);
		pWindow.showAtLocation(v, Gravity.BOTTOM, v.getLeft(),
				v.getHeight());
		clickType = 3;
		updateBottomViewStatus();
	}
	/**
	 * 选中选项卡处理
	 * @param view
	 * @param pos
	 */
	private void updateBottomViewStatus() {
		resetIcon();
		Drawable drawable = null;
		switch (clickType) {
		case 0:
			drawable = getResources().getDrawable(R.drawable.rorder_focus_icon);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			orderRecord.setCompoundDrawables(null, drawable, null, null);
			orderRecord.setTextColor(getResources()
					.getColor(R.color.main_color));
			break;
		case 1:
			drawable = getResources().getDrawable(
					R.drawable.rdocphone_focus_icon);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			phoneDoctorRecord.setCompoundDrawables(null, drawable, null, null);
			phoneDoctorRecord.setTextColor(getResources().getColor(
					R.color.main_color));
			break;
		case 2:
			drawable = getResources().getDrawable(
					R.drawable.rseeillr_focus_icon);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			seeDoctorRecord.setCompoundDrawables(null, drawable, null, null);
			seeDoctorRecord.setTextColor(getResources().getColor(
					R.color.main_color));
			break;
		case 3:
			drawable = getResources().getDrawable(
					R.drawable.raskdoctor_focus_icon);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			textAskRecord.setCompoundDrawables(null, drawable, null, null);
			textAskRecord.setTextColor(getResources().getColor(
					R.color.main_color));
			break;

		case 4:
			drawable = getResources().getDrawable(R.drawable.report_c);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			textReport.setCompoundDrawables(null, drawable, null, null);
			textReport
					.setTextColor(getResources().getColor(R.color.main_color));
			break;
		}
	}

	/**
	 * 把tabBar图片的选中状态复原
	 * */
	private void resetIcon() {
		// 0
		Drawable drawable1 = getResources().getDrawable(
				R.drawable.rorder_normal_icon);
		drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
				drawable1.getMinimumHeight());
		orderRecord.setCompoundDrawables(null, drawable1, null, null);
		orderRecord.setTextColor(getResources().getColor(
				R.color.tab_footer_textcolor));
		// 1
		Drawable drawable2 = getResources().getDrawable(
				R.drawable.rdocphone_normal_icon);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
				drawable2.getMinimumHeight());
		phoneDoctorRecord.setCompoundDrawables(null, drawable2, null, null);
		phoneDoctorRecord.setTextColor(getResources().getColor(
				R.color.tab_footer_textcolor));
		// 2
		Drawable drawable3 = getResources().getDrawable(
				R.drawable.rseeillr_normal_icon);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(),
				drawable3.getMinimumHeight());
		seeDoctorRecord.setCompoundDrawables(null, drawable3, null, null);
		seeDoctorRecord.setTextColor(getResources().getColor(
				R.color.tab_footer_textcolor));

		// 3
		Drawable drawable4 = getResources().getDrawable(
				R.drawable.raskdoctor_normal_icon);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(),
				drawable4.getMinimumHeight());
		textAskRecord.setCompoundDrawables(null, drawable4, null, null);
		textAskRecord.setTextColor(getResources().getColor(
				R.color.tab_footer_textcolor));

		// 4

		Drawable drawable5 = getResources().getDrawable(R.drawable.report_un);
		drawable5.setBounds(0, 0, drawable5.getMinimumWidth(),
				drawable5.getMinimumHeight());
		textReport.setCompoundDrawables(null, drawable5, null, null);
		textReport.setTextColor(getResources().getColor(
				R.color.tab_footer_textcolor));
	}

	/**
	 * 初始化popupwindow
	 */
	public void configPopupWindow() {
		pWindow = new PopupWindow(popView, orderRecord_layout.getWidth(),
				LayoutParams.WRAP_CONTENT, false);
		pWindow.setBackgroundDrawable(new BitmapDrawable());
		pWindow.setOutsideTouchable(true);
		pWindow.setFocusable(true);
		popAdapter = new PopupWindowAdapter(MyRecordActivity.this);
		itemList.setAdapter(popAdapter);
		pWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				updateBottomViewStatus();
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			configPopupWindow();
		}
	};

	/**
	 * 显示预约挂号窗口
	 */
	public void initMenuView() {
		popView = View.inflate(this, R.layout.popwindow_choice, null);
		itemList = (ListView) popView.findViewById(R.id.pop_list);
		itemList.setOnItemClickListener(this);
		handler.sendEmptyMessageDelayed(1, 200);
	}

	/**
	 * 初始化检验报告适配器数值
	 */
	public void initListLInsRecord() {
		if (null == bInsItemList) {
			bInsItemList = new ArrayList<LInsItem>();
		}
		orderBInsAdapter = new OrderBigInsAdapter(this);
		dataList.setAdapter(orderBInsAdapter);
		orderBInsAdapter.setList(bInsItemList);
		if (bInsItemList.size() == 0) {
			Toast.makeText(this, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		dataList.setOnItemClickListener(this);
	}

	/**
	 * 初始化预约挂号adapter
	 */
	public void initListRecord() {
		if (list == null) {
			list = new ArrayList<Record_Info>();
		}
		adapter = new Order_RecordAdapter(this, itemType);
		dataList.setAdapter(adapter);
		adapter.setList(list);
		adapter.setOnRecordClickListener(this);
		if (list.size() == 0) {
			if (itemType == 0) {// 没有已预约数据时相应的预约提醒消息清除
				WhereBuilder whereBuilder=WhereBuilder.b("typeId", "=", "register");
				EztDb.getInstance(mContext).delDataWhere(new MsgType(),
						whereBuilder);
			}
			Toast.makeText(this, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		dataList.setOnItemClickListener(this);
	}

	/**
	 * 初始化电话医生记录adapter
	 */
	public void initTelPhoneRecord() {

		if (telList == null) {
			telList = new ArrayList<PhoneRecordBean>();
		}
		telAdapter = new Telphone_RecordAdapter(this, itemType);
		dataList.setAdapter(telAdapter);
		telAdapter.setList(telList);
		telAdapter.setOnPhoneRecordListener(new onPhoneRecordListener() {

			@Override
			public void onRecordClick(View v, int position, int type) {
				PhoneRecordBean record = telList.get(position);
				if (record == null) {
					return;
				}
				if (type == 5) {
					cancelPhoneOrder(record.getCallRegisterId());
				} else {
					Intent intent = new Intent(MyRecordActivity.this,
							EvaluateActivity.class);
					intent.putExtra("enterType", 2);
					intent.putExtra("record", record);
					startActivity(intent);
				}
			}
		});
		if (telList.size() == 0) {
			Toast.makeText(this, "暂无记录", Toast.LENGTH_SHORT).show();
		}
		dataList.setOnItemClickListener(this);
	}

	/**
	 * 获取预约记录(3、预约记录 6、已就诊记录)
	 */
	public void setOrderRecord(int status) {
		if (BaseApplication.patient == null) {
			return;
		}
		RegistratioImpl impl = new RegistratioImpl();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
//		params.addBodyParameter("rstatus", status + "");
		params.addBodyParameter("rstatus","");
		params.addBodyParameter("rowsPerPage", "100");
		params.addBodyParameter("page", "1");
		impl.getRegRecord(params, this);
		showProgressToast();
	}

	/**
	 * 获取通话记录
	 * 
	 * @param status
	 *            -1取消通话0待通话1通话中2通话成功3未接通(含未接占线)4扣费成功
	 */
	public void getCallRecord(int status) {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("crStatus", status + "");
		params.addBodyParameter("rowsPerPage", "20");
		params.addBodyParameter("page", "1");
		new TelDocImpl().getTelDocRecord(params, this);
		showProgressToast();
	}

	/**
	 * 取消电话预约
	 * @param registerId
	 *            电话医生预约ID
	 */
	public void cancelPhoneOrder(final int registerId) {

		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("温馨提示");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setMessage("确定取消电话医生预约？");
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				RequestParams params=new RequestParams();
				params.addBodyParameter("registerId", registerId + "");
				new TelDocImpl()
						.cancelPhoneOrder(params, MyRecordActivity.this);
				showProgressToast();
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		ab.create().show();
	}

	/**
	 * 退号
	 * @param pfId
	 * @param regId
	 */
	private int regId;

	public void backNums(final String pfId, final String regId) {
		this.regId = Integer.parseInt(regId);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("取消订单");
		ab.setIcon(android.R.drawable.ic_delete);
		ab.setPositiveButton("退号", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp_regId = regId;
				temp_pfId = pfId;
				//2015-12-30 退号获取图片验证码
				validateDialog = new BackValidateDialog(MyRecordActivity.this,MyRecordActivity.this.regId,BaseApplication.patient.getUserId());// 2015-12-30退号获取验证码
				validateDialog.setSure(MyRecordActivity.this);
				validateDialog.show();
				
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				temp_regId = "";
				temp_pfId = "";
			}
		});
		ab.create().show();
	}

	/**
	 * 初始化已登记记录
	 */
	public void initORRecord() {
		if (regList == null) {
			regList = new ArrayList<OrderRegisterRecord>();
		}
		// 2015 11 25 提取全局变量 刘刚
		regAdapter = new AlreadyRegisterAdapter(mContext);
		dataList.setAdapter(regAdapter);
		regAdapter.setList(regList);
		dataList.setOnItemClickListener(this);
		if (regList.size() == 0) {
			Toast.makeText(this, "暂无记录", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 获取登记记录
	 */
	public void getORRecord() {
		if (BaseApplication.patient != null) {
			RequestParams params=new RequestParams();
			params.addBodyParameter("userId",""+ BaseApplication.patient.getUserId());
			new RegistratioImpl().orderRegisterList(params, this);
			showProgressToast();
		}
	}

	@Override
	protected void onActivityResult(int request, int result, Intent arg2) {
		super.onActivityResult(request, result, arg2);
		if (request == 1 && result == 1) {// 退号返回
			setOrderRecord(REGISTER_ORDER);
		} else if (result == 22) {
			itemType = 1;
			clickType = 0;
			getORRecord();
			updateBottomViewStatus();
		}else if(request==com.eztcn.user.hall.utils.Constant.REFRESH_MY_REGISTRATION_NUM){

			setOrderRecord(0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view.getParent() == dataList) {// 记录列表进入
			Intent intent = new Intent();
			if (itemType == 5 || itemType == 6 || itemType == 9) {
				intent.setClass(MyRecordActivity.this,
						PhoneRecordDetailActivity.class);
				intent.putExtra("record", telList.get(position));
				startActivityForResult(intent, 2);
			} else if (itemType == 1) {
				intent.setClass(MyRecordActivity.this,
						AlreadyRegDetailActivity.class);
				intent.putExtra("id", regList.get(position).getId());
				startActivityForResult(intent, 22);
			} else if (itemType == TYPEREPORT) {
				// 检验报告 跳转到检验报告小检验项目详情
				intent.setClass(mContext, InsDetailActivity.class);
				LInsItem lInsItem = bInsItemList.get(position);
				intent.putExtra("bItemName", lInsItem.getbItemName());
				// intent.putExtra("bItemId", lInsItem.getbItemId());
				// TODO
				intent.putExtra("bItemId", "849330");
				startActivity(intent);
			} else {
					if(list.get(position)!=null){
						Record_Info record_info=list.get(position);
						if(record_info!=null){

						if ((record_info.getDoctorId() == 6364 && record_info
								.getDeptId() == 1469)
								|| (record_info.getDoctorId() == 8345 && record_info
										.getDeptId() == 6089)) {
							intent.setClass(MyRecordActivity.this,
									MyOrderDrugDetailsActivity.class);
							intent.putExtra("enterType", itemType);
							intent.putExtra("data", list.get(position));
							startActivityForResult(intent,1);

						}else{
							intent.setClass(MyRecordActivity.this,
									MyRegistrationDetailsActivity.class);
							intent.putExtra("enterType", itemType);
							intent.putExtra("data", list.get(position));
							intent.putExtra("timeType",1);
							startActivityForResult(intent, 1);
						}
						}
					}
			}
			return;
		}
		switch (clickType) {// 菜单栏点击
		case 0:
			if (position == 0) {
				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 0;
					title.setText("预约单");
					setOrderRecord(3);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else if (position == 1) {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 1;
					title.setText("已登记");
					getORRecord();
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else if (position == 2) {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 2;
					title.setText("已就诊");
					setOrderRecord(REGISTER_TREAT);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else if (position == 3) {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 3;
					title.setText("爽约");
					setOrderRecord(REGISTER_FAILS);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 4;
					title.setText("已退号");
					setOrderRecord(REGISTER_BACKNUM);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case 1:
			if (position == 0) {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 5;
					title.setText("已预约");
					getCallRecord(0);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else if (position == 1) {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 6;
					title.setText("已通话");
					getCallRecord(4);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			} else {

				if (BaseApplication.getInstance().isNetConnected) {
					itemType = 9;
					title.setText("已关闭");
					getCallRecord(9);
				} else {
					Toast.makeText(mContext, getString(R.string.network_hint),
							Toast.LENGTH_SHORT).show();
				}

			}
			break;
		case 2:
			if (position == 0) {
				itemType = 7;
				title.setText("未回复");
			} else {
				itemType = 8;
				title.setText("已回复");
			}
			list = null;
			initListRecord();
			break;
		case 3:
			if (position == 0) {
				itemType = 10;
				title.setText("未回复");
			} else {
				itemType = 11;
				title.setText("已回复");
			}
			list = null;
			initListRecord();
			break;
		default:

			break;
		}
		if (pWindow != null) {
			pWindow.dismiss();
		}
	}

	/**
	 * item_button点击事件（1.退号 2.评价 3.写感谢信）
	 */
	@Override
	public void onRecordClick(View v, int position, int type) {

		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (list == null) {
			return;
		}
		Record_Info record = list.get(position);
		if (record == null) {
			return;
		}
		Intent intent = new Intent();
		switch (type) {
		case 1:// 退号
			backNums(record.getPlatformId() + "", record.getId() + "");
			break;
		case 2:
			intent.setClass(this, EvaluateActivity.class);
			intent.putExtra("record", record);
			startActivity(intent);
			break;
		case 3:
			intent.setClass(this, WriteLetterActivity.class);
			intent.putExtra("thanksType", "预约挂号");
			intent.putExtra("record", record);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onSureClick(String valideCode) {
		if (valideCode.isEmpty()) {
			Toast.makeText(MyRecordActivity.this, "请输入验证码", Toast.LENGTH_SHORT)
					.show();
		} else {
			validateDialog.dismiss();
			RegistratioImpl impl = new RegistratioImpl();
//			HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params=new RequestParams();
			params.addBodyParameter("regId", temp_regId);
			params.addBodyParameter("pfId", temp_pfId);
			params.addBodyParameter("mobile", BaseApplication.patient.getEpMobile());
			params.addBodyParameter("code", valideCode);
			params.addBodyParameter("userId", ""+BaseApplication.patient.getUserId());// 2015-12-30
			// 退号时候要求加
			// 用户id
			impl.backNumber(params, MyRecordActivity.this);
			showProgressToast();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
			Toast.makeText(mContext, obj[3] + "", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (!flag) {
			if(taskID==HttpParams.BACKNUMBER){
				//退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME+"_"+temp_regId);
			}
			Toast.makeText(mContext, map.get("msg") + "".toString(),
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (taskID) {
		case HttpParams.GET_REG_RECORD:
			list = (List<Record_Info>) map.get("list");

			if(list!=null&&list.size()!=0){
				for (int i = 1; i <list.size(); i++) {
					Record_Info recordInfo=list.get(i);
					if((recordInfo.getDoctorId() == 6364 && recordInfo
							.getDeptId() == 1469)
							|| (recordInfo.getDoctorId() == 8345 && recordInfo
							.getDeptId() == 6089)){
						list.remove(i);
					}
				}
			}
			break;
		case HttpParams.GET_TEL_DOC_RECORD:
			telList = (List<PhoneRecordBean>) map.get("list");
			break;
		case HttpParams.ORRecord:// 获取预约登记列表
			regList = (List<OrderRegisterRecord>) map.get("list");
			break;
		case HttpParams.BACKNUMBER:
			String msg;
			if (flag) {
				msg = "退号成功";
				//退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME+"_"+temp_regId);
//				EztDb.getInstance(mContext).delDataWhere(new MessageAll(),
//						"msgId = " + this.regId); // 退号删除相应的预约提醒消息
				WhereBuilder b=WhereBuilder.b("msgId","=", regId);
				EztDb.getInstance(mContext).delDataWhere(new MessageAll(),b); // 退号删除相应的预约提醒消息
			} else {
				//退号后移除短信验证时间点信息2015-12-31
				SystemPreferences.remove(EZTConfig.KEY_BACKNUM_TIME+"_"+temp_regId);
				msg = map.get("msg").toString();
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();

			setOrderRecord(REGISTER_ORDER);
			return;
		case HttpParams.CANCEL_PHONEORDER:// 取消电话医生预约
			boolean cancelFlag = (Boolean) map.get("flag");
			if (cancelFlag) {
				msg = "取消成功";
				getCallRecord(0);
			} else {
				msg = map.get("msg").toString();
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
			return;

		case HttpParams.GET_PATIENT_MSG_LIST: {// 检验报告列表
			if (map.containsKey("data"))
				bInsItemList = (List<LInsItem>) map.get("data");
			else {
				Toast.makeText(getApplicationContext(), "服务器繁忙，请重试！",
						Toast.LENGTH_SHORT).show();
			}
		}
			break;

		}
		switch (itemType) {
		case 0:
		case 2:
		case 3:
		case 4:
			initListRecord();
			return;
		case 1:// 预约登记
			initORRecord();
			break;
		case 5:// 电话医生-已预约
		case 6:// 电话医生-已通话
		case 9:// 电话医生-已关闭
			initTelPhoneRecord();
			break;
		case 7:

			break;
		case 8:

			break;
		case TYPEREPORT: {
			// 检验报告 塞入数值
			initListLInsRecord();
		}
			break;
		}
	}
}

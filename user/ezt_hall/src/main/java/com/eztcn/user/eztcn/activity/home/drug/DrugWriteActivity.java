/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.drug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.OrderRegWaitActivity;
import com.eztcn.user.eztcn.activity.mine.AutonymAuthActivity;
import com.eztcn.user.eztcn.activity.mine.ModifyPhoneActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.customView.ValidateDialog;
import com.eztcn.user.eztcn.customView.ValidateDialog.CodeSure;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.CancleBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.SureBtnListener;
import com.eztcn.user.eztcn.customView.wheel.WheelDialog.TextAdapter;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelChangedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelClickedListener;
import com.eztcn.user.eztcn.customView.wheel.view.OnWheelScrollListener;
import com.eztcn.user.eztcn.customView.wheel.view.WheelView;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @author Liu Gang
 * 
 *         2016年3月29日 下午3:01:53
 * 
 */
public class DrugWriteActivity extends FinalActivity implements IHttpResult,
		OnClickListener, CodeSure, SureBtnListener, CancleBtnListener,
		OnWheelClickedListener, OnWheelScrollListener, OnWheelChangedListener {

	@ViewInject(R.id.orderPersonLayout)
	private View orderPersonLayout;
	@ViewInject(R.id.orderTimeLayout)
	private View orderTimeLayout;
	@ViewInject(R.id.costTypeLayout)
	private View costTypeLayout;
	@ViewInject(R.id.costTypeTv)
	private TextView costTypeTv;
	@ViewInject(R.id.orderTimeTv)
	private TextView orderTimeTv;
	@ViewInject(R.id.orderPersonTv)
	private TextView orderPersonTv;
	private List<String> personList;
	private View viewPerson, viewTime;
	private PopupWindow pWindow;
	// oDate, oTime,
	private ScrollerNumberPicker personView;
	private TextView affirmDate, add;

	private String regMark = "";// 参数标识
	private int isFirst = 1;
	private String pfId = ""; // 平台id
	private String sourcePfId = "355";// 平台来源id
	private String poolId = "";// 号池id
	private int payWay = 0;// 缴费方式 0：自费 1：普通医保2：门特医保3：门大医保
	private Builder builder;
	private String patientId ="";
	private ArrayList<FamilyMember> familyList;
	private String medNo = "";
	private String patientMobileStr;
	private String idCard;
	private String hospitalId;
	private String cartoonNumStr;
	private ValidateDialog valideDialog;

	private String orderDate;// 所选日期
	private String orderTime;// 所选时间
	private String person;// 所选就诊人

	// ///////////////号池
	private int pos;// 选中的日期下标
	private int tPos;// 选中的时间下标
	private String poolDate;// 选中的日期
	private List<String> timeList;
	private ArrayList<Pool> poolist;//
	private ArrayList<String> dateList;
	private int tempDateP = 0;// 临时存储日期位置2016-1-22 时间
	private int tempTimeP = 0;// 临时存储时间位置2016-1-22 时间
	private List<List<String>> timeLists;// 日期对应的时间段集合
	private Hospital hospital;
	private int type;
	private String docDeptStr;
	@ViewInject(R.id.deptTv)
	private TextView deptTv;
	@ViewInject(R.id.hosNameTv)
	private TextView hosNameTv;

	private WheelDialog wheelDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drugwrite);
		ViewUtils.inject(DrugWriteActivity.this);
		loadTitleBar(true, "预约药品", null);
		if(BaseApplication.patient!=null){
			patientId=BaseApplication.patient.getId()+"";// 患者id
		}
		hospital = (Hospital) getIntent().getExtras().get("hospital");
		poolist = (ArrayList<Pool>) getIntent().getExtras().get("poolList");
		docDeptStr = getIntent().getExtras().getString("deptStr");
		hosNameTv.setText(hospital.gethName());
		deptTv.setText(docDeptStr);
		initialDialogView();
		initialData();
		wheelDialog = new WheelDialog(DrugWriteActivity.this);
	}

	/**
	 * 初始化人数据
	 */
	private void initialData() {
		dealWithPoolData();
		showProgressToast();
		getMembers();
	}

	/**
	 * 预约时间选择器
	 */
	// private void selectOrderTime() {
	// if (null == dateList) {
	// return;
	// }
	// // 日期
	// initialTimeData(oDate, dateList, pos);
	// // 修改开始的记录位置
	// tempDateP = pos;
	// tempTimeP = tPos;
	//
	// // 时间
	// timeList = timeLists.get(pos);
	// initialTimeData(oTime, timeList, tPos);// 2016-1-22时间
	// configPopupWindow(viewTime);
	// affirmDate.setOnClickListener(this);
	// if (timeList != null && timeList.size() > 0) {// 默认第一个被选中
	// orderTime = timeList.get(tPos);// 初始位置
	// // poolId = poolist.get(pos).getTimeList().get(0).getPoolId();
	// for (int i = 0; i < poolist.size(); i++) {
	// if (poolist.get(i).getDate().equals(orderDate)
	// && poolist.get(i).getTimeList() != null) {
	// if (null != poolist.get(i).getTimeList()) { // 2016-1-20
	// // 个人医生站医生号池
	// // 有日子无时间点导致null异常修复
	// poolId = poolist.get(i).getTimeList().get(0)
	// .getPoolId();
	// break;
	// }
	// }
	// }
	// } else {
	// orderTime = "";
	// poolId = "";
	// }
	// oDate.setOnSelectListener(new OnSelectListener() {
	//
	// @Override
	// public void selecting(int id, String text) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void endSelect(int id, String text) {
	// orderDate = dateList.get(id);
	// timeList = timeLists.get(id);
	// // 联动显示
	// tempDateP = id;
	// tempTimeP = 0;// 2016-1-22 时间..
	// if (timeList != null && timeList.size() > 0) {
	// orderTime = timeList.get(0);// date滚轮修改时候切当旁边time的第0个位置
	// if (null != poolist.get(id).getTimeList())// 2016-1-20
	// // 个人医生站医生号池
	// // 有日子无时间点导致null异常修复
	// poolId = poolist.get(id).getTimeList().get(0)
	// .getPoolId();
	// }
	// oTime.setData(timeList);
	// oTime.setDefault(0);
	//
	// }
	// });
	// oTime.setOnSelectListener(new OnSelectListener() {
	//
	// @Override
	// public void selecting(int id, String text) {
	// }
	//
	// @Override
	// public void endSelect(int id, String text) {
	// orderTime = timeList.get(id);
	// tempTimeP = id;
	// for (int i = 0; i < poolist.size(); i++) {
	// if (poolist.get(i).getDate().equals(orderDate)) {
	// List<PoolTimes> times = poolist.get(i).getTimeList();// 2016-1-20
	// // 个人医生站医生号池
	// // 有日子无时间点导致null异常修复
	// if (null != times)
	// poolId = times.get(id).getPoolId();
	// break;
	// }
	// }
	// }
	// });
	// }

	@OnClick(R.id.commitDrugBtn)
	public void subClick(View v) {
		if (TextUtils.isEmpty(idCard)) {
			hintPerfectInfo("请完善个人信息再进行预约药品!", 1);
			return;
		}
		if (TextUtils.isEmpty(medNo)
				&& payWay != 0) {
			Toast.makeText(mContext, "你暂无绑定医保，请更换你的支付方式！", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(orderTime)) {
			Toast.makeText(mContext, "请选择就诊时间", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils
				.isEmpty(BaseApplication.getInstance().patient.getEpMobile())) {
			Toast.makeText(mContext, "请完善个人信息的手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		// 2015 - 11 -30 如果不是 肿瘤医院 就不用跳转免责声明页面
		valideDialog = new ValidateDialog(DrugWriteActivity.this,R.style.ChoiceDialog);
		valideDialog.setSure(this);
		valideDialog.show();
	}

	/**
	 * 提醒完善信息
	 */
	private void hintPerfectInfo(String hint, final int type) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(hint).setCancelable(false)
				.setNegativeButton("完善", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (type == 1) {// 完善个人信息
							mContext.startActivity(new Intent(mContext,
									AutonymAuthActivity.class));
						} else {// 完善个人手机号
							mContext.startActivity(new Intent(mContext,
									ModifyPhoneActivity.class));
						}
						DrugWriteActivity.this.finish();
					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();
	}

	/**
	 * 处理号池信息
	 */
	private void dealWithPoolData() {

		orderDate = poolist.get(pos).getDate();
		List<PoolTimes> tlist = poolist.get(pos).getTimeList();

		if (pos == 0) {// 2016-1-22 个人医生站进入 号池显示错误bug 大患更正
			if (null == tlist) {
				for (int i = 1; i < poolist.size(); i++) {
					tlist = poolist.get(i).getTimeList();
					if (null != tlist) {
						pos = i;
						orderDate = poolist.get(i).getDate();
						poolDate = orderDate;
						break;
					}
				}
			} else if (null == poolDate) {
				poolDate = orderDate;
			}
		}
		if (tlist != null && tlist.size() > 0) {
			String startTime = poolist.get(pos).getTimeList().get(tPos)
					.getStartDates();
			String endTime = poolist.get(pos).getTimeList().get(tPos)
					.getEndDates();
			orderTime = startTime + "-" + endTime;
			orderTimeTv.setText(poolist.get(pos).getDate() + " " + orderTime);

			// 就诊时间默认值
			pfId = poolist.get(pos).getPfId();
			poolId = poolist.get(pos).getTimeList().get(0).getPoolId();
			regMark = poolist.get(pos).getRegMark();
		}
		if (null == timeLists) {
			dateList = new ArrayList<String>();
			timeLists = new ArrayList<List<String>>();
			List<PoolTimes> ptList;
			int nums = 0;
			for (int i = 0; i < poolist.size(); i++) {
				timeList = new ArrayList<String>();
				ptList = poolist.get(i).getTimeList();
				nums = 0;
				if (ptList == null || ptList.size() == 0) {
					continue;
				}
				for (int j = 0; j < ptList.size(); j++) {
					if (!ptList.get(j).isRemains()) {
						continue;
					}
					timeList.add(ptList.get(j).getStartDates() + "-"
							+ ptList.get(j).getEndDates());
					nums++;
				}
				if (nums > 0) {
					timeLists.add(timeList);
					dateList.add(poolist.get(i).getDate());
				}
			}
			for (int i = 0; i < dateList.size(); i++) {
				if (dateList.get(i).equals(poolDate)) {
					pos = i;
					break;
				}
			}
			// ptList = null;
		}

	}

	/**
	 * 初始化缴费类型
	 */
	public void initPayclaz() {
		final String[] payName = { "自费", "医保" };
		builder = new Builder(this);
		builder.setItems(payName, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				payWay = which;
				costTypeTv.setText(payName[which]);
			}
		});
		builder.create().show();
	}

	/**
	 * 获取就诊人
	 */
	private void getMembers() {
		RequestParams params1 = new RequestParams();
		params1.addBodyParameter("userId",
				String.valueOf(BaseApplication.patient.getUserId()));
		new UserImpl().getMemberCenter(params1, this);
	}

	@OnClick(R.id.costTypeLayout)
	public void typeLayoutClick(View view) {
		initPayclaz();
	}

	@OnClick(R.id.orderPersonLayout)
	public void personLayoutClick(View v) {
		// 选择就诊人
		if (personList != null) {
			type = 2;
			selectPerson();
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		} else {
			Logger.i("就诊人", "就诊人数据为空");
		}
	}

	/**
	 * 窗口设置
	 */
	public void configPopupWindow(final View view) {
		pWindow = new PopupWindow();
		pWindow.setContentView(view);
		pWindow.setWidth(LayoutParams.MATCH_PARENT);
		pWindow.setHeight(LayoutParams.MATCH_PARENT);
		pWindow.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		pWindow.setBackgroundDrawable(dw);
		pWindow.setFocusable(true);
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				float left = v.findViewById(R.id.titleLayout).getLeft();
				float top = v.findViewById(R.id.titleLayout).getTop();
				float right;
				float bottom;
				if (type == 1) {
					right = v.findViewById(R.id.affirm).getRight();
					bottom = v.findViewById(R.id.affirm).getBottom();
				} else {
					right = v.findViewById(R.id.add).getRight();
					bottom = v.findViewById(R.id.add).getBottom();
				}
				float x = event.getX();
				float y = event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {

					if (x < left && x > right) {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					} else {
						if (y < top || y > bottom) {
							pWindow.dismiss();
							pWindow = null;// 2015-12-25
						}
					}
				}
				return true;
			}
		});
	}

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	private void initialTimeData(ScrollerNumberPicker view, List<String> data,
			int pos) {
		view.setData(data);
		view.setDefault(pos);
	}

	/**
	 * 初始化选择框view
	 */
	private void initialDialogView() {

		// 选择时间
		viewTime = LayoutInflater.from(this).inflate(R.layout.selecttime_order,
				null);
		// oDate = (ScrollerNumberPicker) viewTime.findViewById(R.id.date);
		// oTime = (ScrollerNumberPicker) viewTime.findViewById(R.id.time);
		affirmDate = (TextView) viewTime.findViewById(R.id.affirm);
		// 选择就诊人
		viewPerson = LayoutInflater.from(this).inflate(R.layout.selectperson,
				null);
		add = (TextView) viewPerson.findViewById(R.id.add);
		personView = (ScrollerNumberPicker) viewPerson
				.findViewById(R.id.person_wheelview);

	}

	/**
	 * 就诊人选择
	 */
	private void selectPerson() {

		initialTimeData(personView, personList, 0);
		configPopupWindow(viewPerson);
		add.setOnClickListener(this);
		if (personList != null && personList.size() > 0) {// 默认第一个被选中
			person = personList.get(0);
			// int index = person.indexOf(")") + 1;
			// person = person.substring(0, index);
			patientId = familyList.get(0).getPatientId();
			medNo = familyList.get(0).getMedicalNo();
			patientMobileStr = familyList.get(0).getPhone();
		}
		personView.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {

			}

			@Override
			public void endSelect(int id, String text) {
				person = personList.get(id);
				// int index = person.indexOf(")") + 1;
				// person = person.substring(0, index);
				patientId = familyList.get(id).getPatientId();
				idCard = familyList.get(id).getIdCard();
				medNo = familyList.get(id).getMedicalNo();
				patientMobileStr = familyList.get(id).getPhone();
			}
		});

	}

	@Override
	public void result(Object... object) {
		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		hideProgressToast();
		switch (type) {
		case HttpParams.MEMBER_CENTER:// 获取家庭成员
			if (isSuc) {
				familyList = (ArrayList<FamilyMember>) object[2];
				if (familyList != null) {
					personList = new ArrayList<String>();
					for (int i = 0; i < familyList.size(); i++) {
						String strName = familyList.get(i).getMemberName();
						String strCardNo = familyList.get(i).getIdCard();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(i).getRelation() + "");
						String strSex = familyList.get(i).getSex() == 0 ? "男"
								: "女";

						if (familyList.size() == 1) {
							// if (strName == null || "".equals(strName)) {
							// strName = "本人";
							// }
							if (TextUtils.isEmpty(strRelation)) {
								strRelation = "自己";
							}

						}
						if (TextUtils.isEmpty(strName)) {
							strName = "";
						}
						// String strNew = strName + "(" + strRelation + ")";
						String strNew = strName;
						personList.add(strNew);

					}
					if (familyList.size() > 0) {
						String strName = familyList.get(0).getMemberName();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext())
								.getLabelByTag("kinship",
										familyList.get(0).getRelation() + "");
						idCard = familyList.get(0).getIdCard();
						if (strName == null || "".equals(strName)) {
							strName = "本人";
						}
						if (TextUtils.isEmpty(strRelation)) {
							strRelation = "自己";
						}
						// person = strName + "(" + strRelation + ")";
						person = strName;
						orderPersonTv.setText(person);
						patientMobileStr = familyList.get(0).getPhone();
						medNo = familyList.get(0).getMedicalNo();
						getUserOneCard(familyList.get(0).getPatientId());
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				orderPersonTv.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case HttpParams.GET_ONECARD:// 获取一卡通
			if (isSuc) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) object[2];
				if (map == null || map.size() == 0) {
					return;
				}
				boolean flag = (Boolean) map.get("flag");
				if (!flag) {
					return;
				}
				cartoonNumStr = map.get("ucNum").toString();
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {

		if (v == add) {// 确定就诊人
			orderPersonTv.setText(person);
			if (pWindow != null) {
				pWindow.dismiss();
				pWindow = null;
			}
			getUserOneCard(patientId);
		}
		// if(v==affirmDate){
		// String time=orderDate+" "+orderTime;
		// orderTimeTv.setText(time);
		// pos = tempDateP;
		// tPos = tempTimeP;
		// dealWithPoolData();
		// if (pWindow != null) {
		// pWindow.dismiss();
		// }
		// }
	}

	/**
	 * 获取用户一卡通
	 */
	public void getUserOneCard(String patientId) {
		HospitalImpl impl = new HospitalImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("patientId", patientId);
		params.addBodyParameter("hospitalId", hospitalId);
		impl.getOneCard(params, this);
		showProgressToast();
	}

	@OnClick(R.id.orderTimeLayout)
	public void timeClick(View v) {
		type = 1;
		// selectOrderTime();
		// pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		setWheelData();
		Window win = wheelDialog.getWindow();
		LayoutParams params = wheelDialog.getWindow().getAttributes();
		win.setGravity(Gravity.BOTTOM);
		params.width = ScreenUtils.gainDM(mContext).widthPixels;
		params.height = ScreenUtils.gainDM(mContext).heightPixels / 3;
		wheelDialog.show();
	}

	private void setWheelData() {
		// List<String> dataList = new ArrayList<String>();
		// List<String> timeList = new ArrayList<String>();
		//
		// Pool pool = poolist.get(pos);
		//
		// dataList.add(pool.getDate());
		// List<PoolTimes> poolTimes = pool.getTimeList();
		// for (int j = 0; j < poolTimes.size(); j++) {
		// String timeStr = poolTimes.get(j).getStartDates() + "-"
		// + poolTimes.get(j).getEndDates();
		// timeList.add(timeStr);
		// }

		List<Integer> currIndexs = new ArrayList<Integer>();
		currIndexs.add(pos);
		currIndexs.add(tPos);

		List<List<String>> wheelData = new ArrayList<List<String>>();
		wheelData.add(dateList);
		wheelData.add(timeLists.get(pos));

		List<Integer> wheelIds = new ArrayList<Integer>();
		wheelIds.add(0);
		wheelIds.add(1);

		wheelDialog.config(this, this, wheelData, currIndexs, wheelIds, this,
				this, this, getString(R.string.title_Drug_Wheel));
	}

	@Override
	public void onSureClick(String valideCode) {
		if (valideCode.isEmpty()) {
			Toast.makeText(DrugWriteActivity.this, "请输入验证码", Toast.LENGTH_SHORT)
					.show();
		} else {
			valideDialog.dismiss();

			Intent intent = new Intent(DrugWriteActivity.this,
					OrderRegWaitActivity.class);

			intent.putExtra("code", valideCode);
			intent.putExtra("regMark", regMark);
			intent.putExtra("cartoonNum", cartoonNumStr);
			intent.putExtra("cardNum", "");
			intent.putExtra("isFirst", isFirst);
			intent.putExtra("pfId", pfId);
			intent.putExtra("sourcePfId", sourcePfId);
			intent.putExtra("poolId", poolId);
			intent.putExtra("payWay", payWay);
			intent.putExtra("patientId", patientId);
			intent.putExtra("regPerson", person);
			intent.putExtra("mobile", BaseApplication.patient.getEpMobile());
			intent.putExtra("regTime", orderDate + " " + orderTime);
			intent.putExtra("docName", "普号预约");
			intent.putExtra("docDept", docDeptStr);
			intent.putExtra("hosName", hospital.gethName());
			intent.putExtra("isOrderDrug", true);
			intent.putExtra("patientMobile", patientMobileStr);
			startActivity(intent);
			finish();

			// Intent intent = new Intent(DrugWriteActivity.this,
			// RegDrugSuccActivity.class);
			// intent.putExtra("regPerson", person);
			// intent.putExtra("mobile", patientMobileStr);
			// intent.putExtra("regTime", orderDate + " " + orderTime);
			// intent.putExtra("docDept", docDeptStr);
			// intent.putExtra("isOrderDrug", true);
			// intent.putExtra("hosName", hospital.gethName());
			// startActivity(intent);
			// finish();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO 自动生成的方法存根
		wheelDialog.setTextviewSize(wheel.getCurrentItem(),
				(TextAdapter) wheel.getViewAdapter());
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		wheelDialog.setTextviewSize(wheel.getCurrentItem(),
				(TextAdapter) wheel.getViewAdapter());
		int id = wheel.getCurrentItem();
		if (wheel.getId() == 0) {// 日期
			orderDate = dateList.get(id);
			timeList = timeLists.get(id);
			// 联动显示
			tempDateP = id;
			tempTimeP = 0;// 2016-1-22 时间..
			if (timeList != null && timeList.size() > 0) {
				orderTime = timeList.get(0);// date滚轮修改时候切当旁边time的第0个位置
				if (null != poolist.get(id).getTimeList())// 2016-1-20
															// 个人医生站医生号池
															// 有日子无时间点导致null异常修复
					poolId = poolist.get(id).getTimeList().get(0).getPoolId();
			}

			// WheelView timeView =
			// (WheelView)wheelDialog.getWheelLayout().getChildAt(1);
			wheelDialog.replaceData(1, timeList);
			// pos=id;
		} else if (wheel.getId() == 1) {// 时间

			orderTime = timeList.get(id);
			tempTimeP = id;
			for (int i = 0; i < poolist.size(); i++) {
				if (poolist.get(i).getDate().equals(orderDate)) {
					List<PoolTimes> times = poolist.get(i).getTimeList();// 2016-1-20
																			// 个人医生站医生号池
																			// 有日子无时间点导致null异常修复
					if (null != times)
						poolId = times.get(id).getPoolId();
					break;
				}
			}
			// tPos=id;
		}

	}

	@Override
	public void onItemClicked(WheelView wheel, int itemIndex) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void cancleClick() {

	}

	@Override
	public void sureClick(LinearLayout wheelLayout) {
		WheelView dateView = (WheelView) wheelLayout.getChildAt(0);
		WheelView timeView = (WheelView) wheelLayout.getChildAt(1);
		String time = orderDate + " " + orderTime;
		orderTimeTv.setText(time);
		pos = dateView.getCurrentItem();
		tPos = timeView.getCurrentItem();
		dealWithPoolData();
		if (wheelDialog != null) {
			wheelDialog.dismiss();
		}

	}
}

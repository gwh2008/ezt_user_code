/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.ordercheck;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.JudgeUtils;
import com.eztcn.user.hall.utils.Constant;

/**
 * @author Liu Gang
 * 
 *         2016年3月15日 上午8:53:48 填写订单
 */
public class WrittingOrderActivity extends FinalActivity implements IHttpResult {
	@ViewInject(R.id.orderCostLv)
	private ListView orderCostLv;
	@ViewInject(R.id.downBtn)
	private View downBtn;
	@ViewInject(R.id.orderDetailLLayout)
	private LinearLayout orderDetailLLayout;
	@ViewInject(R.id.orderCostRLayout)
	private View orderCostRLayout;
	@ViewInject(R.id.noticeItemTv)
	private TextView noticeItemTv;
	@ViewInject(R.id.patientRLayout)
	private View patientRLayout;
	@ViewInject(R.id.patientTv)
	private TextView patientTv;
	@ViewInject(R.id.mobileTv)
	private TextView mobileTv;
	@ViewInject(R.id.hopeHintTv)
	private TextView hopeHintTv;
	@ViewInject(R.id.hopeEt)
	private EditText hopeEt;
	/**
	 * 实付总额
	 */
	@ViewInject(R.id.costTv)
	private TextView costTv;
	@ViewInject(R.id.hopeRLayout)
	private View hopeRLayout;
	private boolean orderShow = false;
	private String costStr;
	private Animation animToDown, animToUp;
	private ArrayList<FamilyMember> familyList;
	private List<String> personList;
	private String personStr;
	private ScrollerNumberPicker personView;
	/**
	 * 患者id
	 */
	private String patientId;
	private PopupWindow pWindow;
	private View view2;

	private String operateUserId;
	@ViewInject(R.id.commitCheckOrderBtn)
	private View commitCheckOrderBtn;
	private CheckOrderItemAdapter orderItemAdapter;
	@ViewInject(R.id.careMoreTv)
	private TextView careMoreTv;
	private StringBuffer sbAll;
	private StringBuffer sbShow;
	private Boolean allClick = false;
	private Hospital hospital;
	private String baseCost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writtingorder);
		ViewUtils.inject(WrittingOrderActivity.this);
		loadTitleBar(true, "填写订单	", null);

		animToDown = AnimationUtils.loadAnimation(mContext, R.anim.rotatedown);
		animToUp = AnimationUtils.loadAnimation(mContext, R.anim.rotateup);

		view2 = LayoutInflater.from(this).inflate(R.layout.selectperson, null);
		TextView add = (TextView) view2.findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				patientTv.setText(personStr);
//				mobileTv.setText(phoneStr);
				if (pWindow != null) {
					pWindow.dismiss();
					pWindow = null;
				}

			}
		});

		personView = (ScrollerNumberPicker) view2
				.findViewById(R.id.person_wheelview);

		hopeEt.setFocusable(true);
		hopeEt.setFocusableInTouchMode(true);
		hopeEt.addTextChangedListener(watcher);
		hopeEt.setOnFocusChangeListener(txtFocusListener);
		// 编辑窗 textWatcher监听
		orderItemAdapter = new CheckOrderItemAdapter(mContext);
		orderCostLv.setAdapter(orderItemAdapter);

		gainData();
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}

	private OnFocusChangeListener txtFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (v == hopeEt && !hasFocus) {
				if (hopeEt.getText().toString().trim().equals("")) {
					hopeEt.setVisibility(View.INVISIBLE);
					hopeHintTv.setVisibility(View.VISIBLE);
				}
			}
		}
	};
	private TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;
		private int editStart;
		private int editEnd;
		int num = 200;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s.toString();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (s.length() == 0) {
				showSoftInput(hopeEt);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			editStart = hopeEt.getSelectionStart();
			editEnd = hopeEt.getSelectionEnd();
			if (s.length() == 0) {// 当字符串为0时候进行处理
				hopeEt.setVisibility(View.INVISIBLE);
				hideSoftInput(hopeEt);
				hopeHintTv.setVisibility(View.VISIBLE);
			} else if (s.length() > num) {
				toast("预约需求最多可以填写" + num + "字", Toast.LENGTH_SHORT);
				// hopeEt.removeTextChangedListener(watcher);
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				hopeEt.setText(s);
				hopeEt.setSelection(tempSelection);
			}
		}
	};

	/**
	 * 初始化WheelView数据
	 * 
	 * @param view
	 */
	private void initialFamilyData(ScrollerNumberPicker view,
			List<String> data, int pos) {
		view.setData(data);
		view.setDefault(pos);
	}

	private int selectPatientPos = 0;

	@OnClick(R.id.careMoreTv)
	private void moreClickListener(View view) {
		TextView btn = (TextView) view;
		if (allClick) {
			btn.setText("更多");
			noticeItemTv.setText(sbShow.toString());
		} else {
			btn.setText("收回");
			noticeItemTv.setText(sbAll.toString());
		}
		allClick = !allClick;
	}

	/**
	 * 就诊人选择
	 */
	private void selectPerson() {

		configPopupWindow(view2);
		if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (null == personList)
			getMembers();
		else
			initialFamilyData(personView, personList, selectPatientPos);
		if (personList != null && personList.size() > 0) {// 默认第一个被选中
			personStr = personList.get(selectPatientPos);
			patientId = familyList.get(selectPatientPos).getPatientId();
		}
		personView.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {

			}

			@Override
			public void endSelect(int id, String text) {
				selectPatientPos = id;
				personStr = personList.get(id);
				patientId = familyList.get(id).getPatientId();
			}
		});

	}

	/**
	 * 窗口设置
	 */
	private void configPopupWindow(final View view) {
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
				float right = v.findViewById(R.id.add).getRight();
				float bottom = v.findViewById(R.id.add).getBottom();
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

	private void gainData() {
		hospital = (Hospital) getIntent().getExtras().get("hospital");
		baseCost=getIntent().getStringExtra("baseCost");
		
		List<CheckOrderItem> checkOrderItemList = (List<CheckOrderItem>) getIntent()
				.getExtras().get("checkOrderItemList");
		double sum = 0;
		List<String> careStrs = new ArrayList<String>();

		if (null != checkOrderItemList) {
			sum += Double.parseDouble(checkOrderItemList.get(0)
					.getBaseCostStr());
			for (int i = 0; i < checkOrderItemList.size(); i++) {
				CheckOrderItem checkOrderItem = checkOrderItemList.get(i);
				if (null != checkOrderItem.getCaresStr()
						&& checkOrderItem.getCaresStr().size() > 0)
					for (int j = 0; j < checkOrderItem.getCaresStr().size(); j++) {
						String tempCareStr = checkOrderItem.getCaresStr()
								.get(j);
						if (!careStrs.contains(tempCareStr)) {
							careStrs.add(tempCareStr);
						}
					}
				double tempCost = Double.parseDouble(checkOrderItem
						.getCostStr());
				sum += tempCost;
			}
		}

		costStr = new DecimalFormat("###,###,##0.00").format(sum);
		// 订单总金额
		costTv.setText("实付总额¥" + costStr);
		// 注意事项
		if (careStrs.size() == 0) {
			careMoreTv.setVisibility(View.GONE);
			noticeItemTv.setText("暂无");
		} else {
			sbShow = new StringBuffer();
			sbAll = new StringBuffer();
			int visableSize = 3;
			if (careStrs.size() <= visableSize) {// 如果总注意事项数量小于可见数
				careMoreTv.setVisibility(View.GONE);
			} else {
				careMoreTv.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < careStrs.size(); i++) {
				if (i < visableSize) {
					sbShow.append("*" + careStrs.get(i));
					if (i != careStrs.size() - 1) {
						sbShow.append("\n");
					}
				}
				sbAll.append("*" + careStrs.get(i));
				if (i != careStrs.size() - 1) {
					sbAll.append("\n");
				}
			}
			noticeItemTv.setText(sbShow.toString());
		}
		if(baseCost!=null&&!baseCost.equals("")){
			CheckOrderItem item_cost=new CheckOrderItem();
			item_cost.setNameStr("挂号");
			item_cost.setCostStr(baseCost);
			item_cost.setOrderTypeStr("挂号");
			checkOrderItemList.add(item_cost);
		}
		// 就诊人
		orderItemAdapter.setList(checkOrderItemList);
	}

	/**
	 * 获取就诊人
	 */
	private void getMembers() {
		RequestParams params1 = new RequestParams();
		params1.addBodyParameter("userId", operateUserId);
		new UserImpl().getMemberCenter(params1, this);
	}

	@OnClick(R.id.orderDetailLLayout)
	public void orderDetailClick(View v) {
		if (orderShow) {// 展开到关闭
			orderCostRLayout.setVisibility(View.GONE);
			// 箭头动画效果
			downBtn.startAnimation(animToUp);
		} else {
			orderCostRLayout.setVisibility(View.VISIBLE);
			// 初始化适配器

			// 箭头动画效果
			downBtn.startAnimation(animToDown);
		}
		// 置反标志位
		orderShow = !orderShow;
	}

	@OnClick(R.id.hopeRLayout)
	public void hopeLayoutClick(View v) {// 预约要求
		// 隐藏提示
		hopeHintTv.setVisibility(View.GONE);
		// 显示编辑窗
		hopeEt.setVisibility(View.VISIBLE);
		hopeEt.requestFocus();
		showSoftInput(hopeEt);
	}

	@OnClick(R.id.patientRLayout)
	public void patientLayout(View v) {
		if (BaseApplication.patient == null) {
			HintToLogin(Constant.LOGIN_COMPLETE);
			return;
		}

		patientId = BaseApplication.patient.getId()+"";
		operateUserId = BaseApplication.patient.getUserId() + "";// 操作用户id
		// 点选就诊人
		selectPerson();
		pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	@OnClick(R.id.commitCheckOrderBtn)
	public void commitCheckOrderBtnListener(View v) {// 提交按钮
		if (patientTv.getText().toString().trim().equals("必选")) {
			toast("请选择就诊人", Toast.LENGTH_SHORT);
		} else if (mobileTv.getText().toString().trim().equals("")) {
			toast("请输入手机号", Toast.LENGTH_SHORT);
		} else if (!JudgeUtils.isMobile(mobileTv.getText().toString().trim())) {
			toast("请正确输入手机号", Toast.LENGTH_SHORT);
		} else {
			// 跳转
			Intent intent = new Intent(WrittingOrderActivity.this,
					CheckItemPayActivity.class);
			intent.putExtra("sumCost", costStr);
            intent.putExtra("checkOrderItemList",(Serializable)orderItemAdapter.getList());
			FamilyMember familyMember = familyList.get(selectPatientPos);
			intent.putExtra("familyMember", familyMember);
			intent.putExtra("hospital", hospital);
			intent.putExtra("patientPhone", mobileTv.getText().toString());
			intent.putExtra("patientSpecialNeed", hopeEt.getText().toString());
			startActivity(intent);
		}

	}

	private class CheckOrderItemAdapter extends
			BaseArrayListAdapter<CheckOrderItem> {
		private Context context;

		public CheckOrderItemAdapter(Activity context) {
			super(context);
			this.context = context;
		}

		class Holder {
			TextView checkNameTv;
			TextView costTv;
		}

		private Holder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_orderdetial, null);
				holder = new Holder();
				holder.checkNameTv = (TextView) convertView
						.findViewById(R.id.itemCheckNameTv);
				holder.costTv = (TextView) convertView
						.findViewById(R.id.costTv);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			CheckOrderItem checkOrderItem = mList.get(position);

			holder.checkNameTv.setText(checkOrderItem.getNameStr());
			if(checkOrderItem.getNameStr().equals("挂号")){
				holder.costTv.setText("挂号费:¥" + checkOrderItem.getCostStr());
			}else{
				holder.costTv.setText("检查费:¥" + checkOrderItem.getCostStr());
			}

			return convertView;
		}
	}

	@Override
	public void result(Object... object) {

		Integer type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

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
							if (TextUtils.isEmpty(strRelation)) {
							}

						}
						if (TextUtils.isEmpty(strName)) {
							strName = "";
						}
						String strNew = strName;
						personList.add(strNew);

					}
					if (familyList.size() > 0) {
						String strName = familyList.get(selectPatientPos)
								.getMemberName();
						String strRelation = EztDictionaryDB.getInstance(
								getApplicationContext()).getLabelByTag(
								"kinship",
								familyList.get(selectPatientPos).getRelation()
										+ "");
						if (strName == null || "".equals(strName)) {
							strName = "本人";
						}
						if (TextUtils.isEmpty(strRelation)) {
						}
						personStr = strName;
						patientTv.setText(personStr);

					}
					if (patientTv.getText().toString().equals("必选")) {
						initialFamilyData(personView, personList, 0);
					} else {
						initialFamilyData(personView, personList,
								selectPatientPos);
					}
				} else {
					patientTv.setText("必选");
				}
			} else {
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
		hideProgressToast();
	}

}

package com.eztcn.user.eztcn.activity.mine;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.FamilyMember;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker;
import com.eztcn.user.eztcn.customView.ScrollerNumberPicker.OnSelectListener;
import com.eztcn.user.eztcn.db.EztDictionaryDB;
import com.eztcn.user.eztcn.impl.MedicalRecordImpl;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.StringUtil;
/**
 * @title 创建病历（1步）
 * @describe
 * @author ezt
 * @created 2015年1月22日
 */
public class MyMedicalRecordCreateOneActivity extends FinalActivity implements
	OnClickListener,IHttpResult, TextWatcher {//OnClickListener, 

	@ViewInject(R.id.title_tv)
	private TextView title_tv;

	@ViewInject(R.id.time_name)//, click = "onClick"
	private TextView tvTime;// 就诊时间

	@ViewInject(R.id.name)//, click = "onClick"
	private TextView personName;// 就诊人

	@ViewInject(R.id.hos_name)
	private AutoCompleteTextView etHosName;// 就诊医院

	@ViewInject(R.id.doc_name)
	private EditText etDocName;// 就诊医生

	@ViewInject(R.id.disease_name)
	private EditText etDiseaseName;// 所犯疾病

	@ViewInject(R.id.next_step)//, click = "onClick"
	private Button btNextStep;

	private List<String> personList;
	private PopupWindow pWindow;
	private String person;// 所选就诊人

	private int enterType = 0;// 判断是创建还是编辑

	private String operateUserId;// 操作用户id
	private String patientId;// 患者id
	private ArrayList<FamilyMember> familyList;
	private ScrollerNumberPicker personView;
	private TextView add;
	private View view;
	private int type;

	private ArrayAdapter<String> adapter;
	private MedicalRecordImpl impl;

	private String orderDate;
	private String orderTime;

	private MedicalRecord record;
	private String mId;// 病历id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymedicalrecord_create_one);
		ViewUtils.inject(MyMedicalRecordCreateOneActivity.this);
		etDiseaseName.addTextChangedListener(this);
		if (BaseApplication.patient != null) {
			operateUserId = BaseApplication.patient.getUserId() + "";
			patientId = BaseApplication.patient.getId()+"";
		}
		loadTitleBar(true, "创建病历", null);
		getMembers();
		initialDialogView();
		initRecord();
		impl = new MedicalRecordImpl();
		etHosName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				getHospital();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 初始化信息
	 */
	public void initRecord() {
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			record = (MedicalRecord) bundle.getSerializable("record");
			enterType = bundle.getInt("enterType");
		}
		if (record == null) {
			return;
		}
		if (enterType == 0) {
			title_tv.setText("创建病历");
		} else {
			title_tv.setText("编辑病历");
		}
		mId = record.getId();
		person = record.getStrName();
		patientId = record.getPatientId();
		personName.setText(person);
		etHosName.setText(record.getHosName());
		etDocName.setText(record.getDoctorName());
		tvTime.setText(record.getClinicalTime());
		etDiseaseName.setText(record.getDisease());
	}

	/**
	 * 获取就诊人
	 */
	private void getMembers() {
		if (operateUserId != null) {
//			HashMap<String, Object> params1 = new HashMap<String, Object>();
			RequestParams params1=new RequestParams();
			params1.addBodyParameter("userId", operateUserId);
			new UserImpl().getMemberCenter(params1, this);
			showProgressToast();
		}
	}

	/**
	 * 获取相关医院
	 */
	public void getHospital() {
		if (!TextUtils.isEmpty(etHosName.getText().toString())) {
//			HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params=new RequestParams();
			params.addBodyParameter("search", etHosName.getText().toString());
			impl.getHospitalList(params, this);
		} else {
			initHosList(null);
		}
	}

	/**
	 * 获取医院相关医生
	 */
	public void getHosDoctor() {
		if (!TextUtils.isEmpty(etHosName.getText().toString())) {
//			HashMap<String, Object> params = new HashMap<String, Object>();
			RequestParams params=new RequestParams();
			params.addBodyParameter("search", etHosName.getText().toString());
			impl.getHospitalList(params, this);
		} else {
			initHosList(null);
		}
	}

	/**
	 * 创建病历第一步
	 */
	public void createRecord() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("mId", mId);
		params.addBodyParameter("patientId", patientId);
		params.addBodyParameter("hospitalName", etHosName.getText().toString());
		params.addBodyParameter("doctorName", etDocName.getText().toString());
		params.addBodyParameter("clinicalTime", tvTime.getText().toString() + ":00");
		params.addBodyParameter("diseaseName", etDiseaseName.getText().toString());
		impl.createEMR(params, this);
		showProgressToast();
	}

	/**
	 * 初始化相关医院
	 * 
	 * @param list
	 */
	public void initHosList(List<String> list) {
		if (list == null) {
			list = new ArrayList<String>();
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		etHosName.setAdapter(adapter);
	}

	/**
	 * 初始化日期
	 */
	public void initData() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		final int nowYear = calendar.get(Calendar.YEAR);
		final int nowMonth = calendar.get(Calendar.MONTH);
		final int nowDay = calendar.get(Calendar.DATE);
		DatePickerDialog dialog = new DatePickerDialog(this,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						if (year > nowYear || monthOfYear > nowMonth
								|| dayOfMonth >= nowDay) {
							Toast.makeText(getApplicationContext(),
									"就诊时间不可晚于当前时间", Toast.LENGTH_SHORT).show();
							return;
						}
						orderDate = year + "-" + (monthOfYear + 1) + "-"
								+ dayOfMonth;
						initTime();
					}

				}, nowYear, nowMonth, nowDay);
		dialog.show();
	}

	/**
	 * 时间选择器
	 */
	public void initTime() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		TimePickerDialog timePicker = new TimePickerDialog(this,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						String hour = hourOfDay + "";
						String min = minute + "";
						if (hourOfDay < 10) {
							hour = "0" + hour;
						}
						if (minute < 10) {
							min = "0" + min;
						}
						orderTime = hour + ":" + min;
						tvTime.setText(orderDate + " " + orderTime);
					}
				}, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePicker.show();
	}

	/**
	 * 初始化选择框view
	 */
	private void initialDialogView() {

		// 选择就诊人
		view = LayoutInflater.from(this).inflate(R.layout.selectperson, null);
		add = (TextView) view.findViewById(R.id.add);
		personView = (ScrollerNumberPicker) view
				.findViewById(R.id.person_wheelview);
	}

	/**
	 * 就诊人选择
	 */
	private void selectPerson() {

		initialTimeData(personView, personList, 0);
		configPopupWindow(view);
		add.setOnClickListener(this);
		if (personList != null && personList.size() > 0) {// 默认第一个被选中
			person = personList.get(0);
			int index = person.indexOf(")") + 1;
			person = person.substring(0, index);
			patientId = familyList.get(0).getPatientId();
		}
		personView.setOnSelectListener(new OnSelectListener() {

			@Override
			public void selecting(int id, String text) {

			}

			@Override
			public void endSelect(int id, String text) {
				person = personList.get(id);
				int index = person.indexOf(")") + 1;
				person = person.substring(0, index);
				patientId = familyList.get(id).getPatientId();

			}
		});

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
						}
					} else {
						if (y < top || y > bottom) {
							pWindow.dismiss();
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

	@OnClick(R.id.time_name)// 选择就诊时间
	private void time_nameClick(View v){
		initData();
	}
	
	@OnClick(R.id.name)// 选择就诊人
	private void nameClick(View v){
		if (personList != null) {
			selectPerson();
			pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		} else {
			Logger.i("就诊人", "就诊人数据为空");
		}
	}
	
	@OnClick(R.id.add)// 确认就诊人
	private void addClick(View v){
		personName.setText(person);
		if (pWindow != null) {
			pWindow.dismiss();
		}
	}
	
	@OnClick(R.id.next_step)// 下一步
	private void next_stepClick(View v){
		String strName = personName.getText().toString();
		String strTime = tvTime.getText().toString();
		String strHosName = etHosName.getText().toString();
		String strDocName = etDocName.getText().toString();
		String strDisease = etDiseaseName.getText().toString();

		if (TextUtils.isEmpty(strName)) {
			Toast.makeText(mContext, "请选择就诊人", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(strHosName)) {
			Toast.makeText(mContext, "请输入就诊医院", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(strDocName)) {
			Toast.makeText(mContext, "请输入就诊医生", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(strTime)) {
			Toast.makeText(mContext, "请选择就诊时间", Toast.LENGTH_SHORT).show();
			return;
		} else if (TextUtils.isEmpty(strDisease)) {
			Toast.makeText(mContext, "请输入所犯疾病", Toast.LENGTH_SHORT).show();
			return;
		} else {
			createRecord();
		}

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.time_name:// 选择就诊时间
//			initData();
//			break;
//
//		case R.id.name:// 选择就诊人
//			if (personList != null) {
//				selectPerson();
//				pWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//			} else {
//				Logger.i("就诊人", "就诊人数据为空");
//			}
//			break;
		case R.id.add:// 确认就诊人
			personName.setText(person);
			if (pWindow != null) {
				pWindow.dismiss();
			}
			break;
//
//		case R.id.next_step:// 下一步
//			String strName = personName.getText().toString();
//			String strTime = tvTime.getText().toString();
//			String strHosName = etHosName.getText().toString();
//			String strDocName = etDocName.getText().toString();
//			String strDisease = etDiseaseName.getText().toString();
//
//			if (TextUtils.isEmpty(strName)) {
//				Toast.makeText(mContext, "请选择就诊人", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (TextUtils.isEmpty(strHosName)) {
//				Toast.makeText(mContext, "请输入就诊医院", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (TextUtils.isEmpty(strDocName)) {
//				Toast.makeText(mContext, "请输入就诊医生", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (TextUtils.isEmpty(strTime)) {
//				Toast.makeText(mContext, "请选择就诊时间", Toast.LENGTH_SHORT).show();
//				return;
//			} else if (TextUtils.isEmpty(strDisease)) {
//				Toast.makeText(mContext, "请输入所犯疾病", Toast.LENGTH_SHORT).show();
//				return;
//			} else {
//				createRecord();
//			}
//
//			break;
		}

	}

	@Override
	protected void onActivityResult(int request, int result, Intent intent) {
		super.onActivityResult(request, result, intent);

		if (request == 2 && result == 22) {
			setResult(11);
			finish();

		}

	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (taskID) {
		case HttpParams.MEMBER_CENTER:// 获取家庭成员
			if (isSuc) {
				familyList = (ArrayList<FamilyMember>) object[2];
				if (familyList != null) {
					personList = new ArrayList<String>();
					for (int i = 0; i < familyList.size(); i++) {
						String strName = familyList.get(i).getMemberName();
						int isMain = familyList.get(i).getMainUser();
						String strRelation;
						if (isMain == 1) {
							strRelation = "自己";
						} else {
							strRelation = EztDictionaryDB.getInstance(
									getApplicationContext()).getLabelByTag(
									"kinship",
									familyList.get(i).getRelation() + "");
						}
						String strNew = strName + "(" + strRelation + ")";
						personList.add(strNew);
					}

					if (familyList.size() > 0) {
						String strName = familyList.get(0).getMemberName();
						int isMain = familyList.get(0).getMainUser();
						String strRelation;
						if (isMain == 1) {
							strRelation = "自己";
						} else {
							strRelation = EztDictionaryDB.getInstance(
									getApplicationContext()).getLabelByTag(
									"kinship",
									familyList.get(0).getRelation() + "");
						}
						if (TextUtils.isEmpty(personName.getText().toString())) {
							person = strName + "(" + strRelation + ")";
							personName.setText(person);
						}
					}
				} else {
					Toast.makeText(getApplicationContext(),
							getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(getApplicationContext(), object[3].toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case HttpParams.GET_HOSPITAL:// 获取相关医院list
			List<String> hosList = (List<String>) object[2];
			initHosList(hosList);
			break;
		case HttpParams.CREATE_EMR_1:// 创建病历第一步
			if (!isSuc) {
				Toast.makeText(getApplicationContext(), "服务器繁忙，请重试！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Integer m = (Integer) object[2];
			if (m != null) {
				mId = m + "";
			}
			Intent intent = new Intent(mContext,
					MyMedicalRecordCreateTwoActivity.class);
			if (record != null) {
				intent.putExtra("record", record);
			} else {
				intent.putExtra("mId", mId);
			}
			intent.putExtra("enterType", enterType);
			startActivityForResult(intent, 2);
			break;
		}

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (StringUtil.specialLetter(s.toString())) {
			Toast.makeText(mContext, "抱歉，不能包含特殊符号！", Toast.LENGTH_SHORT).show();
			etDiseaseName.setText("");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}

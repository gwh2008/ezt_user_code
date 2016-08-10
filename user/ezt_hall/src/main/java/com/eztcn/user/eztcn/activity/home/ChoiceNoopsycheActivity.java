package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.SwitchButton;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.PinyinComparatorDept;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 智能筛选
 * @describe
 * @author ezt
 * @created 2014年12月11日
 */
public class ChoiceNoopsycheActivity extends FinalActivity implements
		OnCheckedChangeListener, OnClickListener, OnItemClickListener,
		IHttpResult {

	@ViewInject(R.id.noopsyche_state_tv)
	private TextView noopsyche_state_tv;// 是否立即通话

	@ViewInject(R.id.noopsyche_time_tv)
	private TextView tvTime;// 时间值

	@ViewInject(R.id.noopsyche_appoint_tv)
	private TextView tvIsHave;// 是否有号

	@ViewInject(R.id.noopsyche_rate_tv)
	private TextView tvRate;// 预约率高低

	@ViewInject(R.id.noopsyche_evaluate_tv)
	private TextView tvEvaluate;// 评价高低

	@ViewInject(R.id.deptSpinner)
	private Spinner deptSpinner;// 科室

	@ViewInject(R.id.noopsyche_level_tv)
	private TextView tvLevel;// 级别

	@ViewInject(R.id.noopsyche_city_tv)
	private TextView tvCity;// 所选城市

	@ViewInject(R.id.noopsyche_threehos_tv)
	private TextView tvIsThreehos;// 是否三甲医院

	@ViewInject(R.id.title_tv)
	private TextView tvTitle;// 标题

	@ViewInject(R.id.noopsyche_time_layout)//, click = "onClick"
	private FrameLayout layoutTime;//

	@ViewInject(R.id.noopsyche_city_layout)//, click = "onClick"
	private FrameLayout layoutCity;//

	@ViewInject(R.id.choice_noopsyche_confirm_bt)//, click = "onClick"
	private TextView btConfirm;// 确定按钮

	@ViewInject(R.id.choice_noopsyche_close_bt)//, click = "onClick"
	private TextView btClose;// 取消

	@ViewInject(R.id.noopsyche_threehos_layout)
	private FrameLayout layoutThreeHos;//

	@ViewInject(R.id.noopsyche_num_layout)
	private FrameLayout layoutNum;//

	@ViewInject(R.id.noopsyche_rate_layout)
	private FrameLayout layoutRate;//

	@ViewInject(R.id.noopsyche_dept_layout)//, click = "onClick"
	private RelativeLayout layoutDept;//

	@ViewInject(R.id.noopsyche_state_layout)
	private FrameLayout layoutState;//

	@ViewInject(R.id.noopsyche_level_layout)
	private FrameLayout layoutLevel;//

	@ViewInject(R.id.noopsyche_threehos_sb)
	private SwitchButton swThreeHos;// 三甲医院按钮

	@ViewInject(R.id.noopsyche_rate_sb)
	private SwitchButton swRate;// 预约率高低按钮

	@ViewInject(R.id.noopsyche_appoint_sb)
	private SwitchButton swIsHave;// 有无号按钮

	@ViewInject(R.id.noopsyche_evaluate_sb)
	private SwitchButton swEvaluate;// 评价高低按钮

	@ViewInject(R.id.noopsyche_state_sb)
	private SwitchButton swState;// 状态（在线与否）

	@ViewInject(R.id.noopsyche_level_sb)
	private SwitchButton swLevel;// 级别

	private Dialog dialogChoice;
	private ListView ltTime;
	private TextView tvDialogTitle;
	private TextView tvCancel;

	private String deptId;

	private int dialogWidth;// 弹出框宽
	private String[] Times = new String[] { "时间", "时间1", "时间2", "时间3" };
	private String[] cityNames;// 城市数组
	private String[] cityIds;// 城市id数组

	private PopupWindowAdapter timeAdapter;
	private String strTime = "";// 所选择是时间
	private String cityId = "";// 所选的城市id
	private String strCity = "";// 所选的城市名称

	private boolean isThree;

	private boolean isRate;

	private boolean isNum;

	private boolean isEvaluate;

	private boolean isState;

	private boolean isLevel;

	private int type;// 跳转页面类型--1为选择医院点入，2为选择科室点入，3为电话医生点入

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noopsyche);
		ViewUtils.inject(ChoiceNoopsycheActivity.this);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
		type = getIntent().getIntExtra("type", 0);
		dialogWidth = (int)(getWindowWidth()*0.8);
		// dialogHeight = (int) (dm.heightPixels * 0.5);
		cityNames = getResources().getStringArray(R.array.appoint_city_name);
		cityIds = getResources().getStringArray(R.array.appoint_city_id);

		initialView(type);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

	}

	/**
	 * 初始化界面
	 * 
	 * @param type
	 */
	private void initialView(int type) {
		tvTitle.setText("智能筛选");
		strTime = tvTime.getText().toString();
		isThree = SystemPreferences.getBoolean(EZTConfig.KEY_SET_THREEHOS,
				false);

		isRate = SystemPreferences.getBoolean(EZTConfig.KEY_SET_RATE, false);

		isNum = SystemPreferences.getBoolean(EZTConfig.KEY_SET_NUM, false);

		isEvaluate = SystemPreferences.getBoolean(EZTConfig.KEY_SET_EVALUATE,
				false);

		isState = SystemPreferences.getBoolean(EZTConfig.KEY_SET_STATE, false);

		isLevel = SystemPreferences.getBoolean(EZTConfig.KEY_SET_LEVEL, false);

		if (!TextUtils.isEmpty(SystemPreferences
				.getString(EZTConfig.KEY_STR_CITY))) {
			strCity = SystemPreferences.getString(EZTConfig.KEY_STR_CITY);
			cityId = SystemPreferences.getString(EZTConfig.KEY_CITY_ID);
			tvCity.setText(strCity);
		} else {// 检测是否有选中城市
			String myCity = BaseApplication.selectCity;
			if (!TextUtils.isEmpty(myCity)) {
				for (int i = 0; i < cityNames.length; i++) {
					if (myCity.equals(cityNames[i])) {
						tvCity.setText(myCity);// 当前城市
						cityId = cityIds[i];
						break;
					} else if (i == cityNames.length - 1) {
						tvCity.setText("全国");
						cityId = "";
					}
				}
			} else {
				tvCity.setText("全国");// 如果列表没有当前城市,默认选中天津
				cityId = "";
			}

		}

		swState.setChecked(!isState);
		swLevel.setChecked(!isLevel);
		swThreeHos.setChecked(!isThree);
		swRate.setChecked(!isRate);
		swIsHave.setChecked(!isNum);
		swEvaluate.setChecked(!isEvaluate);
		swThreeHos.setOnCheckedChangeListener(this);
		swRate.setOnCheckedChangeListener(this);
		swIsHave.setOnCheckedChangeListener(this);
		swEvaluate.setOnCheckedChangeListener(this);
		swLevel.setOnCheckedChangeListener(this);
		swState.setOnCheckedChangeListener(this);

		noopsyche_state_tv.setText(isState ? "是" : "否");
		tvIsHave.setText(isNum ? "是" : "否");
		tvIsThreehos.setText(isThree ? "是" : "否");
		tvEvaluate.setText(isEvaluate ? "是" : "否");
		tvRate.setText(isRate ? "是" : "否");
		tvLevel.setText(isLevel ? "是" : "否");
		switch (type) {
		case 1:// 医院进入
				// layoutTime.setVisibility(View.VISIBLE);//暂无按时间筛选后期补上
			layoutNum.setVisibility(View.VISIBLE);
			layoutRate.setVisibility(View.VISIBLE);
			layoutLevel.setVisibility(View.VISIBLE);
			break;

		case 2:// 科室进入
				// layoutTime.setVisibility(View.VISIBLE);
			layoutNum.setVisibility(View.VISIBLE);
			layoutCity.setVisibility(View.VISIBLE);
			layoutThreeHos.setVisibility(View.VISIBLE);
			layoutRate.setVisibility(View.VISIBLE);
			layoutLevel.setVisibility(View.VISIBLE);
			break;

		case 3:// 电话医生进入
			layoutDept.setVisibility(View.VISIBLE);
			layoutState.setVisibility(View.VISIBLE);
			layoutThreeHos.setVisibility(View.VISIBLE);
			layoutLevel.setVisibility(View.VISIBLE);
			getDeptTypes();
			break;
		}

		// 初始化弹出框
		dialogChoice = new Dialog(ChoiceNoopsycheActivity.this,
				R.style.ChoiceDialog);
		View dialogView = LinearLayout.inflate(ChoiceNoopsycheActivity.this,
				R.layout.dialog_choice, null);
		ltTime = (ListView) dialogView.findViewById(R.id.dialog_lt);
		tvDialogTitle = (TextView) dialogView.findViewById(R.id.title);
		tvCancel=(TextView) dialogView.findViewById(R.id.cancel_tv);
		tvCancel.setOnClickListener(this);

		timeAdapter = new PopupWindowAdapter(ChoiceNoopsycheActivity.this);
		ltTime.setAdapter(timeAdapter);
		ltTime.setOnItemClickListener(ChoiceNoopsycheActivity.this);
		dialogChoice.setCanceledOnTouchOutside(true);
		dialogChoice.setContentView(dialogView);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isChecked = !isChecked;
		switch (buttonView.getId()) {

		case R.id.noopsyche_threehos_sb:// 是否三甲医院
			isThree = isChecked;
			tvIsThreehos.setText(isChecked ? "是" : "否");
			break;

		case R.id.noopsyche_rate_sb:// 预约率高低
			isRate = isChecked;
			tvRate.setText(isChecked ? "是" : "否");
			break;

		case R.id.noopsyche_appoint_sb:// 是否有号
			isNum = isChecked;
			tvIsHave.setText(isChecked ? "是" : "否");
			break;

		case R.id.noopsyche_evaluate_sb:// 评价高低
			isEvaluate = isChecked;
			tvEvaluate.setText(isChecked ? "是" : "否");
			break;

		case R.id.noopsyche_level_sb:// 副主任医师以上
			isLevel = isChecked;
			tvLevel.setText(isChecked ? "是" : "否");
			break;

		case R.id.noopsyche_state_sb:// 是否在线
			isState = isChecked;
			noopsyche_state_tv.setText(isChecked ? "是" : "否");
			break;
		}

	}
	@OnClick(R.id.choice_noopsyche_close_bt)
	public void closeClick(View v) {
		// 取消
			finish();
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
	@OnClick(R.id.noopsyche_city_layout)
	public void cityClick(View v) {
		// 选择城市
			choiceTime(cityNames, "选择城市", 2);
	}
	@OnClick(R.id.noopsyche_time_layout)
	public void timeClick(View v) {
		// 选择时间
			choiceTime(Times, "选择时间", 1);
	}
	@OnClick(R.id.choice_noopsyche_confirm_bt)
	public void confirmClick(View v) {
		// 确定
			switch (type) {
			case 1:
				setResult(11, new Intent().putExtra("time", strTime));
				break;

			case 2:// 科室智能筛选

				SystemPreferences.save(EZTConfig.KEY_CITY_ID, cityId);
				SystemPreferences.save(EZTConfig.KEY_STR_CITY, strCity);
				setResult(22, new Intent().putExtra("time", strTime));

				break;
			case 3:
				SystemPreferences.save("bigDeptId", deptId);
				setResult(33);
				break;

			}
			if (swThreeHos != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_THREEHOS, isThree);
			}
			if (swRate != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_RATE, isRate);
			}
			if (swIsHave != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_NUM, isNum);
			}
			if (swEvaluate != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_EVALUATE, isEvaluate);
			}
			if (swLevel != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_LEVEL, isLevel);
			}
			if (swState != null) {
				SystemPreferences.save(EZTConfig.KEY_SET_STATE, isState);
			}

			finish();
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);


	}

	@Override
	public void onClick(View v) {
		if(tvCancel==v){
			//弹出框取消
			dialogChoice.dismiss();			
		}
		
		
//		switch(v.getId()){
//		
//		
//		
//		// case R.id.noopsyche_dept_layout:// 选择科室
//		// Toast.makeText(ChoiceNoopsycheActivity.this, "科室",
//		// Toast.LENGTH_SHORT).show();
//		// break;
//
//		case R.id.noopsyche_time_layout:// 选择时间
//			choiceTime(Times, "选择时间", 1);
//			break;
//
//		case R.id.noopsyche_city_layout:// 选择城市
//			choiceTime(cityNames, "选择城市", 2);
//			break;
//
//		case R.id.choice_noopsyche_close_bt:// 取消
//			finish();
//			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
//			break;
//
//		case R.id.choice_noopsyche_confirm_bt:// 确定
//
//			switch (type) {
//			case 1:
//				setResult(11, new Intent().putExtra("time", strTime));
//				break;
//
//			case 2:// 科室智能筛选
//
//				SystemPreferences.save(EZTConfig.KEY_CITY_ID, cityId);
//				SystemPreferences.save(EZTConfig.KEY_STR_CITY, strCity);
//				setResult(22, new Intent().putExtra("time", strTime));
//
//				break;
//			case 3:
//				SystemPreferences.save("bigDeptId", deptId);
//				setResult(33);
//				break;
//
//			}
//			if (swThreeHos != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_THREEHOS, isThree);
//			}
//			if (swRate != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_RATE, isRate);
//			}
//			if (swIsHave != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_NUM, isNum);
//			}
//			if (swEvaluate != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_EVALUATE, isEvaluate);
//			}
//			if (swLevel != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_LEVEL, isLevel);
//			}
//			if (swState != null) {
//				SystemPreferences.save(EZTConfig.KEY_SET_STATE, isState);
//			}
//
//			finish();
//			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
//			break;
//		}

	}

	/**
	 * 获取大科室分类列表
	 */
	private void getDeptTypes() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		HospitalImpl impl = new HospitalImpl();
//		// params.put("hospitalId", hosId);
//		params.put("level", "1");// 分类级别
		
		
		
		RequestParams params=new RequestParams();
		HospitalImpl impl = new HospitalImpl();
		// params.put("hospitalId", hosId);
		params.addBodyParameter("level", "1");// 分类级别
		
		impl.getBigDeptList(params, this);

	}

	/**
	 * 显示科室列表
	 * 
	 * @param deptList
	 */
	public void showDeptView(final List<Dept> deptList) {
		deptId = SystemPreferences.getString("bigDeptId");
		int p = 0;
		List<String> dList = new ArrayList<String>();
		for (int i = 0; i < deptList.size(); i++) {
			dList.add(deptList.get(i).getdName());
			if (deptId.equals(deptList.get(i).getId() + "")) {
				p = i;
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.item_spinner, R.id.spinner_txt, dList);
		deptSpinner.setAdapter(adapter);
		deptSpinner.setSelection(p);
		deptSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				deptId = deptList.get(position).getId() + "";
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 弹出选择时间框(地区框)
	 */
	private void choiceTime(String data[], String title, int flag) {
		tvDialogTitle.setText(title);
		timeAdapter.setFlag(flag);
		timeAdapter.setList(data);
		// 获取listview的高度
		int totalHeight = 0;
		for (int i = 0, len = timeAdapter.getCount(); i < len; i++) {
			View listItem = timeAdapter.getView(i, null, ltTime);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int item_height = listItem.getMeasuredHeight()
					+ ltTime.getDividerHeight();
			totalHeight += item_height; // 统计所有子项的总高度
		}
		Window dialogWindow = dialogChoice.getWindow();

		totalHeight = totalHeight
				+ ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
						mContext, R.dimen.dialog_title_bar_size));
		dialogWindow.setLayout(dialogWidth,
				totalHeight > getWindowHeight() / 2 ? getWindowHeight() / 2
						: totalHeight);
		if (!dialogChoice.isShowing()) {
			dialogChoice.show();
		} else {
			dialogChoice.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (timeAdapter.getFlag()) {
		case 1:// 选择时间
			strTime = timeAdapter.getList().get(position);
			tvTime.setText(strTime);
			break;

		case 2:// 选择城市
			strCity = timeAdapter.getList().get(position);
			cityId = cityIds[position];
			tvCity.setText(strCity);
			break;
		}
		dialogChoice.dismiss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}

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
		switch (taskID) {
		case HttpParams.GET_BIG_DEPT:// 科室分类
			boolean isSucc = (Boolean) object[1];
			if (isSucc) {
				ArrayList<Dept> deptList = (ArrayList<Dept>) object[2];
				if (deptList == null) {
					return;
				}
				for (int i = 0; i < deptList.size(); i++) {
					Dept sortModel = new Dept();
					String dName = deptList.get(i).getdName();
					sortModel.setdName(dName);
					sortModel.setId(deptList.get(i).getId());
					// 汉字转换成拼音
					String pinyin = characterParser.getSelling(dName);
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						sortModel.setSortLetters(sortString.toUpperCase());
					} else {
						sortModel.setSortLetters("#");
					}
					deptList.set(i, sortModel);
					// mSortList.add(sortModel);
				}

				// 根据a-z进行排序源数据
				Collections.sort(deptList, new PinyinComparatorDept());
				if (deptList != null) {
					showDeptView(deptList);
				}
			}
			break;
		}

	}
}

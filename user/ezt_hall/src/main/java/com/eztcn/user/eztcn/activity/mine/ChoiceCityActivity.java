package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.Collections;

import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.adapter.CitySortAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.City;
import com.eztcn.user.eztcn.customView.ClearEditText;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.customView.SideBar;
import com.eztcn.user.eztcn.customView.SideBar.OnTouchingLetterChangedListener;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.impl.CityImpl;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.PinyinComparatorCity;

/**
 * @title 选择城市页面
 * @describe
 * @author ezt
 * @created 2014年12月21日
 */
@SuppressLint("NewApi")
public class ChoiceCityActivity extends FinalActivity implements IHttpResult,
		OnItemClickListener, OnClickListener {

	@ViewInject(R.id.country_lvcountry)
	private ListView sortListView;// 分类城市列表

	private ListView commonLv;// 常用城市列表

	@ViewInject(R.id.dialog)
	private TextView dialog;// 选中显示

	@ViewInject(R.id.sidrbar)
	private SideBar sideBar;// 右边选择栏

	public static TextView tvLtCity;// 定位到的城市

	private TextView tvLtCityHint;//

	private TextView tvCommonCityHint;//

	@ViewInject(R.id.filter_edit)
	private ClearEditText mClearEditText;// 搜索框

	@ViewInject(R.id.clear_edit_line)
	private TextView editLine;// 搜索框下面的线

	@ViewInject(R.id.filter_edit_layout)
	private RelativeLayout clearEditLayout;

	@ViewInject(R.id.title_bar)
	private RelativeLayout titleLayout;

	@ViewInject(R.id.choice_city_cancel_bt)
	// , click = "onClick"
	private TextView cancelBt;// 取消按钮

	private CitySortAdapter adapter;
	private CityCommonAdapterCity adapterCommon;

	private View view;

	private InputMethodManager imm = null;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private ArrayList<City> cityList;
	private ArrayList<City> commonCitys;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparatorCity pinyinComparatorCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LinearLayout.inflate(this, R.layout.activity_choice_city, null);
		setContentView(view);
		ViewUtils.inject(ChoiceCityActivity.this);
		loadTitleBar(true, "选择城市", null);
		initial();

	}

	/**
	 * 初始化
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initial() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparatorCity = new PinyinComparatorCity();
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView.setOnItemClickListener(this);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mClearEditText.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {// 得到焦点时的处理内容
							titleLayout.setVisibility(View.GONE);
							sideBar.setVisibility(View.GONE);
							sortListView.setVisibility(View.GONE);
							tvLtCityHint.setVisibility(View.GONE);
							tvCommonCityHint.setVisibility(View.GONE);
							tvLtCity.setVisibility(View.GONE);
							commonLv.setVisibility(View.GONE);
							cancelBt.setVisibility(View.VISIBLE);
							editLine.setVisibility(View.VISIBLE);
							clearEditLayout
									.setBackgroundResource(android.R.color.white);
						}
						// else {// 此处为失去焦点时的处理内容
						// }
					}
				});

		clearEditLayout.setVisibility(View.VISIBLE);// 显示搜索框

		// 初始化listview头部
		View headView = LinearLayout.inflate(ChoiceCityActivity.this,
				R.layout.city_lv_head, null);
		commonLv = (MyListView) headView
				.findViewById(R.id.choice_common_city_lv);
		commonLv.setOnItemClickListener(this);
		tvLtCity = (TextView) headView
				.findViewById(R.id.choice_location_city_tv);
		tvLtCity.setOnClickListener(this);
		if (!TextUtils.isEmpty(BaseApplication.getInstance().city)) {
			tvLtCity.setText(BaseApplication.getInstance().city);
		}

		tvLtCityHint = (TextView) headView
				.findViewById(R.id.choice_location_cityhint_tv);
		tvCommonCityHint = (TextView) headView
				.findViewById(R.id.choice_common_cityhint_tv);
		// ---------------------//

		sortListView.addHeaderView(headView);
		adapter = new CitySortAdapter(ChoiceCityActivity.this);
		sortListView.setAdapter(adapter);

		adapterCommon = new CityCommonAdapterCity(this);
		commonLv.setAdapter(adapterCommon);
		initialData();
	}

	@OnClick(R.id.choice_city_cancel_bt)
	private void choice_city_cancel_btClick(View v) {

		mClearEditText.clearFocus();
		titleLayout.setVisibility(View.VISIBLE);
		sideBar.setVisibility(View.VISIBLE);
		cancelBt.setVisibility(View.GONE);
		editLine.setVisibility(View.GONE);
		tvCommonCityHint.setVisibility(View.VISIBLE);
		tvLtCityHint.setVisibility(View.VISIBLE);
		tvLtCity.setVisibility(View.VISIBLE);
		commonLv.setVisibility(View.VISIBLE);
		mClearEditText.setText("");
		sortListView.setVisibility(View.VISIBLE);
		clearEditLayout.setBackgroundResource(android.R.color.transparent);
		if (imm == null) {
			imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 单击取消
	 * 
	 * @param v
	 */
	public void onClick(View v) {

		// switch (v.getId()) {
		// case R.id.choice_city_cancel_bt:// 取消
		// mClearEditText.clearFocus();
		// titleLayout.setVisibility(View.VISIBLE);
		// sideBar.setVisibility(View.VISIBLE);
		// cancelBt.setVisibility(View.GONE);
		// editLine.setVisibility(View.GONE);
		// tvCommonCityHint.setVisibility(View.VISIBLE);
		// tvLtCityHint.setVisibility(View.VISIBLE);
		// tvLtCity.setVisibility(View.VISIBLE);
		// commonLv.setVisibility(View.VISIBLE);
		// mClearEditText.setText("");
		// sortListView.setVisibility(View.VISIBLE);
		// clearEditLayout.setBackgroundResource(android.R.color.transparent);
		// if (imm == null) {
		// imm = (InputMethodManager)
		// getSystemService(Context.INPUT_METHOD_SERVICE);
		// }
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		// break;

		// default:// 选择定位城市
		if (v.getId() != R.id.choice_city_cancel_bt) {
			City city = new City();
			city.setCityName(tvLtCity.getText().toString());
			if (!TextUtils.isEmpty(city.getCityName())) {
				setResult(11, new Intent().putExtra("city", city));
				finish();
			}
		}

		// break;
		// }

	}

	@Override
	public void onBackPressed() {

		if (!titleLayout.isShown()) {
			mClearEditText.clearFocus();
			cancelBt.setVisibility(View.GONE);
			titleLayout.setVisibility(View.VISIBLE);
			sideBar.setVisibility(View.VISIBLE);
			editLine.setVisibility(View.GONE);
			tvCommonCityHint.setVisibility(View.VISIBLE);
			tvLtCityHint.setVisibility(View.VISIBLE);
			tvLtCity.setVisibility(View.VISIBLE);
			commonLv.setVisibility(View.VISIBLE);
			clearEditLayout.setBackgroundResource(android.R.color.transparent);
			mClearEditText.setText("");
			sortListView.setVisibility(View.VISIBLE);
			return;
		}
		super.onBackPressed();
	}

	/**
	 * 排序处理得到的城市列表
	 * 
	 * @param cityList
	 */
	private ArrayList<City> newCityLists(ArrayList<City> cityList) {

		for (int i = 0; i < cityList.size(); i++) {
			City sortModel = new City();
			String cityName = cityList.get(i).getCityName();
			sortModel.setCityName(cityName);
			String cityId = cityList.get(i).getCityId();
			sortModel.setCityId(cityId);

			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(cityName);
			if(pinyin.length()>0){
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
			}else{
				sortModel.setSortLetters("#");
			}
			cityList.set(i, sortModel);
			// mSortList.add(sortModel);
		}
		// 根据a-z进行排序源数据
		Collections.sort(cityList, pinyinComparatorCity);

		return cityList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param 输入的关键字
	 */
	private void filterData(String filterStr) {
		if (cityList != null) {
			ArrayList<City> filterDateList = new ArrayList<City>();
			if (TextUtils.isEmpty(filterStr)) {
				filterDateList = cityList;
				sortListView.setVisibility(View.GONE);
			} else {
				if(filterDateList!=null&&filterDateList.size()!=0){
					filterDateList.clear();
				}
				for (City sortModel : cityList) {
					String name="";
					if(sortModel.getCityName()!=null){
						name= sortModel.getCityName();
					}
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
				sortListView.setVisibility(View.VISIBLE);
			}
			// 根据a-z进行排序

			Collections.sort(filterDateList, pinyinComparatorCity);
			adapter.updateListView(filterDateList);
		}

	}

	/**
	 * 初始化city
	 */
	private void initialData() {
		// 常用城市
		commonCitys = new ArrayList<City>();
		for (int i = 0; i < 4; i++) {
			City commonCity = new City();
			switch (i) {
			case 0:
				commonCity.setCityName("北京");
				commonCity.setCityId(1 + "");
				break;
			case 1:
				commonCity.setCityName("上海");
				commonCity.setCityId(3 + "");
				break;
			case 2:
				commonCity.setCityName("广州");
				commonCity.setCityId(20 + "");
				break;
			case 3:
				commonCity.setCityName("天津");
				commonCity.setCityId(2 + "");
				break;
			}
			commonCitys.add(commonCity);
		}
		showProgressToast();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 城市分类
				// ArrayList<City> citys = EztDb.getInstance(
				// ChoiceCityActivity.this).queryAll(new City());
				WhereBuilder b = null;
				ArrayList<City> citys = EztDb.getInstance(
						ChoiceCityActivity.this).queryAll(new City(), b, null);
				if (citys.size() != 0) {
					cityList = newCityLists(citys);
					handler.sendEmptyMessage(1);
				} else {
					RequestParams params = new RequestParams();
					// HashMap<String, Object> params = new HashMap<String,
					// Object>();
					CityImpl impl = new CityImpl();
					impl.getCityList(params, ChoiceCityActivity.this);
				}

			}
		}).start();

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.setList(cityList);
			adapterCommon.setList(commonCitys);
			hideProgressToast();
		}

	};

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_CITY:// 获取城市列表
			if (isSuc) {// 成功
				cityList = (ArrayList<City>) object[2];
				if (cityList == null || cityList.size() == 0) {
					return;
				}
				cityList = newCityLists(cityList);
				handler.sendEmptyMessage(1);
				for (int i = 0; i < cityList.size(); i++) {
					EztDb.getInstance(this).save(cityList.get(i));
				}
			} else {

			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		City city = null;
		switch (parent.getId()) {
		case R.id.country_lvcountry:// 城市分类
			// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
			city = (City) adapter.getItem(position - 1);

			break;

		default:// 常用城市
			city = (City) adapterCommon.getItem(position);
			break;
		}
		setResult(11, new Intent().putExtra("city", city));
		finish();

	}

	private class CityCommonAdapterCity extends BaseArrayListAdapter<City> {

		public CityCommonAdapterCity(Activity context) {
			super(context);
		}

		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			final City mContent = mList.get(position);
			if (view == null) {
				viewHolder = new ViewHolder();
				view = LayoutInflater.from(mContext).inflate(
						R.layout.item_city_city, null);
				viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			viewHolder.tvTitle.setText(mContent.getCityName());
			return view;

		}

		final class ViewHolder {
			TextView tvTitle;
		}

	}
}

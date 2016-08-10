package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.fdoc.DiseaseDetailActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.adapter.DiseasesSortAdapter;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.customView.ClearEditText;
import com.eztcn.user.eztcn.customView.SideBar;
import com.eztcn.user.eztcn.customView.SideBar.OnTouchingLetterChangedListener;
import com.eztcn.user.eztcn.db.SymptomSelfDb;
import com.eztcn.user.eztcn.utils.CharacterParser;
import com.eztcn.user.eztcn.utils.PinyinComparatorDiseases;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 疾病表
 * @describe
 * @author ezt
 * @created 2015年4月20日
 */
public class Disease_FormFragment extends FinalFragment implements
		OnItemClickListener, OnClickListener {

	private Activity mActivity;
	private View view;

	private ListView lv;// 分类症状列表

	private TextView dialog;// 选中显示

	private SideBar sideBar;// 右边选择栏

	private ClearEditText mClearEditText;// 搜索框

	private TextView editLine;// 搜索框下面的线

	private RelativeLayout clearEditLayout;

	private TextView cancelBt;// 取消按钮

	// private ImageView imgQuery;// 查询按钮

	private DiseasesSortAdapter diseaseAdapter;

	private ArrayList<Diseases> dList, filterDateList;// 症状列表

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparatorDiseases pinyinComparatorDisease;

	public static Disease_FormFragment newInstance() {
		Disease_FormFragment fragment = new Disease_FormFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (view == null) {
			view = inflater.inflate(R.layout.activity_choice_symptom, null);
			initial();
		}

		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	private boolean mHasLoadedOnce = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (this.isVisible()) {
			// we check that the fragment is becoming visible
			if (isVisibleToUser && !mHasLoadedOnce && dList == null) {
				initData();
				mHasLoadedOnce = true;
			}
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		((SymptomSelfActivity) mActivity).tvDiseaseForm.setEnabled(false);
		((SymptomSelfActivity) mActivity).tvSymptomForm.setEnabled(false);
		((SymptomSelfActivity) mActivity).tvBodyView.setEnabled(false);
		((SymptomSelfActivity) mActivity).pager.setNoScroll(true);
		FinalActivity.getInstance().showProgressToast();
		thread.start();
	}
	
	
	Thread thread=new Thread(new Runnable() {

		@Override
		public void run() {
			dList = new SymptomSelfDb(mActivity).getDiseasesListAll();
			dList = newDiseases(dList);
			handler.sendEmptyMessage(1);

		}
	});
	
	@Override
	public void onPause() {
		thread.interrupt();
		super.onPause();
	}

	/**
	 * 初始化
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initial() {

		lv = (ListView) view.findViewById(R.id.country_lvcountry);// 分类症状列表

		dialog = (TextView) view.findViewById(R.id.dialog);// 选中显示

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);// 右边选择栏

		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);// 搜索框

		editLine = (TextView) view.findViewById(R.id.clear_edit_line);// 搜索框下面的线

		clearEditLayout = (RelativeLayout) view
				.findViewById(R.id.filter_edit_layout);

		cancelBt = (TextView) view.findViewById(R.id.choice_city_cancel_bt);// 取消按钮
		cancelBt.setOnClickListener(this);

		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparatorDisease = new PinyinComparatorDiseases();
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = diseaseAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lv.setSelection(position);
				}

			}
		});

		lv.setOnItemClickListener(this);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start,
					int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表

				if (TextUtils.isEmpty(s.toString())) {
					diseaseAdapter.setList(dList);
					lv.setVisibility(View.GONE);
				} else if (!StringUtil.allChinese(s.toString())) {
					Toast.makeText(mActivity, "抱歉，只能输入中文！", Toast.LENGTH_SHORT)
							.show();
					mClearEditText.setText("");
				} else {
					FinalActivity.getInstance().showProgressToast();
					new Thread(new Runnable() {

						@Override
						public void run() {
							filterData(s.toString());
						}
					}).start();
					FinalActivity.getInstance().hideSoftInput(mClearEditText);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		mClearEditText
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {// 得到焦点时的处理内容
							showSearchCircle();
						}
						// else {// 此处为失去焦点时的处理内容
						// }
					}
				});

		diseaseAdapter = new DiseasesSortAdapter(mActivity);
		lv.setAdapter(diseaseAdapter);

	}

	/**
	 * 单击取消
	 * 
	 * @param v
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.choice_city_cancel_bt:// 取消
			hiddSearchCircle();
			FinalActivity.getInstance().hideSoftInput(v);
			break;

		default:// 选择搜索
			// if (titleLayout.getVisibility() == View.VISIBLE) {// 显示搜索框
			// showSearchCircle();
			// }
			break;
		}

	}

	/**
	 * 显示搜索框
	 */
	private void showSearchCircle() {
		clearEditLayout.setVisibility(View.VISIBLE);
		// titleLayout.setVisibility(View.GONE);
		sideBar.setVisibility(View.GONE);
		lv.setVisibility(View.GONE);
		cancelBt.setVisibility(View.VISIBLE);
		editLine.setVisibility(View.VISIBLE);
		clearEditLayout.setBackgroundResource(android.R.color.white);
	}

	/**
	 * 隐藏搜索框
	 */
	private void hiddSearchCircle() {
		clearEditLayout.setVisibility(View.GONE);
		mClearEditText.clearFocus();
		cancelBt.setVisibility(View.GONE);
		// titleLayout.setVisibility(View.VISIBLE);
		sideBar.setVisibility(View.VISIBLE);
		editLine.setVisibility(View.GONE);
		clearEditLayout.setBackgroundResource(android.R.color.transparent);
		mClearEditText.setText("");
		lv.setVisibility(View.VISIBLE);
	}

	// @Override
	// public void onBackPressed() {
	//
	// if (!titleLayout.isShown()) {
	// hiddSearchCircle();
	// return;
	// }
	// super.onBackPressed();
	// }

	/**
	 * 排序处理得到的疾病列表
	 * 
	 * @param cityList
	 */
	private ArrayList<Diseases> newDiseases(ArrayList<Diseases> dList) {

		for (int i = 0; i < dList.size(); i++) {
			Diseases sortModel = new Diseases();
			String dName = dList.get(i).getdName();
			sortModel.setdName(dName);
			int dId = dList.get(i).getId();
			sortModel.setId(dId);
			// 汉字转换成拼音
			String pinyin ="";
			try {
				pinyin = characterParser.getSelling(dName);
			} catch (Exception e) {
			}

			if (!TextUtils.isEmpty(pinyin)) {
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
				dList.set(i, sortModel);
			}
		}
		// 根据a-z进行排序源数据
		Collections.sort(dList, pinyinComparatorDisease);
		return dList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param 输入的关键字
	 */
	private void filterData(String filterStr) {
		if (dList != null) {
			filterDateList = new ArrayList<Diseases>();
			if (TextUtils.isEmpty(filterStr)) {
				filterDateList = dList;
			} else {
				filterDateList.clear();
				for (Diseases sortModel : dList) {
					String name = sortModel.getdName();
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
			}
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparatorDisease);
			handler.sendEmptyMessage(2);

		}

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:// 获取数据
				diseaseAdapter.setList(dList);
				sideBar.setVisibility(View.VISIBLE);
				FinalActivity.getInstance().hideProgressToast();
				((SymptomSelfActivity) mActivity).pager.setNoScroll(false);
				((SymptomSelfActivity) mActivity).tvDiseaseForm.setEnabled(true);
				((SymptomSelfActivity) mActivity).tvSymptomForm.setEnabled(true);
				((SymptomSelfActivity) mActivity).tvBodyView.setEnabled(true);
				
				// imgQuery.setEnabled(true);
				break;

			case 2:// 筛选数据
				diseaseAdapter.updateListView(filterDateList);
				lv.setVisibility(View.VISIBLE);
				FinalActivity.getInstance().hideProgressToast();
				break;
			}

		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		startActivity(new Intent(mActivity, DiseaseDetailActivity.class)
				.putExtra("disease", diseaseAdapter.getList().get(position)));
	}
}
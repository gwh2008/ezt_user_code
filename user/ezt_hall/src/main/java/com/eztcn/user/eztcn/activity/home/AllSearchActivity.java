package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Map;

import xutils.ViewUtils;
import xutils.db.sqlite.Selector;
import xutils.db.sqlite.WhereBuilder;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationDetailActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalDetailActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.adapter.HotWordListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Dept;
import com.eztcn.user.eztcn.bean.Doctor;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Information;
import com.eztcn.user.eztcn.bean.SearchKeyword;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.PagerSlidingTobTab;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.fragment.AllSearchChildFragment;
import com.eztcn.user.eztcn.fragment.AllSearchChild_DeptFragment;
import com.eztcn.user.eztcn.fragment.AllSearchChild_DocFragment;
import com.eztcn.user.eztcn.fragment.AllSearchChild_HosFragment;
import com.eztcn.user.eztcn.fragment.AllSearchChild_InfoFragment;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 全站搜索
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
public class AllSearchActivity extends FinalActivity implements
		IHttpResult{//OnClickListener, Iclick

	@ViewInject(R.id.all_search_pager)
	public ViewPager mPager;// 每个页面pager

	@ViewInject(R.id.title_column_layout)
	private LinearLayout columnLayout;

	@ViewInject(R.id.tabs)
	private PagerSlidingTobTab myPagerTab;

	@ViewInject(R.id.all_search_et)
//	private AutoCompleteTextView etSearch;// 搜索框
	private EditText etSearch;// 搜索框

//	@ViewInject(R.id.hot_word_gv)
//	private GridView gvHotWord;// 热词

	@ViewInject(R.id.hot_word_layout)
	private LinearLayout layoutHotWord;

	@ViewInject(R.id.all_search_img)//, click = "onClick"
	private LinearLayout imgSearch;// 搜索按钮
	
	@ViewInject(R.id.lv_history)
	private ListView lv_history;//搜索历史列表2015-12-29
	@ViewInject(R.id.tv_clearSHistory)
	private TextView tv_clearSHistory;//清除搜索历史2015-12-29

	public static ArrayList<Hospital> hosList;// 医院列表
	public ArrayList<Dept> deptList;// 科室列表
	public ArrayList<Doctor> docList;// 医生列表
	public ArrayList<Information> knowLibList;// 知识库列表

	private boolean isOnclick = false;// 标记是否点击搜索按钮
	private boolean hosSuc = false;
	private boolean deptSuc = false;
	private boolean docSuc = false;
	private boolean knowLibSuc = false;
	private String strSearch = "";// 搜索关键字
	public int num = 0;// 栏目数量
	private String[] strName;// 标题数组
	private MyPagerAdapter adapter = null;

	private ArrayList<Hospital> newhosList;
	private ArrayList<Dept> newdeptList;
	private ArrayList<Doctor> newdocList;
	private ArrayList<Information> newknowLibList;
	private HotWordListAdapter hisAdapter;

//	private HotWordListAdapter hotWordAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_search_new);
		ViewUtils.inject(AllSearchActivity.this);
		loadTitleBar(true, "搜索", null);
		hisAdapter=new HotWordListAdapter(this);
		lv_history.setAdapter(hisAdapter);
		getHisKeyWord();
		
//		hotWordAdapter = new HotWordListAdapter(this);
//		hotWordAdapter.AdapterClick(this);
//		gvHotWord.setAdapter(hotWordAdapter);
//		initialDataHotWords();

//		etSearch.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				getHisKeyword(s.toString().trim());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
	}
	@OnItemClick(R.id.lv_history)//2015-12-29 点击某一项热搜关键词
	private void  lv_history_itemClick(AdapterView<?> parent, View view,
			int pos, long id){
		String hotWord = hisAdapter.getList().get(pos);
		etSearch.setText(hotWord);
		imgSearch.performClick();
	}
	@OnClick(R.id.tv_clearSHistory)
	private void clearHistoryClick(View view){
		final MyDialog dialog=new MyDialog(mContext, "确定", "取消", "清除全部历史纪录？", null);
		dialog.setDialogCancle(new DialogCancle() {
			
			@Override
			public void dialogCancle() {
				dialog.dissMiss();
			}
		});
		dialog.setDialogSure(new DialogSure() {
			
			@Override
			public void dialogSure() {
				delAllHis();
				hisAdapter.mList.clear();
				hisAdapter.notifyDataSetChanged();
				tv_clearSHistory.setVisibility(View.GONE);
				dialog.dissMiss();
			}
		});
		dialog.show();
		
	}
	
	/**
	 * 获取热搜历史 2015-12-29
	 */
	private void getHisKeyWord(){
		Selector selector=Selector.from(SearchKeyword.class);;
		ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext).queryAll(new SearchKeyword(), selector, null);
		ArrayList<String> list = new ArrayList<String>();
		if(null!=keywords&&keywords.size()>0){
			tv_clearSHistory.setVisibility(View.VISIBLE);
			for (int i = 0; i < keywords.size(); i++) {
				list.add(keywords.get(i).getKeyWord());
			}
			hisAdapter.setList(list);
		}else{
			tv_clearSHistory.setVisibility(View.GONE);
		}
	
	}
	/**
	 * 删除所有搜索数据
	 */
	private void delAllHis(){
		 EztDb.getInstance(mContext).delDataWhere(new SearchKeyword(), null);
	}
	/**
	 * 获取历史输入的关键字
	 */
//	private void getHisKeyword(String str) {
//
////		ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext)
////				.queryDataWhere(new SearchKeyword(),
////						"keyWord like " + "'" + str + "%'");
//		WhereBuilder whereBuilder=WhereBuilder.b("keyWord",  "like" , str+"%");
//		ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext)
//				.queryAll(new SearchKeyword(), whereBuilder, null
//						);
//		
//		ArrayList<String> list = new ArrayList<String>();
//
//		int size = keywords.size();
//		if (size >= 5) {
//			size = 5;
//		}
//		for (int i = 0; i < size; i++) {
//			list.add(keywords.get(i).getKeyWord());
//		}
//		ArrayAdapter<String> keyWordAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, list);
//		etSearch.setAdapter(keyWordAdapter);
//	}

//	/**
//	 * 初始化热词数据
//	 */
//	private void initialDataHotWords() {
//		String[] hotwords = getResources()
//				.getStringArray(R.array.hot_word_list);// 热词列表
//		hotWordAdapter.setList(hotwords);
//
//	}

	/**
	 * 初始化导航栏数据
	 */
	private void initialColumnData() {
		strName = new String[] { "全 部", "医 院", "科 室", "专 家", "知识库" };
//		strName = new String[] { "全 部", "医 院", "科 室", "专 家"};
		newhosList = new ArrayList<Hospital>();
		newdeptList = new ArrayList<Dept>();
		newdocList = new ArrayList<Doctor>();
		newknowLibList = new ArrayList<Information>();
		if (hosList != null && hosList.size() > 3) {
			for (int i = 0; i < 3; i++) {
				newhosList.add(hosList.get(i));
			}

		} else {
			newhosList = hosList;
		}
		if (docList != null && docList.size() > 3) {
			for (int i = 0; i < 3; i++) {
				newdocList.add(docList.get(i));
			}
		} else {
			newdocList = docList;
		}
		if (deptList != null && deptList.size() > 3) {

			for (int i = 0; i < 3; i++) {
				newdeptList.add(deptList.get(i));
			}

		} else {
			newdeptList = deptList;
		}
		if (knowLibList != null && knowLibList.size() > 3) {
			for (int i = 0; i < 3; i++) {
				newknowLibList.add(knowLibList.get(i));
			}
		} else {
			newknowLibList = knowLibList;
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		myPagerTab.setTextSize(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.medium_size)));
		myPagerTab.setIndicatorColorResource(R.color.main_color);
		myPagerTab.setTabTextSelectColor(getResources().getColor(
				R.color.main_color));
		myPagerTab.setIndicatorHeight(7);
		myPagerTab.setDividerColor(getResources().getColor(
				android.R.color.transparent));
		myPagerTab.setUnderlineHeight(1);
		myPagerTab.setUnderlineColorResource(R.color.dark_gray);
		myPagerTab.setTabPaddingLeftRight(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.large_margin)));
		myPagerTab.setIndicatorMargin(ResourceUtils.dip2px(mContext,
				ResourceUtils.getXmlDef(mContext, R.dimen.medium_margin)));
		myPagerTab.setViewPager(mPager);
		hideProgressToast();
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return strName[position];
		}

		@Override
		public int getCount() {
			return strName.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:// 全部页面fragment页面
				fragment = new AllSearchChildFragment();
				break;

			case 1:// 医院页面
				fragment = new AllSearchChild_HosFragment();
				break;

			case 2:// 科室页面
				fragment = new AllSearchChild_DeptFragment();
				break;

			case 3:// 专家页面
				fragment = new AllSearchChild_DocFragment();
				break;

			case 4:// 知识库
				fragment = new AllSearchChild_InfoFragment();
				break;
			}

			return fragment;
		}

		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			switch (position) {
			case 0:// 全部页面数据
				AllSearchChildFragment fragment = (AllSearchChildFragment) super
						.instantiateItem(container, position);
				fragment.setDeptList(newdeptList);
				fragment.setHosList(newhosList);
				fragment.setDocList(newdocList);
				fragment.setKnowLibList(newknowLibList);
				return fragment;

			case 1:// 医院页面数据
				AllSearchChild_HosFragment hosFragment = (AllSearchChild_HosFragment) super
						.instantiateItem(container, position);
				hosFragment.setHosList(hosList);
				hosFragment.setStrSearch(strSearch);
				return hosFragment;

			case 2:// 科室页面数据
				AllSearchChild_DeptFragment deptFragment = (AllSearchChild_DeptFragment) super
						.instantiateItem(container, position);
				deptFragment.setDeptList(deptList);
				deptFragment.setStrSearch(strSearch);
				return deptFragment;

			case 3:// 专家页面数据
				AllSearchChild_DocFragment docFragment = (AllSearchChild_DocFragment) super
						.instantiateItem(container, position);
				docFragment.setDocList(docList);
				docFragment.setStrSearch(strSearch);
				return docFragment;

			case 4:// 知识库页面数据
				AllSearchChild_InfoFragment infoFragment = (AllSearchChild_InfoFragment) super
						.instantiateItem(container, position);
				infoFragment.setKnowLibList(knowLibList);
				infoFragment.setStrSearch(strSearch);
				return infoFragment;
			}

			return super.instantiateItem(container, position);
		}

	}
	@OnClick(R.id.item_search_title_hos)
	private void hosClick(View v){
		// 医院
		mPager.setCurrentItem(1);
	}
	
	@OnClick(R.id.item_search_title_dept)
	private void deptClick(View v){
		// 科室
		mPager.setCurrentItem(2);
	}
	@OnClick(R.id.item_search_title_professor)
	private void docClick(View v){// 医生
		mPager.setCurrentItem(3);
	}
	@OnClick(R.id.item_search_title_knowlib)
	private void knowLibClick(View v){// 知识库
		mPager.setCurrentItem(4);
	}
	
	@OnClick(R.id.all_search_img)
	private void searchClick(View v){// 搜索
		strSearch = etSearch.getText().toString().trim();
		if (TextUtils.isEmpty(strSearch)) {
			Toast.makeText(mContext, getString(R.string.search_empty),
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!BaseApplication.getInstance().isNetConnected) {
			Toast.makeText(mContext, getString(R.string.network_hint),
					Toast.LENGTH_SHORT).show();
			return;
		} else {
//			ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext)
//					.queryDataWhere(new SearchKeyword(),
//							"keyWord = " + "'" + strSearch + "'");
			
			
			WhereBuilder builder=WhereBuilder.b("keyWord", "=", strSearch);
			ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext)
					.queryAll(new SearchKeyword(),builder,null);
			
			if (keywords.size() <= 0) {
				SearchKeyword keyword = new SearchKeyword();
				keyword.setKeyWord(strSearch);
				EztDb.getInstance(mContext).save(keyword);
			}
			hideSoftInput(etSearch);
			etSearch.clearFocus();
			myPagerTab.index = 0;// 初始化选中下标
			imgSearch.setEnabled(false);
			isOnclick = true;
			hosSuc = false;
			docSuc = false;
			deptSuc = false;
			knowLibSuc = false;
			showProgressToast();
			getHosData();
			getDeptData();
			getDocData();
			getKnowLibData();
		}
	}
//	@Override
//	public void onClick(View v) {// 搜索
//
//		switch (v.getId()) {
//		case R.id.item_search_title_hos:// 医院
//			mPager.setCurrentItem(1);
//			break;
//
//		case R.id.item_search_title_dept:// 科室
//			mPager.setCurrentItem(2);
//			break;
//
//		case R.id.item_search_title_professor:// 医生
//			mPager.setCurrentItem(3);
//
//			break;
//
//		case R.id.item_search_title_knowlib:// 知识库
//			mPager.setCurrentItem(4);
//			break;
//
//		case R.id.all_search_img:// 搜索
//
//			strSearch = etSearch.getText().toString().trim();
//			if (TextUtils.isEmpty(strSearch)) {
//				Toast.makeText(mContext, getString(R.string.search_empty),
//						Toast.LENGTH_SHORT).show();
//				return;
//			} else if (!BaseApplication.getInstance().isNetConnected) {
//				Toast.makeText(mContext, getString(R.string.network_hint),
//						Toast.LENGTH_SHORT).show();
//				return;
//			} else {
//				ArrayList<SearchKeyword> keywords = EztDb.getInstance(mContext)
//						.queryDataWhere(new SearchKeyword(),
//								"keyWord = " + "'" + strSearch + "'");
//				if (keywords.size() <= 0) {
//					SearchKeyword keyword = new SearchKeyword();
//					keyword.setKeyWord(strSearch);
//					EztDb.getInstance(mContext).save(keyword);
//				}
//				hideSoftInput(etSearch);
//				etSearch.clearFocus();
//				myPagerTab.index = 0;// 初始化选中下标
//				imgSearch.setEnabled(false);
//				isOnclick = true;
//				hosSuc = false;
//				docSuc = false;
//				deptSuc = false;
//				knowLibSuc = false;
//				showProgressToast();
//				getHosData();
//				getDeptData();
//				getDocData();
//				getKnowLibData();
//			}
//			break;
//		}

//	}

	/**
	 * 医院跳转医院详情
	 */
	public void toDoctorList(Hospital hos) {
//		startActivity(new Intent(mContext, HospitalDetailActivity.class)
//				.putExtra("hospital", hos));
		startActivity(new Intent(mContext, HosHomeActivity.class)
		.putExtra("hospital", hos));
		
	}

	/**
	 * 科室跳转医生列表(按医院找)
	 */
	public void toDoctorList(String deptName, String hosName, int deptId,
			String dHid) {
		startActivity(new Intent(mContext, DoctorList30Activity.class)
				.putExtra("type", 1).putExtra("deptName", deptName)

				.putExtra("hosId", dHid).putExtra("deptId", deptId + "")

				.putExtra("isAllSearch", true).putExtra("hosName", hosName));
		
		
		
	}

	/**
	 * 跳转医生主页
	 */
	public void toDoctorInfo(String deptId1, String docId, String deptDocId,
			int ehDockingStatus) {// 2015-12-21 ehDockingStatus 医院是否对接

		Intent intent = new Intent(mContext, DoctorIndex30Activity.class)
				.putExtra("deptId", deptId1).putExtra("docId", docId)
				.putExtra("deptDocId", deptDocId).putExtra("ehDockingStatus", ehDockingStatus);
		startActivity(intent);

	}
	

	/**
	 * 跳转资讯详情
	 * 
	 * @param infoId
	 */
	public void toInfomation(String infoId) {

		startActivity(new Intent(mContext, InformationDetailActivity.class)
				.putExtra("infoId", infoId));
	}

	/**
	 * 搜索医院列表
	 */
	public void getHosData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", "1");
//		params.put("rowsPerPage", EZTConfig.PAGE_SIZE + "");
//		params.put("search", strSearch);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", "1");
		params.addBodyParameter("rowsPerPage", EZTConfig.PAGE_SIZE + "");
		params.addBodyParameter("search", strSearch);
		
		new HospitalImpl().getSearchHos(params, this);

	}

	/**
	 * 搜索科室列表
	 */
	public void getDeptData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", "1");
//		params.put("rowsPerPage", EZTConfig.PAGE_SIZE + "");
//		params.put("search", strSearch);
		
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", "1");
		params.addBodyParameter("rowsPerPage", EZTConfig.PAGE_SIZE + "");
		params.addBodyParameter("search", strSearch);
		
		new HospitalImpl().getSearchDept(params, this);
	}

	/**
	 * 搜索医生列表
	 */
	public void getDocData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", "1");
//		params.put("rowsPerPage", EZTConfig.PAGE_SIZE + "");
//		params.put("search", strSearch);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", "1");
		params.addBodyParameter("rowsPerPage", EZTConfig.PAGE_SIZE + "");
		params.addBodyParameter("search", strSearch);
		
		new HospitalImpl().getSearchDoc(params, this);

	}

	/**
	 * 搜索知识库列表
	 */
	public void getKnowLibData() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("page", "1");
//		params.put("rowsPerPage", EZTConfig.PAGE_SIZE + "");
//		params.put("search", strSearch);
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("page", "1");
		params.addBodyParameter("rowsPerPage", EZTConfig.PAGE_SIZE + "");
		params.addBodyParameter("search", strSearch);
		new HospitalImpl().getSearchKnowLib(params, this);
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {
		case HttpParams.GET_SEARCH_HOS:// 获取医院列表
			hosSuc = true;
			if (isSuc) {
				Map<String, Object> hosMap = (Map<String, Object>) object[2];
				if (hosMap != null) {
					hosList = (ArrayList<Hospital>) hosMap.get("hosList");
					if (!isOnclick) {
						if (hosList != null && hosList.size() > 0) {

						} else {
							Logger.i("全站搜索获取医院列表", "暂无数据");
						}
					}
				} else {
					Logger.i("全站搜索获取医院列表", "获取数据失败");
				}
			} else {

			}

			break;

		case HttpParams.GET_SEARCH_DEPT:// 获取科室列表
			deptSuc = true;
			if (isSuc) {
				Map<String, Object> deptMap = (Map<String, Object>) object[2];
				if (deptMap != null) {
					deptList = (ArrayList<Dept>) deptMap.get("deptList");
					if (!isOnclick) {
						if (deptList != null && deptList.size() > 0) {

						} else {

							Logger.i("全站搜索获取科室列表", "暂无数据");
						}
					}
				} else {

					Logger.i("全站搜索获取科室列表", "获取数据失败");
				}
			} else {

			}
			break;

		case HttpParams.GET_SEARCH_DOC:// 获取医生列表
			docSuc = true;
			if (isSuc) {
				Map<String, Object> deptMap = (Map<String, Object>) object[2];

				if (deptMap != null) {
					docList = (ArrayList<Doctor>) deptMap.get("docList");
					if (!isOnclick) {
						if (docList != null && docList.size() > 0) {

						} else {

							Logger.i("全站搜索获取医生列表", "暂无数据");
						}
					}
				} else {

					Logger.i("全站搜索获取医生列表", "获取数据失败");
				}

			} else {

			}
			break;

		case HttpParams.GET_SEARCH_LIB:// 获取知识库列表
			knowLibSuc = true;
			if (isSuc) {
				Map<String, Object> deptMap = (Map<String, Object>) object[2];
				if (deptMap != null) {
					knowLibList = (ArrayList<Information>) deptMap
							.get("infoList");

					if (!isOnclick) {
						if (knowLibList != null && knowLibList.size() > 0) {

						} else {

							Logger.i("全站搜索获取知识库列表", "暂无数据");
						}
					}
				} else {
					Logger.i("全站搜索获取知识库列表", "获取数据失败");
				}

			} else {

			}

			break;

		}
		if (isOnclick && hosSuc && deptSuc && docSuc && knowLibSuc) {// 都四个都获取到数据时，刷新全部页面
//		if (isOnclick && hosSuc && deptSuc && docSuc ) {// 都四个都获取到数据时，刷新全部页面
			initialColumnData();
			layoutHotWord.setVisibility(View.GONE);
			columnLayout.setVisibility(View.VISIBLE);
			mPager.setVisibility(View.VISIBLE);
			isOnclick = false;
			imgSearch.setEnabled(true);
		}

	}

//	@Override
//	public void click(int pos) {
//		String hotWord = hotWordAdapter.getList().get(pos);
//		etSearch.setText(hotWord);
//		imgSearch.performClick();
//	}

}

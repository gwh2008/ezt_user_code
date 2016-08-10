package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.CCBUserActivity;
import com.eztcn.user.eztcn.activity.home.CallENurseActivity;
import com.eztcn.user.eztcn.activity.mine.MyHealthCardActivity;
import com.eztcn.user.eztcn.adapter.BankListAdapter;
import com.eztcn.user.eztcn.adapter.ViewPagerAdapter;
import com.eztcn.user.eztcn.customView.MyGridView;
import com.eztcn.user.eztcn.customView.MyViewPager;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title e护帮
 * @describe
 * @author ezt
 * @created 2015年7月16日
 */
public class ENurseHelpFragment extends FinalFragment implements OnClickListener {

	private ImageView healthCard;// 健康卡包

	private Button call_Nurse;// 我要医护帮

	private MyViewPager viewPager;

	private LinearLayout point_layout;// 圆点layout

	private TextView title_tv, left_btn;
	private ArrayList<GridView> viewList;
	private int currIndex;// 当前页卡编号
	private int oldIndex;// 上一个页卡编号

	private View rootView;
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();

	}

	public static ENurseHelpFragment newInstance() {
		ENurseHelpFragment fragment = new ENurseHelpFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.enurse, null);// 缓存Fragment
			initView();
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initView() {
		call_Nurse = (Button) rootView.findViewById(R.id.call_Nurse);// 我要医护帮
		viewPager = (MyViewPager) rootView.findViewById(R.id.content_vPager);
		point_layout = (LinearLayout) rootView.findViewById(R.id.point_layout);

		title_tv = (TextView) rootView.findViewById(R.id.title_tv);
		title_tv.setText("e护帮");

		left_btn = (TextView) rootView.findViewById(R.id.left_btn);
		left_btn.setVisibility(View.GONE);

		healthCard = (ImageView) rootView.findViewById(R.id.right_btn1);
		healthCard.setVisibility(View.VISIBLE);
		healthCard.setImageResource(R.drawable.healthcard_icon);
		call_Nurse.setOnClickListener(this);
		healthCard.setOnClickListener(this);
		initViewPage();

	}

	public void onClick(View v) {
		Intent intent = new Intent();
		if (v.getId() == R.id.call_Nurse) {
			intent.setClass(activity, CallENurseActivity.class);
			startActivity(intent);
		} else {
			if (BaseApplication.patient == null) {
				((FinalActivity) getActivity()).HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			intent.setClass(activity, MyHealthCardActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 初始化tab
	 */
	public void initViewPage() {
		// viewList = new ArrayList<Fragment>();
		// Fragment fragment = BankListFragment.getInstance(0);
		// Fragment fragment1 = BankListFragment.getInstance(1);
		// viewList.add(fragment);
		// viewList.add(fragment1);

		initGridView();

		// 给ViewPager设置适配器
		// viewPager.setAdapter(new MyFragmentPagerAdapter(
		// ((FragmentActivity) activity).getSupportFragmentManager(),
		// viewList));
		viewPager.setAdapter(new ViewPagerAdapter(viewList));
		// viewPager.setOffscreenPageLimit(50);
		viewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		initPointLayout();
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				setOvalLayout(R.id.point_view, R.drawable.shape_point_focused,
						R.drawable.shape_point_normal2, position);
				// currIndex = position;
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 初始化gridView
	 */
	public void initGridView() {
		viewList = new ArrayList<GridView>();
		// 1
		MyGridView gridView = new MyGridView(getActivity());
		gridView.setNumColumns(3);
		gridView.setVerticalScrollBarEnabled(false);
		gridView.setGravity(Gravity.CENTER);
		gridView.setOnItemClickListener(new onGridViewListener());
		gridView.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
		viewList.add(gridView);
		// 2
		MyGridView gridView2 = new MyGridView(getActivity());
		gridView2.setNumColumns(3);
		gridView2.setVerticalScrollBarEnabled(false);
		gridView2.setGravity(Gravity.CENTER);
		gridView2.setOnItemClickListener(new onGridViewListener());
		gridView2.setSelector(getResources().getDrawable(
				R.drawable.selector_listitem_bg));
		viewList.add(gridView2);

		final List<String> bList = Arrays.asList(getResources().getStringArray(
				R.array.bank_list));
		TypedArray ta = getResources().obtainTypedArray(R.array.bank_list_icon);
		bankLength = bList.size();
		initData(0, gridView, ta, bList);
		initData(1, gridView2, ta, bList);
	}

	private List<String> bankList;
	private List<Integer> bankImgList;
	private int bankLength;

	public void initData(int index, GridView gridView, TypedArray ta,
			List<String> bList) {

		final List<Integer> bImgList = new ArrayList<Integer>();
		for (int i = 0; i < bankLength; i++) {
			bImgList.add(ta.getResourceId(i, 0));
		}
		initBankList(index, bList, bImgList, gridView);
	}

	/**
	 * 获取相应页银行
	 * 
	 * @param page
	 */
	public void initBankList(int page, List<String> blist,
			List<Integer> bImgList, GridView gridView) {
		bankList = blist.subList(page * 6,
				(page * 6 + 6) <= bankLength ? (page * 6 + 6) : bankLength);
		bankImgList = bImgList.subList(page * 6,
				(page * 6 + 6) <= bankLength ? (page * 6 + 6) : bankLength);
		BankListAdapter bAdapter = new BankListAdapter(getActivity()
				.getApplicationContext(), bankList, bankImgList);
		gridView.setAdapter(bAdapter);
	}

	/**
	 * @title gridView点击事件
	 * @describe
	 * @author ezt
	 * @created 2015年7月14日
	 */
	private class onGridViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String bName = "";
			try {
				bName = viewList.get(currIndex).getAdapter().getItem(position)
						.toString();
			} catch (Exception e) {
				bName = "";
			}
			Intent intent = new Intent(getActivity().getApplicationContext(),
					CCBUserActivity.class);
			intent.putExtra("bankName", bName);
			startActivity(intent);
		}

	}

	/**
	 * 初始化滚动圆点
	 */
	public void initPointLayout() {
		if (viewList.size() > 1) {
			point_layout.setVisibility(View.VISIBLE);
		} else {
			point_layout.setVisibility(View.GONE);
		}
		LayoutInflater inflater = LayoutInflater.from(activity
				.getApplicationContext());
		for (int i = 0; i < viewList.size(); i++) {
			point_layout.addView(inflater.inflate(R.layout.item_bottom_point,
					null));
			point_layout.getChildAt(i).findViewById(R.id.point_view)
					.setBackgroundResource(R.drawable.shape_point_normal2);
		}
		if (point_layout != null) {
			// 选中第一个
			View view = point_layout.getChildAt(0);
			View view1 = view.findViewById(R.id.point_view);
			view1.setBackgroundResource(R.drawable.shape_point_focused);//
			// 设置圆点
			setOvalLayout(R.id.point_view, R.drawable.shape_point_focused,
					R.drawable.shape_point_normal2, 0);
		}
	}

	// 设置圆点
	private void setOvalLayout(final int ovalLayoutItemId, final int focusedId,
			final int normalId, int position) {

		currIndex = position % viewList.size();
		// 取消圆点选中
		point_layout.getChildAt(oldIndex).findViewById(ovalLayoutItemId)
				.setBackgroundResource(normalId);
		// 圆点选中
		point_layout.getChildAt(currIndex).findViewById(ovalLayoutItemId)
				.setBackgroundResource(focusedId);
		oldIndex = currIndex;

	}

	@Override
	public void onDestroy() {
		if (viewList != null) {
			viewList.clear();
			viewList = null;
		}
		if (viewPager != null) {
			viewPager.removeAllViews();
			viewPager = null;
		}
		super.onDestroy();
	}
}

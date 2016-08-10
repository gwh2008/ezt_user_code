package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomDetailActivity;
import com.eztcn.user.eztcn.activity.fdoc.SymptomSelfActivity;
import com.eztcn.user.eztcn.adapter.PartListAdapter;
import com.eztcn.user.eztcn.adapter.SymptomListAdapter;
import com.eztcn.user.eztcn.bean.Symptom;
import com.eztcn.user.eztcn.customView.PersonPartView;
import com.eztcn.user.eztcn.customView.PersonPartView.onPartClickListener;
import com.eztcn.user.eztcn.db.SymptomSelfDb;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 人体图
 * @describe
 * @author ezt
 * @created 2015年4月20日
 */
public class Symptom_BodyFragment extends FinalFragment implements OnClickListener,
		OnItemClickListener, onPartClickListener, DrawerListener {

	private Activity mActivity;

	private PersonPartView manFrontView;// 自定义人体view
	private TextView tvSex;// 切换性别
	private TextView tvPos;// 正反面
	public DrawerLayout drawLayout;
	public LinearLayout hideLayout;// 左侧隐藏布局
	private ListView partLv;// 部位列表
	private ListView symptomLv;// 症状列表
	private View view;

	// 左侧滑菜单
	private PartListAdapter partAdapter;// 部位
	private SymptomListAdapter symptomAdapter;// 症状
	private ArrayList<Symptom> list;

	public static Symptom_BodyFragment newInstance() {
		Symptom_BodyFragment fragment = new Symptom_BodyFragment();
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
			view = inflater.inflate(R.layout.activity_symptom_self, null);
			initialView();
		}

		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		return view;
	}

	/**
	 * 初始化ui
	 */
	private void initialView() {
		manFrontView = (PersonPartView) view.findViewById(R.id.person_view);// 自定义人体view
		tvSex = (TextView) view.findViewById(R.id.person_sex_bt);// 切换性别
		tvSex.setOnClickListener(this);
		tvPos = (TextView) view.findViewById(R.id.person_position_tv);// 正反面
		tvPos.setOnClickListener(this);
		drawLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		hideLayout = (LinearLayout) view.findViewById(R.id.hide_layout);// 左侧隐藏布局

		partLv = (ListView) view.findViewById(R.id.hide_part_lv);// 部位列表
		partLv.setOnItemClickListener(this);
		symptomLv = (ListView) view.findViewById(R.id.hide_Symptom_lv);// 症状列表
		symptomLv.setOnItemClickListener(this);

//		drawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // 左侧滑动，关闭手势滑动
		drawLayout.setDrawerListener(this);

		// setDrawerListener

		partAdapter = new PartListAdapter(mActivity);
		partLv.setAdapter(partAdapter);

		symptomAdapter = new SymptomListAdapter(mActivity, true);
		symptomLv.setAdapter(symptomAdapter);

		// 部位数据
		String[] parts = getResources().getStringArray(R.array.parts);
		partAdapter.setList(parts);

		manFrontView.onClickPart(this);

	}

	/**
	 * 根据部位获取症状列表
	 */
	private String newPart;

	private void initialSymptomData(String part) {
		if (part.equals("背部")) {
			part = "皮肤";
		} else if (part.equals("臀部")) {
			part = "排泄";
		} else if (part.equals("生殖部位")) {
			if (manFrontView.getSexType() == 0) {// 男
				part = "男性股沟";
			} else {// 女
				part = "女性盆骨";
			}
		} else if (part.equals("全身症状")) {
			part = "全身";
		}
		newPart = part;

		FinalActivity.getInstance().showProgressToast();
		new Thread(new Runnable() {

			@Override
			public void run() {
				list = new SymptomSelfDb(mActivity)
						.getSymptomListFromPart(newPart);

				handler.sendEmptyMessage(1);
			}
		}).start();

		// if (BaseApplication.getInstance().isNetConnected) {
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("illPlace", part);
		// new SymptomQueryImpl().getSymptomListOfPart(params, this);
		// showProgressToast();
		// } else {
		// Toast.makeText(mContext, getString(R.string.network_hint),
		// Toast.LENGTH_SHORT).show();
		// }

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			symptomAdapter.setList(list);
			FinalActivity.getInstance().hideProgressToast();
			symptomLv.setSelection(0);
		}

	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// if (drawLayout.isDrawerOpen(hideLayout)) {
		// drawLayout.closeDrawer(hideLayout);
		// }

		case R.id.person_sex_bt:// 切换性别

			if ("男".equals(tvSex.getText().toString())) {// 切换到女性
				tvSex.setText("女");
				Drawable rightDrawable = getResources().getDrawable(
						R.drawable.selector_woman_btn_bg);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				tvSex.setCompoundDrawables(null, rightDrawable, null, null);
				tvSex.setCompoundDrawablePadding(ResourceUtils.dip2px(
						mActivity, ResourceUtils.getXmlDef(mActivity,
								R.dimen.medium_margin)));

				manFrontView.setSexType(1);
				manFrontView.invalidate();
			} else {// 切换到男
				tvSex.setText("男");
				Drawable rightDrawable = getResources().getDrawable(
						R.drawable.selector_man_btn_bg);
				rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight());
				tvSex.setCompoundDrawables(null, rightDrawable, null, null);
				tvSex.setCompoundDrawablePadding(ResourceUtils.dip2px(
						mActivity, ResourceUtils.getXmlDef(mActivity,
								R.dimen.medium_margin)));
				manFrontView.setSexType(0);
				manFrontView.invalidate();
			}
			break;

		case R.id.person_position_tv:// 切换正反面
			if (manFrontView.getPosition() == 0) {
				manFrontView.setPosition(1);// 反
				tvPos.setText("反面");
			} else {
				manFrontView.setPosition(0);// 正
				tvPos.setText("正面");
			}
			manFrontView.invalidate();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		switch (parent.getId()) {
		case R.id.hide_part_lv:// 部位
			partAdapter.setSelectedPosition(position);
			partAdapter.notifyDataSetChanged();

			initialSymptomData(partAdapter.getList().get(position));
			break;

		case R.id.hide_Symptom_lv:// 症状
			startActivity(new Intent(mActivity, SymptomDetailActivity.class)
					.putExtra("symptom", symptomAdapter.getList().get(position)));
			break;

		}

	}

	@Override
	public void onPartClick(int part) {
		int pos = 0;
		switch (part) {
		case 1:// 头部
			pos = 1;
			break;

		case 2:// 颈部
			pos = 2;
			break;

		case 3:// 手部
			pos = 5;
			break;

		case 4:
			if (manFrontView.getPosition() == 0) {// 胸部
				pos = 3;
			} else {// 背部
				pos = 9;
			}

			break;

		case 5:
			if (manFrontView.getPosition() == 0) {// 腹部
				pos = 4;
			} else {// 腰部
				pos = 8;
			}

			break;

		case 6:// 臀部
			if (manFrontView.getPosition() == 0) {// 正/"生殖器官"
				pos = 6;
			} else {// 反/"臀部"
				pos = 10;
			}
			break;

		case 7:// 下肢
			pos = 7;
			break;
		}
		setDrawerOpen();
		partAdapter.setSelectedPosition(pos);
		partAdapter.notifyDataSetChanged();
		initialSymptomData(partAdapter.getList().get(pos));
	}

	/**
	 * 打开/关闭左侧列表
	 */
	private void setDrawerOpen() {
		if (drawLayout.isDrawerOpen(hideLayout)) {
			drawLayout.closeDrawer(hideLayout);
		} else {
			drawLayout.openDrawer(hideLayout);
		}
	}

	/** 
     * 当一个抽屉完全关闭的时候调用此方法 
     */ 
	@Override
	public void onDrawerClosed(View arg0) {
		((SymptomSelfActivity) mActivity).pager.setNoScroll(false);
	}

	/** 
     * 当一个抽屉被完全打开的时候被调用 
     */  
	@Override
	public void onDrawerOpened(View arg0) {
		((SymptomSelfActivity) mActivity).pager.setNoScroll(true);
	}

	/**
	 * 当抽屉被滑动的时候调用此方法 arg1 表示 滑动的幅度（0-1）
	 */
	@Override
	public void onDrawerSlide(View arg0, float arg1) {

	}

	/**
	 * 当抽屉滑动状态改变的时候被调用 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）,
	 * STATE_SETTLING（固定--2）中之一。
	 * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
	 */
	@Override
	public void onDrawerStateChanged(int arg0) {

	}

}
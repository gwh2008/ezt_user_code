package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.CCBUserActivity;
import com.eztcn.user.eztcn.adapter.BankListAdapter;
import com.eztcn.user.eztcn.customView.MyGridView;

/**
 * @title 银行列表
 * @describe
 * @author ezt
 * @created 2015年6月16日
 */
public class BankListFragment extends FinalFragment implements OnItemClickListener {

	private MyGridView gridView;
	private BankListAdapter bAdapter;
	private int page;

	private static BankListFragment fragment;
	private List<String> bankList;
	private List<Integer> bankImgList;
	private int bankLength;

	public static Fragment getInstance(int page) {
		BankListFragment fragment = new BankListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("page", page);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (gridView == null) {
			gridView = new MyGridView(getActivity());
			gridView.setNumColumns(3);
			gridView.setVerticalScrollBarEnabled(false);
			gridView.setGravity(Gravity.CENTER);
			gridView.setOnItemClickListener(this);
			gridView.setSelector(getResources().getDrawable(
					R.drawable.selector_listitem_bg));
		}
		ViewGroup parent = (ViewGroup) gridView.getParent();
		if (parent != null) {
			parent.removeView(gridView);
		}
		return gridView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedInstanceState = getArguments();
		if (savedInstanceState != null) {
			page = savedInstanceState.getInt("page");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initData();

	}

	public void initData() {
		final List<String> bList = Arrays.asList(getResources().getStringArray(
				R.array.bank_list));
		TypedArray ta = getResources().obtainTypedArray(R.array.bank_list_icon);
		bankLength = bList.size();
		final List<Integer> bImgList = new ArrayList<Integer>();
		for (int i = 0; i < bankLength; i++) {
			bImgList.add(ta.getResourceId(i, 0));
		}
		initBankList(page, bList, bImgList);
	}

	/**
	 * 获取相应页银行
	 * 
	 * @param page
	 */
	public void initBankList(int page, List<String> blist,
			List<Integer> bImgList) {
		bankList = blist.subList(page * 6,
				(page * 6 + 6) <= bankLength ? (page * 6 + 6) : bankLength);
		bankImgList = bImgList.subList(page * 6,
				(page * 6 + 6) <= bankLength ? (page * 6 + 6) : bankLength);
		bAdapter = new BankListAdapter(getActivity().getApplicationContext(),
				bankList, bankImgList);
		gridView.setAdapter(bAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity().getApplicationContext(),
				CCBUserActivity.class);
		intent.putExtra("bankName", bankList.get(position));
		startActivity(intent);
	}
}

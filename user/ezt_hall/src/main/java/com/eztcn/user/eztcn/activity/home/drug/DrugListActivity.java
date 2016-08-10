/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.drug;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.Pool;
import com.eztcn.user.eztcn.bean.PoolTimes;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;

/**
 * @author Liu Gang
 * 
 *         2016年3月29日 上午10:31:29 预约药品列表
 */
public class DrugListActivity extends FinalActivity implements IHttpResult {
	@ViewInject(R.id.drugLv)
	private ListView drugLv;
	private DrugListAdapter adapter;
	/**
	 * 所有医院的号池
	 */
	private HashMap<Integer, ArrayList<Pool>> hosPoolsMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_druglist);
		ViewUtils.inject(DrugListActivity.this);
		loadTitleBar(true, "预约药品", null);
		hosPoolsMap = new HashMap<Integer, ArrayList<Pool>>();
		adapter = new DrugListAdapter(DrugListActivity.this);
		drugLv.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 获取一中心医院号池数据
		getPools("1469", "6364", HttpParams.GET_DOC_POOL_YZX);
		// 获取人民医院号池数据
		getPools("6089", "8345", HttpParams.GET_DOC_POOL_GRYY);
	}

	private class DrugListAdapter extends BaseArrayListAdapter<Hospital> {
		private LayoutInflater inflater;

		public DrugListAdapter(Activity context) {
			super(context);
			inflater = LayoutInflater.from(context);
		}

		Holder holder;

		class Holder {
			ImageView hosImg;
			TextView hosNameTv;
			TextView hosLevelTv;
			TextView hosAddrTv;
			TextView regDrugTv;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.item_drug, null);
				holder.hosImg = (ImageView) convertView
						.findViewById(R.id.hosImgV);
				holder.hosNameTv = (TextView) convertView
						.findViewById(R.id.hosNameTv);
				holder.hosLevelTv = (TextView) convertView
						.findViewById(R.id.hosLevelTv);
				holder.hosAddrTv = (TextView) convertView
						.findViewById(R.id.hosAddressTv);
				holder.regDrugTv = (TextView) convertView
						.findViewById(R.id.regDrugTv);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			Hospital hospital = mList.get(position);
			holder.hosNameTv.setText(hospital.gethName());
			holder.hosAddrTv.setText(hospital.gethAddress());
			List<Pool> poolList = hosPoolsMap.get(position);
			switch (position) {
			case 0: {
				holder.hosLevelTv.setText("三甲医院		综合医院");
				holder.hosImg.setImageResource(R.drawable.hosyzx);
				if (null != poolList && poolList.size() > 0) {
					setVisable(holder.regDrugTv, position);
				} else {
					setNot(holder.regDrugTv, position);
				}
			}
				break;
			case 1: {
				holder.hosLevelTv.setText("三甲医院		综合医院");
				holder.hosImg.setImageResource(R.drawable.hosrg);
				if (null != poolList && poolList.size() > 0) {
					setVisable(holder.regDrugTv, position);
				} else {
					setNot(holder.regDrugTv, position);
				}
			}
				break;
			}

			return convertView;
		}

	}

	/***
	 * 设置预约按钮为灰底灰字
	 * 
	 * @param regView
	 * @param position
	 */
	@SuppressLint("NewApi")
	private void setNot(TextView regView, int position) {

		if (Build.VERSION.SDK_INT >= 16) {
			regView.setBackground(mContext.getResources().getDrawable(
					R.drawable.conners_grey));
		} else {
			regView.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.conners_grey));
		}
//		regView.setTextColor(mContext.getResources().getColor(
//				R.color.dark_gray2));
		regView.setTextColor(getResources().getColor(android.R.color.white));
		regView.setText("约满");
	}

	/***
	 * 设置预约按钮为蓝底白字
	 * 
	 * @param regView
	 * @param position
	 */
	@SuppressLint("NewApi")
	private void setVisable(TextView regView, int position) {

		if (Build.VERSION.SDK_INT >= 16) {
			regView.setBackground(mContext.getResources().getDrawable(
					R.drawable.conners_green));
		} else {
			regView.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.conners_green));
		}
		regView.setTextColor(mContext.getResources().getColor(
				android.R.color.white));
		regView.setText("预约");
	}

	/**
	 * 获取号池资料
	 * 
	 * @param deptid
	 * @param doctorid
	 * @param whichhos
	 */
	private void getPools(String deptid, String doctorid, int whichhos) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("deptid", deptid);
		params.addBodyParameter("doctorid", doctorid);
		params.addBodyParameter("isExist", 0 + "");// 默认查询预约未满的号池信息
		HospitalImpl impl = new HospitalImpl();
		impl.getDocPool(params, this, whichhos);
	}

	private int resultTime = 0;

	@Override
	public void result(Object... object) {

		if (object == null) {
			hideProgressToast();
			return;
		}
		int type = (Integer) object[0];
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GET_DOC_POOL_YZX:// 获取一中心号池
			if (isSucc) {// 成功
				ArrayList<Pool> poolist = (ArrayList<Pool>) object[2];
				dealWithPoolData(poolist, 0);
				resultTime++;
			}
			break;

		case HttpParams.GET_DOC_POOL_GRYY:// 获取天津市工人医院号池
			if (isSucc) {// 成功
				ArrayList<Pool> poolist = (ArrayList<Pool>) object[2];
				dealWithPoolData(poolist, 1);
				resultTime++;
			}
			break;
		}
		if (resultTime == 2) {
			List<Hospital> list = new ArrayList<Hospital>();
			Iterator<Integer> it = hosPoolsMap.keySet().iterator();
			while (it.hasNext()) {
				int position = it.next();
				List<Pool> pools = hosPoolsMap.get(position);

			}
			Hospital hospital = new Hospital();
			hospital.sethName("天津第一中心医院");
			hospital.sethAddress("天津市南开区复康路24号");
			Hospital hospital1 = new Hospital();
			hospital1.sethName("天津市工人医院");
			hospital1.sethAddress("天津市河东区新开路55号");
			list.add(hospital);
			list.add(hospital1);

			adapter.setList(list);
		}
	}

	/**
	 * 处理号池信息
	 */
	private void dealWithPoolData(ArrayList<Pool> poolist, int position) {
		List<Pool> lastPools = new ArrayList<Pool>();
		if (null != poolist && poolist.size() > 0) {
			for (int i = 0; i < poolist.size(); i++) {
				// 从号池里获取时间列表
				List<PoolTimes> ptList = poolist.get(i).getTimeList();
				if (ptList == null || ptList.size() == 0) {// 时间表为null
					continue;
				}
				for (int j = 0; j < ptList.size(); j++) {
					if (!ptList.get(j).isRemains()) {
						continue;
					} else {
						Pool tempPool = poolist.get(i);
						tempPool.setRemain(1);
						lastPools.add(tempPool);
						break;
					}
				}
			}
		}
		hosPoolsMap.put(position, (ArrayList<Pool>) lastPools);
	}

	@OnItemClick(R.id.drugLv)
	public void rugLvItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		List<Pool> list = hosPoolsMap.get(position);
		if (null != list && list.size() > 0) {
			if (null == BaseApplication.patient) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			Intent intent = new Intent(DrugListActivity.this,
					DrugWriteActivity.class);
            intent.putExtra("poolList",(Serializable)list);
		switch(position){
		case 0:{intent.putExtra("deptStr", "院内制剂");}break;
		case 1:{intent.putExtra("deptStr", "舒筋节利门诊");}break;
		}
			intent.putExtra("hospital", adapter.mList.get(position));
			startActivity(intent);
		} else {
			toast("暂无可预约时间", Toast.LENGTH_SHORT);
		}
	}


//	@Override
//	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//		super.onSaveInstanceState(outState, outPersistentState);
//	}
}

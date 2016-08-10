/**
 * 
 */
package com.eztcn.user.eztcn.activity.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.bitmap.BitmapDisplayConfig;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalDetailActivity;
import com.eztcn.user.eztcn.activity.fdoc.HospitalIntroActivity;
import com.eztcn.user.eztcn.adapter.HosHomeAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.Function;
import com.eztcn.user.eztcn.bean.Hospital;
import com.eztcn.user.eztcn.bean.compent.IntentParams;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.controller.HospitalManager;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.impl.HospitalImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.PicUtils;
import com.eztcn.user.eztcn.utils.ScreenUtils;

/**
 * @author Liu Gang
 * 
 *         2016年3月3日 下午2:27:51 医院首页
 */
public class HosHomeActivity extends FinalActivity implements IHttpResult {
	@ViewInject(R.id.hosFunGridView)
	private GridView hosFunGridView;
	private HosHomeAdapter hosFunAdapter;
	private List<Function> hosFunList;
	private String hosNameStr;
	private String hosId;
	@ViewInject(R.id.deptsTv)
	private View deptsTv;
	@ViewInject(R.id.docsTv)
	private TextView docsTv;
	@ViewInject(R.id.hosIntroTv)
	private View hosIntroTv;
	private Hospital hospital;
	// private final int REG_POS = 0, DAY_REG_POS = 1;
	@ViewInject(R.id.hosLevel)
	private TextView hosLevelTv;
	@ViewInject(R.id.hosType)
	private TextView hosTypeTv;
	@ViewInject(R.id.hosAddr)
	private TextView hosAddrTv;
	@ViewInject(R.id.hosShowView)
	private ImageView hosShowView;
	/**
	 * 服务器的功能列表
	 */
	private List<Function> serFuns;
	private String moreStr = "查看更多";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(HosHomeActivity.this);
		setContentView(R.layout.activity_hoshome);
		savedInstanceState = getIntent().getExtras();
		if (savedInstanceState != null) {
			hospital = (Hospital) savedInstanceState
					.getSerializable("hospital");
			hosId = hospital.getId() + "";
		}



		hosFunAdapter = new HosHomeAdapter(mContext);
		hosFunGridView.setAdapter(hosFunAdapter);
		setData();
		gainServerData();
	}

	private void gainServerData() {
		showProgressToast();
		HospitalImpl hospitalImpl = new HospitalImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("hospitalId", hosId);
		hospitalImpl.getHosConfig(params, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getHospitalDetail();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Object bitmapObj = msg.obj;
			Bitmap bm;
			if (null == bitmapObj) {
				bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.homeloading);

			} else {
				bm = (Bitmap) bitmapObj;

			}

			Bitmap bitmapr = PicUtils.zoomImgH(bm,
					(int) (ScreenUtils.gainDM(mContext).heightPixels * 0.26));
			// hosShowView.getLayoutParams().height=(int)
			// (ScreenUtils.gainDM(mContext).heightPixels*0.26);
			hosShowView.setImageBitmap(bitmapr);
		};
	};

	/**
	 * 设置数据
	 */
	private void setData() {
		// SystemPreferences.save(EZTConfig.KEY_HOS_NAME, hosName);
		// SystemPreferences.save(EZTConfig.KEY_HOS_ID, hosId);
		// SystemPreferences.save(EZTConfig.KEY_SELECT_HOS_POS,
		// selectHosPos);
		// SystemPreferences.save(EZTConfig.KEY_STR_CITY, btCity.getText()
		// .toString());
		// SystemPreferences.save(EZTConfig.KEY_CITY_ID, city_id);
		// SystemPreferences.save(EZTConfig.KEY_SELECT_N_POS, selectNPos);
		// SystemPreferences.save(EZTConfig.KEY_STR_N, strN);
		// SystemPreferences.save(EZTConfig.KEY_SELECT_AREA_POS,
		// selectAreaPos);
		// hosNameStr = SystemPreferences.getString(EZTConfig.KEY_HOS_NAME);
		hosNameStr = hospital.gethName();
		hosId = SystemPreferences.getString(EZTConfig.KEY_HOS_ID);
		if (hosNameStr.length() > 10) {
				loadTitleBar(true, hosNameStr.substring(0, 9) + "...", null);
		} 
		else
		loadTitleBar(true, hosNameStr, null);
		if (null != hospital.getHosLevel()) {
			if(hospital.getHosLevel().equals("380"))
			{
				hosLevelTv.setText("三甲医院");
				hosLevelTv.setVisibility(View.VISIBLE);;
			}
			else
			{
				hosLevelTv.setVisibility(View.INVISIBLE);;
			}
			
			
			hosLevelTv.setBackgroundResource(R.drawable.selector_main_btn_bg);
			
		} else
			hosLevelTv.setVisibility(View.INVISIBLE);;
		hosAddrTv.setText(hospital.gethAddress());

		final String photoUri = EZTConfig.HOS_PHOTO + "hosView"
				+ hospital.getId() + ".jpg";

		// BitmapUtils bitmapUtils=new BitmapUtils(HosHomeActivity.this);

		// bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		// bitmapUtils.configDefaultLoadingImage(defaultBit);
		// bitmapUtils.configDefaultLoadFailedImage(defaultBit);
		// bitmapUtils.display(hosShowView, photoUri);

		// File bitmapFile=bitmapUtils.getBitmapFileFromDiskCache(photoUri);

		new Thread() {

			@Override
			public void run() {
				// 定义一个URL对象
				URL url;
				InputStream is = null;
				try {
					url = new URL(photoUri);
					// 打开该URL的资源输入流
					is = url.openStream();
					// 从InputStream中解析出图片
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
					// 发送消息

				} catch (MalformedURLException e) {
					e.printStackTrace();
					Bitmap bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.homeloading);
					Message msg = handler.obtainMessage();
					msg.obj = bm;
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					Bitmap bm = BitmapFactory.decodeResource(getResources(),
							R.drawable.homeloading);
					Message msg = handler.obtainMessage();
					msg.obj = bm;
					handler.sendMessage(msg);
				} finally {
					try {
						if (null != is)
							is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}.start();

	}

	private void getHospitalDetail() {

		RequestParams params = new RequestParams();

		params.addBodyParameter("hospitalId",
				null != hospital ? String.valueOf(hospital.getId()) : hosId);

		new HospitalImpl().getHospitalDetail(params, this);
		showProgressToast();
	}

	/**
	 * 设置预约挂号、当日挂号数据
	 * 
	 * @param function
	 * 
	 */
	private Function setRegJumpData(Function function) {
		List<IntentParams> intentParamList = new ArrayList<IntentParams>();
		IntentParams params = new IntentParams("isNearHos", false);
		IntentParams params1 = new IntentParams("hosId", hosId);
		IntentParams params2 = new IntentParams("hosName", hosNameStr);
		IntentParams params3 = new IntentParams("hospital", hospital);
		intentParamList.add(params2);
		intentParamList.add(params1);
		intentParamList.add(params);
		intentParamList.add(params3);
		function.setIntentParamList(intentParamList);
		function.setJumpLink("com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity");
		return function;
	}

	private void initGridFunction() {
		for (int i = 0; i < serFuns.size(); i++) {
			Function function = serFuns.get(i);
			if (function.getName().equals(HospitalManager.functionStrs[6])
					|| function.getName().equals(
							HospitalManager.functionStrs[7])) {
				setRegJumpData(function);// 预约挂号和当日挂号跳转参数设置
			}
			if (function.getName().equals(HospitalManager.functionStrs[13])) {// 预约检查参数配置
				List<IntentParams> intentParamList = new ArrayList<IntentParams>();
				IntentParams params = new IntentParams("hospital", hospital);
				intentParamList.add(params);
				function.setIntentParamList(intentParamList);
				function.setJumpLink("com.eztcn.user.eztcn.activity.home.ordercheck.ChoiceCheckItemActivity");
			}
			if (function.getName().equals(HospitalManager.functionStrs[8])) {
				// 大牌名医参数配置
				List<IntentParams> intentParamList = new ArrayList<IntentParams>();
				IntentParams params = new IntentParams("hosId", hosId);
				IntentParams params1 = new IntentParams("hospital", hospital);
				intentParamList.add(params);
				intentParamList.add(params1);
				function.setIntentParamList(intentParamList);
				function.setJumpLink("com.eztcn.user.eztcn.activity.home.BigDoctorList30Activity");
			}
		}

	}

	@OnItemClick(R.id.hosFunGridView)
	public void gridItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Function function = hosFunList.get(position);
		if (function.getIsOpen() == 1) {
			if (null != function.getJumpLink()) {
				try {
					Intent intent = new Intent(HosHomeActivity.this,
							Class.forName(function.getJumpLink()));
					List<IntentParams> params = function.getIntentParamList();
					if (function.getName().equals(moreStr)) {
                        intent.putExtra("functionList",(Serializable)serFuns);
					} else if (null != params && params.size() > 0) {// 设置参数
						for (int i = 0; i < params.size(); i++) {
							IntentParams param = params.get(i);
							Object obj = param.getValue();
							if (obj instanceof Boolean) {
								intent.putExtra(param.getKey(),
										(Boolean) param.getValue());
							} else if (obj instanceof String) {
								intent.putExtra(param.getKey(),
										String.valueOf(obj));
							} else if (obj instanceof Hospital) {
								Hospital hos = (Hospital) obj;
								intent.putExtra(param.getKey(), hos);
							}
						}
					}
					if (function.getName().equals(
							HospitalManager.functionStrs[6])) {// 预约挂号
						SystemPreferences.save(EZTConfig.KEY_DF_IS_DAY_REG,
								false);
					} else if (function.getName().equals(
							HospitalManager.functionStrs[7])) {// 当日挂号
						SystemPreferences.save(EZTConfig.KEY_DF_IS_DAY_REG,
								true);
					}
					startActivity(intent);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} else {
			toast(getString(R.string.function_hint), Toast.LENGTH_SHORT);
		}
	}

	// private class HosHomeAdapter extends BaseArrayListAdapter<Function> {
	// private Holder holder;
	// private Context context;
	//
	// private class Holder {
	// TextView textView;
	// }
	//
	// public HosHomeAdapter(Activity context) {
	// super(context);
	// this.context = context;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// if (null == convertView) {
	// holder = new Holder();
	// convertView = LayoutInflater.from(context).inflate(
	// R.layout.item_fun, null);
	// holder.textView = (TextView) convertView.findViewById(R.id.tv);
	// convertView.setTag(holder);
	// } else {
	// holder = (Holder) convertView.getTag();
	// }
	// String text = mList.get(position).getName();
	// if (!TextUtils.isEmpty(text))
	// holder.textView.setText(text);
	// return convertView;
	// }
	//
	// }

	@OnClick(R.id.deptsTv)
	// 科室列表
	public void deptsClick(View view) {
		Intent intent = new Intent(HosHomeActivity.this,
				ChoiceDeptByHosActivity.class);
		intent.putExtra("isNearHos", false);
		intent.putExtra("hosId", hosId);
		intent.putExtra("hosName", hosNameStr);
		intent.putExtra("hospital", hospital);
		startActivity(intent);
	}

	@OnClick(R.id.docsTv)
	// 医生列表
	public void docsClick(View view) {
		Intent intent = new Intent(mContext, DoctorList30Activity.class);
		intent.putExtra("hospital", hospital);
		startActivity(intent.putExtra("type", 1).putExtra("deptName", "")

		.putExtra("hosId", hospital.getId() + "").putExtra("deptId", "")

		.putExtra("isAllSearch", true).putExtra("hosName", hospital.gethName()));
	}

	@OnClick(R.id.hosIntroTv)
	// 医院简介
	public void hosClick(View view) {
		startActivity(new Intent(this, HospitalIntroActivity.class).putExtra(
				"hospital", hospital));
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer type = (Integer) object[0];
		if (type == null) {
			return;
		}
		if (object[1] == null) {
			return;
		}
		boolean isSucc = (Boolean) object[1];
		switch (type) {
		case HttpParams.GAIN_HOS_CONFIG:// 获取医院列表
			Map map = (Map) object[2];
			if (isSucc) {
				serFuns = (List<Function>) map.get("functionList");
				initGridFunction();

				this.hosFunList = new ArrayList<Function>();
				if (serFuns.size() < 9) {
					for (int i = 0; i < serFuns.size(); i++) {
						Function function = serFuns.get(i);
						this.hosFunList.add(function);
					}
				} else {// 9个功能时候出第二页面
					for (int i = 0; i < 7; i++) {
						Function function = serFuns.get(i);
						this.hosFunList.add(function);
					}
					Function function = new Function();
					function.setIsOpen(1);
					function.setDrawableId(R.drawable.hh_gd);
					function.setName(moreStr);
					// List<IntentParams> intentParamList = new
					// ArrayList<IntentParams>();
					// IntentParams params = new IntentParams("functionList",
					// serFuns);
					// intentParamList.add(params);
					// function.setIntentParamList(intentParamList);
					function.setJumpLink("com.eztcn.user.eztcn.activity.home.HosHomeMoreActivity");
					hosFunList.add(function);
				}
				hosFunAdapter.setList(hosFunList);
			}
			break;

		case HttpParams.GET_HOS_DETAIL:// 获取医院详情
			if (isSucc) {
				Map<String, Object> detailMap = (Map<String, Object>) object[2];
				if (detailMap == null || detailMap.size() == 0) {
					return;
				}
				boolean flag=false;
				if(detailMap.get("flag")!=null){
					flag = (Boolean) detailMap.get("flag");
				}
				if (flag) {
					if(detailMap!=null){
						hospital = (Hospital) detailMap.get("hospital");
					}
						if(null!=hospital&&null!=hospital.getHosLevel()){
							if(hospital.getHosLevel().equals("380"))
							{
								hosLevelTv.setText("三甲医院");
								hosLevelTv.setVisibility(View.VISIBLE);;
							}
							else
							{
								hosLevelTv.setVisibility(View.INVISIBLE);;
							}
						}else{
							hosLevelTv.setVisibility(View.INVISIBLE);;
						}
//					hosLevelTv
//							.setText(hospital.getHosLevel().equals("380") ? "三级甲等"
//									: "");
					hosAddrTv.setText(hospital.gethAddress());
				} else {
					Toast.makeText(mContext, detailMap.get("msg").toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}

	}

	@Override
	protected void onDestroy() {
		hideProgressToast();
		super.onDestroy();
	}
}

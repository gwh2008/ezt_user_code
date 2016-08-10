package com.eztcn.user.eztcn.fragment;

import java.util.Map;
import xutils.http.RequestParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.dragoncard.ActivityDragonCard;
import com.eztcn.user.eztcn.activity.home.dragoncard.DragonToActive30Activity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.HttpParams;

public class DiscoverFragmentNew30 extends FinalFragment implements
		OnClickListener, IHttpResult {

	private Activity mActivity;
	private View rootView;// 缓存Fragment view
	private ImageView long_card_icon, discovery_drug, discovery_union,
			discovery_instrument_mall;
	private DragonCard card;

	public static DiscoverFragmentNew30 newInstance() {
		DiscoverFragmentNew30 fragment = new DiscoverFragmentNew30();
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_discover_new, null);
			WindowManager manager = (WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
			// qrCodeWidth = (int) (dm.widthPixels * 0.7);
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		// setTitleBar(rootView);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		WindowManager manager = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		initView(rootView, dm);
	}

	private void getHealthDragonInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		// HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",
				String.valueOf(BaseApplication.patient.getUserId()));
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.judgeDragonBind(params, this);
		((FinalActivity) mActivity).showProgressToast();
	}

	private void initView(View view, DisplayMetrics dm) {
		long_card_icon = (ImageView) view.findViewById(R.id.long_card_icon);
		discovery_drug = (ImageView) view.findViewById(R.id.discovery_drug);
		discovery_union = (ImageView) view.findViewById(R.id.discovery_union);
		discovery_instrument_mall = (ImageView) view
				.findViewById(R.id.discovery_instrument_mall);
		long_card_icon.setOnClickListener(this);
		discovery_drug.setOnClickListener(this);
		discovery_union.setOnClickListener(this);
		discovery_instrument_mall.setOnClickListener(this);

	}

	private void jumToDragonCardInfo() {
		Intent intent = new Intent();
		intent.setClass(mActivity, ActivityDragonCard.class);
		startActivity(intent);
	}

	private void jumpToDragonActivie() {
		Intent intent = new Intent();
		intent.setClass(mActivity, DragonToActive30Activity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.long_card_icon: {
			if (null == BaseApplication.patient) {
				((FinalActivity) mActivity).HintToLogin(0);
			} else {
				// getHealthDragonInfo();
				Intent intent = new Intent(mActivity, ActivityDragonCard.class);
				startActivity(intent);
			}
		}
			break;
		case R.id.discovery_drug:
			Toast.makeText(mActivity, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.discovery_union:
			Toast.makeText(mActivity, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.discovery_instrument_mall:
			Toast.makeText(mActivity, getString(R.string.function_hint),
					Toast.LENGTH_SHORT).show();
			break;
		}

	}

	@Override
	public void result(Object... object) {
		if (object == null) {
			return;
		}
		int callBackFlag = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (callBackFlag) {
		case HttpParams.GET_CCB_INFO_BY_UID: {
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("flag")) {
					if ((Boolean) map.get("flag")) {
						// 绑定卡了
						if (map.containsKey("data")) {
							card = (DragonCard) map.get("data");
							jumToDragonCardInfo();
						}
					} else {
						// 用户信息不存在 未绑定卡
						card = null;
						jumpToDragonActivie();
					}
				}
			}
		}
			break;

		}
		((FinalActivity) mActivity).hideProgressToast();
	}

}

package com.eztcn.user.eztcn.fragment;
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
import android.widget.RelativeLayout;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.activity.home.drug.DrugListActivity;
import com.eztcn.user.eztcn.activity.home.orderbed.OrderBedNoticeActivity;
/**
 * 服务
 * @author LX 2016-3-16下午7:09:20
 */
public class ServiceFragment30 extends FinalFragment implements OnClickListener{

	private Activity mActivity;
	private View rootView;// 缓存Fragment view
	private RelativeLayout service_order_check_layout,service_order_hospital_bed_layout,
	service_order_drug_layout,service_guide_service_layout,
	service_accompany_service_layout;
	

	public static ServiceFragment30 newInstance() {

		ServiceFragment30 fragment = new ServiceFragment30();
		return fragment;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_service_new, null);

			WindowManager manager = (WindowManager) getActivity()
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		WindowManager manager = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		initView(getView(), dm);
		init();
	}


	private void initView(View view, DisplayMetrics dm) {
		service_order_check_layout=(RelativeLayout) view.findViewById(R.id.service_order_check_layout);
		service_order_hospital_bed_layout=(RelativeLayout) view.findViewById(R.id.service_order_hospital_bed_layout);
		service_order_drug_layout=(RelativeLayout) view.findViewById(R.id.service_order_drug_layout);
		service_guide_service_layout=(RelativeLayout) view.findViewById(R.id.service_guide_service_layout);
		service_accompany_service_layout=(RelativeLayout) view.findViewById(R.id.service_accompany_service_layout);
		service_order_check_layout.setOnClickListener(this);
		service_order_hospital_bed_layout.setOnClickListener(this);
		service_order_drug_layout.setOnClickListener(this);
		service_guide_service_layout.setOnClickListener(this);
		service_accompany_service_layout.setOnClickListener(this);
	}
	private void init() {
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.service_order_check_layout:
			
			Intent intent = new Intent(mActivity, ChoiceHosActivity.class);
			intent.putExtra("isOrderCheck", true);
			startActivity(intent);
			
			break;
		case R.id.service_order_hospital_bed_layout:
			
			startActivity(new Intent(getActivity(),
					OrderBedNoticeActivity.class));
			break;
		case R.id.service_order_drug_layout:
			
			startActivity(new Intent(getActivity(), DrugListActivity.class));
			
			break;
		case R.id.service_guide_service_layout:
			break;
		case R.id.service_accompany_service_layout:
			break;
		}
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}

}

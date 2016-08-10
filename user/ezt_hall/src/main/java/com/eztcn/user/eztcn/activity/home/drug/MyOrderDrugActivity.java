package com.eztcn.user.eztcn.activity.home.drug;
import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.os.Bundle;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MyOrderDrugAdapter;
import com.eztcn.user.eztcn.customView.PullToRefreshListView;
/**
 * @author LX
 *@date2016-3-29 @time上午11:34:33
 *我的预约药品
 */
public class MyOrderDrugActivity extends FinalActivity {
	
	@ViewInject(R.id.my_order_drug_lv)
	private PullToRefreshListView my_order_drug_lv;
	private MyOrderDrugAdapter  adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order_drug);
		ViewUtils.inject(MyOrderDrugActivity.this);
		loadTitleBar(true, "", null);
		adapter = new MyOrderDrugAdapter(mContext);
		my_order_drug_lv.setAdapter(adapter);
		getData();
	}

	//get order_drug_list_data
	private void getData() {
		
		
	}
	

}

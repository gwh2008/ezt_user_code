//package com.eztcn.user.eztcn.activity.watch;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.app.Dialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.BaseApplication;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.activity.MainActivity;
//import com.eztcn.user.eztcn.api.IHttpResult;
//import com.eztcn.user.eztcn.bean.HealthCard;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.impl.ServerImpl;
//import com.eztcn.user.eztcn.impl.UserImpl;
//import com.eztcn.user.eztcn.utils.HttpParams;
//
///**
// * 手表管理界面 服务购买 激活服务 查找手表
// * 
// * @author Liu Gang
// * 
// *         2015年10月14日 下午2:11:09
// * 
// */
//public class WatchManageActivity extends FinalActivity implements
//		OnClickListener, OnItemClickListener, IHttpResult {
//	@ViewInject(R.id.buyServer, click = "onClick")
//	private View buyServer;
////	@ViewInject(R.id.activateServer, click = "onClick")
////	private View activateServer;
//	@ViewInject(R.id.searchWatch, click = "onClick")
//	private View searchWatch;
//
//	/**
//	 * 家庭成员
//	 */
//	private String[] families;
//	private Dialog familyDialog;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_watchmanage);
//		loadTitleBar(true, "我的手表", null);
//	}
//
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.buyServer: {
//			// 跳转到类似于医护帮的界面
//			gainWatchCard();
//		}
//			break;
//		case R.id.searchWatch: {
//			// 我的手表定位功能
//			// 选择家庭成员输入手机号然后给手机发短信，让手机回传短信实现定位
//			familyDialog = new Dialog(WatchManageActivity.this);
//			familyDialog.setTitle("选择家庭成员");
//			familyDialog.setContentView(R.layout.activity_famliy_member);
//			getMembers();
//		}
//			break;
//		}
//
//	}
//
//	private void gainWatchCard(){
//		int currentPage = 1;// 当前页数
//		int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
//		ServerImpl api = new ServerImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", BaseApplication.eztUser.getUserId());
//		params.put("rowsPerPage", pageSize);
//		params.put("page", currentPage);
//		api.getHealthcardList(params, this);
//	}
//	/**
//	 * 获取就诊人
//	 */
//	private void getMembers() {
//		if (null == BaseApplication.eztUser) {
//			HintToLogin(1);
//		} else {
//			HashMap<String, Object> params1 = new HashMap<String, Object>();
//			params1.put("userId", BaseApplication.eztUser.getUserId());
//			new UserImpl().getMemberCenter(params1, this);
//		}
//
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		String name = families[position];
//		Intent intent = new Intent(WatchManageActivity.this,
//				MyWatchActivity.class);
//		intent.putExtra("name", name);
//		familyDialog.cancel();
//		startActivity(intent);
//
//	}
//
//	@Override
//	public void result(Object... object) {
//		if (object == null) {
//			return;
//		}
//		int type = (Integer) object[0];
//		boolean isSuc = (Boolean) object[1];
//		if (isSuc) {
//			switch (type) {
//			case HttpParams.MEMBER_CENTER: {
//				// 获取家庭成员后
//				ListView famliyList = (ListView) familyDialog
//						.findViewById(R.id.familyList);
//				families = new String[] { "家庭成员1", "家庭成员2" };
//				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//						WatchManageActivity.this,
//						android.R.layout.simple_list_item_1, families);
//
//				famliyList.setAdapter(arrayAdapter);
//
//				familyDialog.show();
//				famliyList.setOnItemClickListener(this);
//			}
//				break;
//				
//			case HttpParams.GET_HEALTHCARD_LIST: {
//				Map<String, Object> map = (Map<String, Object>) object[2];
//				// 获取所有用户绑定的套餐
//				HealthCard	healthCard = (HealthCard)  map.get("healthCard");
//				if (null == healthCard) {
//					//没有卡 跳转到绑定套餐
//					// 激活绑定
//					startActivity(new Intent(WatchManageActivity.this,
//							ActivateServerActivity.class));
//				}else{
//					Intent it=new Intent(WatchManageActivity.this, BuyServerActivity.class);
//					it.putExtra("healthCard", healthCard);
//					startActivity(it);
//				}
//			}
//				break;
//			}
//		}
//
//	}
//
//}

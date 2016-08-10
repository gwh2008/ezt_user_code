///**
// * 
// */
//package com.eztcn.user.eztcn.activity.watch;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.bluetooth.BluetoothAdapter;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.DialogInterface.OnKeyListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.BaseApplication;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.activity.mine.UserLoginActivity;
//import com.eztcn.user.eztcn.api.IHttpResult;
//import com.eztcn.user.eztcn.bean.HealthCard;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.db.SystemPreferences;
//import com.eztcn.user.eztcn.impl.ServerImpl;
//import com.eztcn.user.eztcn.utils.HttpParams;
//
///**
// * @author Liu Gang
// * 
// *         2015年10月14日 上午10:55:43 手表管理
// */
//public class WatchActivity extends FinalActivity implements
//		OnItemClickListener, IHttpResult {
//	@ViewInject(R.id.watch_manage_list, itemClick = "onItemClick")
//	private ListView watch_manage_list;
//	private boolean iswatchMC = false;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_watch);
//		loadTitleBar(true, "手表管理", null);
//
//		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("key", "手表辅助登陆");
//		data.add(map);
//		HashMap<String, String> map1 = new HashMap<String, String>();
//		map1.put("key", "我的手表");
//		data.add(map1);
//		SimpleAdapter adapter = new SimpleAdapter(WatchActivity.this, data,
//				R.layout.item_watch, new String[] { "key" },
//				new int[] { R.id.watchSetItem });
//		watch_manage_list.setAdapter(adapter);
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO 自动生成的方法存根
//		super.onResume();
//		iswatchMC = false;
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		Intent intent = new Intent();
//		switch (position) {
//		case 0: {
//			watchLogin();
//		}
//			break;
//		case 1: {
//			if (!iswatchMC) {
//				watchManage();
//				iswatchMC = true;
//			}
//		}
//			break;
//
//		}
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
//
//			case HttpParams.GET_HEALTHCARD_LIST: {
//				Map<String, Object> map = (Map<String, Object>) object[2];
//				// 获取所有用户绑定的套餐
//				HealthCard healthCard = (HealthCard) map.get("healthCard");
//				if (null == healthCard) {
//					// 没有卡 跳转到绑定套餐
//					// 激活绑定
//					startActivity(new Intent(WatchActivity.this,
//							ActivateServerActivity.class));
//				} else {
//					Intent intent = new Intent(this,
//							ServerCardDetialActivity.class);
//					intent.putExtra("info", healthCard);
//					startActivity(intent);
//				}
//			}
//				break;
//			}
//		}
//
//	}
//
//	/**
//	 * 手表管理
//	 */
//	private void watchManage() {
//		if (null == BaseApplication.eztUser) {
//			HintToLogin(0);
//			return;
//		}
//		gainWatchCard();
//		// startActivity(new Intent(WatchActivity.this,
//		// WatchManageActivity.class));
//
//	}
//
//	private void gainWatchCard() {
//		int currentPage = 1;// 当前页数
//		int pageSize = EZTConfig.PAGE_SIZE;// 每页条数
//		ServerImpl api = new ServerImpl();
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", BaseApplication.eztUser.getUserId());
//		params.put("rowsPerPage", pageSize);
//		params.put("page", currentPage);
//		api.getHealthcardList(params, this);
//	}
//
//	private void watchLogin() {
//		// //手表登录
//
//		if (null != BaseApplication.eztUser) {
//			blueToothControl=new BlueToothLoginControl();
//			blueToothControl.init(WatchActivity.this, blueToothHandler);
//		} else {
//			HintToLogin(1);
//		}
//	}
//	private BlueToothLoginControl blueToothControl;
//	public final static int OPENBLUETOOTH = 359;
//	private static final int REQUEST_ENABLE_BT = 2;
//	public final static int NOBLUE = 350;
//	public final static int WRITEUSERINFO = 360;
//	public final static int MESSAGE_DEVICE_NAME = 355;
//	public final static int READBLUEMSG = 357;
//	public final static int ENSUREDISCOVER=351;
//	private Handler blueToothHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case OPENBLUETOOTH: {
//				Intent enableIntent = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//			}
//				break;
//			case NOBLUE: {
//				Toast.makeText(WatchActivity.this, "设备不含蓝牙", Toast.LENGTH_LONG)
//						.show();
//			}
//				break;
//			case WRITEUSERINFO: {
//				Toast.makeText(mContext, "正在从手表端登录用户", Toast.LENGTH_SHORT)
//						.show();
//				blueToothControl.destroy();
//			}
//				break;
//			case ENSUREDISCOVER:{
//				Intent discoverableIntent = new Intent(
//						BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//				discoverableIntent.putExtra(
//						BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//				startActivity(discoverableIntent);
//				Toast.makeText(WatchActivity.this, "请在手表端选择扫描设备", Toast.LENGTH_LONG).show();
//			}break;
//			case MESSAGE_DEVICE_NAME: {
//				String mConnectedDeviceName = String.valueOf(msg.obj);
//				Toast.makeText(getApplicationContext(),
//						"连接到" + mConnectedDeviceName, Toast.LENGTH_SHORT)
//						.show();
//			}
//				break;
//			case READBLUEMSG: {
//				String readMessage = String.valueOf(msg.obj);
//				Toast.makeText(mContext, readMessage, Toast.LENGTH_SHORT)
//						.show();
//			}
//				break;
//			}
//		};
//	};
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case REQUEST_ENABLE_BT:
//			// When the request to enable Bluetooth returns
//			if (resultCode == Activity.RESULT_OK) {
//				// Bluetooth is now enabled, so set up a chat session
//				blueToothControl.setup(WatchActivity.this);
//			} else {
//				// User did not enable Bluetooth or an error occurred
//				Toast.makeText(this, "蓝牙设施不可用", Toast.LENGTH_SHORT).show();
//			}
//		}
//	}
//	@Override
//	protected void onDestroy() {
//		// TODO 自动生成的方法存根
//		super.onDestroy();
//		if(null!=blueToothControl)
//		blueToothControl.destroy();
//	}
//}

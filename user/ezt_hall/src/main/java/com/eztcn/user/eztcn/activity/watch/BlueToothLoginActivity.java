///**
// * 
// */
//package com.eztcn.user.eztcn.activity.watch;
//
//import android.app.Activity;
//import android.app.AlertDialog.Builder;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.activity.FinalActivity;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.db.SystemPreferences;
//
///**
// * @author Liu Gang
// * 
// *         2015年9月21日 下午4:04:46
// *  跳转过来之前 必需 取到用户名 和 密码 否则无法进行数据传输
// *  
// *  my is over
// */
//public class BlueToothLoginActivity extends FinalActivity implements
//		OnClickListener {
//
//	// Message types sent from the BlueToothService Handler
//	public static final int MESSAGE_STATE_CHANGE = 1;
//	public static final int MESSAGE_READ = 2;
//	public static final int MESSAGE_WRITE = 3;
//	public static final int MESSAGE_DEVICE_NAME = 4;
//	public static final int MESSAGE_TOAST = 5;
//
//	// Key names received from the BlueToothService Handler
//	public static final String DEVICE_NAME = "device_name";
//	public static final String TOAST = "toast";
//
//	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 1;
//	private static final int REQUEST_ENABLE_BT = 2;
//
//	private BlueToothService mChatService = null;
//	private String userName,encryptPwd;
//
//	@ViewInject(R.id.btn_open_I, click = "onClick")
//	private Button btn_open_I;
//	@ViewInject(R.id.btn_search_others, click = "onClick")
//	private Button btn_search_others;
//	@ViewInject(R.id.title)
//	private TextView textView;
//	private BluetoothAdapter mBluetoothAdapter = null;
//
//	// Name of the connected device
//	private String mConnectedDeviceName = null;
//
//	@Override
//	protected void onCreate(android.os.Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_bluetooths);
//		getUserInfo();
//		// Get local Bluetooth adapter
//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		// If the adapter is null, then Bluetooth is not supported
//		if (mBluetoothAdapter == null) {
//			Toast.makeText(BlueToothLoginActivity.this, "设备不含蓝牙",
//					Toast.LENGTH_LONG).show();
//			finish();
//			return;
//		}
//
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//		// If BT is not on, request that it be enabled.
//		// setupChat() will then be called during onActivityResult
//		if (!mBluetoothAdapter.isEnabled()) {
//			Intent enableIntent = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//			// Otherwise, setup the chat session
//		} else {
//			if (mChatService == null)
//				setup();
//		}
//	}
//
//	@Override
//	public synchronized void onResume() {
//		super.onResume();
//
//		// Performing this check in onResume() covers the case in which BT was
//		// not enabled during onStart(), so we were paused to enable it...
//		// onResume() will be called when ACTION_REQUEST_ENABLE activity
//		// returns.
//		if (mChatService != null) {
//			// Only if the state is STATE_NONE, do we know that we haven't
//			// started already
//			if (mChatService.getState() == BlueToothService.STATE_NONE) {
//				// Start the Bluetooth chat services
//				mChatService.start();
//			}
//		}
//	}
//
//	private void setup() {
//		// Initialize the BluetoothChatService to perform bluetooth connections
//		mChatService = new BlueToothService(this, mHandler);
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//		case REQUEST_CONNECT_DEVICE_INSECURE:
//			// When DeviceListActivity returns with a device to connect
//			if (resultCode == Activity.RESULT_OK) {
//				connectDevice(data, false);
//			}
//			break;
//		case REQUEST_ENABLE_BT:
//			// When the request to enable Bluetooth returns
//			if (resultCode == Activity.RESULT_OK) {
//				// Bluetooth is now enabled, so set up a chat session
//				setup();
//			} else {
//				// User did not enable Bluetooth or an error occurred
//				Toast.makeText(this, "蓝牙设施不可用", Toast.LENGTH_SHORT).show();
//				finish();
//			}
//		}
//	}
//
//	private void connectDevice(Intent data, boolean secure) {
//		// Get the device MAC address
//		String address = data.getExtras().getString(
//				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//		// Get the BluetoothDevice object
//		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//		// Attempt to connect to the device
//		mChatService.connect(device, secure);
//	}
//
//	// The Handler that gets information back from the BlueToothService
//	private final Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case MESSAGE_STATE_CHANGE:
//				switch (msg.arg1) {
//				case BlueToothService.STATE_CONNECTED:
//					setStatus("连接到" + mConnectedDeviceName + ",准备传输数据");
//					
//					BlueToothLoginActivity.this.sendMessage(userName+","+encryptPwd);
//					break;
//				case BlueToothService.STATE_CONNECTING:
//					setStatus("连接中...");
//					break;
//				case BlueToothService.STATE_LISTEN:
//				case BlueToothService.STATE_NONE:
//					setStatus("蓝牙未连接");
//					break;
//				}
//				break;
//			case MESSAGE_WRITE:
//				// 像对方蓝牙设备写入信息后
//				byte[] writeBuf = (byte[]) msg.obj;
//				// construct a string from the buffer
//				String writeMessage = new String(writeBuf);
//				Toast.makeText(mContext, "正在从手表端登录用户", Toast.LENGTH_SHORT)
//						.show();
//				finish();
//				break;
//			case MESSAGE_READ:
//				byte[] readBuf = (byte[]) msg.obj;
//				// construct a string from the valid bytes in the buffer
//				String readMessage = new String(readBuf, 0, msg.arg1);
//				// Toast.makeText(mContext, readMessage,
//				// Toast.LENGTH_SHORT).show();
//				break;
//			case MESSAGE_DEVICE_NAME:
//				// save the connected device's name
//				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//				Toast.makeText(getApplicationContext(),
//						"连接到" + mConnectedDeviceName, Toast.LENGTH_SHORT)
//						.show();
//				break;
//			case MESSAGE_TOAST:
//				Toast.makeText(getApplicationContext(),
//						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
//						.show();
//				break;
//			}
//		}
//	};
//
//	private void ensureDiscoverable() {
//		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//			Intent discoverableIntent = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//			discoverableIntent.putExtra(
//					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//			startActivity(discoverableIntent);
//		}
//	}
//
//	/**
//	 * Sends a message.
//	 * 
//	 * @param message
//	 *            A string of text to send.
//	 */
//	private void sendMessage(String message) {
//		// Check that we're actually connected before trying anything
//		if (mChatService.getState() != BlueToothService.STATE_CONNECTED) {
//			Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		// Check that there's actually something to send
//		if (message.length() > 0) {
//			// Get the message bytes and tell the BlueToothService to write
//			byte[] send = message.getBytes();
//			mChatService.write(send);
//		}
//	}
//
//	private final void setStatus(CharSequence subTitle) {
//		textView.setText(subTitle);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_open_I: {
//			ensureDiscoverable();
//		}
//			break;
//		case R.id.btn_search_others: {
//			Intent serverIntent = new Intent(this, DeviceListActivity.class);
//			startActivityForResult(serverIntent,
//					REQUEST_CONNECT_DEVICE_INSECURE);
//		}
//			break;
//		}
//	};
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		// Stop the Bluetooth services
//		if (mChatService != null)
//			mChatService.stop();
//		
//		//获取蓝牙 并断开蓝牙
//		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//		mBtAdapter.cancelDiscovery();
//		mBtAdapter.disable();
//	}
//
//	private void getUserInfo() {
//		encryptPwd = SystemPreferences.getString(EZTConfig.KEY_PW, "");
//		userName = SystemPreferences.getString(EZTConfig.KEY_ACCOUNT, "");
//	}
//
//	
//	
//}

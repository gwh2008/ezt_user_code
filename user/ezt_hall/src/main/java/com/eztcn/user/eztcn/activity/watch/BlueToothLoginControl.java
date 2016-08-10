///**
// * 
// */
//package com.eztcn.user.eztcn.activity.watch;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.TextView;
//
//import com.eztcn.user.R;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.eztcn.user.eztcn.config.EZTConfig;
//import com.eztcn.user.eztcn.db.SystemPreferences;
//
///**
// * @author Liu Gang
// * 
// *         2015年9月21日 下午4:04:46 跳转过来之前 必需 取到用户名 和 密码 否则无法进行数据传输
// */
//public class BlueToothLoginControl {
//
//	// Message types sent from the BlueToothService Handler
//	public static final int MESSAGE_STATE_CHANGE = 11;
//	public static final int MESSAGE_READ = 21;
//	public static final int MESSAGE_WRITE = 31;
//	public static final int MESSAGE_DEVICE_NAME = 41;
//	public static final int MESSAGE_TOAST = 51;
//
//	// Key names received from the BlueToothService Handler
//	public static final String DEVICE_NAME = "device_name";
//	public static final String TOAST = "toast";
//
//	private BlueToothService mChatService = null;
//	private String userName, encryptPwd;
//
//	@ViewInject(R.id.title)
//	private TextView textView;
//	private BluetoothAdapter mBluetoothAdapter = null;
//
//	// Name of the connected device
//	private String mConnectedDeviceName = null;
//	private Handler handler;
//
//	public void init(Context context, Handler handler) {
//		this.handler = handler;
//		getUserInfo();
//		// Get local Bluetooth adapter
//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		// If the adapter is null, then Bluetooth is not supported
//		if (mBluetoothAdapter == null) {
//			// 没有蓝牙设备
//			handler.sendEmptyMessage(WatchActivity.NOBLUE);
//			return;
//		}
//
//		if (mBluetoothAdapter.isEnabled()) {
//			// Performing this check in onResume() covers the case in which BT
//			// was
//			// not enabled during onStart(), so we were paused to enable it...
//			// onResume() will be called when ACTION_REQUEST_ENABLE activity
//			// returns.
//			setup(context);
//		} else {
//			// If BT is not on, request that it be enabled.
//			// setupChat() will then be called during onActivityResult
//			if (!mBluetoothAdapter.isEnabled()) {
//				//开启蓝牙设备
//				handler.sendEmptyMessage(WatchActivity.OPENBLUETOOTH);
//				// Otherwise, setup the chat session
//			} else if (mChatService == null) {
//				setup(context);
//			}
//		}
//
//	}
//
//	public void setup(Context context) {
//		if (null == mChatService)
//			// Initialize the BluetoothChatService to perform bluetooth
//			// connections
//			mChatService = new BlueToothService(context, mHandler);
//		// Only if the state is STATE_NONE, do we know that we haven't
//		// started already
//		if (mChatService.getState() == BlueToothService.STATE_NONE) {
//			// Start the Bluetooth chat services
//			mChatService.start();
//		}
//
//		ensureDiscoverable();
//	}
//
//	// The Handler that gets information back from the BlueToothService
//	private final Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case MESSAGE_STATE_CHANGE:
//				switch (msg.arg1) {
//				case BlueToothService.STATE_CONNECTED: {
//					Message msgGo = handler.obtainMessage();
//					msgGo.obj = "连接到" + mConnectedDeviceName + ",准备传输数据";
//					msgGo.what = WatchActivity.READBLUEMSG;
//					handler.sendMessage(msgGo);
//					
//					BlueToothLoginControl.this.sendMessage(userName
//							+","+encryptPwd);
//				}
//					break;
//				case BlueToothService.STATE_CONNECTING:
//					// setStatus("连接中...");
//					break;
//				case BlueToothService.STATE_NONE:{
//					Message msgGo = handler.obtainMessage();
//					msgGo.obj = "蓝牙未连接";
//					msgGo.what = WatchActivity.READBLUEMSG;
//					handler.sendMessage(msgGo);
//				}
//					break;
//				case MESSAGE_WRITE:
//					// 像对方蓝牙设备写入信息后
//					byte[] writeBuf = (byte[]) msg.obj;
//					// construct a string from the buffer
//					String writeMessage = new String(writeBuf);
//					break;
//				case MESSAGE_READ: {
//					byte[] readBuf = (byte[]) msg.obj;
//					// construct a string from the valid bytes in the buffer
//					String readMessage = new String(readBuf, 0, msg.arg1);
//					Message msgGo = handler.obtainMessage();
//					msgGo.obj = readMessage;
//					msgGo.what = WatchActivity.READBLUEMSG;
//					handler.sendMessage(msgGo);
//				}
//					break;
//				case MESSAGE_DEVICE_NAME:
//					// save the connected device's name
//					mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//					Message msgGo = handler.obtainMessage();
//					msgGo.obj = mConnectedDeviceName;
//					msgGo.what = WatchActivity.MESSAGE_DEVICE_NAME;
//					handler.sendMessage(msgGo);
//					break;
//				}
//			}
//		}
//	};
//
//	
//	
//	/**
//	 * 允许设备被检测300秒
//	 */
//	private void ensureDiscoverable() {
//		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
//			handler.sendEmptyMessage(WatchActivity.ENSUREDISCOVER);
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
//			Message msg = handler.obtainMessage();
//			msg.what = WatchActivity.READBLUEMSG;
//			msg.obj = "蓝牙未连接";
//			handler.sendMessage(msg);
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
//	public void destroy() {
//		if (mChatService != null){
//			mChatService.close();	
//			mChatService.stop();
//		}
//		// 获取蓝牙 并断开蓝牙
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
//}

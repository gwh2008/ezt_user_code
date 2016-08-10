/**
 * 
 */
package com.eztcn.user.eztcn.utils.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.eztcn.user.eztcn.bean.MessageItem;

/**
 * @author Administrator
 *
 */
public class MessageReceiver extends BroadcastReceiver {
	private Handler handler;
	public MessageReceiver(Handler handler){
		this.handler=handler;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				"android.provider.Telephony.SMS_RECEIVED")) {
			MessageUtil msgUtils = new MessageUtil();
			msgUtils.gainMsg(intent, handler);
		}

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			MessageItem msgItem = (MessageItem) msg.obj;
			String msgContent = msgItem.getBody();
			

		};
	};

};


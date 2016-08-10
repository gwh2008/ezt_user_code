package com.eztcn.user.eztcn.utils.message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.eztcn.user.eztcn.bean.MessageItem;

public class MessageUtil {
	public void gainMsg(Intent intent, Handler mHandler) {
		SmsMessage[] messages = getMessagesFromIntent(intent);

		for (SmsMessage message : messages)

		{

//			Log.i("hello", message.getOriginatingAddress() + " : " +
//
//			message.getDisplayOriginatingAddress() + " : " +
//
//			message.getDisplayMessageBody() + " : " +
//
//			message.getTimestampMillis());
			MessageItem msgItem = new MessageItem();
			msgItem.setId(message.getProtocolIdentifier());
			msgItem.setBody(message.getDisplayMessageBody());
			msgItem.setPhone(message.getOriginatingAddress());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
			String date = sdf.format(new Date(message.getTimestampMillis()));
			msgItem.setDate(date);
			android.os.Message message1 = new android.os.Message();
			message1.obj = msgItem;
			mHandler.sendMessage(message1);

			break;
		}
	}

	private final SmsMessage[] getMessagesFromIntent(Intent intent)

	{

		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)

		{

			pduObjs[i] = (byte[]) messages[i];

		}

		byte[][] pdus = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		SmsMessage[] msgs = new SmsMessage[pduCount];

		for (int i = 0; i < pduCount; i++)

		{

			pdus[i] = pduObjs[i];

			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

		}

		return msgs;

	}

	/*
	 * http://blog.163.com/langfei520@yeah/blog/static/17271022220111111004662 /
	 * http://www.cnblogs.com/yejiurui/archive/2013/10/28/3392305.html
	 * 
	 * /** 调用系统短信的第二种方式 这种方式可以有自己的界面 所以：你懂得
	 * 
	 * @param mobile 接收方的手机号码
	 * 
	 * @param content 短信内容
	 * 
	 * @param context 环境变量上下文
	 */
	public void send(String mobile, String content, Context context) {

		// 处理返回的发送状态
		String SENT_SMS_ACTION = "SENT_SMS_ACTION";
		Intent intent = new Intent(SENT_SMS_ACTION);
		// 执行 broadcast的 PendingIntent 例如 调用 Context.sendBroadcast().
		// requestCode Private request code for the sender (currently not used).
		PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
				intent, 0);

		// register the Broadcast Receivers
		// The receiver will be called with any broadcast Intent that matches
		// filter
		// The BroadcastReceiver to handle the broadcast.
		// Selects the Intent broadcasts to be received.
		context.registerReceiver(new MsgSendStateReceiver(context),
				new IntentFilter(SENT_SMS_ACTION));
		SmsManager smsManager = SmsManager.getDefault();
		if (content.length() >= 70) {
			// 短信字数大于70，自动分条
			// Divide a message text into several fragments, none bigger than
			// the maximum SMS message size.
			List<String> ms = smsManager.divideMessage(content);
			for (String str : ms) {
				// 短信发送
				// the address to send the message to
				// is the service center address or null to use the current
				// default SMSC
				// the body of the message to send
				// if not NULL this PendingIntent is broadcast when the message
				// is successfully sent
				// if not NULL this PendingIntent is broadcast when the message
				// is delivered to the recipient
				smsManager.sendTextMessage(mobile, null, str, sentIntent, null);
			}
		} else {
			smsManager.sendTextMessage(mobile, null, content, sentIntent, null);
		}

	}

	/**
	 * 短信发送状态监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class MsgSendStateReceiver extends BroadcastReceiver {
		private Context context;

		MsgSendStateReceiver(Context context) {
			this.context = context;
		}

		@Override
		public void onReceive(Context _context, Intent _intent) {
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
				break;
			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				break;
			case SmsManager.RESULT_ERROR_RADIO_OFF:
				break;
			case SmsManager.RESULT_ERROR_NULL_PDU:
				break;
			}
		};
	};

	/**
	 * 调起系统发短信功能
	 * 
	 * @param phoneNumber
	 *            发送短信的接收号码
	 * @param message
	 *            短信内容
	 */
	public void sendSMS(String phoneNumber, String message, Context context) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
				+ phoneNumber));
		intent.putExtra("sms_body", message);
		context.startActivity(intent);
	}

}

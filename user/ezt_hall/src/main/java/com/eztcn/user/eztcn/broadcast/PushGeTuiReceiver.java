package com.eztcn.user.eztcn.broadcast;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import xutils.db.sqlite.WhereBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.activity.home.MsgManageActivity;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.db.SystemPreferences;
import com.eztcn.user.eztcn.fragment.HomeFragment;
import com.eztcn.user.eztcn.utils.JsonUtil;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.MsgUtil;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

/**
 * @title 个推接收器
 * @describe 接收个推发送的消息，可以做相应的跳转处理
 * @author ezt
 * @created 2014年01月18日
 */
public class PushGeTuiReceiver extends BroadcastReceiver {

	boolean flag = false;// 辨别是否为为标准json格式字符串

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");
			int actionId = bundle.getInt("action");
			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			// Logger.i("第三方回执接口调用", (result ? "成功" : "失败"));
			MessageAll msg = null;
			String msgTypeId = "";// 消息类型
			int count = 0;// 消息条数
			if (payload != null) {// 透传内容不为空
				String str = new String(payload);
				if (str != null && !str.equals("")) {
					JSONObject json = null;
					try {
						json = new JSONObject(str);
						if (json != null) {
							flag = true;
							if (!json.isNull("data")) {
								String data = json.get("data").toString();
								msg = JsonUtil.fromJson(data, MessageAll.class);
								if (msg == null) {
									return;
								}

								msgTypeId = msg.getMsgType();

								if (TextUtils.isEmpty(msgTypeId)) {
									flag = false;
								}
								// 处理未登录无法收到需登录消息
								if (BaseApplication.patient == null
										&& (!msgTypeId.equals("custom"))) {
									return;
								}
								// 判断是否为系统更新
								if (msgTypeId.equals("custom")
										&& msg.getMsgChildType().equals(
												"custom-bb")) {// 版本更新

									int code = msg.getVersionCode();// 当前版本
									int oldCode = SystemPreferences.getInt(
											EZTConfig.KEY_SET_VERSION_CODE, 0);
									if (oldCode < code) {// 覆盖升级
										if (!SystemPreferences.getBoolean(
												EZTConfig.KEY_SET_MSG_HINT,
												false)) {
											MsgUtil.createUpdateNotification(
													context, msg.getMsgTitle(),
													msg.getMsgTime(),
													msg.getMsgInfo());
										}

									}

									return;
								}
								MsgType type = new MsgType();// 消息类型
								String title = "";
								String content = "";

								if (msgTypeId.equals("custom")) {// 医指通（系统公告）
									title = "医指通";
								} else if (msgTypeId.equals("register")) {// 预约提醒
									title = "预约提醒";

								}
								content = msg.getMsgInfo();
								// case 3:// 停诊消息
								// title = "停诊消息";
								// content = "停诊消息内容";
								// break;
								//
								// case 4:// 患患沟通
								// title = msg.getMsgTitle();
								// content = msg.getMsgInfo();
								// break;
								
								WhereBuilder whereb=!msgTypeId.equals("4")?WhereBuilder.b("typeId", "=", msgTypeId):WhereBuilder.b("typeId", "=", msgTypeId)
										.and("patientId", "=", msg.getMsgId());// 类型为4的时候，根据患者id是否为不同患者
								
								ArrayList<MsgType> types = EztDb.getInstance(
										context).queryAll(
										new MsgType(),
									whereb,null);// 类型为4的时候，根据患者id是否为不同患者
								
//								ArrayList<MsgType> types = EztDb.getInstance(
//										context).queryDataWhere(
//										new MsgType(),
//										!msgTypeId.equals("4") ? "typeId = "
//												+ "'" + msgTypeId + "'"
//												: "typeId = " + msgTypeId
//														+ " and patientId = "
//														+ "'" + msg.getMsgId()
//														+ "'");// 类型为4的时候，根据患者id是否为不同患者
								/**
								 * 根据条件
								 */
//								ArrayList<MsgType> types = EztDb.getInstance(
//										context).queryDataWhere(
//										new MsgType(),
//										!msgTypeId.equals("4") ? "typeId = "
//												+ "'" + msgTypeId + "'"
//												: "typeId = " + msgTypeId
//														+ " and patientId = "
//														+ "'" + msg.getMsgId()
//														+ "'");// 类型为4的时候，根据患者id是否为不同患者

								type.setCreateTypeTime(msg.getMsgTime());

								type.setTypeId(msgTypeId);
								type.setTypeContent(content);
								type.setTypeTitle(title);
								type.setPatientId(msg.getMsgId());
								type.setClickState(0);

								if (types.size() != 0) {// 有则更新数据
									count = types.get(0).getTypeCount() + 1;
									type.setTypeCount(count);
									
									WhereBuilder b=!msg.getMsgType().equals(
											"4")?WhereBuilder.b("typeId", "=", msg.getMsgType()):WhereBuilder.b("typeId", "=", msg.getMsgType()).and("patientId", "=", msg.getMsgId());
									
								
											EztDb.getInstance(context)
											.update(type,
													b);
											
											
//									EztDb.getInstance(context)
//											.update(type,
//													!msg.getMsgType().equals(
//															"4") ? "typeId = "
//															+ "'"
//															+ msg.getMsgType()
//															+ "'" : "typeId = "
//															+ "'"
//															+ msg.getMsgType()
//															+ "'"
//															+ " and patientId="
//															+ msg.getMsgId());
								} else {// 无则增加一条数据
									count += 1;
									type.setTypeCount(count);
									EztDb.getInstance(context).save(type);

								}
								msg.setClickState(0);// 设置未点击状态
								EztDb.getInstance(context).save(msg);
							}
						}
					} catch (JSONException e) {
						flag = false;
						e.printStackTrace();

					}

				}

			}

			if (flag) {// 正确解析json消息格式
				if (FinalActivity.mContext != null
						&& FinalActivity.mContext instanceof MsgManageActivity
						&& !BaseApplication.getInstance().isBackground) {
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra("action", actionId);
					intent.setClass(context, MsgManageActivity.class);// 刷新消息箱
					context.startActivity(intent);
				}
				if (FinalActivity.mContext != null
						&& FinalActivity.mContext instanceof MainActivity
						&& !BaseApplication.getInstance().isBackground) {// 刷新首页消息显示数量
					HomeFragment.initialMsgNum();
//					((MainActivity)(FinalActivity.mContext)).comingMsg();
				}

				if (!SystemPreferences.getBoolean(EZTConfig.KEY_SET_MSG_HINT,
						false)) {
					MsgUtil.createNotification(context, msg.getMsgTitle(),
							msg.getMsgInfo(), msgTypeId, msg.getMsgId(), count);// 创建通知栏
				}

			}

			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			SystemPreferences.save(EZTConfig.KEY_CID, cid);
			Logger.i("cid", cid);

			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;
		default:
			break;
		}
	}
}

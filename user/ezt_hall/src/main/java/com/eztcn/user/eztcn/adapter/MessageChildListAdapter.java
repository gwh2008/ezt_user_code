package com.eztcn.user.eztcn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.home.SystemMsgInfoActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 消息子列表adapter
 * @describe
 * @author ezt
 * @created 2015年1月16日
 */
public class MessageChildListAdapter extends BaseArrayListAdapter<MessageAll>
		implements OnClickListener {

	public MessageChildListAdapter(Activity context) {
		super(context);
	}

	Intent intent = new Intent();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_messagedetail, null);
			holder.time = (TextView) convertView.findViewById(R.id.time);// 时间
			holder.msgContent = (TextView) convertView
					.findViewById(R.id.msgContent);// 内容
			holder.lookDetail = (TextView) convertView
					.findViewById(R.id.lookDetail);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.msgTitle);// 标题
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lookDetail.setTag(position);
		holder.lookDetail.setOnClickListener(this);
		MessageAll msg = mList.get(position);
		holder.tvTitle.setText(msg.getMsgTitle());
		holder.msgContent.setText(msg.getMsgInfo());
		holder.time.setText(StringUtil.judgeTime(msg.getMsgTime()));

		return convertView;
	}

	class ViewHolder {
		TextView time;
		TextView tvTitle;
		TextView msgContent;
		TextView lookDetail;
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) v.getTag();
		String type = mList.get(pos).getMsgType();
		judgeToIntent(type, mList.get(pos));
	}

	/**
	 * 根据类型跳转到相应页面
	 * 
	 * 消息类型(type:custom为医指通类(childType:custom-gg、系统公告) 2为预约提醒 3为停诊消息)
	 */
	public void judgeToIntent(String type, MessageAll msg) {
		if (type.equals("custom")) {
			intent.setClass(mContext, SystemMsgInfoActivity.class);
			intent.putExtra("title", msg.getMsgTitle());
			intent.putExtra("info", msg.getMsgInfo());
		} else if (type.equals("register")) {// 我的记录/预约记录页面
			intent.setClass(mContext, MyRecordActivity.class);
		} else if (type.equals("3")) {// 我/就医问诊的【已退号】记录页面
			intent.setClass(mContext, MyRecordActivity.class);
		}

		if (intent != null) {
			mContext.startActivity(intent);
		}

	}

}

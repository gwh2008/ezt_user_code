package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.eztcn.user.R;
import xutils.ViewUtils;
import xutils.db.sqlite.WhereBuilder;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import xutils.view.annotation.event.OnItemLongClick;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.MessageChildListAdapter;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.db.EztDb;
/**
 * @title 消息箱子列表
 * @describe
 * @author ezt
 * @created 2015年1月12日
 */
public class MessageChildListActivity extends FinalActivity 
		 {// implements OnItemLongClickListener

	@ViewInject(R.id.title_tv)
	private TextView title;
	@ViewInject(R.id.msgDetailList)//, itemClick = "onItemClick"
	private ListView msgDetailList;

	@ViewInject(R.id.none_layout)
	private LinearLayout noneLayout;

	@ViewInject(R.id.none_txt)
	private TextView tvHint;

	private MessageChildListAdapter adapter;
	private String typeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagedetail);
		ViewUtils.inject(MessageChildListActivity.this);
		String title = getIntent().getStringExtra("title");
		loadTitleBar(true, title, null);
		tvHint.setText("暂无消息");
		typeId = getIntent().getStringExtra("typeId");
		adapter = new MessageChildListAdapter(this);
		msgDetailList.setAdapter(adapter);
//		msgDetailList.setOnItemLongClickListener(this);

	}

	@Override
	protected void onResume() {
		initialMsgData();
		super.onResume();
	}

	/**
	 * 获取消息列表
	 */
	private void initialMsgData() {
		showProgressToast();
//		ArrayList<MessageAll> mList = EztDb.getInstance(mContext)
//				.queryDataWhere(new MessageAll(),
//						"msgType = " + "'" + typeId + "'", "_id desc");
		
		WhereBuilder b=WhereBuilder.b("msgType","=", typeId);
		
		ArrayList<MessageAll> mList = EztDb.getInstance(mContext)
				.queryAll(new MessageAll(),
						b, "_id desc");
		adapter.setList(mList);
		msgDetailList.setSelection(0);
		if (mList.size() == 0) {
			msgDetailList.setVisibility(View.GONE);
			noneLayout.setVisibility(View.VISIBLE);
//			EztDb.getInstance(mContext).delDataWhere(new MsgType(),
//					"typeId = " + "'" + typeId + "'");
			WhereBuilder b1=WhereBuilder.b("typeId", "=", typeId);
			EztDb.getInstance(mContext).delDataWhere(new MsgType(),
					b1);

		} else {
			msgDetailList.setVisibility(View.VISIBLE);
			noneLayout.setVisibility(View.GONE);
		}

		hideProgressToast();
	}

//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view,
//			int position, long id) {
		@OnItemLongClick(R.id.msgDetailList)
		public boolean itemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
		HintDel(position);
		return true;
	}

		@OnItemClick(R.id.msgDetailList)
		public void itemClick(AdapterView<?> parent, View view,
				int position, long id) {
	}
	/**
	 * 删除提醒
	 */
	public void HintDel(final int pos) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage("确定删除该消息？").setCancelable(false)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int msgId = adapter.getList().get(pos).getMsgId();
//						EztDb.getInstance(mContext).delDataWhere(
//								new MessageAll(), "msgId = " + msgId);
						WhereBuilder b=WhereBuilder.b("msgId", "=", msgId);
						EztDb.getInstance(mContext).delDataWhere(
								new MessageAll(), b);
						initialMsgData();

					}
				}).setPositiveButton("取消", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();
	}

}

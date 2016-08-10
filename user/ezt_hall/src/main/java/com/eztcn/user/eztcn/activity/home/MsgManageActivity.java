package com.eztcn.user.eztcn.activity.home;

import java.util.ArrayList;
import java.util.Set;

import xutils.ViewUtils;
import xutils.db.sqlite.Selector;
import xutils.db.sqlite.WhereBuilder;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.MainActivity;
import com.eztcn.user.eztcn.adapter.MsgManageAdapter;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.utils.AppManager;
import com.eztcn.user.eztcn.utils.MsgUtil;

/**
 * @title 消息箱
 * @describe
 * @author ezt
 * @created 2014年12月15日
 */
public class MsgManageActivity extends FinalActivity implements
		OnClickListener, OnCheckedChangeListener {// OnItemClickListener,

	@ViewInject(R.id.msg_manage_info_et)
	private EditText etInfo;// 搜索框

	@ViewInject(R.id.none_layout)
	// , click = "onClick"
	private LinearLayout noneLayout;// 暂无数据

	@ViewInject(R.id.msg_manage_footer)
	private RelativeLayout layoutFooter;// 页面底部栏

	@ViewInject(R.id.msg_manage_search_img)
	// , click = "onClick"
	private LinearLayout imgSearch;// 搜索按钮

	@ViewInject(R.id.msg_manage_choiceall)
	public CheckBox cbChoiceAll;// 选择全部

	@ViewInject(R.id.msg_manage_del)
	// , click = "onClick"
	public TextView tvDel;// 选中删除

	@ViewInject(R.id.msg_manage_lv)
	// , click = "onClick"
	private ListView ltMsg;// 消息列表

	private TextView tvBack;// 返回按钮
	private MsgManageAdapter adapter;
	private ArrayList<MsgType> msgList;
	private TextView tvEdit;
	public int flag = 0;// 0为全选按钮取消全选，1为各个item不满全选时取消全选
	public Animation showAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_manage);
		ViewUtils.inject(MsgManageActivity.this);
		tvEdit = loadTitleBar(true, "消息", "编辑");
		tvEdit.setBackgroundResource(R.drawable.selector_title_bar_btn_bg);
		tvEdit.setTextColor(getResources().getColor(android.R.color.black));
		// tvEdit.setPadding(R.dimen.medium_margin, R.dimen.small_margin,
		// R.dimen.medium_margin, R.dimen.small_margin);
		tvBack = (TextView) findViewById(R.id.left_btn);
		showAnim = new AnimationUtils()
				.loadAnimation(mContext, R.anim.alpha_in);
		tvBack.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
		cbChoiceAll.setOnCheckedChangeListener(this);
		adapter = new MsgManageAdapter(this);
		ltMsg.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		// initialData("");
		initialData(null);
		super.onResume();
	}

	/**
	 * 填充数据
	 * 
	 * @param where
	 *            有值为搜索查询
	 */
	private void initialData(WhereBuilder where) {// String where
		showProgressToast();
		if (BaseApplication.patient != null) {// 登录
			if (null == where) {// 正常
				msgList = EztDb.getInstance(mContext).queryAll(new MsgType(),
						where, "_id desc");
			} else {// 搜索
				msgList = EztDb.getInstance(mContext).queryAll(new MsgType(),
						where, "_id desc");
			}
		} else {// 未登录
			if (null == where) {// 正常
				where = WhereBuilder.b("typeId", "=", "custom").or("typeId",
						"=", "停诊通知");
				msgList = EztDb.getInstance(mContext).queryAll(new MsgType(),
						where, "_id desc");
			} else {// 搜索
				WhereBuilder bulider = where;
				WhereBuilder where1 = WhereBuilder.b("typeId", "=", "custom")
						.or("typeId", "=", "停诊通知");
				Selector selector = Selector.from(new MsgType().getClass());
				selector = selector.where(bulider);
				selector.and(where1);
				msgList = EztDb.getInstance(mContext).queryAll(new MsgType(),
						selector, "_id desc");
			}
		}

		if (msgList != null) {
			adapter.setList(msgList);
		}
		if (msgList == null || msgList.size() == 0) {
			tvEdit.setVisibility(View.GONE);
			noneLayout.setVisibility(View.VISIBLE);
			ltMsg.setVisibility(View.GONE);
		} else {
			noneLayout.setVisibility(View.GONE);
			ltMsg.setVisibility(View.VISIBLE);
			tvEdit.setVisibility(View.VISIBLE);

		}
		imgSearch.setEnabled(true);
		hideProgressToast();
	}

	@OnClick(R.id.none_layout)
	public void none_layoutClick(View v) {
		// 暂无数据点击刷新
		initialData(null);
	}

	@OnClick(R.id.left_btn)
	public void backClick(View v) {

		// 返回按钮
		onBackPressed();

	}

	@OnClick(R.id.msg_manage_search_img)
	public void msg_manage_search_imgClick(View v) {
		// 搜索
		imgSearch.setEnabled(false);
		hideSoftInput(etInfo);
		etInfo.clearFocus();
		String str = etInfo.getText().toString().trim();
		if (TextUtils.isEmpty(str)) {
			Toast.makeText(mContext, getString(R.string.search_empty),
					Toast.LENGTH_SHORT).show();
			imgSearch.setEnabled(true);
			return;
		} else {
			WhereBuilder b = WhereBuilder.b("typeTitle", "like", "%" + str
					+ "%");
			initialData(TextUtils.isEmpty(str) ? null : b);
		}

	}

	@OnClick(R.id.msg_manage_del)
	public void msg_manage_delClick(View v) {

		// 选中删除
		MsgUtil.clearNotificationMsg();
		if (adapter.isSelectAll) {// 全选
			EztDb.getInstance(mContext).delDataWhere(new MsgType(), null);

			for (int i = 0; i < msgList.size(); i++) {
				String typeId = msgList.get(i).getTypeId();
				WhereBuilder b = WhereBuilder.b("msgType", "=", typeId);
				ArrayList<MessageAll> childMsgList = EztDb
						.getInstance(mContext).queryAll(new MessageAll(), b,
								"_id desc");

				for (int j = 0; j < childMsgList.size(); j++) {
					MessageAll msgAll = childMsgList.get(j);
					if (msgAll.getClickState() == 0) {
						msgAll.setClickState(1);
						WhereBuilder b_up = WhereBuilder.b("msgType", "=",
								typeId);
						EztDb.getInstance(mContext).update(msgAll, b_up);// 更新消息子类型点击状态
					}
				}
			}

		} else {
			Set<Integer> checkedIds = adapter.checkedRecId;
			for (Integer pos : checkedIds) {
				EztDb.getInstance(mContext).delItemData(msgList.get(pos));
				WhereBuilder qB = WhereBuilder.b("msgType", "=",
						msgList.get(pos).getTypeId());
				ArrayList<MessageAll> childMsgList = EztDb
						.getInstance(mContext).queryAll(new MessageAll(), qB,
								"_id desc");

				for (int j = 0; j < childMsgList.size(); j++) {
					MessageAll msgAll = childMsgList.get(j);
					if (msgAll.getClickState() == 0) {
						msgAll.setClickState(1);
						WhereBuilder upB = WhereBuilder.b("msgType", "=",
								msgList.get(pos).getTypeId());
						EztDb.getInstance(mContext).update(msgAll, upB);// 更新消息子类型点击状态
					}
				}
			}
			adapter.checkedRecId.clear();// 清理所有内容
		}
		initialData(null);
		tvEdit.performClick();
	}

	@Override
	public void onClick(View v) {

		if (v == tvEdit) {
			if ("编辑".equals(tvEdit.getText().toString())) {// 编辑
				tvEdit.setText("取消");
				adapter.isShow = true;
				layoutFooter.setVisibility(View.VISIBLE);
				layoutFooter.startAnimation(showAnim);

				tvDel.setEnabled(false);
				tvDel.setTextColor(getResources().getColor(R.color.light_gray));
			} else {// 取消
				tvEdit.setText("编辑");
				adapter.isShow = false;
				layoutFooter.setVisibility(View.GONE);
				layoutFooter.clearAnimation();
				cbChoiceAll.setChecked(false);
			}
			adapter.notifyDataSetChanged();
		}
		
		if(v==tvBack){
			// 返回按钮
			onBackPressed();

		}
	}

	@OnItemClick(R.id.msg_manage_lv)
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MsgType type = msgList.get(position);
		type.setClickState(1);// 点击状态
		type.setTypeCount(0);// 初始化类消息条数
		WhereBuilder b = WhereBuilder.b("typeId", "=", type.getTypeId());
		EztDb.getInstance(mContext).update(type, b);// 更新消息父类型点击状态
		WhereBuilder qB = WhereBuilder.b("msgType", "=", type.getTypeId());
		ArrayList<MessageAll> msgList = EztDb.getInstance(mContext).queryAll(
				new MessageAll(), qB, "_id desc");

		for (int i = 0; i < msgList.size(); i++) {
			MessageAll msgAll = msgList.get(i);
			if (msgAll.getClickState() == 0) {
				msgAll.setClickState(1);
				WhereBuilder b_ = WhereBuilder.b("_id", "=", msgAll.get_id());
				EztDb.getInstance(mContext).update(msgAll, b_);// 更新消息子类型点击状态
			}
		}
		Intent intent = new Intent(MsgManageActivity.this,
				MessageChildListActivity.class);

		intent.putExtra("typeId", type.getTypeId());
		intent.putExtra("title", type.getTypeTitle());
		startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (flag == 0) {
			if (isChecked) {// 全选
				adapter.isSelectAll = true;
			} else {
				adapter.isSelectAll = false;
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		initialData(null);

	}

	@Override
	public void onBackPressed() {
		if (!AppManager.getAppManager().isLaunch("MainActivity")) {// 未存在（消息通知栏直接跳入消息箱）
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		finish();
		super.onBackPressed();
	}

}

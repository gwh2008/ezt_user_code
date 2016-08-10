/**
 * 
 */
package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Set;
import xutils.db.sqlite.WhereBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.eztcn.user.eztcn.activity.home.MessageChildListActivity;
import com.eztcn.user.eztcn.adapter.MsgManageAdapter;
import com.eztcn.user.eztcn.bean.MessageAll;
import com.eztcn.user.eztcn.bean.MsgType;
import com.eztcn.user.eztcn.db.EztDb;
import com.eztcn.user.eztcn.utils.MsgUtil;
/**
 * @author Liu Gang
 * 
 *         2015年11月11日 下午1:28:16
 * 
 */
public class MsgManageFragment extends FinalFragment implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener {
	private View rootView;
	private EditText etInfo;// 搜索框
	private TextView msg_manage_title_tv;

	private LinearLayout noneLayout;// 暂无数据

	private RelativeLayout layoutFooter;// 页面底部栏

	private LinearLayout imgSearch;// 搜索按钮

	public CheckBox cbChoiceAll;// 选择全部

	public TextView tvDel;// 选中删除

	private ListView ltMsg;// 消息列表

	private MsgManageAdapter adapter;
	private ArrayList<MsgType> msgList;
	private TextView tvEdit;
	public int flag = 0;// 0为全选按钮取消全选，1为各个item不满全选时取消全选
	public Animation showAnim;
	private Activity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = this.getActivity();

	}
	public static MsgManageFragment newInstance() {
		MsgManageFragment fragment = new MsgManageFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_msg_manage, null);// 缓存Fragment
		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		return rootView;
	}

	private void initView() {
		msg_manage_title_tv = (TextView) rootView
				.findViewById(R.id.msg_manage_title_tv);
		etInfo = (EditText) rootView.findViewById(R.id.msg_manage_info_et);
		noneLayout = (LinearLayout) rootView.findViewById(R.id.none_layout);
		noneLayout.setOnClickListener(this);
		layoutFooter = (RelativeLayout) rootView
				.findViewById(R.id.msg_manage_footer);
		imgSearch = (LinearLayout) rootView
				.findViewById(R.id.msg_manage_search_img);
		imgSearch.setOnClickListener(this);
		cbChoiceAll = (CheckBox) rootView
				.findViewById(R.id.msg_manage_choiceall);
		tvDel = (TextView) rootView.findViewById(R.id.msg_manage_del);
		tvDel.setOnClickListener(this);
		ltMsg = (ListView) rootView.findViewById(R.id.msg_manage_lv);
		ltMsg.setOnItemClickListener(this);
		tvEdit = (TextView) rootView.findViewById(R.id.right_btn);
		msg_manage_title_tv.setText("消息");
		tvEdit.setText("编辑");
		tvEdit.setOnClickListener(this);
		// tvEdit.setPadding(R.dimen.medium_margin, R.dimen.small_margin,
		// R.dimen.medium_margin, R.dimen.small_margin);
		tvEdit.setBackgroundResource(R.drawable.selector_title_bar_btn_bg);
		tvEdit.setTextColor(getResources().getColor(android.R.color.black));

		showAnim = new AnimationUtils().loadAnimation(getActivity(),
				R.anim.alpha_in);
		cbChoiceAll.setOnCheckedChangeListener(this);
		adapter = new MsgManageAdapter(MsgManageFragment.this);
		ltMsg.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		initialData(null);
		super.onResume();
	}

	/**
	 * 填充数据
	 * 
	 * @param where
	 *            有值为搜索查询
	 */
	private void initialData(WhereBuilder whereBuilder) {
		((FinalActivity) activity).showProgressToast();
		if (BaseApplication.patient != null) {// 登录
			if (null==whereBuilder) {// 正常
				msgList = EztDb.getInstance(activity).queryAll(new MsgType(), whereBuilder, "_id desc");
			} else {// 搜索
				
				msgList = EztDb.getInstance(activity).queryAll(
						new MsgType(), whereBuilder, "_id desc");
			}
		} else {// 未登录
			if (null==whereBuilder) {// 正常
				whereBuilder=WhereBuilder.b("typeId", "=", "custom").or("typeId", "=", "停诊通知");
				msgList = EztDb.getInstance(activity).queryAll(
						new MsgType(),whereBuilder,
						"_id desc");
			} else {// 搜索
				whereBuilder=whereBuilder.and("typeId", "=", "custom").or("typeId", "=", "停诊通知");
				msgList = EztDb.getInstance(activity).queryAll(
						new MsgType(),
						whereBuilder,
						"_id desc");
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
		((FinalActivity) activity).hideProgressToast();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.none_layout:// 暂无数据点击刷新
			initialData(null);
			break;

		case R.id.msg_manage_search_img:// 搜索

			imgSearch.setEnabled(false);
			((FinalActivity) activity).hideSoftInput(etInfo);
			etInfo.clearFocus();
			String str = etInfo.getText().toString().trim();
			
			if (TextUtils.isEmpty(str)) {
				Toast.makeText(activity, getString(R.string.search_empty),
						Toast.LENGTH_SHORT).show();
				imgSearch.setEnabled(true);
				return;
			} else {
				WhereBuilder whereBuilder=WhereBuilder.b("typeTitle", "like", "%" + str + "%");
				initialData(TextUtils.isEmpty(str) ? null : whereBuilder );
			}

			break;

		case R.id.msg_manage_del:// 选中删除
			MsgUtil.clearNotificationMsg();
			if (adapter.isSelectAll) {// 全选
				EztDb.getInstance(activity).delDataWhere(new MsgType(), null);

				for (int i = 0; i < msgList.size(); i++) {
					String typeId = msgList.get(i).getTypeId();
					WhereBuilder whereBuilder=WhereBuilder.b("msgType", "=", typeId);
					ArrayList<MessageAll> childMsgList = EztDb.getInstance(
							activity).queryAll(new MessageAll(),
							whereBuilder, "_id desc");
					for (int j = 0; j < childMsgList.size(); j++) {
						MessageAll msgAll = childMsgList.get(j);
						if (msgAll.getClickState() == 0) {
							msgAll.setClickState(1);
							// 更新消息子类型点击状态
							EztDb.getInstance(activity).update(msgAll, whereBuilder);
						}
					}

				}

			} else {
				Set<Integer> checkedIds = adapter.checkedRecId;
				for (Integer pos : checkedIds) {
					EztDb.getInstance(activity).delItemData(msgList.get(pos));
					WhereBuilder whereBuilder=WhereBuilder.b("msgType", " =",  msgList.get(pos).getTypeId());
					
					ArrayList<MessageAll> childMsgList = EztDb.getInstance(
							activity).queryAll(
							new MessageAll(),
							whereBuilder, "_id desc");

					for (int j = 0; j < childMsgList.size(); j++) {
						MessageAll msgAll = childMsgList.get(j);
						if (msgAll.getClickState() == 0) {
							msgAll.setClickState(1);
							EztDb.getInstance(activity).update(
									msgAll,
									whereBuilder);// 更新消息子类型点击状态
						}
					}

				}
				adapter.checkedRecId.clear();// 清理所有内容
			}

			initialData(null);
			tvEdit.performClick();
			break;

		default:// 编辑/取消
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
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MsgType type = msgList.get(position);
		type.setClickState(1);// 点击状态
		type.setTypeCount(0);// 初始化类消息条数
		WhereBuilder whereBuilder=WhereBuilder.b("typeId", "=",type.getTypeId());
		EztDb.getInstance(activity).update(type,
				whereBuilder);// 更新消息父类型点击状态

		
		// 取出该类型下的所有子数据
		whereBuilder=WhereBuilder.b("msgType", "=",type.getTypeId());
		ArrayList<MessageAll> msgList = EztDb
				.getInstance(activity)
				.queryAll(new MessageAll(),
						whereBuilder, "_id desc");

		for (int i = 0; i < msgList.size(); i++) {
			MessageAll msgAll = msgList.get(i);
			if (msgAll.getClickState() == 0) {
				msgAll.setClickState(1);
				whereBuilder=WhereBuilder.b("_id ", "=",msgAll.get_id());
				EztDb.getInstance(activity).update(msgAll,
						whereBuilder );// 更新消息子类型点击状态
			}
		}
		Intent intent = new Intent(getActivity(),
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

//	@Override
//	public void onDestroyView() {
//		if (!AppManager.getAppManager().isLaunch("MainActivity")) {// 未存在（消息通知栏直接跳入消息箱）
//			Intent intent = new Intent(getActivity(), MainActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//
//		}
//		super.onDestroyView();
//	}

}

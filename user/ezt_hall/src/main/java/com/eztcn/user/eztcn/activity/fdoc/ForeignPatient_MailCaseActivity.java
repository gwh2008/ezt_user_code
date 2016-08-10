package com.eztcn.user.eztcn.activity.fdoc;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;

import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceDeptByHosActivity;
import com.eztcn.user.eztcn.activity.home.ChoiceHosActivity;
import com.eztcn.user.eztcn.adapter.PopupWindowAdapter;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 邮寄病历
 * @describe (外患)
 * @author ezt
 * @created 2015年2月27日
 */
public class ForeignPatient_MailCaseActivity extends FinalActivity implements
		OnItemClickListener {// OnClickListener

	@ViewInject(R.id.tv_money)
	private TextView tvMoney;

	@ViewInject(R.id.layout_hos)
	// , click = "onClick"
	private RelativeLayout layoutHos;

	@ViewInject(R.id.tv_hos)
	private TextView tvHos;

	@ViewInject(R.id.layout_dept)
	// , click = "onClick"
	private RelativeLayout layoutDept;

	@ViewInject(R.id.tv_dept)
	private TextView tvDept;

	@ViewInject(R.id.layout_level)
	// , click = "onClick"
	private RelativeLayout layoutLevel;

	@ViewInject(R.id.tv_level)
	private TextView tvLevel;

	@ViewInject(R.id.ill_describe_et)
	private EditText etDescribe;

	@ViewInject(R.id.bt)
	// , click = "onClick"
	private Button bt;

	private String hosId, deptId;
	private String hosName, deptName;

	/**
	 * 选择级别
	 */
	private Dialog deptChoice;

	private PopupWindowAdapter levelAdapter;

	private ListView ltDept;

	private TextView tvDeptTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mailcase);
		ViewUtils.inject(ForeignPatient_MailCaseActivity.this);
		loadTitleBar(true, "邮寄病历", null);

		// 选择医生级别
		deptChoice = new Dialog(this, R.style.ChoiceDialog);
		View dialogView = LinearLayout.inflate(this, R.layout.dialog_choice,
				null);
		ltDept = (ListView) dialogView.findViewById(R.id.dialog_lt);
		tvDeptTitle = (TextView) dialogView.findViewById(R.id.title);

		levelAdapter = new PopupWindowAdapter(mContext);
		ltDept.setAdapter(levelAdapter);
		ltDept.setOnItemClickListener(this);
		deptChoice.setCanceledOnTouchOutside(true);
		deptChoice.setContentView(dialogView);

	}

	@OnClick(R.id.layout_hos)
	private void choiceHos(View v) {// 选择医院
		startActivityForResult(new Intent(mContext, ChoiceHosActivity.class)
				.putExtra("requestFlag", 1).putExtra("isChoice", true), 1);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@OnClick(R.id.layout_dept)
	private void choiceDept(View v) {// 选择科室
		if (TextUtils.isEmpty(hosId)) {
			Toast.makeText(mContext, "请先选择就诊医院", Toast.LENGTH_SHORT).show();
			return;
		}
		startActivityForResult(new Intent(mContext,
				ChoiceDeptByHosActivity.class).putExtra("hosId", hosId)
				.putExtra("isAllSearch", true).putExtra("isChoice", true), 2);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@OnClick(R.id.layout_level)
	private void levelClick(View v) {// 医生级别
		String[] levels = new String[] { "级别1", "级别2", "级别3", "界别4", "级别5" };
		choiceLevel(levels, "选择医生级别");
	}

	@OnClick(R.id.bt)
	private void commitClick(View v) {// 提交
	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.layout_hos:// 选择医院
	// // startActivityForResult(
	// // new Intent(mContext, ChoiceHosActivity.class).putExtra(
	// // "requestFlag", 1).putExtra("isChoice", true), 1);
	// // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	// break;
	//
	// case R.id.layout_dept:// 选择科室
	// // if (TextUtils.isEmpty(hosId)) {
	// // Toast.makeText(mContext, "请先选择就诊医院", Toast.LENGTH_SHORT).show();
	// // return;
	// // }
	// // startActivityForResult(new Intent(mContext,
	// // ChoiceDeptByHosActivity.class).putExtra("hosId", hosId)
	// // .putExtra("isAllSearch", true).putExtra("isChoice", true),
	// // 2);
	// // overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	// break;
	//
	// case R.id.layout_level:// 医生级别
	// // String[] levels = new String[] { "级别1", "级别2", "级别3", "界别4", "级别5" };
	// // choiceLevel(levels, "选择医生级别");
	// break;
	//
	// // case R.id.bt:// 提交
	// //
	// // break;
	// // }
	//
	// }

	/**
	 * 弹出框选择区域
	 */
	private void choiceLevel(String data[], String title) {
		tvDeptTitle.setText(title);
		levelAdapter.setList(data);
		// 获取listview的高度
		int totalHeight = 0;
		for (int i = 0, len = levelAdapter.getCount(); i < len; i++) {
			View listItem = levelAdapter.getView(i, null, ltDept);
			listItem.measure(0, 0); // 计算子项View 的宽高
			int item_height = listItem.getMeasuredHeight()
					+ ltDept.getDividerHeight();
			totalHeight += item_height; // 统计所有子项的总高度
		}
		Window dialogWindow = deptChoice.getWindow();

		totalHeight = totalHeight
				+ ResourceUtils.dip2px(mContext, ResourceUtils.getXmlDef(
						mContext, R.dimen.dialog_title_bar_size));
		dialogWindow.setLayout((int) (getWindowWidth() * 0.8),
				totalHeight > getWindowHeight() / 2 ? getWindowHeight() / 2
						: totalHeight);
		if (!deptChoice.isShowing()) {
			deptChoice.show();
		} else {
			deptChoice.dismiss();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1) {// 选择医院
			if (intent != null) {
				hosName = intent.getStringExtra("hosName");
				hosId = intent.getStringExtra("hosId");
				if (!TextUtils.isEmpty(hosName))
					tvHos.setText(hosName);

				if (!TextUtils.isEmpty(deptName)) {
					deptName = "";
					tvDept.setText(deptName);
				}

				if (!TextUtils.isEmpty(deptId)) {
					deptId = "";
				}
			}

		} else if (requestCode == 2) {// 选择科室
			if (intent != null) {
				deptName = intent.getStringExtra("deptName");
				deptId = intent.getStringExtra("deptId");

				if (!TextUtils.isEmpty(deptName))
					tvDept.setText(deptName);
			}

		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String str = levelAdapter.getList().get(position);
		str = str.replace(" ", "");
		tvLevel.setText(str);
		deptChoice.dismiss();
	}
}

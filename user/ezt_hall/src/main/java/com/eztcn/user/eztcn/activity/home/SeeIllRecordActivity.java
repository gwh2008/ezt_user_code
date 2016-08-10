package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.MyMedicalRecordListActivity;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 专家看病历
 * @describe
 * @author ezt
 * @created 2015年1月13日
 */
public class SeeIllRecordActivity extends FinalActivity implements
		OnClickListener {

	@ViewInject(R.id.see_ill_free_tv)
	private TextView tvFree;//

	@ViewInject(R.id.see_ill_hos_tv)
	private TextView tvHosName;//

	@ViewInject(R.id.see_ill_dept_tv)
	private TextView tvDeptName;//

	@ViewInject(R.id.see_ill_doc_tv)
	private TextView tvDocName;//

	@ViewInject(R.id.see_ill_record_tv)
	private TextView tvRecordName;//

	@ViewInject(R.id.see_ill_hos_layout)//
	private RelativeLayout layoutHosName;//

	@ViewInject(R.id.see_ill_dept_layout)//
	private RelativeLayout layoutDeptName;//

	@ViewInject(R.id.see_ill_doc_layout)//
	private RelativeLayout layoutDocName;//

	@ViewInject(R.id.see_ill_record_layout)//
	private RelativeLayout layoutRecordName;//

	@ViewInject(R.id.see_ill_bt)//
	private Button bt;//

	private String hosId, deptId, docId;
	private String hosName, deptName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_ill_record);
		ViewUtils.inject(SeeIllRecordActivity.this);
		loadTitleBar(true, "专家看病历", null);
	}
	
	
	@OnClick(R.id.see_ill_hos_layout)
	public void onClick(View v) {
		// 选择医院
			startActivityForResult(
					new Intent(mContext, ChoiceHosActivity.class).putExtra(
							"requestFlag", 1).putExtra("isChoice", true), 1);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	
	}
	@OnClick(R.id.see_ill_dept_layout)
	public void see_ill_deptClick(View v) {
	// 选择科室
			if (TextUtils.isEmpty(hosId)) {
				Toast.makeText(mContext, "请先选择就诊医院", Toast.LENGTH_SHORT).show();
				return;
			}
			startActivityForResult(new Intent(mContext,
					ChoiceDeptByHosActivity.class).putExtra("hosId", hosId)
					.putExtra("isAllSearch", true).putExtra("isChoice", true),
					2);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	@OnClick(R.id.see_ill_doc_layout)
	public void see_ill_docClick(View v) {

		// 选择医生
			if (TextUtils.isEmpty(hosId)) {
				Toast.makeText(mContext, "请先选择就诊医院", Toast.LENGTH_SHORT).show();
				return;
			} else if (TextUtils.isEmpty(deptId)) {
				Toast.makeText(mContext, "请先选择就诊科室", Toast.LENGTH_SHORT).show();
				return;
			}

			startActivityForResult(
					new Intent(mContext, DoctorListActivity.class)
							.putExtra("type", 1)
							.putExtra("isAllSearch", true)
							// 默认为全站搜索进入
							.putExtra("isChoice", true)
							.putExtra("deptId", deptId)
							.putExtra("hosId", hosId)
							.putExtra(
									"hosName",
									TextUtils.isEmpty(hosName) ? "选择医院"
											: hosName)
							.putExtra(
									"deptName",
									TextUtils.isEmpty(deptName) ? "选择科室"
											: deptName), 3);
	}
	@OnClick(R.id.see_ill_record_layout)
	public void see_ill_recordClick(View v) {

		// 选择病历
			if (BaseApplication.patient == null) {
				HintToLogin(Constant.LOGIN_COMPLETE);
				return;
			}
			startActivityForResult(new Intent(mContext,
					MyMedicalRecordListActivity.class).putExtra("isChoice",
					true), 4);

	}
	@OnClick(R.id.see_ill_bt)
	public void see_illClick(View v) {

		// 确定
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1) {// 选择医院
			if (intent != null) {
				hosName = intent.getStringExtra("hosName");
				hosId = intent.getStringExtra("hosId");
				if (!TextUtils.isEmpty(hosName))
					tvHosName.setText(hosName);

				if (!TextUtils.isEmpty(deptName)) {
					deptName = "";
					tvDeptName.setText(deptName);
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
					tvDeptName.setText(deptName);
			}

		} else if (requestCode == 3) {// 选择医生

			if (intent != null) {
				hosName = intent.getStringExtra("hosName");
				deptName = intent.getStringExtra("deptName");
				String docName = intent.getStringExtra("docName");

				docId = intent.getStringExtra("docId");
				deptId = intent.getStringExtra("deptId");
				hosId = intent.getStringExtra("hosId");

				if (!TextUtils.isEmpty(hosName))
					tvHosName.setText(hosName);

				if (!TextUtils.isEmpty(deptName))
					tvDeptName.setText(deptName);

				if (!TextUtils.isEmpty(docName))
					tvDocName.setText(docName);
			}

		} else if (requestCode == 4) {// 选择病历
			if (intent != null) {
			}
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

}

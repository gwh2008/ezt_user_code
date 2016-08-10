package com.eztcn.user.eztcn.activity.mine;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.impl.MedicalRecordImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
/**
 * @title 创建病历（2步）
 * @describe
 * @author ezt
 * @created 2015年1月22日
 */
public class MyMedicalRecordCreateTwoActivity extends FinalActivity implements
		 IHttpResult {//OnClickListener,

	@ViewInject(R.id.title_tv)
	private TextView title_tv;

	@ViewInject(R.id.intro_symptom)//, click = "onClick"
	private EditText etSymptom;// 症状描述

	@ViewInject(R.id.intro_diagnose)//, click = "onClick"
	private EditText etDiagnose;// 医生诊断描述

	@ViewInject(R.id.intro_drug_use)//, click = "onClick"
	private EditText etDrugUse;// 用药情况

	@ViewInject(R.id.intro_allergy)//, click = "onClick"
	private EditText etAllergy;// 药物过敏史

	@ViewInject(R.id.last_step_bt)//, click = "onClick"
	private Button btLastStep;// 上一步

	@ViewInject(R.id.next_step_bt)//, click = "onClick"
	private Button btNextStep;// 完成

	private MedicalRecord record;
	private String mId;// 病历id

	private int enterType = 0;// 判断是创建还是编辑
	private TextView tvBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymedicalrecord_create_two);
		ViewUtils.inject(MyMedicalRecordCreateTwoActivity.this);
		loadTitleBar(true, "创建病历", null);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			record = (MedicalRecord) bundle.getSerializable("record");
			enterType = bundle.getInt("enterType");
			if (record == null) {
				mId = bundle.getString("mId");
			} else {
				mId = record.getId();
			}
		}
		if (enterType == 0) {
			title_tv.setText("创建病历");
		} else {
			title_tv.setText("编辑病历");
		}
		initRecord();
	}
	

	public void initRecord() {
		if (record == null) {
			return;
		}
		if ("暂无".equals(record.getSymptomsDetail())) {
			etDiagnose.setHint("症状和检验报告等相关检查描述");
		} else {
			etSymptom.setText(record.getSymptomsDetail());
		}
		if ("暂无".equals(record.getDiagnosisDetial())) {
			etDiagnose.setHint("医生诊断描述");
		} else {
			etDiagnose.setText(record.getDiagnosisDetial());
		}
		if ("暂无".equals(record.getDrugDetail())) {
			etDrugUse.setHint("用药情况");
		} else {
			etDrugUse.setText(record.getDrugDetail());
		}
		if ("暂无".equals(record.getAllergy())) {
			etAllergy.setHint("药物过敏史");
		} else {
			etAllergy.setText(record.getAllergy());
		}

	}

	/**
	 * 创建病历第二步
	 */
	public void createRecord(String allergy, String strSymptom,
			String strDiagnose, String strDrugUse) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("mId", mId);
		params.addBodyParameter("allergy", allergy);
		params.addBodyParameter("symptomsDetail", strSymptom);
		params.addBodyParameter("diagnosisDetial", strDiagnose);
		params.addBodyParameter("drugDetail", strDrugUse);
		new MedicalRecordImpl().createEMR_second(params, this);
		showProgressToast();
	}
	
	@OnClick(R.id.last_step_bt)// 上一步
	private void last_step_btClick(View v){
		finish();
	}
	
	@OnClick(R.id.next_step_bt)// 完成
	private void next_step_btClick(View v){
		String strAllergy = etAllergy.getText().toString();

		String strSymptom = etSymptom.getText().toString();

		String strDiagnose = etDiagnose.getText().toString();

		String strDrugUse = etDrugUse.getText().toString();

		if (TextUtils.isEmpty(strSymptom)) {
			strSymptom = "暂无";
		}

		if (TextUtils.isEmpty(strDiagnose)) {
			strDiagnose = "暂无";
		}

		if (TextUtils.isEmpty(strDrugUse)) {
			strDrugUse = "暂无";
		}

		if (TextUtils.isEmpty(strAllergy)) {
			strAllergy = "暂无";
		}
		createRecord(strAllergy, strSymptom, strDiagnose, strDrugUse);
	}


//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.last_step_bt:// 上一步
//			finish();
//			break;
//
//		case R.id.next_step_bt:// 完成
//			String strAllergy = etAllergy.getText().toString();
//
//			String strSymptom = etSymptom.getText().toString();
//
//			String strDiagnose = etDiagnose.getText().toString();
//
//			String strDrugUse = etDrugUse.getText().toString();
//
//			if (TextUtils.isEmpty(strSymptom)) {
//				strSymptom = "暂无";
//			}
//
//			if (TextUtils.isEmpty(strDiagnose)) {
//				strDiagnose = "暂无";
//			}
//
//			if (TextUtils.isEmpty(strDrugUse)) {
//				strDrugUse = "暂无";
//			}
//
//			if (TextUtils.isEmpty(strAllergy)) {
//				strAllergy = "暂无";
//			}
//			createRecord(strAllergy, strSymptom, strDiagnose, strDrugUse);
//			break;
//		}
//
//	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		if (object == null) {
			return;
		}
		Integer taskID = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];
		switch (taskID) {
		case HttpParams.CREATE_EMR_2:// 创建病历第二步
			if (!isSuc) {
				Toast.makeText(getApplicationContext(), "服务器繁忙，请重试！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(this,
					MyMedicalRecordCreateThreeActivity.class);
			if (record == null) {
				intent.putExtra("mId", mId);
			} else {
				intent.putExtra("record", record);
			}
			intent.putExtra("enterType", enterType);
			startActivity(intent);
			break;
		}
	}

}

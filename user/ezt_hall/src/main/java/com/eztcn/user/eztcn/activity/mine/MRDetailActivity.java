package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.BitmapUtils;
import xutils.ViewUtils;
import xutils.bitmap.BitmapCommonUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.CreateEMRAdapter;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.MedicalRecord;
import com.eztcn.user.eztcn.bean.Medical_img;
import com.eztcn.user.eztcn.config.EZTConfig;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.customView.RoundImageView;
import com.eztcn.user.eztcn.impl.MedicalRecordImpl;

/**
 * @title 病历详情
 * @describe 病历详细信息
 * @author ezt
 * @created 2014年12月12日
 */
public class MRDetailActivity extends FinalActivity implements OnClickListener,
		IHttpResult {

	private TextView editTv;// 编辑按钮

	@ViewInject(R.id.name_tv)
	private TextView tvName;// 患者名称

	@ViewInject(R.id.sex)
	private TextView tvSex;// 性别

	@ViewInject(R.id.allergy)
	private TextView tvAlleray;// 药物过敏史

	@ViewInject(R.id.item_doc_name)
	private TextView tvDocName;// 医生名称

	@ViewInject(R.id.item_hos_name)
	private TextView tvHosName;// 所属医院

	@ViewInject(R.id.clinical_time)
	private TextView tvClinicalTime;// 就诊时间

	@ViewInject(R.id.illness_desribe)
	private TextView tvIllnessDesribe;// 病情描述

	@ViewInject(R.id.drug_detail)
	private TextView tvDrugDetail;// 用药情况

	@ViewInject(R.id.diagnosis_detial)
	private TextView tvDiagnosisDetial;// 检验报告、数据

	@ViewInject(R.id.disease_tv)
	private TextView tvDisease;// 所犯疾病

	@ViewInject(R.id.head_pic)
	private RoundImageView imgUserHead;// 用户患者头像

	@ViewInject(R.id.item_doc_img)
	private RoundImageView imgDocHead;// 医生头像

	@ViewInject(R.id.scrollView1)
	private ScrollView scroll;

	@ViewInject(R.id.imgs_lv)
	private MyListView imgList;

	private MedicalRecord record;
	private CreateEMRAdapter imgAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mrdetail);
		ViewUtils.inject(MRDetailActivity.this);
		String strName = getIntent().getStringExtra("recordNum");
		String id = getIntent().getStringExtra("recordId");
		editTv = loadTitleBar(true, strName, "编辑");
		editTv.setVisibility(View.GONE);
		editTv.setBackgroundResource(R.drawable.selector_main_btn_bg);
		editTv.setOnClickListener(this);
		initialData(id);
	}

	/**
	 * 获取病历详情
	 */
	private void initialData(String id) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		MedicalRecordImpl impl = new MedicalRecordImpl();
		params.addBodyParameter("mrId", id);
		impl.getMyIllDetails(params, this);
		showProgressToast();

	}

	/**
	 * 初始化图片列表
	 * 
	 * @param list
	 */
	private void initImgList(List<Medical_img> list) {
		if (list == null) {
			list = new ArrayList<Medical_img>();
		}
		imgAdapter = new CreateEMRAdapter(mContext, 1);
		imgList.setAdapter(imgAdapter);
		imgAdapter.setList(list);
	}

	@Override
	public void onClick(View v) {
		startActivityForResult(
				new Intent(mContext, MyMedicalRecordCreateOneActivity.class)
						.putExtra("record", record).putExtra("enterType", 1), 2);
	}

	@Override
	public void result(Object... object) {
		boolean isSuc = (Boolean) object[1];

		if (!isSuc) {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();
			return;
		}
		record = (MedicalRecord) object[2];
		if (record != null) {
			editTv.setVisibility(View.VISIBLE);
			scroll.setVisibility(View.VISIBLE);
			tvName.setText(record.getStrName());// 患者名称

			tvDisease.setText(TextUtils.isEmpty(record.getDisease()) ? ""
					: "所犯疾病:\n\t" + record.getDisease());// 所犯疾病

			tvDocName.setText(record.getDoctorName());// 医生名称

			tvSex.setText(record.getStrSex());// 医生性别

			tvHosName.setText(record.getHosName());// 所属医院

			tvClinicalTime.setText(record.getClinicalTime());// 就诊时间

			tvAlleray
					.setText(TextUtils.isEmpty(record.getSymptomsDetail()) ? ""
							: "药物过敏史:\n\t" + record.getAllergy());// 药物过敏史

			tvIllnessDesribe.setText(TextUtils.isEmpty(record
					.getSymptomsDetail()) ? "" : "病情描述:\n\t"
					+ record.getSymptomsDetail());// 病情描述

			tvDrugDetail.setText(TextUtils.isEmpty(record.getDrugDetail()) ? ""
					: "用药情况:\n\t" + record.getDrugDetail()); // 用药情况

			tvDiagnosisDetial.setText(TextUtils.isEmpty(record
					.getDiagnosisDetial()) ? "" : "检验报告:\n\t"
					+ record.getDiagnosisDetial());// 检验报告、数据
			Bitmap defaultBit;
			if ("男".equals(record.getStrSex())) {
				defaultBit = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.userman);
			} else {
				defaultBit = BitmapFactory.decodeResource(
						mContext.getResources(), R.drawable.userwomen);
			}
			final String imgurl = EZTConfig.DOC_PHOTO + record.getHeadImgUrl();
//			FinalBitmap.create(this).display(imgUserHead, imgurl, defaultBit,
//					defaultBit);
			BitmapUtils bitmapUtils=new BitmapUtils(MRDetailActivity.this);
			bitmapUtils.configDefaultBitmapMaxSize(BitmapCommonUtils.getScreenSize(mContext).scaleDown(3));
		    bitmapUtils.configDefaultLoadingImage(defaultBit);
			bitmapUtils.configDefaultLoadFailedImage(defaultBit);
			bitmapUtils.display(imgUserHead, imgurl);
			
			initImgList(record.getImgList());
		} else {
			Toast.makeText(mContext, getString(R.string.request_fail),
					Toast.LENGTH_SHORT).show();
		}
		hideProgressToast();

	}

}

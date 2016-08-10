package com.eztcn.user.eztcn.activity.mine;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.bean.EztUser;
import com.eztcn.user.eztcn.utils.ResourceUtils;
import com.eztcn.user.hall.model.PatientBean;
import com.eztcn.user.hall.model.bean.Patient;

/**
 * @title 会员中心
 * @describe
 * @author ezt
 * @created 2015年6月30日
 */
public class MemberCenterActivity extends FinalActivity {

	@ViewInject(R.id.phone)
	private TextView phone;
	@ViewInject(R.id.mIntegral)
	private TextView mIntegral;// 会员积分
	@ViewInject(R.id.mGrowthValue)
	private TextView mGrowthValue;// 会员成长值
	@ViewInject(R.id.lookPrivilege)//, click = "onClick"
	private RelativeLayout lookPrivilege;// 查看特权
	@ViewInject(R.id.earnIntegral)//, click = "onClick"
	private RelativeLayout earnIntegral;// 赚取积分
	@ViewInject(R.id.exchange)//, click = "onClick"
	private TextView exchange;// 积分兑换
	@ViewInject(R.id.levelName)
	private TextView levelName;
	@ViewInject(R.id.integral)
	private TextView integral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.membercenter);
		ViewUtils.inject(MemberCenterActivity.this);
		loadTitleBar(true, "会员中心", null);
		init();
	}

	/**
	 * 初始化会员信息
	 */
	public void init() {
		if (BaseApplication.patient == null) {
			return;
		}
		PatientBean patient = BaseApplication.patient;
		String p = patient.getEpMobile();// 手机号
		if (!TextUtils.isEmpty(p)) {
			String temp = p.substring(3, p.length() - 4);
			phone.setText(p.replace(temp, "****"));
		}
	//	integral.setText(patient.getMemberIntegral() + "");
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String vip = bundle.getString("memberlevel");
			levelName.setText(vip);
			levelName.setBackground(ResourceUtils.setBackgroundColor(
					Color.parseColor("#ffc451"), 6));
		}
	}
}

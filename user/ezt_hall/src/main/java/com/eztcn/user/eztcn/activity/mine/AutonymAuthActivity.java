package com.eztcn.user.eztcn.activity.mine;

import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.impl.UserImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.Logger;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 实名认证（完善资料）
 * @describe
 * @author ezt
 * @created 2015年1月22日
 */
public class AutonymAuthActivity extends FinalActivity implements IHttpResult {

	@ViewInject(R.id.name)
	private EditText name;
	@ViewInject(R.id.idCard)
	private EditText idCard;
	@ViewInject(R.id.sex)//, click = "onClick"
	private TextView sex;
	@ViewInject(R.id.auth)//, click = "onClick"
	private Button auth;

	private int sexStr = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autonymauth);
		ViewUtils.inject(AutonymAuthActivity.this);
		loadTitleBar(true, "实名认证", null);
	}
	
	@OnClick(R.id.auth)
	private void authClick(View v){
		if (judgeData()) {
			authName();
		}
	}
	
	@OnClick(R.id.sex)
	private void sexClick(View v){
		String[] arrys = { "男", "女" };
		showSelector(arrys);
	}
	
		
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.auth:
//			if (judgeData()) {
//				authName();
//			}
//			break;
//		case R.id.sex:
//			String[] arrys = { "男", "女" };
//			showSelector(arrys);
//			break;
//		}
//	}

	/**
	 * 判断数据
	 */
	public boolean judgeData() {
		if (BaseApplication.patient == null) {

			return false;
		}
		String n = name.getText().toString();
		if (TextUtils.isEmpty(n)) {
			Toast.makeText(mContext, "姓名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (n.length() < 2 || n.length() > 4) {
			Toast.makeText(mContext, "请输入正确的姓名", Toast.LENGTH_SHORT).show();
			return false;
		}
		String ic = idCard.getText().toString();
		if (TextUtils.isEmpty(ic)) {
			Toast.makeText(mContext, "身份证不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (!StringUtil.validateCard(ic)) {
			Toast.makeText(mContext, "身份证格式有误", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	/**
	 * 保存
	 */
	public void authName() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", BaseApplication.patient.getUserId() + "");
		params.addBodyParameter("name", name.getText().toString());
		if (sex.getText().equals("男")) {
			params.addBodyParameter("sex", "0");
		} else {
			params.addBodyParameter("sex", "1");
		}
		params.addBodyParameter("idnoType", "1");
		params.addBodyParameter("idno", idCard.getText().toString());
		new UserImpl().userAutonymName(params, this);
		showProgressToast();
	}

	public void refreshData() {
		if (BaseApplication.patient != null) {
			BaseApplication.patient.setEpName(name.getText().toString());
			BaseApplication.patient.setEpPid(idCard.getText().toString());
			BaseApplication.patient.setEpSex(sexStr);
		}
	}

	/**
	 * 选择窗口
	 */
	public void showSelector(String[] arrys) {
		Builder builder = new Builder(this);
		builder.setItems(arrys, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					sex.setText("男");
				} else {
					sex.setText("女");
				}
				sexStr = which;
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void result(Object... object) {
		hideProgressToast();
		String msg;
		if (object == null) {
			Toast.makeText(getApplicationContext(), getString(R.string.service_error), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		Object[] obj = object;
		Integer taskID = (Integer) obj[0];
		if (taskID == null) {
			Toast.makeText(getApplicationContext(), getString(R.string.service_error), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		boolean status = (Boolean) obj[1];
		if (!status) {
          Logger.i("认证", obj[3]);
			return;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) obj[2];
		if (map == null || map.size() == 0) {
			Toast.makeText(getApplicationContext(), "认证失败", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		boolean flag = (Boolean) map.get("flag");
		if (taskID == HttpParams.USER_AUTONYM) {
			if (flag) {
				msg = "认证成功";
				refreshData();
				finish();
			} else {
				msg = (String) map.get("msg");
			}
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
					.show();
		}
	}
}

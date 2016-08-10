package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.home.HealthCardServiceDetailActivity;
import com.eztcn.user.eztcn.activity.home.MyDialog;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogCancle;
import com.eztcn.user.eztcn.activity.home.MyDialog.DialogSure;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter.ItemBtOnclick;
import com.eztcn.user.eztcn.api.IEztServiceCard;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 我的医指通龙卡
 * @describe
 * @author ezt
 * @created 2015年6月16日
 */
public class MyDragonCardActivity extends FinalActivity implements
		ItemBtOnclick, IHttpResult,
		DialogCancle, DialogSure {
	/**
	 * 未激活
	 */
	@ViewInject(R.id.not_activite_layout)
	private LinearLayout layout1;

	@ViewInject(R.id.title_tv)
	private TextView tvTitle;

	@ViewInject(R.id.info_tv)
	private TextView tvIntro;

	@ViewInject(R.id.activate_bt)//, click = "onClick"
	private Button btActivate;

	@ViewInject(R.id.img)
	private ImageView imgCard;

	/**
	 * 已激活
	 */
	@ViewInject(R.id.card_intro_layout)
	private LinearLayout layout2;

	@ViewInject(R.id.cardnum_tv)
	private TextView tvCardNum;// 卡号

	@ViewInject(R.id.card_state_tv)
	private TextView tvCardState;// 状态

	@ViewInject(R.id.card_date_tv)
	private TextView tvCardDate;// 有效期

	@ViewInject(R.id.service_intro_tv)
	private TextView tvServiceIntro;// 服务内容

	@ViewInject(R.id.service_know_tv)
	private TextView tvServiceKnow;// 服务须知

	@ViewInject(R.id.service_flow_tv)
	private TextView tvServiceFlow;// 服务流程

	@ViewInject(R.id.intro_img)
	private ImageView imgCardIntro;// 卡详情图片

	@ViewInject(R.id.myListView)//, itemClick = "onItemClick"
	private MyListView ltServices;// 服务列表

	private LightAccompanyServiceAdapter adapter;

	// private boolean isActivate;// 是否激活

	private final String usedUrl = "http://192.168.0.162/guanwang/Ccbh5/Index/";// 服务使用地址
	// 点击解绑之后弹出的自定义对话框
	private MyDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthdragon);
		ViewUtils.inject(MyDragonCardActivity.this);
		intialData(String.valueOf(BaseApplication.patient.getUserId()));
	}
	

	@OnClick(R.id.activate_bt)// 激活绑定
	private void activate_btClick(View v){
		startActivity(new Intent(getApplicationContext(),
				MyDragonCardActivateActivity.class));
	}
	/**
	 * 获取详情
	 * 
	 * @param cardId
	 */
	private void intialData(String userId) {

		RequestParams params=new RequestParams();
		params.addBodyParameter("userId", userId);
		EztServiceCardImpl api = new EztServiceCardImpl();
		api.getHealthDragonInfo(params, this);
		showProgressToast();
	}

	@Override
	public void adapterOnclick(int pos, int status) {

		if (status == 0) {// 可使用
			String appUsage = adapter.getList().get(pos).getAppUsage();
			String url = usedUrl + appUsage + "/userid/" + 1 + "/objectId/"
					+ adapter.getList().get(pos).getItemId() + ".html";
			startActivity(new Intent(getApplicationContext(),
					CardUsedActivity.class).putExtra("url", url).putExtra(
					"title", adapter.getList().get(pos).getItemName()));
		} else if (status == 2) {// 使用中

		}
	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];

		if (isSuc) {

			Map<String, Object> map = (Map<String, Object>) object[2];
			if (map != null && map.size() != 0) {
				layout2.setVisibility(View.VISIBLE);
				ArrayList<LightAccompanying> list = (ArrayList<LightAccompanying>) map
						.get("itemList");
				adapter.setList(list);
				// hosList = (ArrayList<Hospital>) map.get("hosList");
				// remark = (String) map.get("remark");
				String cardNo = map.get("cardNo").toString();
				String sTime = map.get("startTime").toString();
				String eTime = map.get("endTime").toString();

				if (!TextUtils.isEmpty(sTime) && !TextUtils.isEmpty(eTime)) {
					sTime = sTime.split(" ")[0];
					eTime = eTime.split(" ")[0];
				}

				String strState = "无";
				if (map.get("state") != null) {
					int state = (Integer) map.get("state");
					switch (state) {
					case 0:
						strState = "未使用";
						break;

					case 1:
						strState = "已使用";
						break;

					case 2:
						strState = "使用中";
						break;
					}
				}
				for (int i = 0; i < cardNo.length(); i++) {
					if (i > cardNo.length() - 4) {
						try {
							String a = StringUtil.replace(cardNo, i - 1, "*");
							String b = StringUtil.replace(a, i - 2, "*");
							String c = StringUtil.replace(b, i - 3, "*");
							String d = StringUtil.replace(c, i - 4, "*");
							cardNo = d;
							break;
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
				tvCardNum.setText(cardNo);

				tvCardState.setText(strState);

				tvCardDate.setText(sTime + " 至 " + eTime);
				// 添加代码 查询已经激活后初始化
				adapter = new LightAccompanyServiceAdapter(mContext);
				adapter.click(this);
				ltServices.setAdapter(adapter);
				loadTitleBar(true, "建行医指通龙卡",null );//TextView unBind =  "解除绑定"
			} else {
				// 未激活
				layout1.setVisibility(View.VISIBLE);
				loadTitleBar(true, "建行医指通龙卡", null);
			}
		} else {
			layout1.setVisibility(View.VISIBLE);
		loadTitleBar(true, "建行医指通龙卡", null);
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		hideProgressToast();

	}

	@OnItemClick(R.id.myListView)// 激活绑定
	private void myListViewClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		LightAccompanying l = adapter.getList().get(position);
		String name = l.getItemName();
		String hcuId = l.getId();
		String itemId = l.getItemId();
		String appUsage = l.getAppUsage();
		String url = usedUrl + appUsage + "/userid/" + 1 + "/objectId/"
				+ itemId + ".html";
		startActivity(new Intent(mContext,
				HealthCardServiceDetailActivity.class).putExtra("title", name)
				.putExtra("hcuId", hcuId).putExtra("itemId", itemId)
				.putExtra("url", url));
	}
	@Override
	public void dialogSure() {
		// TODO
		if (null != dialog)
			dialog.dissMiss();
	}

	@Override
	public void dialogCancle() {
		if (null != dialog)
			dialog.dissMiss();
	}

}

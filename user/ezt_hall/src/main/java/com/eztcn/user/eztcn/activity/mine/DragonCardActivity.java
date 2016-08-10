/**
 * 
 */
package com.eztcn.user.eztcn.activity.mine;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter;
import com.eztcn.user.eztcn.adapter.LightAccompanyServiceAdapter.ItemBtOnclick;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.DragonCard;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.customView.MyListView;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @author Liu Gang
 * 
 *         2015年11月26日 下午5:00:40
 * 
 */
public class DragonCardActivity extends FinalActivity implements ItemBtOnclick,
		IHttpResult{//, OnItemClickListener 

	@ViewInject(R.id.title_tv)
	private TextView tvTitle;

	@ViewInject(R.id.info_tv)
	private TextView tvIntro;
	@ViewInject(R.id.img)
	private ImageView imgCard;

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


	private final String usedUrl = "http://192.168.0.162/guanwang/Ccbh5/Index/";// 服务使用地址
	private DragonCard card;
	private List<LightAccompanying> serverList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragon);
		ViewUtils.inject(DragonCardActivity.this);
		getHealthDragonInfo();

		loadTitleBar(true, "建行医指通龙卡", null);

	}

	private void getHealthDragonInfo() {
		if (BaseApplication.patient == null) {
			return;
		}
		RequestParams params=new RequestParams();
		 params.addBodyParameter("CustID", BaseApplication.patient.getEpPid());
		 EztServiceCardImpl api = new EztServiceCardImpl();
		api.getCCbInfo(params, this);
		api.getItemDetail(params, this);
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
			int type = (Integer) object[0];

			switch (type) {
			case HttpParams.GET_ITEM_DETAIL: {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("flag")) {
					if ((Boolean) map.get("flag")) {
						if (map.containsKey("data")) {
							serverList = (List<LightAccompanying>) map
									.get("data");
						}
					}
				}
			}
				break;
			default: {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map.containsKey("flag")) {
					if ((Boolean) map.get("flag")) {

						// 绑定卡了
						if (map.containsKey("data")) {

							if (null != map && map.size() != 0
									&& map.containsKey("data")) {

								// 添加代码 查询已经激活后初始化
								adapter = new LightAccompanyServiceAdapter(
										mContext);
								adapter.click(this);
								ltServices.setAdapter(adapter);

								card = (DragonCard) map.get("data");
								List<LightAccompanying> list = card
										.getServerList();

								adapter.setList(list);
								String cardNo = card.getBankCardId();
								String strState = "使用中";
								for (int i = 0; i < cardNo.length(); i++) {
									if (i > cardNo.length() - 4) {
										try {
											String a = StringUtil.replace(
													cardNo, i - 1, "*");
											String b = StringUtil.replace(a,
													i - 2, "*");
											String c = StringUtil.replace(b,
													i - 3, "*");
											String d = StringUtil.replace(c,
													i - 4, "*");
											cardNo = d;
											break;
										} catch (Throwable e) {
											e.printStackTrace();
										}
									}
								}
								tvCardNum.setText(cardNo);

								tvCardState.setText(strState);
								if (StringUtils.isEmpty(card.getValid())) {
									tvCardDate.setText(card.getValid());
								}

							}

						}

					}
				}
			}
				break;
			}

		}
		hideProgressToast();

	}
	
	@OnItemClick(R.id.myListView)
	private void myListViewItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		/**
		 * 详情
		 */
		LightAccompanying l = adapter.getList().get(position);
		String serverId = l.getItemId();
		for (int i = 0; i < serverList.size(); i++) {
			String tempId = serverList.get(i).getId();
			if (serverId.equals(tempId)) {

			}
		}
	}
}

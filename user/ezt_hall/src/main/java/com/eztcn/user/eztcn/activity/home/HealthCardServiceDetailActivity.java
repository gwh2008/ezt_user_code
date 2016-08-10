package com.eztcn.user.eztcn.activity.home;

import java.util.Map;

import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.LightAccompanying;
import com.eztcn.user.eztcn.impl.EztServiceCardImpl;
import com.eztcn.user.eztcn.utils.ResourceUtils;

/**
 * @title 健康卡服务详情
 * @describe
 * @author ezt
 * @created 2015年3月31日
 */

public class HealthCardServiceDetailActivity extends FinalActivity implements
		IHttpResult {// OnClickListener,

	@ViewInject(R.id.scrollView1)
	private ScrollView scroll;

	@ViewInject(R.id.bottom_layout)
	private RelativeLayout layoutBottom;

	@ViewInject(R.id.service_intro_tv)
	private TextView tvServiceIntro;// 服务内容

	@ViewInject(R.id.service_know_tv)
	private TextView tvServiceKnow;// 服务须知

	@ViewInject(R.id.service_flow_tv)
	private TextView tvServiceFlow;// 服务流程

	@ViewInject(R.id.used_tv)
	// , click = "onClick"
	private TextView tvUsed;// 使用按钮

	@ViewInject(R.id.num_tv)
	private TextView tvNum;// 使用数量

	private String title;

	private String url;// 使用地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_service_detail);
		ViewUtils.inject(HealthCardServiceDetailActivity.this);
		title = getIntent().getStringExtra("title");
		loadTitleBar(true, title, null);
		String itemId = getIntent().getStringExtra("itemId");
		String hcuId = getIntent().getStringExtra("hcuId");
		url = getIntent().getStringExtra("url");
		initialData(itemId, hcuId);
	}

	// @Override
	// public void onClick(View v) {

	@OnClick(R.id.used_tv)
	public void click(View v) {
		int state = (Integer) v.getTag();

		switch (state) {
		case 0:// 可使用
				// startActivity(new Intent(getApplicationContext(),
				// CardUsedActivity.class).putExtra("url",
				// "http://www.baidu.com").putExtra("title", title));
			hintTelDialog(getString(R.string.health_tel_num), "确定拨打健康服务电话？");
			break;

		case 2:// 使用中
			break;
		}

	}

	/**
	 * 获取服务项详情
	 */
	private void initialData(String itemId, String hcuId) {

		// IEztServiceCard api = new EztServiceCardImpl();
		// HashMap<String, Object> params = new HashMap<String, Object>();
		// params.put("itemId", itemId);
		// params.put("hcuId", hcuId);

		EztServiceCardImpl api = new EztServiceCardImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("itemId", itemId);
		params.addBodyParameter("hcuId", hcuId);
		api.getItemDetail(params, this);
		showProgressToast();

	}

	@Override
	public void result(Object... object) {

		boolean isSuc = (Boolean) object[1];
		if (isSuc) {
			Map<String, Object> map = (Map<String, Object>) object[2];
			scroll.setVisibility(View.VISIBLE);
			layoutBottom.setVisibility(View.VISIBLE);
			if (map != null && map.size() != 0) {
				LightAccompanying l = (LightAccompanying) map.get("item");

				String strRemark = l.getRemark();
				if (strRemark != null) {
					tvServiceIntro.setText(Html.fromHtml(strRemark));// 服务内容
				}
				String strNotice = l.getNotice();
				if (strNotice != null) {
					tvServiceKnow.setText(Html.fromHtml(strNotice));// 服务须知
				}
				String strProcess = l.getProcess();
				if (strProcess != null) {
					tvServiceFlow.setText(Html.fromHtml(strProcess));// 服务流程
				}
				String strNums = "未使用次数 " + l.getRemainNums() + "次";

				SpannableStringBuilder style = new SpannableStringBuilder(
						strNums);
				style.setSpan(
						new ForegroundColorSpan(getResources().getColor(
								R.color.main_txt_color)), 0, 5,
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				style.setSpan(
						new AbsoluteSizeSpan(ResourceUtils.getXmlDef(mContext,
								R.dimen.small_size), true), 0, 5,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				tvNum.setText(style);
				switch (l.getItemStatus()) {

				case 0:// 可使用
					tvUsed.setText("使用服务");
					break;
				case 1:// 使用中
					tvUsed.setText("已使用");
					tvUsed.setBackgroundResource(R.drawable.shape_border_small_light_gray);
					break;

				case 2:// 使用中
					tvUsed.setText("使用中");
					break;
				}

				tvUsed.setTag(l.getItemStatus());
			}

		} else {
			Toast.makeText(mContext, getString(R.string.service_error),
					Toast.LENGTH_SHORT).show();

		}
		hideProgressToast();

	}

}

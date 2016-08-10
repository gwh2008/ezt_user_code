package com.eztcn.user.eztcn.activity.home;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.InformationActivity;
import com.eztcn.user.eztcn.activity.mine.MyHealthCardActivity;
import com.eztcn.user.eztcn.activity.mine.MyOrderListActivity;
import com.eztcn.user.eztcn.activity.mine.MyRecordActivity;
/**
 * @title 咨询成功页面（电话医生）
 * @describe 立即咨询，预约咨询，预约登记成功页面
 * @author ezt
 * @created 2014年12月12日
 */
public class SucHintActivity extends FinalActivity implements OnClickListener {

	@ViewInject(R.id.immediate_suc_text)//, click = "onClick"
	private TextView tvHintInfo;// 提示内容

	@ViewInject(R.id.immediate_suc_hint)
	private TextView tvHintTitle;// 预约咨询提醒标题

	@ViewInject(R.id.more)//, click = "onClick"
	private TextView tvMore;
	private int type;// 1为立即咨询跳入，2为预约咨询跳入，3为预约登记跳入
						// ，4为预约挂号跳入，5为免费提问，6快速求助,7健康卡激活,8健康卡购买成功
    private  ImageView imgHome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_immediate_suc);
		ViewUtils.inject(SucHintActivity.this);
		type = getIntent().getIntExtra("type", 0);
		String title = "";
		String hint = "";
		SpannableStringBuilder style = null;
		switch (type) {
		case 1:
			hint = "正在等待医生拨打您的电话，请您保持手机畅通，谢谢！";
			title = "支付成功";
			style = new SpannableStringBuilder(hint);
			// style.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16,
			// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("拨号成功");
			break;

		case 2:
			title = "预约成功";
			hint = "请保持手机畅通，详情可在我的咨询里面查看，谢谢";

			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			// style.setSpan(
			// new AbsoluteSizeSpan(ResourceUtils.getXmlDef(mContext,
			// R.dimen.small_size), true), 12, 16,
			// Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("预约咨询成功");
			//BaseApplication.patient.setFirstAppoint(true);
			break;

		case 3:
			title = "登记成功";
			hint = "请保持手机畅通，详情可在我的记录里面查看，谢谢!\n提示：需等待医生确认回复后,在登记详情页面确认预约挂号";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("预约登记成功");
			break;

		case 4:
			title = "预约成功";
			hint = "请您按时就诊，详情可在我的记录里面查看，如果突发情况不能就诊，请进行退号，让给有需求的患者，谢谢。";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 11, 15,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			// style.setSpan(
			// new AbsoluteSizeSpan(ResourceUtils.getXmlDef(mContext,
			// R.dimen.small_size), true), 11, 15,
			// Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("预约成功");
			// tvDidi.setVisibility(View.VISIBLE);//滴滴快车红包
			break;

		case 5:
			title = "提问成功";
			hint = "请保持手机畅通，详情可在我的记录里面查看，谢谢";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("免费提问成功");
			break;

		case 6:
			title = "求助成功";
			hint = "请保持手机畅通，详情可在我的记录里面查看，谢谢";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 16,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("求助成功");
			break;

		case 7:
			title = "激活成功";
			hint = "激活成功，可在我/我的钱包/医指通健康卡页面查看详情和使用，谢谢";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 7, 20,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("激活成功");
			break;

		case 8:
			title = "支付成功";
			hint = "支付成功，可在我/我的钱包/我的订单页面查看详情和使用，谢谢";
			style = new SpannableStringBuilder(hint);
			style.setSpan(new ForegroundColorSpan(Color.BLUE), 7, 18,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			tvHintTitle.setVisibility(View.VISIBLE);
			tvHintTitle.setText("支付成功");

			break;

		}

		tvHintInfo.setText(style);
//		ImageView imgHome
		imgHome = loadTitleBar(false, title, R.drawable.ic_home);
		imgHome.setOnClickListener(this);
	}
	@OnClick(R.id.more)
	public void moreClick(View v){
		// 更多
					BackHome(this, 0);
					Intent intent = new Intent(mContext, InformationActivity.class);
					intent.putExtra("flag", 1);
					startActivity(intent);
					finish();
	}
	@OnClick(R.id.immediate_suc_text)
	public void immediate_sucClick(View v){
		// 跳转相关页面
					BackHome(this, 0);
					switch (type) {
					case 2:// 电话预约

						startActivity(new Intent(mContext, MyRecordActivity.class)
								.putExtra("type", 2));
						break;

					case 3:// 预约登记
						startActivity(new Intent(mContext, MyRecordActivity.class)
								.putExtra("type", 3));
						break;

					case 4:// 挂号预约
						startActivity(new Intent(mContext, MyRecordActivity.class)
								.putExtra("type", 4));

						break;

					case 5:// 免费提问
						startActivity(new Intent(mContext, MyRecordActivity.class)
								.putExtra("type", 5));
						break;

					case 6:// 快速求助
						startActivity(new Intent(mContext, MyRecordActivity.class)
								.putExtra("type", 6));
						break;

					case 7:// 健康卡激活
						startActivity(new Intent(mContext, MyHealthCardActivity.class));
						break;

					case 8:// 购买健康卡
						startActivity(new Intent(mContext, MyOrderListActivity.class));
						break;
					}

					finish();

	}
	@Override
	public void onClick(View v) {
		if(imgHome==v)
			switch (type) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				BackHome(SucHintActivity.this, 0);
				break;
			}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		BackHome(SucHintActivity.this, 0);
	}

}

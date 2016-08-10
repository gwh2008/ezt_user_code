package com.eztcn.user.eztcn.activity.fdoc;

import java.util.ArrayList;
import java.util.Map;
import xutils.ViewUtils;
import xutils.http.RequestParams;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eztcn.user.R;
import com.eztcn.user.eztcn.BaseApplication;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.mine.ShoppingCarActivity;
import com.eztcn.user.eztcn.api.IHttpResult;
import com.eztcn.user.eztcn.bean.ForeignPatient_Service;
import com.eztcn.user.eztcn.impl.ForeignPatientImpl;
import com.eztcn.user.eztcn.utils.HttpParams;
import com.eztcn.user.hall.utils.Constant;

/**
 * @title 外患服务套餐详情
 * @describe
 * @author ezt
 * @created 2015年3月25日
 */
public class ForeignPatient_TPDetailsActivity extends FinalActivity implements
		OnClickListener, IHttpResult {

	@ViewInject(R.id.group_info)
	private TextView tvGroupInfo;// 组合内容

	@ViewInject(R.id.time_tv)
	private TextView tvTime;// 节省时间

	@ViewInject(R.id.intro)
	private TextView tvIntro;// 产品详情

	@ViewInject(R.id.price_tv)
	private TextView tvPrice;// 商品价格

	@ViewInject(R.id.add_card_tv)//, click = "onClick"
	private TextView tvAddCart;

	private ImageView imgShop;

	private String deptId, id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foreignpatient_details);
		ViewUtils.inject(ForeignPatient_TPDetailsActivity.this);
		String strName = getIntent().getStringExtra("title");
		id = getIntent().getStringExtra("id");
		deptId = getIntent().getStringExtra("deptId");

		imgShop = loadTitleBar(true, strName, R.drawable.shopping_icon);
		imgShop.setOnClickListener(this);
		initialData(id);
	}
	@OnClick(R.id.add_card_tv)
private void addCardClick(View v){
		addShoppingCart(BaseApplication.getInstance().patient.getUserId()
				+ "", id, deptId);
}
	@Override
	public void onClick(View v) {
		if(v==imgShop)
			if (BaseApplication.getInstance().patient != null) {
				startActivity(new Intent(mContext, ShoppingCarActivity.class));
			} else {
				HintToLogin(Constant.LOGIN_COMPLETE);
			}
	}

	/**
	 * 获取数据
	 */
	private void initialData(String id) {
		RequestParams params=new RequestParams();
		params.addBodyParameter("id",id);
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.getTrapackageDetail(params, this);
		showProgressToast();
	}

	@Override
	public void result(Object... object) {
		int type = (Integer) object[0];
		boolean isSuc = (Boolean) object[1];

		switch (type) {
		case HttpParams.GET_TRAPACKAGE_DETAIL:
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						ArrayList<ForeignPatient_Service> list = (ArrayList<ForeignPatient_Service>) map
								.get("data");
						ForeignPatient_Service f = list.get(0);

						tvGroupInfo.setText(f.getTitle());
						tvTime.setText(f.getTime());
						tvIntro.setText(Html.fromHtml(f.getIntro()));
						tvPrice.setText("价格：" + f.getPrice() + "元");

					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(mContext, getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}
			hideProgressToast();
			break;

		default:// 添加购物车
			if (isSuc) {
				Map<String, Object> map = (Map<String, Object>) object[2];
				if (map != null) {
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						// Toast.makeText(mContext, "添加成功",
						// Toast.LENGTH_SHORT).show();
						hintDialog();
					} else {
						Toast.makeText(mContext, map.get("msg").toString(),
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(mContext, getString(R.string.request_fail),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				Toast.makeText(mContext, getString(R.string.service_error),
						Toast.LENGTH_SHORT).show();
			}
			hideProgressToast();
			break;
		}

	}

	/**
	 * 添加购物车成功提醒
	 */
	private void hintDialog() {

		AlertDialog.Builder builder = new Builder(mContext);
		builder.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("提示")
				.setMessage("添加购物车成功！")
				.setCancelable(false)
				.setNegativeButton("去购物车",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (BaseApplication.getInstance().patient != null) {
									startActivity(new Intent(mContext,
											ShoppingCarActivity.class));
								} else {
									HintToLogin(Constant.LOGIN_COMPLETE);
								}

							}
						}).setPositiveButton("继续购买", null);

		AlertDialog dialog = builder.create();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (dialog != null) {
						dialog.dismiss();
					}
				}
				return false;
			}
		});
		dialog.show();

	}

	/**
	 * 添加购物车
	 */
	private void addShoppingCart(String userId, String objectId, String deptId) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("userId", userId);
//		params.put("objectType", "2");
//		params.put("objectId", objectId);
//		params.put("deptId", deptId);
//		params.put("nums", "1");
//		params.put("doctorId", "");
//		IForeignPatient api = new ForeignPatientImpl();
		RequestParams params = new RequestParams();
		params.addBodyParameter("userId", userId);
		params.addBodyParameter("objectType", "2");
		params.addBodyParameter("objectId", objectId);
		params.addBodyParameter("deptId", deptId);
		params.addBodyParameter("nums", "1");
		params.addBodyParameter("doctorId","");
		ForeignPatientImpl api = new ForeignPatientImpl();
		api.addTraShoppingCart(params, this);
		showProgressToast();
	}

}

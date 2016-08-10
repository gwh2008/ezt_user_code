/**
 * 
 */
package com.eztcn.user.eztcn.activity.home.ordercheck;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.adapter.BaseArrayListAdapter;
import com.eztcn.user.eztcn.bean.ordercheck.CheckOrderItem;

/**
 * @author Liu Gang
 * 
 *         2016年3月17日 下午6:21:43
 * 
 */
public class CheckOrderDetailActivity extends FinalActivity {
	@ViewInject(R.id.order_details_succeed_layout)
	private View order_details_succeed_layout;
	@ViewInject(R.id.order_hintTv)
	private View order_hintTv;
	@ViewInject(R.id.order_details_lv)
	private ListView order_details_lv;
	@ViewInject(R.id.secNameTv)
	private TextView secNameTv;
	@ViewInject(R.id.secMobileTv)
	private TextView secMobileTv;
	private ItemAadpter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_corder_details30);
		ViewUtils.inject(CheckOrderDetailActivity.this);
		loadTitleBar(true, "详情", null);
		adapter = new ItemAadpter(mContext);
		order_details_lv.setAdapter(adapter);
		getData();
	}

	private void getData() {
		String serName = getIntent().getStringExtra("secretary");// 判断秘书姓名是否为空

		if (StringUtils.isEmpty(serName)) {
			order_hintTv.setVisibility(View.VISIBLE);
			order_details_succeed_layout.setVisibility(View.GONE);
		} else {
			secNameTv.setText(serName);
			String secMobileStr = getIntent().getStringExtra("secMobile");// 判断秘书姓名是否为空
			secMobileTv.setText(secMobileStr);
			order_hintTv.setVisibility(View.GONE);
			order_details_succeed_layout.setVisibility(View.VISIBLE);
		}
		List<CheckOrderItem> list = (List<CheckOrderItem>) getIntent()
				.getExtras().get("checkOrderList");
		adapter.setList(list);
	}

	private class ItemAadpter extends BaseArrayListAdapter<CheckOrderItem> {

		private Context context;

		class Holder {
			ImageView imgView;
			TextView nameTv;
			TextView addrTv;
			TextView costTv;
			TextView costTypeTv;
			TextView itemcheck_time;
		}

		Holder holder;

		public ItemAadpter(Activity context) {
			super(context);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				holder = new Holder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_checkitemshow, null);
				holder.imgView = (ImageView) convertView
						.findViewById(R.id.checkitemIv);
				holder.nameTv = (TextView) convertView
						.findViewById(R.id.itemTitleTv);
				holder.addrTv = (TextView) convertView
						.findViewById(R.id.itemAddressTv);
				holder.costTypeTv = (TextView) convertView
						.findViewById(R.id.itemcostTypeTv);
				holder.costTv = (TextView) convertView
						.findViewById(R.id.itemCostValTv);
				holder.itemcheck_time = (TextView) convertView
						.findViewById(R.id.itemcheck_time);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			CheckOrderItem checkOrderItem = mList.get(position);

			String typeStr = checkOrderItem.getOrderTypeStr();
			if (typeStr.contains("分泌物")) {
				holder.imgView.setImageResource(R.drawable.cifmw);
			} else if (typeStr.contains("影像")) {
				holder.imgView.setImageResource(R.drawable.cimovie);
			} else {
				holder.imgView.setImageResource(R.drawable.cimr);
			}
			holder.addrTv.setText(checkOrderItem.getAddr());
			holder.nameTv.setText(checkOrderItem.getNameStr());

			holder.costTypeTv.setText("检查费");
			holder.costTv.setText("¥" + checkOrderItem.getCostStr());
			String timeStr = checkOrderItem.getTimeStr();
			if (StringUtils.isNotBlank(timeStr))
				holder.itemcheck_time.setText(timeStr);
			return convertView;
		}
	}
}

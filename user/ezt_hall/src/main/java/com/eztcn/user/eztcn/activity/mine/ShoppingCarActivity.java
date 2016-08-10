package com.eztcn.user.eztcn.activity.mine;

import java.util.ArrayList;
import java.util.List;

import xutils.ViewUtils;
import xutils.view.annotation.ViewInject;
import xutils.view.annotation.event.OnClick;
import xutils.view.annotation.event.OnItemClick;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.activity.FinalActivity;
import com.eztcn.user.eztcn.activity.discover.BuyPayActivity;
import com.eztcn.user.eztcn.activity.discover.ProductDetailActivity;
import com.eztcn.user.eztcn.adapter.ShoppingCarAdapter;
import com.eztcn.user.eztcn.adapter.ShoppingCarAdapter.ProductListener;
import com.eztcn.user.eztcn.bean.EztProduct;

/**
 * @title 购物车
 * @describe
 * @author ezt
 * @created 2015年2月28日
 */
public class ShoppingCarActivity extends FinalActivity implements
		OnCheckedChangeListener, ProductListener {

	@ViewInject(R.id.productList)//, itemClick = "onItemClick"
	private ListView productList;
	@ViewInject(R.id.recharge)//, click = "onClick"
	private TextView recharge;// 结算
	@ViewInject(R.id.allSelect)//, click = "onClick"
	private TextView allSelect;// 全选
	@ViewInject(R.id.totalCost)
	private TextView totalCost;// 总计费用

	private ShoppingCarAdapter adapter;
	private boolean[] selectBox;
	private boolean isSelect = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppingcar);
		ViewUtils.inject(ShoppingCarActivity.this);
		loadTitleBar(true, "购物车", null);
		initProductList();
	}
	
	@OnClick(R.id.recharge)
	private void rechargeClick(View v){// 结算
		Intent intent = new Intent();
		intent.setClass(this, BuyPayActivity.class);
		startActivity(intent);
	}
	
	@OnClick(R.id.allSelect)
	private void allSelectClick(View v){
		updateCheckBox(isSelect);
	}

//	public void onClick(View v) {
//		if (v.getId() == R.id.recharge) {// 结算
//			Intent intent = new Intent();
//			intent.setClass(this, BuyPayActivity.class);
//			startActivity(intent);
//		} else if (v.getId() == R.id.allSelect) {
//			updateCheckBox(isSelect);
//		}
//	}

	/**
	 * 初始化全选按钮图片
	 */
	public void updateCheckedImg(boolean bool) {
		int w = R.drawable.check_off;
		if (bool) {
			w = R.drawable.check_on;
		} else {
			w = R.drawable.check_off;
		}
		Drawable rightDrawable = getResources().getDrawable(w);
		rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
				rightDrawable.getMinimumHeight());
		allSelect.setCompoundDrawables(rightDrawable, null, null, null);
	}

	/**
	 * 初始化购物车list
	 */
	public void initProductList() {
		List<EztProduct> list = new ArrayList<EztProduct>();
		EztProduct product = new EztProduct();
		product.setProductCount(1);
		product.setProductPrice(1000);
		EztProduct product2 = new EztProduct();
		product2.setProductCount(1);
		product2.setProductPrice(1000);
		EztProduct product3 = new EztProduct();
		product3.setProductCount(1);
		product3.setProductPrice(1000);
		list.add(product);
		list.add(product2);
		list.add(product3);
		selectBox = new boolean[list.size()];
		adapter = new ShoppingCarAdapter(mContext, selectBox);
		productList.setAdapter(adapter);
		adapter.setList(list);
		adapter.setOnCountChangedListener(this);
	}

	/**
	 * 全选按钮点击事件
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		updateCheckBox(isChecked);
	}

	/**
	 * 更新选中状态
	 */
	public void updateCheckBox(boolean bool) {
		if (selectBox == null) {
			return;
		}
		for (int i = 0; i < selectBox.length; i++) {
			selectBox[i] = bool;
		}
		adapter.setChecked(selectBox);
		isSelect = !bool;
		updateCheckedImg(bool);
	}

	/**
	 * 判断是否全选状态
	 * 
	 * @return
	 */
	public int judgeSelectedStatus() {
		if (selectBox == null) {
			return 0;
		}
		int selectCount = 0;
		for (int i = 0; i < selectBox.length; i++) {
			if (selectBox[i]) {
				selectCount++;
			} else {
				selectCount--;
			}
		}
		return selectCount;
	}

	public void updateData() {
		int pc = 0;
		int price = 0;
		List<EztProduct> list = adapter.getList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isSelect()) {
				pc = list.get(i).getProductCount();
				price += list.get(i).getProductPrice() * pc;
			}
		}
		totalCost.setText("总计：" + price + "元");
	}

	/**
	 * adapter数据、状态更新返回信息
	 */
	@Override
	public void onCountChanged(int p) {
		int sCount = judgeSelectedStatus();
		if (sCount == selectBox.length) {
			updateCheckedImg(true);
			isSelect = false;
		} else {
			updateCheckedImg(false);
			isSelect = true;
		}
		updateData();
	}
	
	@OnItemClick(R.id.productList)
	private void productListItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ProductDetailActivity.class);
		intent.putExtra("product", adapter.getList().get(position));
		startActivity(intent);
	}

	/**
	 * listview item
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		Intent intent = new Intent(this, ProductDetailActivity.class);
//		intent.putExtra("product", adapter.getList().get(position));
//		startActivity(intent);
//	}
}

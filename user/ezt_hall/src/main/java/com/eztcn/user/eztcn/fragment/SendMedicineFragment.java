package com.eztcn.user.eztcn.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.adapter.SendMedicineAdapter;
import com.eztcn.user.eztcn.bean.Product;
import com.eztcn.user.eztcn.utils.CacheUtils;

/**
 * @title 首页
 * @describe
 * @author ezt
 * @created 2014年12月11日
 */
public class SendMedicineFragment extends FinalFragment implements OnClickListener {

	private View rootView;

	private final String ADS_LIST_DATA = "infoListData";// 缓存key-内容
	private CacheUtils mCache;
	/**
	 * 标题
	 */
	private TextView tv_title;
	/**
	 * 价格
	 */
	private TextView tv_s_m_Price;
	/**
	 * 销量
	 */
	private TextView tv_s_m_Sales;
	/**
	 * 列表
	 */
	private ListView lv_sendM;
	private int tv_focus_id = -1;
	private List<Product> productList;
	private SendMedicineAdapter send_m_adapter;

	public static SendMedicineFragment newInstance() {
		SendMedicineFragment fragment = new SendMedicineFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity activity = getActivity();
		mCache = CacheUtils.get(activity);
		productList = new ArrayList<Product>();
		send_m_adapter = new SendMedicineAdapter(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 避免UI重新加载
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_sendmedicine, null);// 缓存Fragment

		}
		// 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		tv_title.setText(getString(R.string.tab_send_medicine));
		send_m_adapter.setList(productList);
		lv_sendM.setAdapter(send_m_adapter);
		loadMedData();
		return rootView;
	}

	private void initView() {
		tv_title = (TextView) rootView
				.findViewById(R.id.sendMediction_title_tv);
		tv_s_m_Price = (TextView) rootView.findViewById(R.id.tv_s_m_Price);
		tv_s_m_Sales = (TextView) rootView.findViewById(R.id.tv_s_m_Sales);
		lv_sendM = (ListView) rootView.findViewById(R.id.lv_sendM);
		tv_focus_id = R.id.tv_s_m_Price;
		// setFontgrey();
		// setFontRed(tv_focus_id);
		tv_s_m_Price.setOnClickListener(this);
		tv_s_m_Sales.setOnClickListener(this);
	}

	/**
	 * 加载数据
	 */
	private void loadMedData() {
		productList.clear();
		Product product = new Product();
		product.setP_Name("中老年高血压管理服务全新启动!");
		product.setP_Price(900);
		product.setP_Sales(1024);
//		product.setImageUrl(R.drawable.shop1);
		productList.add(product);

		Product product1 = new Product();
		product1.setP_Name("健康在线预约");
		product1.setP_Price(328);
		product1.setP_Sales(2400);
//		product1.setImageUrl(R.drawable.shop2);
		productList.add(product1);

		Product product2 = new Product();
		product2.setP_Name("健康套餐限时优惠全家分享");
		product2.setP_Price(900);
		product2.setP_Sales(897);
//		product2.setImageUrl(R.drawable.shop3);
		productList.add(product2);

		Product product3 = new Product();
		product3.setP_Name("孕产妇全程健康管理套餐");
		product3.setP_Price(900);
		product3.setP_Sales(1236);
//		product3.setImageUrl(R.drawable.shop4);
		productList.add(product3);

		send_m_adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		setFontgrey();
		setFontRed(v.getId());
		switch (v.getId()) {
		case R.id.tv_s_m_Price: {
			// 价格排序
			sort_product_Price();
		}
			break;
		case R.id.tv_s_m_Sales: {
			// 销量排序
			sort_product_Sales();

		}
			break;
		}
		send_m_adapter.setList(productList);
	}

	private void setFontgrey() {
		tv_s_m_Price.setTextColor(getResources().getColor(
				R.color.font_unclick_color));
		tv_s_m_Sales.setTextColor(getResources().getColor(
				R.color.font_unclick_color));
	}

	private void setFontRed(int tv_id) {
		TextView tv = (TextView) rootView.findViewById(tv_id);
		tv.setTextColor(getResources().getColor(R.color.main_color));
	}

	// /**
	// * 按bean的属性值对list集合进行排序
	// *
	// * @param list
	// * 要排序的集合
	// * @param propertyName
	// * 集合元素的属性名
	// * @param isAsc
	// * 排序方向,true--正向排序,false--逆向排序
	// * @return 排序后的集合
	// */
	// public static List sortList(List list, String propertyName, boolean
	// isAsc) {
	// // 借助commons-collections包的ComparatorUtils
	// // BeanComparator，ComparableComparator和ComparatorChain都是实现了Comparator这个接口
	// Comparator mycmp = ComparableComparator.getInstance();
	// mycmp = ComparatorUtils.nullLowComparator(mycmp); // 允许null
	// if (isAsc) {
	// mycmp = ComparatorUtils.reversedComparator(mycmp); // 逆序
	// }
	// Comparator cmp = new BeanComparator(propertyName, mycmp);
	// Collections.sort(list, cmp);
	// return list;
	// }

	public void sort_product_Price() {
		Comparator comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				Product p1 = (Product) o1;
				Product p2 = (Product) o2;
				if (p1.getP_Price() < p2.getP_Price())
					return 1;
				else if (p1.getP_Price() == p2.getP_Price())
					return 0;
				else if (p1.getP_Price() > p2.getP_Price())
					return -1;
				return 0;
			}
		};
		Collections.sort(productList, comp);

	}

	public void sort_product_Sales() {
		Comparator comp = new Comparator() {
			public int compare(Object o1, Object o2) {
				Product p1 = (Product) o1;
				Product p2 = (Product) o2;
				if (p1.getP_Sales() < p2.getP_Sales())
					return 1;
				else if (p1.getP_Sales() == p2.getP_Sales())
					return 0;
				else if (p1.getP_Sales() > p2.getP_Sales())
					return -1;
				return 0;
			}
		};
		Collections.sort(productList, comp);
	}

	@Override
	public void onDestroyView() {
		setFontgrey();
		super.onDestroyView();

	}
}

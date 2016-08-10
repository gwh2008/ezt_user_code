package com.eztcn.user.eztcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.eztcn.user.eztcn.bean.ChildOrder;
import com.eztcn.user.eztcn.bean.Order;
import com.eztcn.user.eztcn.utils.ResolveResponse;
import com.eztcn.user.eztcn.utils.StringUtil;

/**
 * @title 订单相关数据解析
 * @describe
 * @author ezt
 * @created 2014年12月18日
 */
public class OrderManager {

	/**
	 * 创建订单
	 */
	public static Map<String, Object> createOrdere(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			if (!json.isNull("data")) {

			}
			if (!flag) {
				map.put("msg", json.getString("detailMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取订单列表
	 * 
	 * @param t
	 * @return
	 */
	public static ArrayList<Order> getOrderList(String t) {
		ArrayList<Order> orderList = null;
		try {
			Object object = ResolveResponse.resolveData(t);// 解析返回的数据
			if (object instanceof JSONObject) {// 成功

				JSONObject j = (JSONObject) object;
				JSONArray jArray = null;
				if (!j.isNull("rows")) {
					jArray = j.getJSONArray("rows");
				} else {
					return orderList;
				}
				orderList = new ArrayList<Order>();
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject ject = jArray.getJSONObject(i);
					Order order = new Order();

					if (!ject.isNull("orderBean")) {
						JSONObject orderBeanJect = ject
								.getJSONObject("orderBean");

						if (!orderBeanJect.isNull("id"))
							order.setId(orderBeanJect.getString("id"));

						if (!orderBeanJect.isNull("createtime")) {
							String str = orderBeanJect.getString("createtime");
							str = StringUtil.dealWithDate(str);
							order.setCreateTime(str);
						}

						if (!orderBeanJect.isNull("eoAmount"))
							order.setTotalPrice(orderBeanJect
									.getString("eoAmount"));

						if (!orderBeanJect.isNull("eoNumber"))
							order.setOrderNo(orderBeanJect
									.getString("eoNumber"));

						if (!orderBeanJect.isNull("eoStatus")) {
							String strState = "";
							int state = orderBeanJect.getInt("eoStatus");// 订单状态
							// -1放弃交易0未付款1已付款2等待发货3已发货4确认收货5交易成功
							switch (state) {
							case -1:
								strState = "放弃交易";
								break;
							case 0:
								strState = "未付款";
								break;

							case 1:
								strState = "已付款";
								break;

							case 2:
								strState = "等待发货";
								break;

							case 3:
								strState = "已发货";
								break;

							case 4:
								strState = "确认收货";
								break;

							case 5:
								strState = "交易成功";
								break;
							}
							order.setOrderState(strState);
						}
					}
					if (!ject.isNull("orderDetailBean")) {
						JSONArray childOrderArray = ject
								.getJSONArray("orderDetailBean");
						ArrayList<ChildOrder> childList = new ArrayList<ChildOrder>();

						int allNum = 0;
						for (int k = 0; k < childOrderArray.length(); k++) {
							JSONObject childJect = childOrderArray
									.getJSONObject(k);

							ChildOrder child = new ChildOrder();
							if (!childJect.isNull("nums")) {
								int num = childJect.getInt("nums");
								allNum += num;
								child.setOrderAmount(num + "");
							}

							if (!childJect.isNull("categoryName"))
								child.setOrderName(childJect
										.getString("categoryName"));

							if (!childJect.isNull("objectPic"))
								child.setImgUrl(childJect
										.getString("objectPic"));

							if (!childJect.isNull("nums"))
								child.setOrderAmount(childJect
										.getString("nums"));

							if (!childJect.isNull("amount"))
								child.setTotalPrice(childJect
										.getString("amount"));

							childList.add(child);
						}
						order.setChildOrderList(childList);
						order.setOrderAmount(allNum + "");// 订单总数量
					}
					if (!ject.isNull("payBean")) {
						JSONObject payJson = ject.getJSONObject("payBean");
						int payStatus = 0;
						if (!payJson.isNull("rpStatus")) {
							payStatus = payJson.getInt("rpStatus");
						}
						if (payStatus == 1 && !payJson.isNull("createtime")) {
							String str = payJson.getString("createtime");
							str = StringUtil.dealWithDate(str);
							order.setPayTime(str);
						}
					}
					orderList.add(order);
				}
			} else if (object instanceof Boolean) {
				boolean isSuc = (Boolean) object;
				if (!isSuc) {// 访问失败
					orderList = null;
				} else {// 数据为空

				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
			orderList = null;
		}
		return orderList;

	}

	/**
	 * 删除订单
	 * 
	 * @param t
	 * @return
	 */
	public static Map<String, Object> delOrder(String t) {
		Map<String, Object> map = null;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = new JSONObject(t);
			boolean flag = json.getBoolean("flag");
			map.put("flag", flag);
			map.put("msg", json.getString("detailMsg"));
		} catch (JSONException e) {
			e.printStackTrace();
			map = null;
		}
		return map;
	}
}

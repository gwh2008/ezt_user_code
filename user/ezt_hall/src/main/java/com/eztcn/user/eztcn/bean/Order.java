package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title 订单实体
 * @describe
 * @author ezt
 * @created 2015年3月31日
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String orderState;// 订单状态

	private String createTime;// 创建时间(下单时间)

	private String orderAmount;// 商品数量

	private String totalPrice;// 订单金额

	private String orderNo;// 订单号

	private String payTime;// 付款时间

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	private ArrayList<ChildOrder> childOrderList;// 子订单列表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public ArrayList<ChildOrder> getChildOrderList() {
		return childOrderList;
	}

	public void setChildOrderList(ArrayList<ChildOrder> childOrderList) {
		this.childOrderList = childOrderList;
	}

	public String getPayTime() {
		if (payTime == null) {
			payTime = "";
		}
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

}

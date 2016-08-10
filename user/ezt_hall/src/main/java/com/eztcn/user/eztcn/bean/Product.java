/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @author EZT 药给送实体
 */
public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 图片网址
	 */
	private int imageUrl;
	/**
	 * 产品名称
	 */
	private String p_Name;
	/**
	 * 产品销量
	 */
	private int p_Sales;
	/**
	 * 产品价格
	 */
	private int p_Price;

	public int getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(int imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getP_Name() {
		return p_Name;
	}

	public void setP_Name(String p_Name) {
		this.p_Name = p_Name;
	}

	public int getP_Sales() {
		return p_Sales;
	}

	public void setP_Sales(int p_Sales) {
		this.p_Sales = p_Sales;
	}

	public int getP_Price() {
		return p_Price;
	}

	public void setP_Price(int p_Price) {
		this.p_Price = p_Price;
	}

}

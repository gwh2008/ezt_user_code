/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @author Liu Gang
 * 
 *         2015年8月4日 下午2:06:19
 * 
 */
public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

/**
 * 
 */
package com.eztcn.user.eztcn.bean.ordercheck;

import java.io.Serializable;

/**
 * @author Liu Gang
 * 
 * 2016年3月19日
 * 下午3:11:08
 * 
 */
public class Guider implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String guysNumer;
	private String healthSecretary;
	private String guysMobile;
	private String guysName;
	public String getGuysNumer() {
		return guysNumer;
	}
	public void setGuysNumer(String guysNumer) {
		this.guysNumer = guysNumer;
	}
	public String getHealthSecretary() {
		return healthSecretary;
	}
	public void setHealthSecretary(String healthSecretary) {
		this.healthSecretary = healthSecretary;
	}
	public String getGuysMobile() {
		return guysMobile;
	}
	public void setGuysMobile(String guysMobile) {
		this.guysMobile = guysMobile;
	}
	public String getGuysName() {
		return guysName;
	}
	public void setGuysName(String guysName) {
		this.guysName = guysName;
	}
	

}

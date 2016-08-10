/**
 * 
 */
package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

/**
 * @author EZT
 *	疫苗（宝宝疫苗界面用到的实体）
 */
public class Vaccine implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//接种时间
	private String vac_time;
	//疫苗名称
	private String vac_name;
	//接种次数
	private String vac_times;
	//可预防传染病
	private String vac_use;
	//接种部位
	private String vac_part;
	/**
	 * 
	 * @param vac_time
	 * @param vac_name
	 * @param vac_times
	 * @param vac_use
	 * @param vac_part
	 */
	public Vaccine(String vac_time,String vac_name,String vac_times,String vac_use,String vac_part){
		this.vac_time=vac_time;
		this.vac_name=vac_name;
		this.vac_times=vac_times;
		this.vac_use=vac_use;
		this.vac_part=vac_part;
	}
	public String getVac_time() {
		return vac_time;
	}
	public void setVac_time(String vac_time) {
		this.vac_time = vac_time;
	}
	public String getVac_name() {
		return vac_name;
	}
	public void setVac_name(String vac_name) {
		this.vac_name = vac_name;
	}
	public String getVac_times() {
		return vac_times;
	}
	public void setVac_times(String vac_times) {
		this.vac_times = vac_times;
	}
	public String getVac_use() {
		return vac_use;
	}
	public void setVac_use(String vac_use) {
		this.vac_use = vac_use;
	}
	public String getVac_part() {
		return vac_part;
	}
	public void setVac_part(String vac_part) {
		this.vac_part = vac_part;
	}
	
	
}

/**
 * 
 */
package com.eztcn.user.eztcn.bean.compent;

import java.io.Serializable;

/**
 * @author Liu Gang
 * 
 * 2015年11月17日
 * 上午10:44:20
 * Intent 参数
 */
public class IntentParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private Object value;
	public IntentParams(String key,Object value){
		this.key=key;
		this.value=value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}

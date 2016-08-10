/**
 * 
 */
package com.eztcn.user.eztcn.bean.compent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liu Gang
 * 
 *         2015年11月17日 上午10:41:47
 * 
 */
public class Compent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ItemCompent> children; // 子控件 ep. “模块名称00,模块名称01”
	
	public Compent(){
		children=new ArrayList<ItemCompent>();
	}
	public void addChild(ItemCompent item){
		children.add(item);
	}
	public List<ItemCompent> getChildren(){
		return children;
	}
}

/**
 * 
 */
package com.eztcn.user.eztcn.bean.compent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liu Gang
 * 
 * 2015年11月17日
 * 上午10:56:10
 * 
 */
public class GridCompent extends Compent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 列数
	 */
	private int row;
	public GridCompent(){
		super();
	}
	/**
	 * 获取有几列
	 * @return
	 */
	public int getRow() {
		return row;
	}
	/**
	 * 设置有几列
	 * @param row 列数
	 */
	public void setRow(int row){
		this.row=row;
	}
}

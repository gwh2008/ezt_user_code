package com.eztcn.user.eztcn.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @title 父类实体
 * @describe 就医行程
 * @author ezt
 * @created 2015年3月9日
 */

public class Route_ParentEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String groupName;

	private ArrayList<Route_ChildEntity> childs;

	
	public String getGroupName() {
		return groupName;
	}

	public ArrayList<Route_ChildEntity> getChilds() {
		return childs;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setChilds(ArrayList<Route_ChildEntity> childs) {
		this.childs = childs;
	}

}

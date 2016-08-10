package com.eztcn.user.eztcn.bean;

import java.util.ArrayList;

/**
 * @title 子类（就医行程）
 * @describe
 * @author ezt
 * @created 2015年3月9日
 */

public class Route_ChildEntity {

	private String groupName;

	private ArrayList<String> childNames;
	

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<String> getChildNames() {
		return childNames;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setChildNames(ArrayList<String> childNames) {
		this.childNames = childNames;
	}

}

package com.eztcn.user.eztcn.bean;

import java.io.Serializable;

import xutils.db.annotation.Column;
import xutils.db.annotation.Id;
import xutils.db.annotation.Table;

/**
 * @title 搜索关键字实体
 * @describe
 * @author ezt
 * @created 2015年7月23日
 */

@Table(name = "search_keyword")
public class SearchKeyword implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id(column = "_id")
	private int _id;
	@Column(column = "keyWord")
	private String keyWord;//搜索关键字

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	

}

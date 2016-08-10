package com.eztcn.user.hall.utils;


import com.eztcn.user.hall.model.City;

import java.util.Comparator;

/**
 * @title 根据拼音筛选类
 * @describe 用于选择城市筛选
 * @author ezt
 * @created 2014年12月21日
 */
public class PinyinComparatorCity implements Comparator<City> {

	public int compare(City o1, City o2) {//默认#符号排在最后，@符号排在最前面
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}

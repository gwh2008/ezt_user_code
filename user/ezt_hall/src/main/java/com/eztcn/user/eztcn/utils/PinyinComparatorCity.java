package com.eztcn.user.eztcn.utils;

import java.util.Comparator;

import com.eztcn.user.eztcn.bean.City;

/**
 * @title 根据拼音筛选类
 * @describe 用于选择城市筛选
 * @author ezt
 * @created 2014年12月21日
 */
public class PinyinComparatorCity implements Comparator<City> {

	public int compare(City o1, City o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}

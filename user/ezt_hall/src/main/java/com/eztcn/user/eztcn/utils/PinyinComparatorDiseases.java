package com.eztcn.user.eztcn.utils;

import java.util.Comparator;

import com.eztcn.user.eztcn.bean.Diseases;

/**
 * @title 根据拼音筛选类
 * @describe 用于选择疾病筛选
 * @author ezt
 * @created 2014年12月24日
 */
public class PinyinComparatorDiseases implements Comparator<Diseases> {

	public int compare(Diseases o1, Diseases o2) {
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

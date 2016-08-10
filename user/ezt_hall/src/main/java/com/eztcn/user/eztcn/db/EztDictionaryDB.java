package com.eztcn.user.eztcn.db;

import java.util.ArrayList;
import java.util.List;

import xutils.DbUtils;
import xutils.db.sqlite.Selector;
import xutils.db.sqlite.WhereBuilder;
import xutils.exception.DbException;
import android.content.Context;

import com.eztcn.user.eztcn.bean.EztDictionary;

/**
 * @title 字典表
 * @describe
 * @author ezt
 * @created 2015年2月11日
 */
public class EztDictionaryDB {

	private DbUtils db;
	private static EztDictionaryDB manager;

	private EztDictionaryDB(Context context) {
		db = DbUtils.create(context, "ezt_dictionary");
	}

	public static EztDictionaryDB getInstance(Context context) {
		if (manager == null) {
			manager = new EztDictionaryDB(context);
		}
		return manager;
	}

	public void clearTable() {
		try {
			db.delete(EztDictionary.class, null);
		} catch (Exception e) {

		}
	}

	/**
	 * 获取字典数量
	 */
	public int getDictionarySize() {
		List<EztDictionary> list=null;
		try {
			list = db.findAll(EztDictionary.class);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	/**
	 * 保存
	 * 
	 * @param obj
	 */
	public void save(Object obj) {
		try {
			db.save(obj);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 根据kindship获取单个对应中文值
	 * 
	 * @param enName
	 *            标签
	 * @param value
	 *            对应值 408
	 */
	public String getLabelByTag(String enName, String value) {
		Selector selector = Selector.from(EztDictionary.class);
		WhereBuilder whereBuilder = WhereBuilder.b("enName", "=", enName).and(
				"value", "=", value);
		selector.where(whereBuilder);
		List<EztDictionary> list = null;
		try {
			list = db.findAll(selector);
		} catch (DbException e) {
			e.printStackTrace();
		}
//		if (list != null && list.size() != 0) {
//			return list.get(0).getLabel();
//		}
//		return "123";
		return (null!=list&&list.size()>0)?list.get(0).getLabel():"";
	}

	/**
	 * 根据kindship获取list列表
	 */
	public List<EztDictionary> getDictionaryList(String enName) {
		Selector selector = Selector.from(EztDictionary.class);
		WhereBuilder whereBuilder = WhereBuilder.b("enName", "=", enName);
		selector.where(whereBuilder);
		List<EztDictionary> list =null;
		try {
			list=db.findAll(selector);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return list;
	}
}

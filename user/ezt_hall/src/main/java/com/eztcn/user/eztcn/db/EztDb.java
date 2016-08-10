package com.eztcn.user.eztcn.db;

import java.util.ArrayList;

import android.content.Context;

import xutils.DbUtils;
import xutils.db.sqlite.Selector;
import xutils.db.sqlite.WhereBuilder;
import xutils.exception.DbException;

/**
 * @title 本地数据库
 * @describe
 * @author ezt
 * @created 2014年12月22日
 */
public class EztDb<T> {

	private DbUtils db;
	private static EztDb manager;

	private EztDb(Context context) {
		db=DbUtils.create(context,  "eztdb");
	}

	public static EztDb getInstance(Context context) {
		if (manager == null) {
			manager = new EztDb(context);
		}
		return manager;
	}

	/**
	 * 插入数据
	 * 
	 * @param ject
	 */
	public void save(Object ject) {
		try {
			db.save(ject);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * 更新数据
	 * 
	 * @param ject
	 */
	public void update(Object ject, WhereBuilder whereBuilder,String...updateColumnNames) {
		try {
			db.update(ject, whereBuilder, updateColumnNames);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查一个表的所有数据
	 */
	public ArrayList<T> queryAll(T t,WhereBuilder whereBuilder,String orderBy) {
		Selector selector=Selector.from(t.getClass());
		if(null!=whereBuilder)
			selector.where(whereBuilder);
		if(null!=orderBy){
			String[] order=orderBy.split(" ");
			selector.orderBy(order[0],order[1].toLowerCase().equals("desc")?true:false);}
		ArrayList<T> list=new ArrayList<T>();
			try {
				list = (ArrayList<T>)db.findAll(selector);
			} catch (DbException e) {
				e.printStackTrace();
			}
		return null==list?new ArrayList<T>():list;
	}
	/**
	 * 查一个表的所有数据
	 */
	public ArrayList<T> queryAll(T t,Selector selector,String orderBy) {
		if(null!=orderBy)
			selector.orderBy(orderBy);
		ArrayList<T> list=new ArrayList<T>();
			try {
				list = (ArrayList<T>)db.findAll(selector);
			} catch (DbException e) {
				e.printStackTrace();
			}
			return null==list?new ArrayList<T>():list;
	}

	

	/**
	 * 删除一条数据
	 */

	public void delItemData(T t) {
		try {
			db.delete(t);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据条件删除数据
	 * where 为null为删除所有
	 */
	public void delDataWhere(T t,WhereBuilder whereBuilder) {
		try {
			db.delete(t.getClass(), whereBuilder);
		} catch (DbException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
}

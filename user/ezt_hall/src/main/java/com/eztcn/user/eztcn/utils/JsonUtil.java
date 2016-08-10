package com.eztcn.user.eztcn.utils;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Java对象和JSON字符串相互转化工具类 使用fastjson.jar
 * 
 * @author Administrator
 * 
 */
public final class JsonUtil {

	private JsonUtil() {
	}

	/**
	 * @标题: toJson
	 * @描述: 对象转json
	 * @处理步骤:
	 * @作者: ljc
	 * @参数: @param object
	 * @参数: @return
	 * @返回类型: String
	 * @throws抛出
	 */
	public static String toJson(Object object) {
		FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
		return JSON.toJSONString(object, filter,
				SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect);
	}
	
	/**
	 * @标题: fromJson
	 * @描述: json字符串转化为List<T>
	 * @处理步骤:
	 * @作者: ljc
	 * @参数: @param json
	 * @参数: @param classes
	 * @参数: @return
	 * @返回类型: List<T>
	 * @throws抛出
	 */
	public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
		return JSON.parseArray(json,clazz);
	}
	/**
	 * @标题: fromJsonListFor
	 * @描述: json字符串遍历转化为List<T>
	 * @处理步骤:
	 * @作者: ljc
	 * @参数: @param json
	 * @参数: @param clazz
	 * @参数: @return
	 * @返回类型: List<T>
	 * @throws抛出
	 */
	public static <T> List<T> fromJsonListFor(String json, Class<T> clazz) {
		List<T> list=new ArrayList<T>();
		try {
			JSONArray jsonArr = JSON.parseArray(json);
			for (int i = 0; i < jsonArr.size(); i++) {
				T  t= jsonArr.getObject(i,clazz);
				list.add(t);
			}
			if (list.size() == 0)
				return null;
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 
	 * @标题: fromJson
	 * @描述: json字符串转化为T
	 * @处理步骤:
	 * @作者: ljc
	 * @参数: @param str
	 * @参数: @param classes
	 * @参数: @return
	 * @返回类型: T
	 * @throws抛出
	 */
	public static <T> T fromJson(String str, Class<T> clazz) {
		try {
			T t = JSON.parseObject(str,clazz);
			return t;
		} catch (Exception e) {
			return null;
		}
	}
	
	   //快速解析jsonBean
		 public static <T> T getBean(String jsonString, Class<T> cls) {
		        T t = null;
		        try {
		            Gson gson = new Gson();
		            t = gson.fromJson(jsonString, cls);
		        } catch (Exception e) {
		        }
		        return t;
		    }
		 //解析数组。。
		 public static <T, cls> List<T> getList(String jsonString, Class<T> cls) {
		        List<T> list = new ArrayList<T>();
		        try {
		            Gson gson = new Gson();
		            list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {
		            }.getType());
		        } catch (Exception e) {
		        	
		        }
		        return list;
		    }
	
	
}

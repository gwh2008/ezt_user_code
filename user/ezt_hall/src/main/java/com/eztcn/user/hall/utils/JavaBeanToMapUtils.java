package com.eztcn.user.hall.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lizhipeng
 * @Data: 16/6/20 下午2:23
 * @Description: javaBean 转 map
 */
public class JavaBeanToMapUtils {
    /**
     * 获取java bean 属性对应的map集合
     * @param obj
     * @return
     */
    public static Map<String, String> beanToMap(Object obj) {
        Class clazz = obj.getClass();
        // 获取类中的所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> map = new HashMap<>();
        String value = "";
        for (int i = 0; i < fields.length; i++) {
            //设置权限
            fields[i].setAccessible(true);
            // 获取字段的名称
            String fieldName = fields[i].getName();
            // 过滤掉UID
            if (fieldName.endsWith("serialVersionUID")) {
                continue;
            }
            // 获取字段的类型
            String fieldType = fields[i].getGenericType().toString();
            char firstChar = 0;//获取字段的第一个字母
            char secondChar = 0;//获取字段的第二个字母
            if (fieldName.length() > 1) {
                secondChar = fieldName.charAt(1);
            }
            //拼接get方法
            String methodName = "get";
            if (fieldName.length() > 1) {
                if (Character.isUpperCase(secondChar) || Character.isUpperCase(firstChar)) {//如果是大写
                    methodName = methodName + fieldName;
                } else {
                    methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                }
            } else {
                if (Character.isUpperCase(firstChar)) {//如果是大写
                    methodName = methodName + fieldName;
                } else {
                    methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                }
            }
            try {
                // 判断变量类型
                if (fieldType.endsWith("class java.lang.String")) {
                    Method getMethod = clazz.getMethod(methodName, String.class);
                    getMethod.invoke(obj,value);
                }else if (fieldType.endsWith("int")
                        || fieldType.endsWith("class java.lang.Integer")){
                    Method getMethod = clazz.getMethod(methodName, int.class);
                    int intValue = -1;
                    getMethod.invoke(obj,intValue);
                    value = intValue+"";
                }else if (fieldType.endsWith("boolean")
                        || fieldType.endsWith("fieldType:class java.lang.Boolean")){
                    Method getMethod = clazz.getMethod(methodName, boolean.class);
                    boolean booValue = false;
                    getMethod.invoke(obj,booValue);
                    value = booValue+"";
                }else if (fieldType.endsWith("double")
                        || fieldType.endsWith("fieldType:class java.lang.Double")){
                    Method getMethod = clazz.getMethod(methodName, double.class);
                    double doubleValue = -1d;
                    getMethod.invoke(obj,doubleValue);
                    value = doubleValue+"";
                }else if (fieldType.endsWith("char")){
                    Method getMethod = clazz.getMethod(methodName, String.class);
                    getMethod.invoke(obj,value);
                }else if (fieldType.endsWith("float")
                        || fieldType.endsWith("fieldType:class java.lang.Float")){
                    Method getMethod = clazz.getMethod(methodName, double.class);
                    double doubleValue = -1d;
                    getMethod.invoke(obj,doubleValue);
                    value = doubleValue+"";
                }else if (fieldType.endsWith("short")
                        || fieldType.endsWith("fieldType:class java.lang.Short")){
                    Method getMethod = clazz.getMethod(methodName, short.class);
                    int intValue = -1;
                    getMethod.invoke(obj,intValue);
                    value = intValue+"";
                }else if (fieldType.endsWith("byte")
                        || fieldType.endsWith("fieldType:class java.lang.Byte")){
                    Method getMethod = clazz.getMethod(methodName, byte.class);
                    int intValue = -1;
                    getMethod.invoke(obj,intValue);
                    value = intValue+"";
                }else if (fieldType.endsWith("long")
                        || fieldType.endsWith("fieldType:class java.lang.Long")){
                    Method getMethod = clazz.getMethod(methodName, long.class);
                    long longValue = -1l;
                    getMethod.invoke(obj,longValue);
                    value = longValue+"";
                }
                if (!TextUtils.isEmpty(value)){
                    map.put(fieldName,value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }

}

package com.eztcn.user.hall.utils;

/**
 * Created by lx on 2016/7/2.
 * 判断字符的工具。
 */
public class CharacterTool {

   public  static  boolean isChineseCharacter(String name) {

       String reg = "[\\u4e00-\\u9fa5]+";//表示+表示一个或多个中文，
       return  name.matches(reg);
   }
}

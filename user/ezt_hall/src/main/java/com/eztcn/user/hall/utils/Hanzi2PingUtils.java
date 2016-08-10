package com.eztcn.user.hall.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Hanzi2PingUtils {
	
	/**
	 * 获得指定字符的拼音首字母,输入汉字字节才可以,其他字节返回为#
	 */
	public static char getFirstLetter(char ch) {
		String[] results = PinyinHelper.toHanyuPinyinStringArray(ch);
		if (results == null) 
			return '#';
		
		char rch = results[0].charAt(0);
		if (rch >= 'a' && rch <= 'z')
			rch -= 32;
		return rch;
	}

	/**
	 * 获取对应汉字字符串对应的汉语拼音集合
	 */
	public static ArrayList<StringBuilder[]> getPinYinList(String src) {
		if (src != null && !src.trim().equalsIgnoreCase("")) {
			char[] srcChar;
			srcChar = src.toCharArray();
			// 汉语拼音格式输出类
			HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

			// 输出设置，大小写，音标方式等
			hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
			hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

			String[][] temp = new String[src.length()][];
			for (int i = 0; i < srcChar.length; i++) {
				char c = srcChar[i];
				// 是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
				if (String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
					try {
						temp[i] = PinyinHelper.toHanyuPinyinStringArray(
								srcChar[i], hanYuPinOutputFormat);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
					// } else if (((int) c >= 65 && (int) c <= 90)
					// || ((int) c >= 97 && (int) c <= 122)) {
					// temp[i] = new String[] { String.valueOf(srcChar[i]) };
				} else {
					temp[i] = new String[] { String.valueOf(srcChar[i]) };
					// temp[i] = new String[] { " " };
				}
			}
			return getPinYinList(temp);
		}

		return new ArrayList<StringBuilder[]>();
	}

    /**
     * 将汉字转化为拼音，不考虑汉字的各种叫法问题，默认返回其中一个叫法的拼音
     * @param inputString
     * @return
     */
	public static String getPinYin(String inputString) {
        
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();  
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        format.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);  
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);  
  
        char[] input = inputString.trim().toCharArray();  
        StringBuffer output = new StringBuffer("");
  
        try {  
            for (int i = 0; i < input.length; i++) {  
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output.append(temp[0]);  
                    output.append(" ");  
                } else  
                    output.append(Character.toString(input[i]));
            }  
        } catch (BadHanyuPinyinOutputFormatCombination e) {  
            e.printStackTrace();  
        }  
        return output.toString();  
    } 

	/**
	 * 得到汉字不同叫法的拼音组合，一个汉字可能有多个拼音
	 */
	private static ArrayList<StringBuilder[]> getPinYinList(String[][] strJaggedArray) {
		// 组合数量
		int count = 1;
		// 将汉语拼音数组转化为对应的list，用来当做构建不同汉字发音目标数据的数据源
		ArrayList<HashSet<String>> sources = new ArrayList<HashSet<String>>();
		for (int i = 0; i < strJaggedArray.length; i++) {
			List<String> list = (List<String>) Arrays.asList(strJaggedArray[i]);
			HashSet<String> set = new HashSet<String>();
			set.addAll(list);
			count *= set.size();
			sources.add(set);
		}

		// 构建用来加载汉字不同发音的数据的数据源，来保存不同发音的组合，
        // 第一个位置代表整个拼音字符串，第二个位置代表汉字首字母组成的字符串
		ArrayList<StringBuilder[]> results = new ArrayList<StringBuilder[]>(count);
		for (int i = 0; i < count; i++) {
			results.add(new StringBuilder[] { new StringBuilder(),new StringBuilder() });
		}


        //将不同发音数据源的数据进行区分和调整，加载到需要返回到方法外的数据源中，可以让调用者获得不同发音的汉字对应的字符串
		for (HashSet<String> set : sources) {
			int index = 0;
			int size = set.size();
			int num = count / size;

			for (String s : set) {
				s = s.toLowerCase();
				for (int i = 0; i < num; i++) {
					results.get(i * size + index)[0].append(s);
					results.get(i * size + index)[1].append(s.charAt(0));
				}

				index++;
			}
		}

		return results;
	}

    /**
     * 测试方法
     */
    public static void test() {
        String str = "单田芳和";
        ArrayList<StringBuilder[]> results = getPinYinList(str);

        for (StringBuilder[] s : results) {
            Log.i("aaaaa",s[0].toString() + "...." + s[1].toString());
        }
    }

}

package com.eztcn.user.eztcn.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.net.ParseException;

/**
 * @title 字符串工具类
 * @describe 对字符串进行相应的处理，及一些判断
 * @author ezt
 * @created 2014年10月28日
 */
public class StringUtil {
	/** 中国公民身份证号码最小长度。 */
	public static final int CHINA_ID_MIN_LENGTH = 15;
	/** 中国公民身份证号码最大长度。 */
	public static final int CHINA_ID_MAX_LENGTH = 18;
	/** 省、直辖市代码表 */
	public static final String cityCode[] = { "11", "12", "13", "14", "15",
			"21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
			"42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61",
			"62", "63", "64", "65", "71", "81", "82", "91" };
	/** 每位加权因子 */
	public static final int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
			10, 5, 8, 4, 2 };
	/** 第18位校检码 */
	public static final String verifyCode[] = { "1", "0", "X", "9", "8", "7",
			"6", "5", "4", "3", "2" };
	/** 最低年限 */
	public static final int MIN = 1930;
	public static Map<String, String> cityCodes = new HashMap<String, String>();
	/** 台湾身份首字母对应数字 */
	public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();
	/** 香港身份首字母对应数字 */
	public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();
	static {
		cityCodes.put("11", "北京");
		cityCodes.put("12", "天津");
		cityCodes.put("13", "河北");
		cityCodes.put("14", "山西");
		cityCodes.put("15", "内蒙古");
		cityCodes.put("21", "辽宁");
		cityCodes.put("22", "吉林");
		cityCodes.put("23", "黑龙江");
		cityCodes.put("31", "上海");
		cityCodes.put("32", "江苏");
		cityCodes.put("33", "浙江");
		cityCodes.put("34", "安徽");
		cityCodes.put("35", "福建");
		cityCodes.put("36", "江西");
		cityCodes.put("37", "山东");
		cityCodes.put("41", "河南");
		cityCodes.put("42", "湖北");
		cityCodes.put("43", "湖南");
		cityCodes.put("44", "广东");
		cityCodes.put("45", "广西");
		cityCodes.put("46", "海南");
		cityCodes.put("50", "重庆");
		cityCodes.put("51", "四川");
		cityCodes.put("52", "贵州");
		cityCodes.put("53", "云南");
		cityCodes.put("54", "西藏");
		cityCodes.put("61", "陕西");
		cityCodes.put("62", "甘肃");
		cityCodes.put("63", "青海");
		cityCodes.put("64", "宁夏");
		cityCodes.put("65", "新疆");
		cityCodes.put("71", "台湾");
		cityCodes.put("81", "香港");
		cityCodes.put("82", "澳门");
		cityCodes.put("91", "国外");
		twFirstCode.put("A", 10);
		twFirstCode.put("B", 11);
		twFirstCode.put("C", 12);
		twFirstCode.put("D", 13);
		twFirstCode.put("E", 14);
		twFirstCode.put("F", 15);
		twFirstCode.put("G", 16);
		twFirstCode.put("H", 17);
		twFirstCode.put("J", 18);
		twFirstCode.put("K", 19);
		twFirstCode.put("L", 20);
		twFirstCode.put("M", 21);
		twFirstCode.put("N", 22);
		twFirstCode.put("P", 23);
		twFirstCode.put("Q", 24);
		twFirstCode.put("R", 25);
		twFirstCode.put("S", 26);
		twFirstCode.put("T", 27);
		twFirstCode.put("U", 28);
		twFirstCode.put("V", 29);
		twFirstCode.put("X", 30);
		twFirstCode.put("Y", 31);
		twFirstCode.put("W", 32);
		twFirstCode.put("Z", 33);
		twFirstCode.put("I", 34);
		twFirstCode.put("O", 35);
		hkFirstCode.put("A", 1);
		hkFirstCode.put("B", 2);
		hkFirstCode.put("C", 3);
		hkFirstCode.put("R", 18);
		hkFirstCode.put("U", 21);
		hkFirstCode.put("Z", 26);
		hkFirstCode.put("X", 24);
		hkFirstCode.put("W", 23);
		hkFirstCode.put("O", 15);
		hkFirstCode.put("N", 14);
	}

	// 字符串截取
	public static String subString(String text, int length, String endWith) {
		int textLength = text.length();
		int byteLength = 0;
		StringBuffer returnStr = new StringBuffer();
		for (int i = 0; i < textLength && byteLength < length * 2; i++) {
			String str_i = text.substring(i, i + 1);
			if (str_i.getBytes().length == 1) {// 英文
				byteLength++;
			} else {// 中文
				byteLength += 2;
			}
			returnStr.append(str_i);
		}
		try {
			if (byteLength < text.getBytes("GBK").length) {// getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3
				returnStr.append(endWith);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnStr.toString();
	}

	/**
	 * 将过长的标题进行省略号处理
	 * 
	 * @param titleString
	 *            原始的标题
	 * @displayCharCount 允许显示的字符数
	 * @return 处理后的标题（带有省略号的）
	 */
	public static String subTitle(String titleString, int displayCharCount) {
		if (titleString == null || titleString.equals("")) {
			return "";
		}
		if (titleString.length() > displayCharCount) {
			titleString = StringUtil.subString(titleString.trim(),
					displayCharCount, "...");
		}
		return titleString;
	}

	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;
		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static int chineseLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 去除尾部空格
	 */
	public static String tailRemoveSpace(String content) {
		while (content.endsWith("&nbsp")) {
			content = content.replaceAll("&nbsp", "");
			/*
			 * char[] strCopy = content.toCharArray(); char[] strTemp = new
			 * char[strCopy.length - 1]; for (int i = 0; i <= strCopy.length -
			 * 2; i++) { strTemp[i] = strCopy[i]; } strCopy = strTemp; content =
			 * String.copyValueOf(strCopy);
			 */
		}
		return content;
	}

	/**
	 * 去除头部空格
	 */
	public static String headRemoveSpace(String content) // 去除头部空格
	{
		char[] strCopy = content.toCharArray();
		while (content.startsWith(" ")) {
			content = String.copyValueOf(strCopy, 1, strCopy.length - 1);
		}
		return content;
	}

	/**
	 * 判断是否为中文
	 * 
	 * @return
	 */
	public static boolean allChinese(String pValue) {
		Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]");
		for (int i = 0; i < pValue.length(); i++) {
			if (!pattern.matcher(pValue.charAt(i) + "").find()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否为英文
	 * 
	 * @return
	 */
	public static boolean allEnglish(String pValue) {
		Pattern pattern = Pattern.compile("[a-zA-Z]");
		for (int i = 0; i < pValue.length(); i++) {
			String str = pValue.charAt(i) + "";
			if (!(pattern.matcher(str).find())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否有汉字
	 * 
	 * @param pValue
	 * @return
	 */
	public static boolean haveChineseStr(String pValue) {
		for (int i = 0; i < pValue.length(); i++) {
			if ((int) pValue.charAt(i) > 256)
				return true;
		}
		return false;
	}

	/**
	 * 判断是否包含特殊符号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean specialLetter(String str) {
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static boolean isIdcard(String number) {
		String pattern = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
		Pattern p = Pattern.compile(pattern);
		Matcher m = null;
		m = p.matcher(number);

		return m.matches();
	}

	// //////////////////////////////////////////////////////////////////////////////////

	/**
	 * 将15位身份证号码转换为18位
	 * 
	 * @param idCard
	 *            15位身份编码
	 * @return 18位身份编码
	 */
	public static String conver15CardTo18(String idCard) {
		String idCard18 = "";
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return null;
		}
		if (isNum(idCard)) {
			// 获取出生年月日
			String birthday = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			// 获取出生年(完全表现形式,如：2010)
			String sYear = String.valueOf(cal.get(Calendar.YEAR));
			idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
			// 转换字符数组
			char[] cArr = idCard18.toCharArray();
			if (cArr != null) {
				int[] iCard = converCharToInt(cArr);
				int iSum17 = getPowerSum(iCard);
				// 获取校验位
				String sVal = getCheckCode18(iSum17);
				if (sVal.length() > 0) {
					idCard18 += sVal;
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
		return idCard18;
	}

	/**
	 * 验证身份证是否合法
	 */
	public static boolean validateCard(String idCard) {
		String card = idCard.trim();
		if (validateIdCard18(card)) {
			return true;
		}
		if (validateIdCard15(card)) {
			return true;
		}
		String[] cardval = validateIdCard10(card);
		if (cardval != null) {
			if (cardval[2].equals("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证18位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard18(String idCard) {
		boolean bTrue = false;
		if (idCard.length() == CHINA_ID_MAX_LENGTH) {
			// 前17位
			String code17 = idCard.substring(0, 17);
			// 第18位
			String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
			if (isNum(code17)) {
				char[] cArr = code17.toCharArray();
				if (cArr != null) {
					int[] iCard = converCharToInt(cArr);
					int iSum17 = getPowerSum(iCard);
					// 获取校验位
					String val = getCheckCode18(iSum17);
					if (val.length() > 0) {
						if (val.equalsIgnoreCase(code18)) {
							bTrue = true;
						}
					}
				}
			}
		}
		return bTrue;
	}

	/**
	 * 验证15位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard15(String idCard) {
		if (idCard.length() != CHINA_ID_MIN_LENGTH) {
			return false;
		}
		if (isNum(idCard)) {
			String proCode = idCard.substring(0, 2);
			if (cityCodes.get(proCode) == null) {
				return false;
			}
			String birthCode = idCard.substring(6, 12);
			Date birthDate = null;
			try {
				birthDate = new SimpleDateFormat("yy").parse(birthCode
						.substring(0, 2));
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar cal = Calendar.getInstance();
			if (birthDate != null)
				cal.setTime(birthDate);
			if (!valiDate(cal.get(Calendar.YEAR),
					Integer.valueOf(birthCode.substring(2, 4)),
					Integer.valueOf(birthCode.substring(4, 6)))) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 验证10位身份编码是否合法
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 身份证信息数组
	 *         <p>
	 *         [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
	 *         若不是身份证件号码则返回null
	 *         </p>
	 */
	public static String[] validateIdCard10(String idCard) {
		String[] info = new String[3];
		String card = idCard.replaceAll("[\\(|\\)]", "");
		if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
			return null;
		}
		if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
			info[0] = "台湾";
			String char2 = idCard.substring(1, 2);
			if (char2.equals("1")) {
				info[1] = "M";
			} else if (char2.equals("2")) {
				info[1] = "F";
			} else {
				info[1] = "N";
				info[2] = "false";
				return info;
			}
			info[2] = validateTWCard(idCard) ? "true" : "false";
		} else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
			info[0] = "澳门";
			info[1] = "N";
			// TODO
		} else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
			info[0] = "香港";
			info[1] = "N";
			info[2] = validateHKCard(idCard) ? "true" : "false";
		} else {
			return null;
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateTWCard(String idCard) {
		String start = idCard.substring(0, 1);
		String mid = idCard.substring(1, 9);
		String end = idCard.substring(9, 10);
		Integer iStart = twFirstCode.get(start);
		Integer sum = iStart / 10 + (iStart % 10) * 9;
		char[] chars = mid.toCharArray();
		Integer iflag = 8;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true
				: false;
	}

	/**
	 * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
	 * <p>
	 * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
	 * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
	 * </p>
	 * <p>
	 * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
	 * </p>
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateHKCard(String idCard) {
		String card = idCard.replaceAll("[\\(|\\)]", "");
		Integer sum = 0;
		if (card.length() == 9) {
			sum = (Integer.valueOf(card.substring(0, 1).toUpperCase()
					.toCharArray()[0]) - 55)
					* 9
					+ (Integer.valueOf(card.substring(1, 2).toUpperCase()
							.toCharArray()[0]) - 55) * 8;
			card = card.substring(1, 9);
		} else {
			sum = 522 + (Integer.valueOf(card.substring(0, 1).toUpperCase()
					.toCharArray()[0]) - 55) * 8;
		}
		String mid = card.substring(1, 7);
		String end = card.substring(7, 8);
		char[] chars = mid.toCharArray();
		Integer iflag = 7;
		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * iflag;
			iflag--;
		}
		if (end.toUpperCase().equals("A")) {
			sum = sum + 10;
		} else {
			sum = sum + Integer.valueOf(end);
		}
		return (sum % 11 == 0) ? true : false;
	}

	/**
	 * 将字符数组转换成数字数组
	 * 
	 * @param ca
	 *            字符数组
	 * @return 数字数组
	 */
	public static int[] converCharToInt(char[] ca) {
		int len = ca.length;
		int[] iArr = new int[len];
		try {
			for (int i = 0; i < len; i++) {
				iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return iArr;
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 * 
	 * @param iArr
	 * @return 身份证编码。
	 */
	public static int getPowerSum(int[] iArr) {
		int iSum = 0;
		if (power.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				for (int j = 0; j < power.length; j++) {
					if (i == j) {
						iSum = iSum + iArr[i] * power[j];
					}
				}
			}
		}
		return iSum;
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断
	 * 
	 * @param iSum
	 * @return 校验位
	 */
	public static String getCheckCode18(int iSum) {
		String sCode = "";
		switch (iSum % 11) {
		case 10:
			sCode = "2";
			break;
		case 9:
			sCode = "3";
			break;
		case 8:
			sCode = "4";
			break;
		case 7:
			sCode = "5";
			break;
		case 6:
			sCode = "6";
			break;
		case 5:
			sCode = "7";
			break;
		case 4:
			sCode = "8";
			break;
		case 3:
			sCode = "9";
			break;
		case 2:
			sCode = "x";
			break;
		case 1:
			sCode = "0";
			break;
		case 0:
			sCode = "1";
			break;
		}
		return sCode;
	}

	/**
	 * 根据身份编号获取年龄
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 年龄
	 */
	// public static int getAgeByIdCard(String idCard) {
	// int iAge = 0;
	// if (idCard.length() == CHINA_ID_MIN_LENGTH) {
	// idCard = conver15CardTo18(idCard);
	// }
	// String year = idCard.substring(6, 10);
	// Calendar cal = Calendar.getInstance();
	// int iCurrYear = cal.get(Calendar.YEAR);
	// iAge = iCurrYear - Integer.valueOf(year);
	// return iAge;
	// }

	/**
	 * 根据身份编号获取年龄
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idCard) {
		int iAge = 0;
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
        try{
            String year = idCard.substring(6, 10);
            Integer month = Integer.valueOf(idCard.substring(10, 12));
            Integer day = Integer.valueOf(idCard.substring(12, 14));
            Calendar cal = Calendar.getInstance();
            int iCurrYear = cal.get(Calendar.YEAR);
            int iCurrMonth = cal.get(Calendar.MONTH) + 1;
            int iCurrDay = cal.get(Calendar.DAY_OF_MONTH);
            iAge = iCurrYear - Integer.valueOf(year);
        }catch (Exception e){//捕获异常，如果年龄计算出现问题就返回0
            iAge=0;
        }

//		if (month - iCurrMonth > 0) {
//			return iAge < 0 ? 0 : iAge;
//		} else if (month == iCurrMonth && day - iCurrDay >= 0) {
//			return iAge < 0 ? 0 : iAge;
//		}
//		return iAge - 1 < 0 ? 0 : iAge - 1;
		return iAge;
	}

	/**
	 * 根据身份编号获取生日
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirthByIdCard(String idCard) {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return idCard.substring(6, 14);
	}

	/**
	 * 根据身份编号获取生日年
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(yyyy)
	 */
	public static Short getYearByIdCard(String idCard) {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(6, 10));
	}

	/**
	 * 根据身份编号获取生日月
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(MM)
	 */
	public static Short getMonthByIdCard(String idCard) {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(10, 12));
	}

	/**
	 * 根据身份编号获取生日天
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(dd)
	 */
	public static Short getDateByIdCard(String idCard) {
		Integer len = idCard.length();
		if (len < CHINA_ID_MIN_LENGTH) {
			return null;
		} else if (len == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		return Short.valueOf(idCard.substring(12, 14));
	}

	/**
	 * 根据身份编号获取性别
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 性别(M-男，F-女，N-未知)
	 */
	public static String getGenderByIdCard(String idCard) {
		String sGender = "N";
		if (idCard.length() == CHINA_ID_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
		}
		String sCardNum = idCard.substring(16, 17);
		if (Integer.parseInt(sCardNum) % 2 != 0) {
			sGender = "M";
		} else {
			sGender = "F";
		}
		return sGender;
	}

	/**
	 * 根据身份编号获取户籍省份
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 省级编码。
	 */
	public static String getProvinceByIdCard(String idCard) {
		int len = idCard.length();
		String sProvince = null;
		String sProvinNum = "";
		if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
			sProvinNum = idCard.substring(0, 2);
		}
		sProvince = cityCodes.get(sProvinNum);
		return sProvince;
	}

	/**
	 * 数字验证
	 * 
	 * @param val
	 * @return 提取的数字。
	 */
	public static boolean isNum(String val) {
		return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
	}

	/**
	 * 验证小于当前日期 是否有效
	 * 
	 * @param iYear
	 *            待验证日期(年)
	 * @param iMonth
	 *            待验证日期(月 1-12)
	 * @param iDate
	 *            待验证日期(日)
	 * @return 是否有效
	 */
	public static boolean valiDate(int iYear, int iMonth, int iDate) {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int datePerMonth;
		if (iYear < MIN || iYear >= year) {
			return false;
		}
		if (iMonth < 1 || iMonth > 12) {
			return false;
		}
		switch (iMonth) {
		case 4:
		case 6:
		case 9:
		case 11:
			datePerMonth = 30;
			break;
		case 2:
			boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
					&& (iYear > MIN && iYear < year);
			datePerMonth = dm ? 29 : 28;
			break;
		default:
			datePerMonth = 31;
		}
		return (iDate >= 1) && (iDate <= datePerMonth);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static boolean isPhone(String number) {
		String pattern = "^1[3-8]{1}\\d{9}$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(number);
		return m.matches();
	}

	public static boolean isEmail(String number) {
//		String pattern = "(?=^[\\w.@]{6,50}$)\\w+@\\w+(?:\\.[\\w]{2,3}){1,2}";
		
		String pattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		// 参照 http://blog.csdn.net/lizhenning87/article/details/8182643
		 
//		([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})
//		/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;  
//		 ^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$
//		^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(number);
		return m.matches();
	}
	public static boolean isEmail2(String email){     
	     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	        Pattern p = Pattern.compile(str);     
	        Matcher m = p.matcher(email);     
	        return m.matches();     
	    } 
	
	
	
	
	
	
	
	
	
	
	
	
	
	/******** 外设 ***************/
	public static String getSystemInfos() {
		String phoneInfo = " Product: " + android.os.Build.PRODUCT;
		phoneInfo += "\n ID：" + android.os.Build.ID;
		phoneInfo += "\n SDK: " + android.os.Build.VERSION.SDK;
		phoneInfo += "\n VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
		phoneInfo += "\n CPU_ABI: " + android.os.Build.CPU_ABI;
		phoneInfo += "\n TAGS: " + android.os.Build.TAGS;
		phoneInfo += "\n VERSION_CODES.BASE: "
				+ android.os.Build.VERSION_CODES.BASE;
		phoneInfo += "\n MODEL: " + android.os.Build.MODEL;

		phoneInfo += "\n DEVICE: " + android.os.Build.DEVICE;
		// phoneInfo += "\n DISPLAY: " + android.os.Build.DISPLAY;
		phoneInfo += "\n BRAND: " + android.os.Build.BRAND;
		// phoneInfo += "\n BOARD: " + android.os.Build.BOARD;
		// phoneInfo += "\n FINGERPRINT: " + android.os.Build.FINGERPRINT;
		// phoneInfo += "\n MANUFACTURER: " + android.os.Build.MANUFACTURER;
		// phoneInfo += "\n USER: " + android.os.Build.USER;

		return phoneInfo;
	}

	/**
	 * 保留小数点后2位
	 * 
	 * @return
	 */
	public static String getTwoRadixPoint(double number) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		return df.format(number);
	}

	/**
	 * 保留小数点后1位
	 * 
	 * @param number
	 * @return
	 */
	public static String getOneRadixPoint(double number) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
		return df.format(number);
	}

	/**
	 * 字符串处理 去掉ss
	 * 
	 * @param date
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dealWithDate(String date) {
		String newTime = date.substring(0, date.lastIndexOf(":"));
		return newTime;
	}

	/**
	 * 判断时间显示:时间倒序排列，5分钟内显示刚刚，5分钟之后且当天显示时间，非当天则显示日期即可
	 * 
	 * @param time
	 *            yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	private static Date systemDate;

	public static String judgeTime(String time) {
		String str = "";
		java.text.DateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(time);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		systemDate = new Date(System.currentTimeMillis());
		if (date.getDay() == systemDate.getDay()) {
			if (date.getHours() == systemDate.getHours()
					&& systemDate.getMinutes() - date.getMinutes() < 5) {
				str = "刚刚";
			} else {
				str = time.substring(time.indexOf(" "), time.length());
				str = str.substring(0, str.lastIndexOf(":"));

			}
		} else {
			str = time.substring(0, time.indexOf(" "));
		}
		return str;
	}

	/**
	 * 根据星期获取日期(当天起往后7天的日期)
	 */
	public static String getDateByWeek(int week) {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int nowWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (week == nowWeek) {
			return sdf.format(calendar.getTime());
		}
		if (week > nowWeek) {//日期往后加天数之差
			calendar.set(Calendar.DAY_OF_MONTH, day + (week - nowWeek));
		} else {//日期+（-天数之差）
			calendar.set(Calendar.DAY_OF_MONTH, day + (7 - nowWeek + week));
		}
		return sdf.format(calendar.getTime());
	}

	/**
	 * 字符串替换
	 * 
	 * @param str
	 * @param n
	 * @param newChar
	 * @return
	 * @throws Throwable
	 */
	public static String replace(String str, int n, String newChar)
			throws Throwable {
		String s1 = "";
		String s2 = "";
		try {
			s1 = str.substring(0, n - 1);
			s2 = str.substring(n, str.length());
		} catch (Exception ex) {
			throw new Throwable("替换的位数大于字符串的位数");
		}
		return s1 + newChar + s2;
	}
	
	 public static String getStringDateShort(String dt ) {
		  /* Date currentTime = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   formatter.for
		   String dateString = formatter.format(currentTime);
		   return dateString;*/
		 
		 String str="";
		return str = String.format("{0:d}", dt);
		}
	 public static String getStringTimeShort(String dt ) {
		 /* Date currentTime = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   formatter.for
		   String dateString = formatter.format(currentTime);
		   return dateString;*/
		 
		 String str="";
		 return str = String.format("{11:d}", dt);
	 }




}

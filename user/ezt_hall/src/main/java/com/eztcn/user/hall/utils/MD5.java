package com.eztcn.user.hall.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @title 标准MD5加密方法
 * @describe 使用java类库的security包的MessageDigest类处理,也可变为非标准MD5，请修改下面的移位算法
 * @author ezt
 * @created 2014年11月10日
 */
public class MD5 {
	/**
	 * 获得MD5加密密码的方法
	 */
	public static String getMD5ofStr(String origString) {
		String origMD5 = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(origString.getBytes());
			origMD5 = byteArray2HexStr(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return origMD5;
	}

	/**
	 * 处理字节数组得到MD5密码的方法
	 */
	private static String byteArray2HexStr(byte[] bs) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(byte2HexStr(b));
		}
		return sb.toString();
	}

	/**
	 * 字节标准移位转十六进制方法
	 */
	private static String byte2HexStr(byte b) {
		String hexStr = null;
		int n = b;
		if (n < 0) {
			// 若需要自定义加密,请修改这个移位算法即可
			n = b & 0x7F + 128;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}

	/**
	 * 提供一个MD5多次加密方法
	 */
	public static String getMD5ofStr(String origString, int times) {
		String md5 = getMD5ofStr(origString);
		for (int i = 0; i < times - 1; i++) {
			md5 = getMD5ofStr(md5);
		}
		return getMD5ofStr(md5);
	}

	/**
	 * 密码验证方法
	 */
	public static boolean verifyPassword(String inputStr, String MD5Code) {
		return getMD5ofStr(inputStr).equals(MD5Code);
	}

	/**
	 * 多次加密时的密码验证方法
	 */
	public static boolean verifyPassword(String inputStr, String MD5Code,
			int times) {
		return getMD5ofStr(inputStr, times).equals(MD5Code);
	}
	/**
	 * MD5的32位加密。
	 */
	public static String MD5_32_16(String sourceStr,int place){
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			if(place==16){
				 result= buf.toString().substring(8, 24);
			}else if(place==32){
				result = buf.toString();
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
			return result;
		}
	}
}

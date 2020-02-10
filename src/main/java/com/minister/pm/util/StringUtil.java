package com.minister.pm.util;

/**
 * @date 2020年1月15日 下午2:57:09
 * @author jianxinliu
 */
public class StringUtil {

	/**
	 * 判断一个字符串是否为空。<br>
	 * <code>
	 * str == null || str.length() == 0 || "".equals(str);
	 * </code>
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 || "".equals(str);
	}

	/**
	 * 判断字符串是否不存在可见字符,全是空格则全为不可见字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		boolean ret = true;
		if (isEmpty(str)) {
			return ret;
		} else {
			char[] chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				if (32 != chars[i]) {
					return false;
				}
			}
		}
		return ret;
	}
	
	/**
	 * 判断一个字符串是否存在可见字符
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}

	/**
	 * 判断一个字符串非空。<br>
	 * !isEmpty()
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}

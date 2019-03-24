package com.minister.pm.util;

/**
 * 文本处理相关工具
 * @author ljx
 * @Date Mar 23, 2019 9:50:49 PM
 *
 */
public class TextUtil {

	/**
	 * 将指定字符串使用空格延长到指定长度
	 * @param origin
	 * @param targetLen
	 * @return
	 */
	public String extendTo(String origin,int targetLen) {
		if(origin.length() < targetLen) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < targetLen - origin.length(); i++) {
				sb.append(" ");
			}
			return origin + sb.toString();
		}else {
			return origin;
		}
	}
}

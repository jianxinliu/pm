package com.minister.pm.json;

import java.util.Map;

/**
 * JSON 字符串转对象
 * @author ljx
 * @Date Jun 9, 2019 9:34:50 PM
 *
 */
public class JSONString<T> {

	/**
	 * 转换成的对象
	 */
	private T tarObj;
	
	/**
	 * 转换成的对象的属性键值对
	 */
	private Map<String,Object> properties;
	
	/**
	 * 操作
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return properties.get(key);
	}
}

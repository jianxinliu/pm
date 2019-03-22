package com.minister.pm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 10:57:04 PM
 *
 */
public class Util {
	/**
	 * 通用的获取配置文件对象的方法，适用于配置文件的值是类路径的情况
	 * 
	 * @param maps        存放配置文件内容的 Map ，value 是新创建的对象
	 * @param clazz       创建对象的类型
	 * @param profileName 配置文件路径
	 * @return properties map 对传进来的 map 进行赋值后返回
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getProps(Map<String, T> maps, Class<?> clazz, String profileName) {
		Properties p = new Properties();

		if (maps == null)
			maps = new HashMap<String, T>();

		try {
			p.load(clazz.getResourceAsStream(profileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Object e : p.keySet()) {
			String className = String.valueOf(p.get(e));
			String key = String.valueOf(e);
			try {
				maps.put(key, (T) Class.forName(className).newInstance());
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return maps;
	}

	/**
	 * 获取配置文件的 Properties 对象
	 * 
	 * @param file 配置文件路径
	 * @return Properties 对象
	 */
	public static Properties getProps(String file) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("target/classes/" + file)));
		} catch (IOException e) {
			return p;
		}
		return p;
	}

//	../../../../../../../resources/pm.properties
	public static void main(String[] args) {
		Properties props = getProps("target/classes/pm.properties");
		System.out.print(props.get("port"));
	}
}

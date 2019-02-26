package com.minister.pm.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.minister.pm.server.bean.RequestBean;

/**
 *
 * @author ljx
 * @Date Feb 26, 2019 5:37:12 AM
 *
 */
public class HttpUtil {

	public static RequestBean parse(String data) {
		RequestBean ret = new RequestBean();
		if (data.length() != 0) {
			String sp = ": ";
			String[] items = data.split("\n");

			Map<String, String> kv = new HashMap<String, String>();

			for (int i = 0; i < items.length - 1; i++) { // 最后一行空行
				if (i == 0) {
					String[] ss = items[0].split(" ");
					kv.put("Method", ss[0]);
					kv.put("Url", ss[1]);
					kv.put("Protocol", ss[2]);
				} else {
					String k = items[i].split(sp)[0];
					String value = items[i].split(sp)[1];
					kv.put(k, value);
				}
			}

//			kv.forEach((k, v) -> {
//				System.out.println(k + "==" + v);
//			});

			Class<?> clz = RequestBean.class;

			Method[] methods = clz.getMethods();
			for (int j = 0; j < methods.length; j++) {
				Method m = methods[j];
				if (m.getName().startsWith("set")) {
					String name = m.getName();
					String head = name.substring(name.indexOf("set") + 3, name.length());
					String trueHead = addBar(head);
					String value = kv.get(trueHead);
					m.setAccessible(true);
					try {
//						System.out.println(name + "-----------------" + value);
						m.invoke(ret, value);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 例子： 将 UserAgent 转换成 User-Agent
	 * 
	 * @param method
	 * @return
	 */
	private static String addBar(String method) {
		String ret = method;
		char[] charArray = method.toCharArray();
		for (int i = 1; i < charArray.length - 1; i++) {
			if (charArray[i] >= 65 && charArray[i] <= 90) { // 大写字母
				String pre = method.substring(0, i);
				String sub = method.substring(i, method.length());
				ret = pre + "-" + sub;
			}
		}
		return ret;
	}

}

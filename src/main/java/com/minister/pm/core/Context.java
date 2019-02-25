package com.minister.pm.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.minister.pm.define.Autowired;
import com.minister.pm.define.URLMapping;

/**
 * a global object
 * 
 * @author ljx
 * @Date Feb 22, 2019 12:35:20 AM
 *
 */
public class Context {

	public Map<String,Object> components;
	
	/**
	 * 路由映射。</br>
	 * key 的组成：路由。</br>
	 * 	&nbsp;&nbsp; 路由由上级路由（定义在类体上）和下级路由（定义在方法体上）共同组成</br>
	 * value 的组成：请求处理器及其所属的类名和请求方法，以：分割</br>
	 * 	&nbsp;&nbsp; 例如：<code>com.minister.pm.controller.Hello:getUser:GET</code>
	 */
	public Map<String,String> mappers;
	
	public Scanner scanner = new Scanner();
	
	public PrimeMinister pm;

	public void autowired(Class clz) throws InstantiationException, IllegalAccessException {
		Object newInstance = clz.newInstance();
		// auto wire
//		if(clz.isAnnotationPresent(Component.class)) {
		Field[] fields = clz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			Field f = fields[j];
			if (f.isAnnotationPresent(Autowired.class)) {
				Class<?> type = f.getType();
				f.setAccessible(true);
				if (f.get(newInstance) == null) {
					f.set(newInstance, type.newInstance());
				}
			}
		}
//		}
	}

	// 映射 URL 并执行对应的 handler
	public String mapping(Class clz, String v)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String ret = "";
		Object newInstance = clz.newInstance();

		// url mapping
		Method[] methods = clz.getMethods();
		int i;
		for (i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.isAnnotationPresent(URLMapping.class)) {
				URLMapping mapper = m.getAnnotation(URLMapping.class);
				String value = mapper.value();
				if (v.equals(value)) {
					Object invoke = m.invoke(newInstance, new Object[] {});
					ret = (String) invoke;
				}
			}
		}

		if (i == methods.length && "".equals(ret)) {
			ret = "no such url";// should throw error
		}
		return ret;
	}
}
package com.minister.pm.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import reflection.annotation.define.Autowired;
import reflection.annotation.define.URLMapping;

/**
 * a global object
 * 
 * @author ljx
 * @Date Feb 22, 2019 12:35:20 AM
 *
 */
public class Context {

	private Object[] components;

	private Object[] mappers;
	
	private Scanner scanner = new Scanner();

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

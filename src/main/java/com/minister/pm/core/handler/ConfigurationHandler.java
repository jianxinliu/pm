package com.minister.pm.core.handler;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.util.ConfigUtil;
import com.minister.pm.core.Context;
import com.minister.pm.define.config.Value;
import com.minister.pm.log.Logger;

/**
 * <code>@Configuration</code> 注解的处理类
 * 
 * @date 2020年1月15日 下午10:50:27
 * @author jianxinliu
 */
public class ConfigurationHandler {

	public static void handler(Context ctx, Class<?> clz) {
		Field[] fields = clz.getFields();
		for (Field field : fields) {
			String path = field.getDeclaredAnnotation(Value.class).path();
			// logger.info("path:field->{},path->{}", field.getName(),path);
			try {
				List<String> cfgValue = ConfigUtil.getConfigValueFrom(ctx.configObjects, path);
				field.setAccessible(true);
				try {
					if (cfgValue.size() == 1) {
						// 单值转化
						Type type = field.getGenericType();
						Object castValue = cast(type, cfgValue.get(0));
						field.set(null, castValue);
					} else {
						// 列表转化，只支持数组和 List
						Class<?> type = field.getType();
						if (type.isArray() || (type.getClass().isInstance(java.util.List.class))) {
							if (type.getTypeName().endsWith("[]")) {
								Object castArray = castArray(type.getComponentType(), cfgValue);
								field.set(null, castArray);
							} else {
								Type genericType = field.getGenericType();
								if (genericType instanceof ParameterizedType) {
									ParameterizedType pt = (ParameterizedType) genericType;
									// 得到泛型里的 class 类型对象
									Class<?> eleType = (Class<?>) pt.getActualTypeArguments()[0];
									Object castList = castList(eleType, cfgValue);
									field.set(null, castList);
								}
							}
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					System.exit(1);
				}
			} catch (NoSuchConfigException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T cast(Type type, String value) {
		String typeName = type.getTypeName();
		T ret = null;
		Integer.class.getName();
		switch (typeName) {
		case "boolean":
		case "java.lang.Blloean":
			ret = (T) Boolean.valueOf(value);
			break;
		case "byte":
		case "java.lang.Byte":
			ret = (T) Byte.valueOf(value);
			break;
		case "short":
		case "java.lang.Short":
			ret = (T) Short.valueOf(value);
			break;
		case "char":
		case "java.lang.Character":
			ret = (T) (Character) value.charAt(0);
			break;
		case "int":
		case "java.lang.Integer":
			ret = (T) Integer.valueOf(value);
			break;
		case "float":
		case "java.lang.Float":
			ret = (T) Float.valueOf(value);
			break;
		case "double":
		case "java.lang.Double":
			ret = (T) Double.valueOf(value);
			break;
		case "long":
		case "java.lang.Long":
			ret = (T) Long.valueOf(value);
			break;
		default:
			ret = (T) value;
			break;
		}
		return ret;
	}

	
	/**
	 * @param type
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> List<T> castList(Type type, List<String> values) {
		List<T> ret = new ArrayList<T>();
		values.forEach(v -> {
			T cast = (T) cast(type, v);
			ret.add(cast);
		});
		return (List<T>) ret;
	}

	@SuppressWarnings("unchecked")
	private static <T> T castArray(Class<?> type, List<String> values) {
		Object array = Array.newInstance(type, values.size());
		for (int i = 0; i < values.size(); i++) {
			String v = values.get(i);
			T cast = (T) cast(type, v);
			Array.set(array, i, cast);
		}
		return (T) array;

	}

	private static Logger logger = Logger.getLogger(ConfigurationHandler.class);

}

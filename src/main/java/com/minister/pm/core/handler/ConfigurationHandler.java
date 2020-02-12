package com.minister.pm.core.handler;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.config.PMConfig;
import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.util.ConfigUtil;
import com.minister.pm.core.Context;
import com.minister.pm.define.config.Value;
import com.minister.pm.log.Logger;
import com.minister.pm.magic.MagicWords;

/**
 * <code>@Configuration</code> 注解的处理类
 * 
 * @date 2020年1月15日 下午10:50:27
 * @author jianxinliu
 */
public class ConfigurationHandler {

	/**
	 * FIXME: 设置值时进行类型转换,特别注意列表值
	 * 
	 * @param clz
	 */
	public static void handler(Context ctx, Class clz) {
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
						int listType = 0;
						String eleType = null;
						if (type.getClass().isInstance(java.util.List.class)) {
							if(type.getTypeName().endsWith("[]")){
								listType = MagicWords.LIST_TYPE_ARRAY.getIndex();
								eleType = type.getComponentType().getTypeName();
							}else{
								listType = MagicWords.LIST_TYPE_JUL.getIndex();
								Class<?> componentType = type.getComponentType();
								eleType = type.getComponentType().getTypeName();
							}
						}
						Object castList = castList(listType, eleType, cfgValue);
						field.set(null, castList);
						// java.lang.String[]
						// java.util.List<java.lang.String>
						System.out.println(type.getTypeName());
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
	 * 1. 统一按List处理，最后若是数组再转<br>
	 * 2. 列表内元素也需要逐个转换为对应类型
	 * @param ListType
	 * @param type
	 * @param values
	 * @return
	 */
	private static <T> T castList(int ListType, String type, List<String> values) {
		return null;
	}

	private static Logger logger = Logger.getLogger(ConfigurationHandler.class);

}

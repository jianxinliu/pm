package com.minister.pm.core.handler;

import java.lang.reflect.Field;
import java.util.List;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.config.PMConfig;
import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.util.ConfigUtil;
import com.minister.pm.core.Context;
import com.minister.pm.define.config.Value;
import com.minister.pm.log.Logger;

/**
 * <code>@Configuration</code> 注解的处理类
 * @date 2020年1月15日 下午10:50:27
 * @author jianxinliu
 */
public class ConfigurationHandler {

	/**
	 * TODO: 设置值时进行类型转换,特别注意列表值
	 * @param clz 
	 */
	public static void handler(Context ctx,Class clz){
		Field[] fields = clz.getFields();
		for (Field field : fields) {
			String path = field.getDeclaredAnnotation(Value.class).path();
//			logger.info("path:field->{},path->{}", field.getName(),path);
			try {
				List<String> cfgValue = ConfigUtil.getConfigValueFrom(ctx.configObjects, path);
				field.setAccessible(true);
				try {
					field.set(null, cfgValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchConfigException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static Logger logger = Logger.getLogger(ConfigurationHandler.class);
	
}

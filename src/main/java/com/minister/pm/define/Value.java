package com.minister.pm.define;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 配置注解
 * @date 2020年1月7日 下午9:41:26
 * @author jianxinliu
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, LOCAL_VARIABLE })
public @interface Value {

	/**
	 * 获取配置值的值路径
	 * @return
	 */
	String path();
	
}

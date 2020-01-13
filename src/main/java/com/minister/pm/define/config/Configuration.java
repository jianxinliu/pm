package com.minister.pm.define.config;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 配置类注解
*  @date 2020年1月13日 下午9:05:53
*  @author jianxinliu
*/
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Configuration {
	
}

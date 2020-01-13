package com.minister.pm.define;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 包扫描根路径
 * @author ljx
 * @Date Mar 23, 2019 7:08:13 PM
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScanRoot {

	/**
	 * 包扫描根路径，不指定则默认为 @App 注解所在的类的路径
	 * @return
	 */
	String root();
}

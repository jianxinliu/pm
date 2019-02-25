package com.minister.pm.define;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ METHOD,TYPE})
/**
 *
 * @author ljx
 * @Date Feb 21, 2019 7:23:36 PM
 *
 */
public @interface URLMapping {
	String value(); // URL
	String method() default "GET"; // request method
}

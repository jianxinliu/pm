package com.minister.pm.define;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *
 * @author ljx
 * @Date Feb 21, 2019 11:42:02 PM
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface Component {

	String value() default ""; // can assign component name,default set to component's type name
}

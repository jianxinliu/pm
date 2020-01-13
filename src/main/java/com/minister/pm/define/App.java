package com.minister.pm.define;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <pre>
 * @App
 * </pre>
 * 
 * annotation is to help package Scanner to find the project base path
 * 
 * @author ljx
 * @Date Feb 22, 2019 12:34:23 AM
 *
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface App {

}

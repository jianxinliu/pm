package com.minister.pm.define;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, LOCAL_VARIABLE })
/**
 * auto wired simulation
 * 
 * @author ljx
 * @Date Feb 21, 2019 11:41:09 PM
 *
 */
public @interface Autowired {

}

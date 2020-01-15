package com.minister.pm.core.exception;

/**
 * 注入异常，通常是 需要的组件未加 @Component 注解
 * @author ljx
 * @Date Mar 30, 2019 2:10:28 AM
 *
 */
public class InjectionException extends Exception {

	private static final long serialVersionUID = 1L;

	public InjectionException(String err) {
		super(err);
	}
}

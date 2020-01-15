package com.minister.pm.config.exception;

/**
 * 配置项的子配置项和其值是互斥的，有值则无子配置项。
 * 
 * @date 2020年1月15日 下午2:52:57
 * @author jianxinliu
 */
public class ValueAndSubItemConflictException extends Exception {

private static final long serialVersionUID = 1L;
	
	public ValueAndSubItemConflictException(String err) {
		super(err);
	}
}

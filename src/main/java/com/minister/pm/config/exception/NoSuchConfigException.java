package com.minister.pm.config.exception;
/**
 * 路径表示不彻底，或无此配置项
*  @date 2020年1月15日 下午2:34:22
*  @author jianxinliu
*/
public class NoSuchConfigException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoSuchConfigException(String err) {
		super(err);
	}
}

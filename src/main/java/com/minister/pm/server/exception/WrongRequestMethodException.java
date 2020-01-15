package com.minister.pm.server.exception;

/**
 *
 * @author ljx
 * @Date Feb 26, 2019 1:08:21 AM
 *
 */
public class WrongRequestMethodException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WrongRequestMethodException(String should,String but) {
		super("请求方法不对！应该是："+should+"，但却是："+but);
	}
}

package com.minister.pm.config.exception;

/**
 * 解析异常
 * @date 2020年2月10日 下午9:58:58
 * @author jianxinliu
 */
public class ParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParseException(String err) {
		super(err);
	}

}

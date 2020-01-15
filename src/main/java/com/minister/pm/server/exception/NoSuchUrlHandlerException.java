package com.minister.pm.server.exception;

/**
 *
 * @author ljx
 * @Date Feb 26, 2019 1:06:07 AM
 *
 */
public class NoSuchUrlHandlerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoSuchUrlHandlerException(String err) {
		super(err);
	}
}

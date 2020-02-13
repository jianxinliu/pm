package com.minister.pm.server.bean;

/**
 *
 * @author ljx
 * @Date Feb 21, 2019 12:28:54 AM
 *
 */
public enum StatuCode {

	NOT_FOUND("Not Found", "404"), SERVER_FAIL("Server Fail", "500"), SUCCESS("Success", "200");

	private String name;
	private String code;

	private StatuCode(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}

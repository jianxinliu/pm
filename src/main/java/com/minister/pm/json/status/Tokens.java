package com.minister.pm.json.status;

/**
 * 代表 JSON 字符串的片段
 * @author ljx
 * @Date Jun 9, 2019 9:39:08 PM
 *
 */
public enum Tokens {
	
	PRE_BRACE(1,"{"),
	
	PRE_BRACKET(2,"["),
	
	SUF_BRACE(3,"}"),
	
	SUF_BRACKET(4,"]"),
	
	COLON(5,":"),
	
	COMMA(6,","),
	
	QUOTATION(7,"'")
	
	;

	private int index;
	private String name;

	private Tokens(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}
}

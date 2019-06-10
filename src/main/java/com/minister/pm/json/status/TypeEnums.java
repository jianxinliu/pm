package com.minister.pm.json.status;

/**
 * 对象类型
 * 
 * @author ljx
 * @Date Jun 9, 2019 9:19:38 PM
 *
 */
public enum TypeEnums {

	OBJECT(1, "Object"),

	ARRAY(2, "Array"),

	STRING(3, "String")

	;

	private int index;
	private String name;

	private TypeEnums(int index, String name) {
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

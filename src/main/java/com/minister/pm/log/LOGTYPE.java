package com.minister.pm.log;

/**
 * 日志级别
 * 
 * @author ljx
 * @Date Mar 22, 2019 2:21:25 AM
 *
 */
public enum LOGTYPE {

	INFO(0, "INFO"), ERROR(1, "ERROR"), DEBUG(2, "DEBUG"), WARNING(3, "WARNING");

	private int index;
	private String name;

	private LOGTYPE(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

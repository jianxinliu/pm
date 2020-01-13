package com.minister.pm.magic;

import java.io.File;

/**
 * 全局魔法值
 * @date 2020年1月9日 上午9:32:39
 * @author jianxinliu
 */
public enum MagicWords {
	DEFAULT_CONFIG_FILE_NAME("defaultConfig.yml",0),
	JAVA_PATH("",1),
	HOST("127.0.0.1",2),
	PORT("8079",3),
	;
	
	private String name;
	private int index;

	private MagicWords(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}
	
	public String getJavaPath(String prefix){
		StringBuilder sb = new StringBuilder().append(prefix);
		sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
		.append("java");
		return sb.toString();
	}
}

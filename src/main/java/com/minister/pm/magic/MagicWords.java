package com.minister.pm.magic;

/**
 * 全局魔法值
 * 
 * @date 2020年1月9日 上午9:32:39
 * @author jianxinliu
 */
public enum MagicWords {
	DEFAULT_CONFIG_FILE_NAME("/defaultConfig.yml", 0),
//	DEFAULT_CONFIG_PATH(File.separator + "com" + File.separator +
//			"minister" + File.separator + "pm" + File.separator + 
//			"config" + File.separator + "defaultConfig.yml",1), 
	HOST("127.0.0.1", 2), 
	PORT("8079", 3), 
	TAB("    ", 4), 
	CONFIG_SUFFUX("yml", 5), 
	CONFIG_PREFIX_CONFIG("config.yml", 6), 
	CONFIG_PREFIX_APPLIOCATION("application.yml", 7), 
	CONFIG_PREFIX_BOOTSTRAP("bootstrap.yml", 8),
	
	/**
	 * ': '
	 */
	KV_SPLITTER(": ", 9), 
	LIST_PREFIX("- ", 10), 
	COMMENT("# ", 11), 
	LIST_LEVEL("LIST", 30),
	
	LIST_TYPE_ARRAY("ARRAY",13),
	LIST_TYPE_JUL("java.util.List",14);

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
}

package com.minister.pm.config.yaml;

/**
 * <code>
 * 节点类型：
 *	1 # 开头的注释行
 *	2 - 开头的列表
 *	3  其他则是正常的节点
 *</code>
 * 
 * @date 2020年2月9日 下午10:37:43
 * @author jianxinliu
 */
public enum NodeType {
	
	COMMENT("注释",0),
	LIST("列表",1),
	VALUE("配置值",2),
	OBJECT("有子配置项",3),
	WRONG("语法错误",4);

	private String name;
	private int index;

	private NodeType(String name, int index) {
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

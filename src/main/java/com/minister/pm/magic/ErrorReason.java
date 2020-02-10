package com.minister.pm.magic;
/**
 * 异常提示语
*  @date 2020年2月9日 上午9:34:25
*  @author jianxinliu
*/
public enum ErrorReason {
	
	WRONG_SPACE("YAML 语法——>前置空格不正确",0),
	NO_SUCH_CONFIG("不存在此配置！",1),
	PATH_WITH_ERROR("路径表示错误，获取不到值！",2),
	PARSE_WRONG_TYPE("解析错误，类型不匹配！",3),;

	
	private String name;
	private int index;

	private ErrorReason(String name, int index) {
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

package com.minister.pm.config.exception;
/**
 * Yaml 语法错误
*  @date 2020年2月9日 上午9:32:22
*  @author jianxinliu
*/
public class YamlSyntaxExcaption extends Exception {

	private static final long serialVersionUID = 1L;
	
	public YamlSyntaxExcaption(String err){
		super(err);
	}

}

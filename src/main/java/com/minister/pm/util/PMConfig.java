package com.minister.pm.util;

import java.util.Properties;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 11:57:30 PM
 *
 */
public class PMConfig {

	/**
	 * TODO:
	 * 逻辑有问题，这是要获取客工程的配置文件，而不是自己工程的配置文件
	 */
	private final static String pmFile = "pm.properties";
	private final static String kPORT = "port";
	
	
	public static int getPort() {
		Properties props = Util.getProps(pmFile);
		if(props.get(kPORT) != null) {
			return Integer.parseInt(props.get(kPORT).toString());
		}else {
			return 8080; // default port
		}
	}
}

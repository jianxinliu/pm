package com.minister.pm.config;

import java.util.Properties;

import com.minister.pm.define.config.Configuration;
import com.minister.pm.define.config.Value;
import com.minister.pm.util.Util;

/**
 * pm 默认配置,暂时只支持静态的属性作为配置项
 * @author ljx
 * @Date Feb 24, 2019 11:57:30 PM
 *
 */
@Configuration
public class PMConfig {

	/**
	 * TODO: 逻辑有问题，这是要获取客工程的配置文件，而不是自己工程的配置文件
	 */
	private final static String pmFile = "pm.properties";
	private final static String kPORT = "port";

	public static int getPort() {
		Properties props = Util.getProps(pmFile);
		if (props.get(kPORT) != null) {
			return Integer.parseInt(props.get(kPORT).toString());
		} else {
			return 8080; // default port
		}
	}
	
	@Value(path = "server.port")
	public static String port;
	
	@Value(path = "pm.tab")
	public static int tab;
	
	@Value(path = "spring.dataSource.urls")
	public static String[] urls;
	
}

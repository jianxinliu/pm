package com.minister.pm.config;

import com.minister.pm.define.config.Configuration;
import com.minister.pm.define.config.Value;

/**
 * pm 默认配置,暂时只支持静态的属性作为配置项
 * @author ljx
 * @Date Feb 24, 2019 11:57:30 PM
 *
 */
@Configuration
public class PMConfig {
	
	@Value(path = "server.port")
	public static int port;
	
//	@Value(path = "pm.tab")
//	public static int tab;
//	
//	@Value(path = "spring.dataSource.urls")
//	public static String[] urls;
//	
//	@Value(path = "spring.dataSource.ids")
//	public static List<Integer> ids;
//	
//	@Value(path = "spring.dataSource.urls")
//	public static List<String> urls2;
	
}

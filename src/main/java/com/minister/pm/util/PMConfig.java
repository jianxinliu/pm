package com.minister.pm.util;

/**
 *
 * @author ljx
 * @Date Feb 24, 2019 11:57:30 PM
 *
 */
public class PMConfig {

	private final static String pmFile = "pm.properties";
	private final static String kPORT = "port";
	
	
	public static int getPort() {
		return Integer.parseInt(Util.getProps(pmFile).get(kPORT).toString());
	}
}

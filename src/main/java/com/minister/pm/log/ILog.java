package com.minister.pm.log;

/**
 *
 * @author ljx
 * @return 
 * @Date Mar 22, 2019 4:03:04 AM
 *
 */
public interface ILog {

	void info(Object pattern,Object... args);
	void debug(Object pattern,Object... args);
	void error(Object pattern,Object... args);
	void warning(Object pattern,Object... args);
}

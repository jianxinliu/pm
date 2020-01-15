package com.minister.pm.config.util;

import java.util.List;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.log.Logger;

/**
 * @date 2020年1月13日 下午11:51:23
 * @author jianxinliu
 */
public class ConfigUtil {

	/**
	 * 解析 yml 文件成 ConfigItem 对象
	 * 
	 * @param
	 * @return
	 */
	public static ConfigItem parseYml() {
		ConfigItem ret = new ConfigItem();

		return ret;
	}
	

	/**
	 * 将配置文件（暂时支持 properties 文件）解析成对象存在 Context 中
	 * 
	 * @param path
	 *            配置文件路径
	 */
	private void parseConfigFile(String path) {

	}


	/**
	 * 根据配置项路径从多个配置项中获取配置值
	 * 
	 * @param cfgs
	 *            多个配置项
	 * @param path
	 *            配置路径，支持如此写法： spring.profile.active
	 * @return 配置值
	 * @throws CommonException 
	 * @throws NoSuchConfigException 
	 */
	public static String getConfigValueFrom(List<ConfigItem> cfgs, String path) throws NoSuchConfigException {
		String value = "";
		// 单层配置值。 如自定义配置：mydata:123
		if (path.indexOf(".") == -1) {
			for (ConfigItem configItem : cfgs) {
				if (configItem.getItemName() == path) {
					value = configItem.getValue();
				}
			}
		} else {
			// 嵌套配置值，如：server.host
			String firstLevel = path.substring(0, path.indexOf("."));
			for (ConfigItem ci : cfgs) {
				if (firstLevel.equals(ci.getItemName())){
					getConfigValueFrom(ci, path);
				}
			}
		}
		return value;

	}

	/**
	 * 根据配置项路径从单个配置项中获取配置值</br>
	 * 如：根据 sprig.profile.active.dev 获取 true 值 </br>
	 * <code>
	 * spring: 
	 *     profile: 
	 *	       active: 
	 *	           dev: true
	 * </code>
	 * 
	 * @param cfg
	 * @return
	 * @throws CommonException 
	 * @throws NoSuchConfigException 
	 */
	public static String getConfigValueFrom(ConfigItem cfg, String path) throws NoSuchConfigException {
		String ret = null;
		String[] paths = path.split("\\.");
		if (!cfg.getItemName().equals(paths[0])) {
			throw new NoSuchConfigException("不存在此配置！");
		}
		ConfigItem item = cfg;
		for (int i = 1; i < paths.length; i++) {
			if(item.hasSubItems()){
				try {
					ConfigItem sub = item.getSubItemByName(paths[i]);
					item = sub;
				} catch (NoSuchConfigException e1) {
					logger.error("路径 {} 错误！",path);
					return ret;
				}
			}
		}
		// 路径表示不彻底，获取不到值
		if (item.hasSubItems()){
			throw new NoSuchConfigException("路径表示错误，获取不到值！");
		} else {
			ret = item.getValue();
		}
		return ret;
	}
	
	private static Logger logger = Logger.getLogger(ConfigUtil.class);
}

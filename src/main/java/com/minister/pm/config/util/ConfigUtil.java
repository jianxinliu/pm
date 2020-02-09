package com.minister.pm.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.exception.YamlSyntaxExcaption;
import com.minister.pm.log.Logger;
import com.minister.pm.magic.ErrorReason;

/**
 * <code>
 * YAML 格式解析步骤：
 * 1. 构建配置森林（buildYamlTree）。
 * 		一种配置项(包含子配置项)构建成一棵树，多个配置项组成森林。
 * 		树的构建：
 * 			按空格组数分层（组数同则层数同），四个
 * 空格为一组，同一组数据在树的一层中。
 * 2. 收集（collect）从树根开始收集
 * 		接下进行深度优先遍历，同一层的节点都作为各自父节点的子配置项，叶结点为配置值。
 * 
 * 配置森林中树的特点：
 * 1. 只有一个根节点，但不能只有根节点，至少有一个叶结点。举例：<code>mydata:123</code>
 * 2. 配置值都在叶结点，无值则有子节点
 * 3. 节点类型：
 * 		3.1 # 开头的注释行
 * 		3.2 - 开头的列表
 * 		3.3  其他则是正常的节点
 * 
 * </code>
 * 
 * @date 2020年1月13日 下午11:51:23
 * @author jianxinliu
 */
@SuppressWarnings({ "unused" })
public class ConfigUtil {

	// 配置分组，下标表示前置空格组数。
	@SuppressWarnings("unchecked")
	ArrayList<ConfigItem>[] cfgItemGroup = new ArrayList[10];
	// ConfigItem[][] cfgItemGroup = new ConfigItem[10][];
	// List<ArrayList<ConfigItem>> cfgItemGroup = new
	// ArrayList<ArrayList<ConfigItem>>(10);

	/**
	 * 解析 yml 文件成 ConfigItem 对象。真正解析yaml语法较复杂，这里只做简单实现 <br>
	 * <code>
	 * spring: 
	 *	    profile: 
	 *	        active: 
	 *	            dev: true
	 *	            prod: false
	 *	    dataSource: 
	 *	        url: jdbc:mysql://localhost:3306/db
	 * mydata: 123
	 * </code>
	 * 
	 * @param path
	 *            yml文件路径
	 * @return List<ConfigItem>
	 */
	public static List<ConfigItem> parseYml(String path) {
		List<ConfigItem> ret = new ArrayList<ConfigItem>();

		logger.plantInfo(path);

		/**
		 * 解析注意：<br>
		 * # 是注释，此行不解析<br>
		 * - 是列表，配置值是列表<br>
		 * 键值间以": "分割
		 */

		try (FileReader fr = new FileReader(new File(path)); BufferedReader br = new BufferedReader(fr);) {
			// FileInputStream fin = new FileInputStream(new File(path));
			// BufferedInputStream bfin = new BufferedInputStream(fin);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info("line:{}", line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return ret;
	}

	/**
	 * 解析一行
	 */
	private void parseLine(String line) {

	}

	/**
	 * 一行头部空格计数,四个为一组。凑不成组的报错
	 * 
	 * @param line
	 * @return 空格组的数量
	 * @throws YamlSyntaxExcaption
	 */
	private int countHeadSpace(String line) throws YamlSyntaxExcaption {
		int ret = 0;
		int cnt = 0;
		char[] chars = line.toCharArray();
		for (char c : chars) {
			if (32 == c)
				cnt++;
			else
				break;
		}
		if (cnt % 4 != 0) {
			throw new YamlSyntaxExcaption(ErrorReason.WRONG_SPACE.getName());
		} else {
			ret = cnt / 4;
		}
		return ret;
	}

	/**
	 * 对不同空格组的配置项进项分组
	 * 
	 * @param line
	 * @throws YamlSyntaxExcaption
	 */
	private void buildYamlTree(String line) throws YamlSyntaxExcaption {
		int spaces = countHeadSpace(line);
		ArrayList<ConfigItem> cfgs = cfgItemGroup[spaces];
		if (cfgs == null) {
			cfgs = new ArrayList<ConfigItem>();
		} else {
			cfgs.add(getCfgItemFromLine(spaces, line));
			cfgItemGroup[spaces] = cfgs;
		}
	}

	/**
	 * 从一行中获取配置值
	 * 
	 * @param line
	 * @return
	 */
	private ConfigItem getCfgItemFromLine(int spaces, String line) {
		ConfigItem cfg = new ConfigItem();
		String cfgStr = line.trim();
		return cfg;
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
				if (configItem.getItemName().equals(path)) {
					value = configItem.getValue();
				}
			}
		} else {
			// 嵌套配置值，如：server.host
			String firstLevel = path.substring(0, path.indexOf("."));
			for (ConfigItem ci : cfgs) {
				if (firstLevel.equals(ci.getItemName())) {
					value = getConfigValueFrom(ci, path);
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
			throw new NoSuchConfigException(ErrorReason.NO_SUCH_CONFIG.getName());
		}
		ConfigItem item = cfg;
		for (int i = 1; i < paths.length; i++) {
			if (item.hasSubItems()) {
				try {
					ConfigItem sub = item.getSubItemByName(paths[i]);
					item = sub;
				} catch (NoSuchConfigException e1) {
					logger.error("路径 {} 错误！", path);
					return ret;
				}
			}
		}
		// 路径表示不彻底，获取不到值
		if (item.hasSubItems()) {
			throw new NoSuchConfigException(ErrorReason.PATH_WITH_ERROR.getName());
		} else {
			ret = item.getValue();
		}
		return ret;
	}

	private static Logger logger = Logger.getLogger(ConfigUtil.class);

	public static void main(String[] args) {
		System.out.println(Integer.valueOf(' ') + "   " + (32 == ' '));

		ConfigUtil cu = new ConfigUtil();
		try {
			System.out.println(cu.countHeadSpace("        "));
			System.out.println(cu.countHeadSpace("        x3"));
			System.out.println(cu.countHeadSpace("        34"));
			String line = "        r4";
			int spaces = cu.countHeadSpace(line);
			System.out.println(line.substring(spaces * 4, line.length()));
			System.out.println(line.trim());
		} catch (YamlSyntaxExcaption e) {
			e.printStackTrace();
		}
	}
}

package com.minister.pm.config.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.exception.ParseException;
import com.minister.pm.config.exception.ValueAndSubItemConflictException;
import com.minister.pm.config.exception.YamlSyntaxException;
import com.minister.pm.config.yaml.NodeType;
import com.minister.pm.log.Logger;
import com.minister.pm.magic.ErrorReason;
import com.minister.pm.magic.MagicWords;
import com.minister.pm.util.StringUtil;

/**
 * @date 2020年1月13日 下午11:51:23
 * @author jianxinliu
 */
@SuppressWarnings({ "unused" })
public class ConfigUtil {

	/**
	 * 将每一个有意义的行读入栈，此时栈中元素的层级，从底到顶是多个递增序列，一个文件都读完之后进行出栈，此时的操作说明如下：<br>
	 * 1. 也就是逆向操作，将层级最低的项归于高一级的项<br>
	 * 2. 同级元素暂时缓存成组，待碰到更低一级的元素再归于，注意同级元素和列表项（可分为列表栈和元素栈）<br>
	 * 2. 用一个栈缓存临时数据<br>
	 * 
	 * <code>
	 * level   config    
	 *	0    |spring: 
	 *	1    |    profile: 
	 *	2    |        active: 
	 *	3    |            dev: true
	 *	3    |            prod: false
	 *	1    |    dataSource: 
	 *	2    |        urls: 
	 *	3    |            - jdbc:mysql://localhost:3306/db
	 *	3    |            - jdbc:oracle://localhost:1521/db
	 *	0    |mydata: 123
	 *	0    |server: 
	 *	1    |    port: 8079
	 *	0    |pm: 
	 *	1    |    tab: 4
	 *	1    |    banner: /banner_alpha.txt
	 * </code>
	 * 
	 * @param path
	 * @return 配置列表
	 * @throws YamlSyntaxException
	 */
	public static List<ConfigItem> parseYml(String path) throws YamlSyntaxException {
		Stack<ConfigItem> stack = load(path);
		return buildConfigTree(stack);
	}

	private static Stack<ConfigItem> load(String path) throws YamlSyntaxException {
		logger.info(path);
		// ====================== parse initial ========================= //
		Stack<ConfigItem> stack = new Stack<ConfigItem>();
		// FIXME: 栈用于缓存所有行，长度应该可变
		stack.init(new ArrayList<ConfigItem>());

		// root 节点，一个配置文件中的多个配置节点作为其子节点，无值，处于 -1 层
		ConfigItem root = new ConfigItem("root");
		root.setLevel(-1);

		// 当前行的层级
		int currLevel = root.getLevel();
		stack.push(root);

		// ============================= 所有行数据入栈 ============================ //
		try (FileReader fr = new FileReader(new File(path)); BufferedReader br = new BufferedReader(fr);) {
			String line = null;
			while ((line = br.readLine()) != null) {
				// 空行则略过
				if (StringUtil.isBlank(line)) {
					continue;
				}
				// 是否含有注释
				int sharp = line.indexOf(MagicWords.COMMENT.getName());
				if (sharp != -1) {
					// '# '在行首和行间的情况都兼顾
					line = line.substring(0, sharp);
					if (StringUtil.isBlank(line)) {
						// 去除注释之后为空，则表示'# '在行首，此行不解析
						continue;
					}
				}
				
				currLevel = countLevel(line);

				ConfigItem lineConfig = new ConfigItem();
				NodeType lineType = lineType(line);

				// 一、解析当前行，有三种情况，举例如下：
				// 1. spring:（对应有子配置项和列表名两种情况）
				// 2. dev: true
				// 3. - jdbc:mysql://localhost:3306/db
				if (lineType == NodeType.LIST) {
					try {
						// 列表项特殊名字，用 ConfigItem 对象包装只是为了统一
						lineConfig = new ConfigItem(MagicWords.LIST_LEVEL.getName(), getListValue(line));
						// 列表项特殊层级
						lineConfig.setLevel(MagicWords.LIST_LEVEL.getIndex());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if (lineType == NodeType.VALUE || lineType == NodeType.OBJECT) {
					try {
						lineConfig = getCfgItemFrom(line);
						lineConfig.setLevel(currLevel);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					// 语法错误
					throw new YamlSyntaxException(ErrorReason.WRONG_SYNTAX.getName());
				}
				stack.push(lineConfig);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return stack;
	}

	// ============================= 退栈，挂载 ============================= //
	private static List<ConfigItem> buildConfigTree(Stack<ConfigItem> stack) {
		Stack<ConfigItem> cacheStack = new Stack<ConfigItem>();
		cacheStack.init(new ArrayList<ConfigItem>());

		ConfigItem cfg = null;
		while ((cfg = stack.pop()) != null) {
			ConfigItem cacheTop = null;
			if ((cacheTop = cacheStack.getTop()) != null) {
				int diff = cfg.getLevel() - cacheTop.getLevel();
				if (diff >= 0) {
					cacheStack.push(cfg);
				} else {
					// 更高层级的项即将入栈，从栈顶开始将顶部层级比其大的项出栈（到栈空或者遇到同层级的项为止）
					ConfigItem temp = null;
					int level = cfg.getLevel();
					int cnt = 1;
					while ((temp = cacheStack.get(cnt++)) != null) {
						if (cfg.getLevel() >= temp.getLevel()) {
							break;
						}
					}
					// cnt - 2 :原始 cnt == 1 ，需减去；cnt++ 惯性又加一，需减去
					for (int i = 0; i < cnt - 2; i++) {
						ConfigItem topCfg = cacheStack.pop();
						// 挂载时注意项的类型
						if (MagicWords.LIST_LEVEL.getName().equals(topCfg.getItemName())
								&& topCfg.getLevel() == MagicWords.LIST_LEVEL.getIndex()) {
							try {
								cfg.addValue(topCfg.getValue().get(0));
							} catch (ValueAndSubItemConflictException e) {
								e.printStackTrace();
							}
						} else {
							try {
								cfg.addSubItem(topCfg);
							} catch (ValueAndSubItemConflictException e) {
								e.printStackTrace();
							}
						}
					}
					cacheStack.push(cfg);
				}
			} else {
				cacheStack.push(cfg);
			}
		}

		// 丢弃 root 节点
		return cacheStack.pop().getSubItems();
	}

	/**
	 * 简易栈实现
	 * 
	 * @author jianxinliu
	 */
	private static class Stack<T> {
		private List<T> ele;
		private int top = -1;

		public void init(List<T> instance) {
			this.ele = instance;
		}

		/**
		 * 返回当前推入数据在栈中的位置，也就是 top
		 * 
		 * @param el
		 * @return
		 */
		public int push(T el) {
			this.ele.add(el);
			top++;
			return top;
		}

		public T pop() {
			if (top < 0) {
				return null;
			}
			return this.ele.remove(top--);
		}

		public T getTop() {
			if(top < 0){
				return null;
			}
			return this.ele.get(top);
		}

		public int getTopPtr() {
			return this.top;
		}

		/**
		 * 获取栈中从 top 开始倒数指定位置的元素,如：<br>
		 * <code>
		 * stack = [5,3,7,0,9,1];
		 * top = 3;
		 * get(1); ==> 0
		 * get(2); ==> 7
		 * </code>
		 * @param ptr get(1) == getTop()
		 * @return
		 */
		public T get(int ptr) {
			int idx = top - ptr + 1;
			if(idx < 0){
				return null;
			}
			return this.ele.get(idx);
		}

		public List<T> popAll() {
			List<T> ret = new ArrayList<T>();
			T t = null;
			while ((t = this.pop()) != null) {
				ret.add(t);
			}
			return ret;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Stack [ele=").append(Arrays.toString(ele.toArray())).append(", top=").append(top).append("]");
			return builder.toString();
		}
	}

	/**
	 * 判断一行属于什么类型，可检测不符合语法的行
	 * 
	 * @param line
	 * @return
	 */
	private static NodeType lineType(String line) {
		if (line.indexOf(MagicWords.LIST_PREFIX.getName()) != -1
				&& line.indexOf(MagicWords.KV_SPLITTER.getName()) == -1) {
			// 列表项的特点：有 '- ' ，无 ': '
			return NodeType.LIST;
		} else if (line.indexOf(MagicWords.KV_SPLITTER.getName()) != -1) {
			return line.split(MagicWords.KV_SPLITTER.getName()).length == 1 ? NodeType.OBJECT : NodeType.VALUE;
		} else if (line.indexOf(MagicWords.COMMENT.getName()) != -1) {
			return NodeType.COMMENT;
		} else {
			return NodeType.WRONG;
		}

	}

	/**
	 * 一行头部空格计数,四个为一组。
	 * 
	 * @param line
	 * @return 空格组的数量(层级)
	 * @throws YamlSyntaxException
	 *             凑不成组的报错
	 */
	private static int countLevel(String line) throws YamlSyntaxException {
		int tab = MagicWords.TAB.getName().length();
		int ret = 0;
		int cnt = 0;
		char[] chars = line.toCharArray();
		for (char c : chars) {
			// 32 == ' '
			if (32 == c)
				cnt++;
			else
				break;
		}
		if (cnt % tab != 0) {
			throw new YamlSyntaxException(ErrorReason.WRONG_SPACE.getName());
		} else {
			ret = cnt / tab;
		}
		return ret;
	}

	/**
	 * 从一行中获取解析过的 ConfigItem 对象<br>
	 * 
	 * 特别的，如果行中含有'# ',则1）'# '在行首：该行不传入；2）'# '在行中,则传入的line不包含
	 * '#'及其后面的内容，实际上变成了普通行
	 * 
	 * @param line
	 *            只解析普通行，列表行和注释行不解析。即只解析格式为：<code>key: value</code> 或
	 *            <code>key: </code>的行
	 * @return
	 * @throws ParseException
	 *             传入不匹配的行类型时报该异常
	 */
	private static ConfigItem getCfgItemFrom(String line) throws ParseException {
		if (line.indexOf(MagicWords.KV_SPLITTER.getName()) == -1) {
			throw new ParseException(ErrorReason.PARSE_WRONG_TYPE.getName());
		}
		ConfigItem cfg = new ConfigItem();
		String[] cfgStrs = line.split(MagicWords.KV_SPLITTER.getName());
		String _k = cfgStrs[0].trim();
		cfg.setItemName(_k);
		String _v = null;
		// 支持节点类型 OBJECT的值
		if (cfgStrs.length != 1) {
			_v = cfgStrs[1].trim();
			try {
				cfg.setValue(_v);
			} catch (ValueAndSubItemConflictException e) {
				e.printStackTrace();
			}
		}
		return cfg;
	}

	/**
	 * 解析列表值
	 * 
	 * @param listItem
	 *            列表项，格式为：<code>- www.baidu.com</code>
	 * @return
	 * @throws ParseException
	 *             传入不匹配的行类型时报该异常
	 */
	private static String getListValue(String listItem) throws ParseException {
		int list = listItem.indexOf(MagicWords.LIST_PREFIX.getName());
		if (list == -1) {
			throw new ParseException(ErrorReason.PARSE_WRONG_TYPE.getName());
		}
		return listItem.substring(list + MagicWords.LIST_PREFIX.getName().length(), listItem.length());
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
	public static List<String> getConfigValueFrom(List<ConfigItem> cfgs, String path) throws NoSuchConfigException {
		List<String> value = new ArrayList<String>();
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
	public static List<String> getConfigValueFrom(ConfigItem cfg, String path) throws NoSuchConfigException {
		List<String> ret = null;
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

		try {
			System.out.println(ConfigUtil.countLevel("        "));
			System.out.println(ConfigUtil.countLevel("        x3"));
			System.out.println(ConfigUtil.countLevel("        34"));
			String line = "        r4";
			int spaces = ConfigUtil.countLevel(line);
			System.out.println(line.substring(spaces * 4, line.length()));
			System.out.println(line.trim());
		} catch (YamlSyntaxException e) {
			e.printStackTrace();
		}

		// test
		String tex = "kd: lkd";
		int idx = tex.indexOf(": ");
		System.out.println(tex.substring(idx + 2, tex.length()));
		String tex2 = "kd: ";
		String[] split = tex2.split(": ");
		System.out.println("a:" + split[1] + ":a");
	}
}

package com.minister.pm.config;

import java.util.Arrays;
import java.util.List;

import com.minister.pm.config.exception.NoSuchConfigException;
import com.minister.pm.config.exception.ValueAndSubItemConflictException;
import com.minister.pm.config.util.ConfigUtil;
import com.minister.pm.log.Logger;
import com.minister.pm.magic.MagicWords;
import com.minister.pm.util.StringUtil;

/**
 * yml 配置文件的单个配置项，包含多层子配置项，如：</br>
 * <code>
 * spring: </br>
 *     dataSource: </br>
 *         url: </br>
 *         username: </br>
 *     profile: </br>
 * 		   active: dev</br>
 * </code>
 * 
 * @date 2020年1月13日 下午9:21:35
 * @author jianxinliu
 */
public class ConfigItem {

	// 配置项名，如：spring
	private String itemName;

	// 配置值，=若为空，则有子配置项,默认为空字符串
	private String value = "";

	// 子配置项，如：dataSource,profile
	private List<ConfigItem> subItems;

	public ConfigItem() {
	}

	public ConfigItem(String itemName, String value) {
		this.itemName = itemName;
		this.value = value;
	}

	public ConfigItem(String itemName) {
		this.itemName = itemName;
	}

	// yml 格式的配置内容输出
	private StringBuilder ymlStyle = new StringBuilder();
	// 记录前置 tab 个数
	private int blanks = 0;

	/**
	 * 以 yml 格式输出
	 * 
	 * @param preBlanks
	 *            前置 tab 数，默认为 0 个
	 * @return
	 */
	private String format(ConfigItem config) {
		ymlStyle.append(config.getItemName()).append(": ");
		// 有子配置项
		if (config.getSubItems() != null) {
			for (ConfigItem items : config.getSubItems()) {
				blanks++;
				ymlStyle.append("\n");
				for (int i = 0; i < blanks; i++) {
					ymlStyle.append(MagicWords.TAB.getName());
				}
				format(items);
				blanks--;
			}
		} else {
			// 否则，显示配置值
			ymlStyle.append(config.getValue());
		}
		return ymlStyle.toString();
	}

	/**
	 * 以 yml 格式输出
	 * 
	 * @param prefix
	 *            前置空格数，默认为 0 个
	 * @return
	 */
	public String format() {
		format(this);
		return ymlStyle.toString();
	}

	/**
	 * 判断是否有子配置项
	 * 
	 * @return
	 */
	public boolean hasSubItems() {
		List<ConfigItem> subs = this.getSubItems();
		return subs != null && subs.size() != 0;
	}

	/**
	 * 通过名称查找子配置项
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchConfigException
	 * @throws NullPointerException
	 *             未找到
	 */
	public ConfigItem getSubItemByName(String name) throws NoSuchConfigException {
		ConfigItem ret = null;
		List<ConfigItem> subItems = this.getSubItems();
		for (int i = 0; i < subItems.size(); i++) {
			if (name.equals(subItems.get(i).getItemName())) {
				ret = subItems.get(i);
				break;
			}
		}
		if (ret == null) {
			throw new NoSuchConfigException("未找到");
		}
		return ret;
	}

	public String getItemName() {
		return itemName;
	}

	public ConfigItem setItemName(String itemName) {
		this.itemName = itemName;
		return this;
	}

	public List<ConfigItem> getSubItems() {
		return subItems;
	}

	public ConfigItem setSubItems(List<ConfigItem> subItems) throws ValueAndSubItemConflictException {
		if (StringUtil.isNotEmpty(this.getValue())) {
			throw new ValueAndSubItemConflictException("该配置项已经有值了！");
		} else {
			this.subItems = subItems;
		}
		return this;
	}

	public String getValue() {
		return value;
	}

	public ConfigItem setValue(String value) throws ValueAndSubItemConflictException {
		if (this.hasSubItems()) {
			throw new ValueAndSubItemConflictException("该配置项已经有子配置项了！");
		} else {
			this.value = value;
		}
		return this;
	}

	// ================================ test =============================== //
	private static Logger logger = Logger.getLogger(ConfigItem.class);

	public static void main(String[] args) throws ValueAndSubItemConflictException {
		ConfigItem profile = new ConfigItem("profile");
		ConfigItem active = new ConfigItem("active");
		ConfigItem dev = new ConfigItem("dev", "true");
		active.setSubItems(Arrays.asList(dev));

		profile.setSubItems(Arrays.asList(active));

		ConfigItem dataSource = new ConfigItem("dataSource");
		ConfigItem url = new ConfigItem("url", "jdbc:mysql://localhost:3306/db");

		dataSource.setSubItems(Arrays.asList(url));

		ConfigItem spring = new ConfigItem("spring");

		spring.setSubItems(Arrays.asList(profile, dataSource));

		System.out.println(spring.format());

		ConfigItem mydata = new ConfigItem();
		mydata.setItemName("mydata").setValue("123").format();

		System.out.println(new ConfigItem().setItemName("mydata").setValue("123").format());

		// logger.info("get configValue {}",mydata.getConfigValue("mydata"));

		try {
			System.out.println(spring.getSubItemByName("profile").format());
		} catch (NoSuchConfigException e1) {
			System.err.println("获取不到值");
		}

		try {
			System.out.println("sdf  " + ConfigUtil.getConfigValueFrom(spring, "spring.dataSource.url"));
		} catch (NoSuchConfigException e) {
			System.err.println("路径表示不彻底，获取不到值");
		}

	}
}
package com.minister.pm.define.config;

import java.util.Arrays;
import java.util.List;

import com.minister.pm.log.Logger;

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

	// 默认缩进为 4 个空格
	private final static String TAB = "    ";
	
	public ConfigItem(){}
	
	public ConfigItem(String itemName,String value){
		this.itemName = itemName;
		this.value = value;
	}
	
	public ConfigItem(String itemName){
		this.itemName = itemName;
	}

	// yml 格式的配置内容输出
	StringBuilder ymlStyle = new StringBuilder();
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
					ymlStyle.append(TAB);
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

	public ConfigItem setSubItems(List<ConfigItem> subItems) {
		this.subItems = subItems;
		return this;
	}

	public String getValue() {
		return value;
	}

	public ConfigItem setValue(String value) {
		this.value = value;
		return this;
	}

	// ================================ test =============================== //
	private static Logger logger = Logger.getLogger(ConfigItem.class);
	public static void main(String[] args) {
		ConfigItem profile = new ConfigItem("profile");
		ConfigItem active = new ConfigItem("active");
		ConfigItem dev = new ConfigItem("dev","true");
		active.setSubItems(Arrays.asList(dev));

		profile.setSubItems(Arrays.asList(active));

		ConfigItem dataSource = new ConfigItem("dataSource");
		ConfigItem url = new ConfigItem("url","jdbc:mysql://localhost:3306/db");

		dataSource.setSubItems(Arrays.asList(url));
		
		ConfigItem spring = new ConfigItem("spring");

		spring.setSubItems(Arrays.asList(profile, dataSource));

		System.out.println(spring.format());
		
		ConfigItem mydata = new ConfigItem();
		mydata.setItemName("mydata").setValue("123").format();
		
		System.out.println(new ConfigItem().setItemName("mydata").setValue("123").format());
		
//		logger.info("get configValue {}",mydata.getConfigValue("mydata"));
	}
}
package com.minister.pm.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.minister.pm.config.util.ConfigUtil;
import com.minister.pm.core.Context;
import com.minister.pm.log.Logger;
import com.minister.pm.magic.MagicWords;

/**
 * @date 2020年1月7日 下午9:47:37
 * @author jianxinliu
 */
public class ConfigReader {

	private static final String pwd = System.getProperty("user.dir");
	private static StringBuilder sb = new StringBuilder().append(pwd);

	private static Context ctx;

	public ConfigReader(Context ctx) {
		this.ctx = ctx;
	}
	// static {
	// sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
	// .append("java");
	// }

	/**
	 * 读取工程中的配置文件，解析注入 Context </br>
	 * 默认读取文件名为：config,application,bootsrtap </br>
	 * 后缀名为：yml </br>
	 * 默认读取位置是当前工程根位置。
	 */
	public void findFile() throws IndexOutOfBoundsException {
		try {
			Files.list(Paths.get(sb.toString())).forEach(f -> {
				if (!f.toFile().isDirectory()) {
//					String config_file_key = "";
					String config_file_path = "";
					String java_path = getJavaPath(pwd);
					// 工程根目录下有用户自定义配置文件，则读取
					if (f.getFileName().endsWith(MagicWords.CONFIG_SUFFUX.getName())
							&& (f.getFileName().startsWith(MagicWords.CONFIG_PREFIX_CONFIG.getName())
									|| f.getFileName().startsWith(MagicWords.CONFIG_PREFIX_APPLIOCATION.getName())
									|| f.getFileName().startsWith(MagicWords.CONFIG_PREFIX_BOOTSTRAP.getName()))) {
						logger.info("found config file {},", f.getFileName());
						// read config file and cache to context
						String file_name = f.getFileName().toString();
						// 先简单实现为 List
//						try {
//							// 应用于不同环境的配置文件命名规则，如：application-dev.yml、application-prod.yml。用 - 区分
//							config_file_key = file_name.substring(file_name.indexOf("-") + 1, file_name.indexOf(".yml"));
//						} catch (Exception e) {
//							logger.warning("配置文件命名方式不正确 {}，应用默认配置文件！ ", f.getFileName().toString());
//							config_file_key = "default";
//							config_file_path = java_path + MagicWords.DEFAULT_CONFIG_PATH.getName();
//						}
						config_file_path = java_path + file_name;
					} else {
						logger.plantInfo("config file not found, default config will be use!");
						// read default config file which name is
						// defaultConfig.yml on com.minister.pm.config
//						config_file_key = "default";
						config_file_path = java_path + MagicWords.DEFAULT_CONFIG_PATH.getName();
					}
					System.out.println(f.getFileName());
					// 解析配置文件
					List<ConfigItem> configItems = ConfigUtil.parseYml(config_file_path);
//					ctx.configObjects.put(config_file_key, configItems);
					ctx.configObjects = configItems;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getJavaPath(String prefix) {
		StringBuilder sb = new StringBuilder().append(prefix);
		sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
				.append("java");
		return sb.toString();
	}

	private static Logger logger = Logger.getLogger(ConfigReader.class);

	// public static void main(String[] args) {
	// findFile();
	// }
}

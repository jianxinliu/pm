package com.minister.pm.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import com.minister.pm.config.exception.YamlSyntaxException;
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

	private static Context ctx;

	public ConfigReader(Context context) {
		ctx = context;
	}

	/**
	 * 读取工程中的配置文件，解析注入 Context </br>
	 * 默认读取文件名为：config,application,bootsrtap </br>
	 * 后缀名为：yml </br>
	 * 默认读取位置是 src/main/resources,且不包含在其他文件夹中。</br>
	 * 读取优先级：application.yml > bootsrtap.yml > config.yml
	 * 
	 * @throws YamlSyntaxException
	 */
	public void findFile() throws YamlSyntaxException {
		String config_file_path = null;
		final String resources_path = getResourcePath(pwd);
		try {
			Object[] cfg_files = Files.list(Paths.get(resources_path)).filter(new Predicate<Path>() {
				@Override
				public boolean test(Path f) {
					if (f.toFile().isDirectory() || f.getFileName().endsWith(".java")) {
						return false;
					}
					String fileName = f.getFileName().toString();
					if (fileName.endsWith(MagicWords.CONFIG_SUFFUX.getName())
							&& (fileName.startsWith(MagicWords.CONFIG_PREFIX_CONFIG.getName())
									|| fileName.startsWith(MagicWords.CONFIG_PREFIX_APPLIOCATION.getName())
									|| fileName.startsWith(MagicWords.CONFIG_PREFIX_BOOTSTRAP.getName()))) {
						return true;
					} else {
						return false;
					}
				}
			}).toArray();
			if(cfg_files.length > 0){
				for (int i = 0; i < cfg_files.length; i++) {
					if(cfg_files[i] != null){
						Path p = (Path)cfg_files[i];
						config_file_path = p.toString();
						logger.info("Found config file: {}", p.getFileName().toString());
						break;
					}
				}
			} else {
				config_file_path = getJavaPath(pwd) + MagicWords.DEFAULT_CONFIG_PATH.getName();
				logger.info("Config file not found,apply default config!");
			}
			
			// TODO: 应用于不同环境的配置文件命名规则，如：application-dev.yml、application-prod.yml。用 - 区分
			// ctx.configObjects.put(config_file_key, configItems);
			
			// 解析配置文件
			List<ConfigItem> configItems = null;
			try {
				configItems = ConfigUtil.parseYml2(config_file_path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (configItems != null) {
//				configItems.forEach(cfg -> {
//					System.out.println(cfg.format());
//				});
				ctx.configObjects = configItems;
			} else {
				System.exit(1);
			}
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

	private String getResourcePath(String prefix) {
		StringBuilder sb = new StringBuilder().append(prefix);
		sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
				.append("resources");
		return sb.toString();
	}

	private static Logger logger = Logger.getLogger(ConfigReader.class);

	public static void main(String[] args) {
		ConfigReader cr = new ConfigReader(ctx);
		try {
			cr.findFile();
		} catch (YamlSyntaxException e) {
			e.printStackTrace();
		}
	}
}

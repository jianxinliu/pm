package com.minister.pm.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	
	private static Context ctx ;
	
	public ConfigReader(Context ctx){
		this.ctx = ctx;
	}
//	static {
//		sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
//				.append("java");
//	}

	/**
	 * 读取工程中的配置文件，解析并作为 Bean 注入 Context </br>
	 * 默认读取文件名为：config,application,bootsrtap </br>
	 * 后缀名为：properties </br>
	 * 默认读取位置是当前工程根位置。
	 */
	public static void findFile() {
		try {
			Files.list(Paths.get(sb.toString())).forEach(f -> {
				if(!f.toFile().isDirectory()){
					if(f.getFileName().endsWith("properties") &&	
						   (f.getFileName().startsWith("config") ||
							f.getFileName().startsWith("application") || 
							f.getFileName().startsWith("bootsrtap")
						   )){
						logger.info("found config file {},",f.getFileName());
						// read config file and cache to context
						ctx.files.put(f.getFileName().toString(), sb.toString());
						
					}else{
						logger.plantInfo("config file not found, default config will be use!");
						// read default config file which name is defaultConfig.properties on com.minister.pm.config
//						sb.append(File.separator).append("src").append(File.separator).append("main").append(File.separator)
//								.append("java");
						ctx.files.put(MagicWords.DEFAULT_CONFIG_FILE_NAME.getName(), MagicWords.JAVA_PATH.getJavaPath(pwd));
//						Properties props = Util.getProps(sb.toString());
//						props.get("server.port");
					}
					System.out.println(f.getFileName());
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Logger logger = Logger.getLogger(ConfigReader.class);

	public static void main(String[] args) {
		findFile();
	}
}

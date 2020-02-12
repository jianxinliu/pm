package com.minister.pm.core;

import java.io.IOException;
import java.util.Map.Entry;

import com.minister.pm.config.ConfigReader;
import com.minister.pm.config.exception.YamlSyntaxException;
import com.minister.pm.log.Logger;
import com.minister.pm.server.HttpServer;
import com.minister.pm.util.BannerUtil;

/**
 *
 * @author ljx
 * @Date Feb 22, 2019 12:16:21 AM
 *
 */
public class PrimeMinister {

	private static Context context;
	private static HttpServer server = HttpServer.getServer();

	static {
		context = Context.getContext();
	}

	private static ConfigReader cfgReader = new ConfigReader(context);

	public static void run() {
		
		BannerUtil.printBanner();

		// 寻找配置文件，并解析成配置对象，存于 Context 中
		try {
			cfgReader.findFile();
		} catch (YamlSyntaxException e1) {
			e1.printStackTrace();
		}
		logger.info("1. Config ready!");
		// test
		for (Entry<String, Object> ent : context.beans.entrySet()) {
			String key = ent.getKey();
			Object value = ent.getValue();
			logger.info("entity:key={},value={}", key, value);
		}

		// read @App annotation
		context.start();
		logger.info("2. Context ready!");
		// read all annotation

		// let all things ready

		// start a Server
		try {
			server.run(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Logger logger = Logger.getLogger(PrimeMinister.class);
}

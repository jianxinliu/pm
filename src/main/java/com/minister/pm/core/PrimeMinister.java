package com.minister.pm.core;

import java.io.IOException;
import java.util.Map.Entry;

import com.minister.pm.config.ConfigReader;
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
		
		// read config
		cfgReader.findFile();
		
		// test
		for(Entry<String, Object> ent:context.beans.entrySet()){
			String key = ent.getKey();
			Object value = ent.getValue();
			logger.info("entity:key={},value={}", key,value);
		}
		
		BannerUtil.printBanner();
		// read @App annotation
		context.start();
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

package com.minister.pm.core;

import java.io.IOException;

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

	public static void run() {
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
}

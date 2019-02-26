package com.minister.pm.core;

import java.io.IOException;

import com.minister.pm.server.HttpServer;

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
		context = new Context();
	}
	
	public static void run() {
		// read @App annotation
		context.start();
		// read all annotation
		
		// let all things ready
		
		// start a Server
		try {
			server.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

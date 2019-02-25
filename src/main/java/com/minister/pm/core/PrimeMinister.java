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
	
	private Context context;
	
	public static void run() {
		// read @App annotation
		
		// read all annotation
		
		// let all things ready
		
		// start a Server
		try {
			HttpServer.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

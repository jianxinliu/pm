package com.minister.pm;

import java.io.IOException;

import com.minister.pm.core.PrimeMinister;
import com.minister.pm.define.App;
import com.minister.pm.server.HttpServer;

/**
 *
 * @author ljx
 * @Date Feb 25, 2019 12:15:21 AM
 *
 */
@App
public class Test {
	public static void main(String[] args) {
		PrimeMinister.run();
//		HttpServer server = HttpServer.getServer();
//		try {
//			server.run();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}

package com.minister.pm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

import com.minister.pm.server.bean.ResponseBean;
import com.minister.pm.server.bean.StatuCode;
import com.minister.pm.util.HttpUtil;
import com.minister.pm.util.PMConfig;

/**
 *
 * @author ljx
 * @Date Feb 20, 2019 11:13:58 PM
 *
 */
public class HttpServer {
	
	private static volatile HttpServer me = new HttpServer();
	
	private HttpServer() {}
	
	public static HttpServer getServer() {
		return me;
	}

	public void run() throws IOException {
		String host = "127.0.0.1";
		int port = PMConfig.getPort();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(host, port));// listen at 127.0.0.1:8080
		while (true) {
			System.out.println("Server listening on " + host + ":" + port + "....\n");
			SocketChannel socket = ssc.accept();
			System.out.println("socket:"+socket);
			ByteBuffer reqBuf = ByteBuffer.allocate(1024);

			socket.read(reqBuf);
			reqBuf.flip();
			StringBuffer sb = new StringBuffer();
			while (reqBuf.hasRemaining())
				sb.append((char) reqBuf.get());
			String reqText = sb.toString();
			
			// TODO: 解析请求对象，并做对应的事
			
//			GET / HTTP/1.1
//			Host: localhost:8080
//			Connection: keep-alive
//			User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/71.0.3578.98 Chrome/71.0.3578.98 Safari/537.36
//			Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
//			Accept-Encoding: gzip, deflate, br
//			Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
			
			// test
			if(reqText != null)
				System.out.println(HttpUtil.parse(reqText));
			reqBuf.clear();

			// response
			ByteBuffer resBuf = ByteBuffer.allocate(1024);
			
			String respText = "{\"stu\":[{\"addr\":\"beiji],\"name\":\"lary\"}]}\n";
			
			ResponseBean resp = new ResponseBean();
			resp.setProtocol("HTTP/1.1");
			resp.setStatu(StatuCode.SUCCESS);
			resp.setContentType("text/json;charset=UTF-8");
			resp.setContentLen(String.valueOf(respText.length()));
			resp.setDate(new Date());
			resp.setData(respText);
			
			resBuf.put(resp.toString().getBytes());
			resBuf.flip();
			socket.write(resBuf);
			socket.close();// state less
		}
	}

}

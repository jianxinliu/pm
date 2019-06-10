package com.minister.pm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;

import com.minister.pm.core.Context;
import com.minister.pm.exception.WrongRequestMethodException;
import com.minister.pm.log.Logger;
import com.minister.pm.server.bean.RequestBean;
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
	// 即时初始化
	private static HttpServer me = new HttpServer();
	private static Context ctx;
	private static final String HOST = "127.0.0.1";
	private static final int PORT = PMConfig.getPort();

	private HttpServer() {
	}

	public static HttpServer getServer() {
		return me;
	}

	public void run(Context context) throws IOException {
		ctx = context;

		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(HOST, PORT));// listen at 127.0.0.1:8080
		logger.info("Server listening on {}:{}....\n", HOST, PORT);
		while (true) {
			SocketChannel socket = ssc.accept();
			ByteBuffer reqBuf = ByteBuffer.allocate(1024);

			socket.read(reqBuf);
			reqBuf.flip();
			StringBuffer sb = new StringBuffer();
			while (reqBuf.hasRemaining())
				sb.append((char) reqBuf.get());
			String reqText = sb.toString();
			ResponseBean resp = new ResponseBean();

			if (reqText != null) {
				resp = dispatchRequest(HttpUtil.parse(reqText));
				reqBuf.clear();
			}
			// response
			ByteBuffer resBuf = ByteBuffer.allocate(1024);
			resBuf.put(resp.toString().getBytes());
			resBuf.flip();
			socket.write(resBuf);
			socket.close();// state less
		}
	}

	/**
	 * 解析请求对象，做对应的事 返回相应对象
	 * 
	 * @param req
	 */
	private ResponseBean dispatchRequest(RequestBean req) {
		ResponseBean ret = new ResponseBean();
		DispatchRequest dispatch = new DispatchRequest(req, ctx);
		String data = "";

		ret.setProtocol("HTTP/1.1");
		ret.setContentType("text/json;charset=UTF-8");
		ret.setDate(new Date());
		try {
			data = dispatch.urlMapper();
			if (data != StatuCode.SERVER_FAIL.getName()) {
				ret.setStatu(StatuCode.SUCCESS);
				ret.setData(data);
			} else {
				ret.setStatu(StatuCode.SERVER_FAIL);
				ret.setData("");
			}
			ret.setContentLen(String.valueOf(data.length()));
		} catch (WrongRequestMethodException e) {
			ret.setStatu(StatuCode.SERVER_FAIL);
			ret.setData(e.getMessage());
			ret.setContentLen(String.valueOf(e.getMessage().length()));
		}
		return ret;
	}

	private static Logger logger = Logger.getLogger(HttpServer.class);
}

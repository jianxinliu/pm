package com.minister.pm.server;

import com.minister.pm.core.Context;
import com.minister.pm.server.bean.RequestBean;
import com.minister.pm.server.exception.WrongRequestMethodException;

/**
 * 对请求对象进行分发 - 路由映射 - 连接时间 - Accept-XXX ...
 * 
 * @author ljx
 * @Date Feb 26, 2019 6:15:04 AM
 *
 */
public class DispatchRequest {

	private RequestBean request;

	private Context ctx;

	public DispatchRequest(RequestBean req, Context ctx) {
		this.request = req;
		this.ctx = ctx;
	}

	/**
	 * 路由映射
	 * 
	 * @return 返回 handler 的执行结果，目前只是 String
	 * @throws WrongRequestMethodException
	 */
	public String urlMapper() throws WrongRequestMethodException {
		String ret = "";
		String url = request.getUrl();
		// TODO 支持带请求参数
//		String data = request.getData();
//		if(data != null) {
//			
//		}
		ret = ctx.letHandlerInvoke(url, request.getMethod());
		return ret;
	}
}

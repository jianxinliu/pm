package com.minister.pm.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.minister.pm.define.URLMapping;
import com.minister.pm.exception.NoSuchUrlHandlerException;
import com.minister.pm.exception.WrongRequestMethodException;
import com.minister.pm.log.Logger;
import com.minister.pm.server.HttpServer;
import com.minister.pm.server.bean.HttpMethod;
import com.minister.pm.server.bean.StatuCode;

/**
 * a global object
 * 
 * @author ljx
 * @Date Feb 22, 2019 12:35:20 AM
 *
 */
public class Context {

	private static Context me = new Context();

	private Context() {
	}

	public static Context getContext() {
		return me;
	}

	/**
	 * Map<clz.getTypeName(), componentInstance>
	 */
	public Map<String, Object> components = new HashMap<String, Object>();

	/**
	 * 路由映射。</br>
	 * key 的组成：路由。</br>
	 * &nbsp;&nbsp; 路由由上级路由（定义在类体上）和下级路由（定义在方法体上）共同组成</br>
	 * value 的组成：请求处理器及其所属的类名和请求方法，以：分割</br>
	 * &nbsp;&nbsp; 例如：<code>com.minister.pm.controller.Hello:getUser:GET</code>
	 */
	public Map<String, String> mappers = new HashMap<String, String>();

	public Scanner scanner = new Scanner(this);

	public PrimeMinister pm;

	public void start() {
		Context ctx = scanner.run();
		this.components = ctx.components;
		this.mappers = ctx.mappers;
	}

	// 映射 URL 并执行对应的 handler
	@Deprecated
	private String mapping(Class clz, String v)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String ret = "";
		Object newInstance = clz.newInstance();

		// url mapping
		Method[] methods = clz.getMethods();
		int i;
		for (i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (m.isAnnotationPresent(URLMapping.class)) {
				URLMapping mapper = m.getAnnotation(URLMapping.class);
				String value = mapper.value();
				if (v.equals(value)) {
					Object invoke = m.invoke(newInstance, new Object[] {});
					ret = (String) invoke;
				}
			}
		}
		if (i == methods.length && "".equals(ret)) {
			ret = "no such url";// should throw error
		}
		return ret;
	}

	/**
	 * 根据请求路由获取一个处理器 直接执行
	 * 
	 * @param url
	 * @throws NoSuchUrlHandlerException
	 * @throws WrongRequestMethodException
	 */
	public String letHandlerInvoke(String url, HttpMethod method) {
		// TODO: 暂时统一把返回值作为 String
		logger.info("URL:{}\n", url);
		String ret = "";
		for (Entry<String, String> entry : this.mappers.entrySet()) {
			String k = entry.getKey(), v = entry.getValue();
			// 路由直接匹配，需要完全匹配
			if (url != null && url.equals(k)) {
				// 拆分 value -> handleClassName:methodName:requestMethod
				String[] split = v.split(":");
				// 如果请求方法不对，则不是这个处理器处理
				if (!method.getName().equals(split[2])) {
					try {
						throw new WrongRequestMethodException(split[2], method.getName());
					} catch (WrongRequestMethodException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					}
				} else {
					// 请求方法正确，开始实例化处理器并且调用
					
					String clazz = split[0], handlerMethod = split[1];
					try {
						Object obj = this.components.get(clazz);
						// TODO： 暂时都当做没有参数的 handler 处理
						Class<?>[] args1 = null;

						Method handler = obj.getClass().getMethod(handlerMethod, args1);

						handler.setAccessible(true);
						// 总是调用出错,是因为给没有参数的方法传了参数，区别对待就行了
						if (handler.getParameterCount() > 0) {
							Object[] params = new Object[handler.getParameterCount()];
							ret = (String) handler.invoke(obj, params);
						} else {
							ret = (String) handler.invoke(obj);
						}
						// TODO： 分析需要抛出的异常，根据异常构建响应码
					} catch (NoSuchMethodException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					} catch (SecurityException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					} catch (IllegalAccessException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					} catch (IllegalArgumentException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					} catch (InvocationTargetException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getName();
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 从 components 列表中获取一个组件实例
	 * 
	 * @param clzName
	 * @return
	 */
	public Object getComponents(String clzName) {
		return this.components.get(clzName);
	}

	private static Logger logger = Logger.getLogger(Context.class);
}

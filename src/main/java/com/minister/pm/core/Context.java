package com.minister.pm.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.minister.pm.config.ConfigItem;
import com.minister.pm.define.URLMapping;
import com.minister.pm.log.Logger;
import com.minister.pm.server.bean.HttpMethod;
import com.minister.pm.server.bean.StatuCode;
import com.minister.pm.server.exception.NoSuchUrlHandlerException;
import com.minister.pm.server.exception.WrongRequestMethodException;

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

	public Remedy remedy = Remedy.getRemedy();

	/**
	 * 用于补救时的数据存储
	 * 
	 * @author jianxinliu
	 *
	 */
	static class Remedy {
		private static final Remedy remedy = new Remedy();

		private Remedy() {
		}

		public static Remedy getRemedy() {
			return remedy;
		}

		/**
		 * 用于临时存储在类扫描阶段没有注入到的组件，待扫描完成一次性补救注入<br>
		 * fieldsNeedInject:存储需要注入组件的属性 InjectCompnent：存储 fieldsNeedInject 对应位置属性需注入的组件
		 */
		private List<Field> fieldsNeedInject = new ArrayList<Field>();
		private List<Class<?>> injectCompnent = new ArrayList<Class<?>>();

		public void addTempComponentName(Field f, Class<?> name) {
			fieldsNeedInject.add(f);
			injectCompnent.add(name);
		}

		public int getSize() {
			return fieldsNeedInject.size();
		}

		public Field getField(int idx) {
			return fieldsNeedInject.get(idx);
		}

		public Class<?> getComponent(int idx) {
			return injectCompnent.get(idx);
		}
	}

	/**
	 * simple beans map.</br>
	 * Map<a,b>.</br>
	 * a should be bean name,b should be bean obejct or bean value.</br>
	 * Bean name default to be variable name.
	 */
	public Map<String, Object> beans = new HashMap<String, Object>();

	/**
	 * 全局配置项,多种配置，如： dev,default,product...</br>
	 * Map<String,List<ConfigItem>> => Map<configName,List<ConfigItem>><br>
	 * 先简化为List<ConfigItem>
	 */
	// public Map<String,List<ConfigItem>> configObjects = new
	// HashMap<String,List<ConfigItem>>();
	public List<ConfigItem> configObjects = new ArrayList<ConfigItem>();

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
		this.beans = ctx.beans;
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
		logger.info("URL:{}", url);
		String ret = "";
		if(url == null){
			return StatuCode.NOT_FOUND.getName();
		}
		// URL 是否命中，若未命中，则返回 404
		boolean urlNotHit = true;
		for (Entry<String, String> entry : this.mappers.entrySet()) {
			String k = entry.getKey(), v = entry.getValue();
			// 路由直接匹配，需要完全匹配
			if (url.equals(k)) {
				urlNotHit = false;
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
						ret = StatuCode.SERVER_FAIL.getCode();
					} catch (SecurityException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getCode();
					} catch (IllegalAccessException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getCode();
					} catch (IllegalArgumentException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getCode();
					} catch (InvocationTargetException e) {
						logger.error(e);
						ret = StatuCode.SERVER_FAIL.getCode();
					}
				}
			}
		}
		// 路由未命中，404
		if(urlNotHit){
			logger.info("Not Found url:{}",url);
			ret = StatuCode.NOT_FOUND.getCode();
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

package com.minister.pm.core;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.minister.pm.config.ConfigReader;
import com.minister.pm.core.exception.InjectionException;
import com.minister.pm.core.handler.ConfigurationHandler;
import com.minister.pm.define.PmApplication;
import com.minister.pm.define.Autowired;
import com.minister.pm.define.Component;
import com.minister.pm.define.RestController;
import com.minister.pm.define.URLMapping;
import com.minister.pm.define.config.Configuration;
import com.minister.pm.log.Logger;

/**
 * package scanner
 * 
 * @author ljx
 * @Date Feb 22, 2019 1:14:00 AM
 *
 */
public class Scanner {

	private static Context ctx;

	// 扫描的思路需要变，实际应用时，需要扫描的代码和这个框架的代码不在一起，只需要扫描实际代码（实际业务代码）即可，而不是框架代码
	// 应该以 @App 注解所在的类的位置开始扫描全包
	private static String pwd = System.getProperty("user.dir") + "/src/main/java";/// home/ljx/data_code/sts_workspace/pm

	private static ConfigReader cfgReader = new ConfigReader(ctx);
	
	public Scanner(Context context) {
		ctx = context;
	}

	public Context run() {
		rootTrval(pwd);
		return ctx;
	}

	/**
	 * 遍历文件，寻找注解，并将对应注解转换成运行时，转换成运行时的意思是： 需要注入的类：创建类并赋值 映射的路由：完成路由和 Handler 的对应
	 * 
	 * @param p
	 */
	public static void rootTrval(String p) {
		try {
			Files.list(Paths.get(p)).forEach(path -> {
				if (path.toFile().isDirectory()) {
					rootTrval(path.toString());
				} else {
					if (path.toString().endsWith(".java")) {
						String javaPath = convertToJavaPath(path);
						try {
							Class<?> clz = Class.forName(javaPath);
							logger.debug(clz.getName()); 
							findClassAnnotation(clz);
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InjectionException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将Java文件的绝对路径转换成 Java 路径
	 * 
	 * @param path
	 * @return
	 */
	private static String convertToJavaPath(Path path) {
		String ret = "";
		String p = path.toString();
		// 去掉 .java 后缀
		int dot = p.indexOf(".java");
		ret = p.substring(0, dot);

		// 去掉包名前缀
		String split = File.separator + "java" + File.separator;
		ret = ret.substring(ret.indexOf(split) + split.length(), ret.length());

		// 路径分隔符换成 .
		ret = ret.replace(File.separator, ".");
		return ret;
	}

	/**
	 * 对给定的类进行扫描，发现注解，并将相应的内容设置到 Context 中
	 * 
	 * FIXME: 修改实现方式，支持多种类型的注解，使用设计模式
	 * @param clz 类
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InjectionException  
	 */
	private static void findClassAnnotation(Class<?> clz) throws InstantiationException, IllegalAccessException, InjectionException {
		Annotation[] clzAnnos = clz.getAnnotations();
		URLMapping classReqMap = clz.getAnnotation(URLMapping.class);
		String reqMethod = "";
		String pUrl = "";
		// 如果类体定义了 URLMapping 则此类内的所有方法接受的请求类型均为此注解指定的方法
		if (classReqMap != null) {
			// 方法内不再查看 URLMapping 的请求方法
			reqMethod = classReqMap.method();
			pUrl = classReqMap.value();
		}
		// 当前类体上是否由 URLMapping 注解
		if (clz.isAnnotationPresent(Component.class)) {
			ctx.components.put(clz.getTypeName(), clz.newInstance()); // Component 组件集合
		} else if (clz.isAnnotationPresent(RestController.class)) {
			ctx.components.put(clz.getTypeName(), clz.newInstance()); // Component 组件集合
			Method[] methods = clz.getMethods();

			for (Method m : methods) {
				if (m.isAnnotationPresent(URLMapping.class)) {
					URLMapping mtdAnno = m.getAnnotation(URLMapping.class);
					String url = "";
					// 下级路由，定义在方法上
					String sUrl = mtdAnno.value();
					// 如果有上级路由，则将下级路由拼接在上级路由之后
					if (classReqMap != null) {
						url = pUrl + sUrl;
					} else {
						// 没有定义上级路由，则方法的路由作为最终路由，方法的请求方法作为最终请求方法
						reqMethod = mtdAnno.method();
						url = sUrl;
					}
					// 路由处理器的类名加方法名，以 ： 分割
					String handlerName = clz.getName() + ":" + m.getName();
					// mappers 的 key:路由---由上级路由加下级路由组成
					// mappers 的 value:处理器+请求方法，以 ： 分割
					ctx.mappers.put(url, handlerName + ":" + reqMethod);
					logger.info("Mapped url:[{}] to :{}.{}()", url, clz.getName(), m.getName());
				}
			}
		} else if (clz.isAnnotationPresent(PmApplication.class)) {

		} else if(clz.isAnnotationPresent(Configuration.class)){
			ConfigurationHandler.handler(ctx,clz);
		}
		// 将当前类中需要注入的组件注入
		letComponentWired(clz);
	}

	/**
	 * 自动植入被依赖的组件. 从 Components 列表中查找，若没有则报异常
	 * 
	 * @param clz
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InjectionException 
	 */
	public static void letComponentWired(Class<?> clz) throws IllegalAccessException, InjectionException {
		// auto wire
		Object newInstance = null;
		Object targetInstance = null;
		Field[] fields = clz.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			Field f = fields[j];
			if (f.isAnnotationPresent(Autowired.class)) {
				Class<?> type = f.getType();
				// 从 ctx.components 中查找需要的组件，不存在则报找不到组件的错误
				targetInstance = ctx.components.get(type.getName());
				newInstance = ctx.components.get(clz.getName());
				if (targetInstance != null) {
					f.setAccessible(true);
					if (f.get(newInstance) == null) {
						f.set(newInstance, targetInstance);
					}
				} else {
					// 注入错误
					throw new InjectionException("目标组件:"+type.getName()+"不存在!");
				}
			}
		}
	}

	/**
	 * 查看当前需要注入的组件是否被扫描（需要的组件是否加了 @Component注解）
	 * 
	 * @param clzName
	 * @return
	 */
	private Class<?> clzInComponents(String clzName) {
		ctx.components.entrySet().forEach(c -> {

		});
		return null;
	}

	private static Logger logger = Logger.getLogger(Scanner.class);
}

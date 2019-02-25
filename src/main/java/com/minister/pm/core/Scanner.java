package com.minister.pm.core;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.minister.pm.define.App;
import com.minister.pm.define.Component;
import com.minister.pm.define.RestController;
import com.minister.pm.define.URLMapping;

/**
 * package scanner
 * 
 * @author ljx
 * @Date Feb 22, 2019 1:14:00 AM
 *
 */
public class Scanner {

	private static Context ctx = new Context();

	private static String pwd = System.getProperty("user.dir") + "/src/main/java";/// home/ljx/data_code/sts_workspace/pm

	public static void main(String[] args) throws IOException {
//		convertToJavaPath(Paths.get("/home/ljx/data_code/sts_workspace/pm/src/main/java/com/minister/pm/util/PMConfig.java"));
		rootTrval(pwd);
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
//							System.out.println(clz.getName());
							findClassAnnotation(clz);
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
						System.out.println(path);
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
		// TODO: dump way
		ret = ret.substring(ret.indexOf("/java/") + 6, ret.length());

		// 斜杠换成 .
		ret = ret.replace("/", ".");
		return ret;
	}

	/**
	 * 对给定的类进行扫描，发现注解，并将相应的内容设置到 Context 中
	 * 
	 * @param clz 类
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void findClassAnnotation(Class<?> clz) throws InstantiationException, IllegalAccessException {
		Annotation[] clzAnnos = clz.getAnnotations();
		for (Annotation an : clzAnnos) {
			Class<? extends Annotation> annoType = an.annotationType();
			if (annoType.isAnnotationPresent(Component.class)) {
				ctx.components.put(clz.getSimpleName(), clz.newInstance());
			} else if (annoType.isAnnotationPresent(RestController.class)) {
				// 当前类体上是否由 URLMapping 注解
				boolean hasPUrl = clz.isAnnotationPresent(URLMapping.class);
				Method[] methods = clz.getMethods();
				String reqMethod = "";
				if (hasPUrl) {// 如果类体定义了 URLMapping 则次类内的所有方法接受的请求类型均为此注解指定的方法
					reqMethod = ((URLMapping) an).method();// 方法内不再查看 URLMapping 的请求方法
				}
				for (Method m : methods) {
					if (m.isAnnotationPresent(URLMapping.class)) {
						URLMapping mtdAnno = m.getAnnotation(URLMapping.class);
						String url = "";
						// 下级路由，定义在方法上
						String sUrl = mtdAnno.value();
						if (hasPUrl) {// 如果有上级路由，则将下级路由拼接在上级路由之后
							String pUrl = ((URLMapping) an).value();
							url = pUrl + sUrl;
						} else {// 没有定义上级路由，则方法的路由作为最终路由，方法的请求方法作为最终请求方法
							reqMethod = mtdAnno.method();
							url = sUrl;
						}
						// 路由处理器的类名加方法名，以 ： 分割
						String handlerName = clz.getName() + ":" + m.getName();
						// mappers的 key:路由---由上级路由加下级路由组成
						// mappers的 value:处理器+请求方法，以 ： 分割
						ctx.mappers.put(url, handlerName + ":" + reqMethod);
					}
				}
			} else if (annoType.isAnnotationPresent(App.class)) {

			}
		}
	}

}

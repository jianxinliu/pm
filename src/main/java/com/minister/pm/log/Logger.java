package com.minister.pm.log;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import com.minister.pm.log.exception.LogException;

/**
 *
 * @author ljx
 * @Date Feb 27, 2019 4:55:55 AM
 *
 */
public class Logger implements ILog {

	private static Class<?> clz = null;
	private static volatile Logger logger;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static String shortName = ""; // 短类名

	private Logger(Class<?> clz) {
		Logger.clz = clz;
	}

	public static Logger getLogger(Class<?> clazz) {
		if (logger == null) {
			synchronized (Logger.class) {
				if (logger == null) {
					logger = new Logger(clazz);
				}
			}
		}
		shortName = shortenClsName(clz.getName());
		return logger;
	}

	/**
	 * 简单的区别打印，支持模板解析
	 * 
	 * @param pattern 模板
	 * @param args    填入模板的值
	 */
	public void debug(Object pattern, Object... args) {
		try {
			String ret = parse(pattern, LOGTYPE.DEBUG, args);
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 简单的区别打印，支持模板解析
	 * 
	 * @param pattern 模板
	 * @param args    填入模板的值
	 */
	public void warning(Object pattern, Object... args) {
		try {
			String ret = parse(pattern, LOGTYPE.WARNING, args);
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 简单的区别打印，支持模板解析
	 * 
	 * @param pattern 模板
	 * @param args    填入模板的值
	 */
	public void error(Object pattern, Object... args) {
		try {
			String ret = parse(pattern, LOGTYPE.ERROR, args);
			System.err.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用于异常输出
	 * 
	 * @param e
	 */
	public void error(Exception e) {
		error("Cause by: {}\n",e.getClass().getName());
		if (e.getCause() != null && e.getCause().toString().length() > 0) {
			error(e.getCause());
		}
		if (e.getMessage() != null && e.getMessage().toString().length() > 0) {
			error(e.getMessage());
		}
		error(e.getStackTrace());
	}

	/**
	 * 简单的区别打印，支持模板解析
	 * 
	 * @param pattern 模板
	 * @param args    填入模板的值
	 */
	public void info(Object pattern, Object... args) {
		try {
			String ret = parse(pattern, LOGTYPE.INFO, args);
			System.out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parse(Object pattern, LOGTYPE type, Object... args) throws Exception {
		if (pattern == null) {
			return "";
		}
		LogEntity le = new LogEntity();

		le.setTime(sdf.format(new Date()));
		le.setType(type.getName());
		String extra = parseObject(pattern, args);
		le.setExtra(extra);
		le.setClzFull(shortName);

		return le.toString();
	}

	/**
	 * 如果是数组或列表，转换成字符串
	 * 
	 * @param obj
	 * @return
	 * @throws LogException
	 */
	private String parseObject(Object obj, Object... args) throws LogException {
		String ret = "";
		if (obj instanceof String) {
			ret = parse0((String) obj, args);
		} else if (obj.getClass().isArray()) {
			StringJoiner sj = new StringJoiner("\n");
			sj.add("\n");
			for (int i = 0; i < Array.getLength(obj); i++) {
				sj.add("    " + Array.get(obj, i).toString());
			}
			sj.add("\n");
			ret = sj.toString();
		} else if (obj instanceof List) {
			StringJoiner sj = new StringJoiner("\n");
			sj.add("\n");
			List<Object> list = Arrays.asList(obj);
			list.forEach(l -> {
				sj.add("    " + l.toString());
			});
			sj.add("\n");
			ret = sj.toString();
		}
		return ret;
	}

	/**
	 * 解析日志占位符
	 * 
	 * @param pattern
	 * @param args
	 * @return
	 * @throws LogException
	 */
	private String parse0(String pattern, Object... args) throws LogException {
		StringBuffer logStr = new StringBuffer();
		char[] chars = pattern.toCharArray();
		int argsLen = args.length;
		int j = 0;
		for (int i = 0; i < chars.length; i++) {
			try {
				if (chars[i] == '{' && chars[i + 1] == '}') { // 占位符，判断是否完整的占位符，是则填入值，否则不处理
					if (j > argsLen - 1) {
						throw new LogException("参数和值的个数不匹配！");
					} else {
						logStr.append(args[j]);
					}
					j++;

					// 跳过下一个 '}'
					i++;
					continue;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// do nothing，最后加单括号的情况，忽略
			}
			// 一般字符，照常输出
			logStr.append(chars[i]);
		}
		return logStr.toString();
	}

	/**
	 * 短类名
	 * 
	 * @param cName com.minister.pm.log.Logger
	 * @return c.m.p.l.Logger
	 */
	private static String shortenClsName(String cName) {
		StringJoiner sj = new StringJoiner(".");
		int dotIdx = 0;
		int dotIdxPre = dotIdx - 1;
		int cLen = cName.length();
		while ((dotIdx = cName.indexOf(".", dotIdxPre + 1)) != -1) {
			sj.add(cName.substring(dotIdxPre + 1, dotIdxPre + 2)); // 首字母
			dotIdxPre = dotIdx;
		}
		sj.add(cName.substring(cName.lastIndexOf(".") + 1, cLen)); // 类名
		return sj.toString();
	}

	public static void main(String[] args) {
		Logger logger2 = Logger.getLogger(Logger.class);
//		logger2.error("/user/hello start req:{},{},{}", "jack", 2, 5, 6, 4);
//		logger2.info(new int[] { 1, 4, 5, 7, 8 });
//		logger2.info(Arrays.asList(1, 4, 5, 7, 8));
//		logger2.info("dkimfnglkl");
		try {
			int a = 2 / 0;
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			System.out.println(e.getCause());
			System.out.println(e.getMessage());
			logger2.error(e.getStackTrace());
			System.out.println(e.getLocalizedMessage());
		}
		logger2.info(null);
//		System.out.println(logger2.shortenClsName("com.minister.pm.log.Logger"));
//		String str = "jid.mmk.okod";
//		System.out.println(str.substring(0, str.indexOf(".", str.indexOf(".") + 1)));
//		System.out.println(str.substring(str.lastIndexOf(".") + 1, str.length()));
//		System.out.println(new String(str.toCharArray(), 0, 1));
	}

}

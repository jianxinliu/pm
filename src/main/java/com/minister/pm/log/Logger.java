package com.minister.pm.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

import com.minister.pm.log.exception.LogException;

/**
 *
 * @author ljx
 * @Date Feb 27, 2019 4:55:55 AM
 *
 */
public class Logger {

	private static Class<?> clz;
	private static volatile Logger logger;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static final String shortName = shortenClsName(clz.getName());

	private Logger(Class<?> clz) {
		this.clz = clz;
	}

	public static Logger getLogger(Class<?> clazz) {
		if (logger == null) {
			synchronized (Logger.class) {
				if (logger == null) {
					logger = new Logger(clazz);
				}
			}
		}
		return logger;
	}

	/**
	 * 简单的区别打印，支持模板解析
	 * 
	 * @param pattern 模板
	 * @param args    填入模板的值
	 */
	public void debug(String pattern, Object... args) {
		try {
			System.out.println(parse(pattern,LOGTYPE.DEBUG, args));
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
	public void warning(String pattern, Object... args) {
		try {
			System.out.println(parse(pattern,LOGTYPE.WARNING, args));
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
	public void error(String pattern, Object... args) {
		try {
			System.out.println(parse(pattern,LOGTYPE.ERROR, args));
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
	public void info(String pattern, Object... args) {
		try {
			System.out.println(parse(pattern,LOGTYPE.INFO, args));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String parse(String pattern,LOGTYPE type, Object... args) throws Exception {
		LogEntity le = new LogEntity();
		
		le.setTime(sdf.format(new Date()));
		le.setType(type.getName());
		le.setExtra(parse0(pattern,args));
		le.setClzFull(shortName);

		return le.toString();
	}
	
	/**
	 * 解析日志占位符
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
	 * @param cName com.minister.pm.log.Logger
	 * @return c.m.p.l.Logger
	 */
	private static String shortenClsName(String cName) {
		StringJoiner sj = new StringJoiner(".");
		int dotIdx = 0;
		int dotIdxPre = dotIdx -1 ;
		int cLen = cName.length();
		while ((dotIdx = cName.indexOf(".", dotIdxPre+1)) != -1) {
			sj.add(cName.substring(dotIdxPre+1,dotIdxPre+2));   // 首字母
			dotIdxPre = dotIdx;
		}
		sj.add(cName.substring(cName.lastIndexOf(".") + 1, cLen)); // 类名
		return sj.toString();
	}

	public static void main(String[] args) {
		Logger logger2 = Logger.getLogger(Logger.class);
		logger2.info("/hello/user start req：{}, {},{}", "Jack", 2, 4, 3);
//		System.out.println(logger2.shortenClsName("com.minister.pm.log.Logger"));
//		String str = "jid.mmk.okod";
//		System.out.println(str.substring(0, str.indexOf(".", str.indexOf(".") + 1)));
//		System.out.println(str.substring(str.lastIndexOf(".") + 1, str.length()));
//		System.out.println(new String(str.toCharArray(), 0, 1));
	}

}

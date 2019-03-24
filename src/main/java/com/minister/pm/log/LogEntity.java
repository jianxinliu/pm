package com.minister.pm.log;

import com.minister.pm.util.TextUtil;

/**
 * 日志实体
 * 
 * @author ljx
 * @Date Mar 22, 2019 2:27:53 AM
 *
 */
public class LogEntity {

	private final static int clsPathMaxLen = 30;

	/**
	 * 打印时间
	 */
	private String time;

	/**
	 * 日志级别
	 */
	private String type;

	/**
	 * 类名全名
	 */
	private String clzFull;

	/**
	 * 附加数据
	 */
	private String extra;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClzFull() {
		return clzFull;
	}

	public void setClzFull(String clzFull) {
		this.clzFull = clzFull;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	/**
	 * TODO: 格式化输出（对齐） 格式化输出只需要让类路径对齐即可，只有类名的长度不固定。 可以设置一个类名的最大长度。没达到则补齐空格
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		time = textUtil.extendTo(time,24);
		builder.append(time);
		builder.append("  ");
		type = textUtil.extendTo(type,8);
		builder.append(type);
		builder.append(" --- [");
		
		clzFull = textUtil.extendTo(clzFull,clsPathMaxLen);
		builder.append(clzFull);

		builder.append("] : ");
		builder.append(extra);
		return builder.toString();
	}
	
	private TextUtil textUtil = new TextUtil();
}

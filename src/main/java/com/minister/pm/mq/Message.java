package com.minister.pm.mq;

/**
 * @date 2020年3月25日 上午9:33:05
 * @author jianxinliu
 */
public class Message {

	private String name;
	private String msg;
	private boolean readable;
	
	public Message(){
		this.readable = true;
	}
	
	public Message(String name,String msg){
		this.name = name;
		this.msg = msg;
		this.readable = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public boolean isReadable() {
		return readable;
	}

	public void consume(){
		this.readable = false;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Topic [name=").append(name).append(", msg=").append(msg).append(", readable=").append(readable)
				.append("]");
		return builder.toString();
	}
}

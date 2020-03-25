package com.minister.pm.mq;

/**
 * @date 2020年3月25日 上午9:27:36
 * @author jianxinliu
 */
public class Publisher {

	private Broker b;

	public Publisher(Broker b) {
		this.b = b;
	}

	public void publishAndPush(Message m) {
		this.b.publish(m, true);
	}

	public void publish(Message m) {
		this.b.publish(m, false);
	}
	
	public void broadcase(Message m){
		this.b.broadcast(m);
	}
}

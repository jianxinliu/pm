package com.minister.pm.mq;

import java.util.Optional;

/**
 * @date 2020年3月25日 上午9:27:51
 * @author jianxinliu
 */
public class Subscriber {

	private Broker b;

	public Subscriber(Broker b) {
		this.b = b;
	}

	/**
	 * 被动消费
	 * 
	 * @param t
	 * @return
	 */
	public boolean recive(Message t) {
		t.consume();
		System.out.printf("%s\t recive:\t %s\n", Thread.currentThread().getName(), t.getMsg());
		return true;
	}

	/**
	 * 主动消费
	 * 
	 * @param name
	 * @return
	 */
	public boolean consume() {
		Optional<Message> poll;
		synchronized (b) {
			poll = this.b.poll();
		}
		if (poll.isPresent()) {
			System.out.printf("%s\t poll:\t %s %s\n", Thread.currentThread().getName(), poll.get().getName(),
					poll.get().getMsg());
			return true;
		} else {
			System.out.printf("%s\t poll:\tfail!\n", Thread.currentThread().getName());
			return false;
		}
	}

	public void subscribe(String channel) {
		this.b.subscribe(channel, this);
	}
}

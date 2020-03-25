package com.minister.pm.mq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @date 2020年3月25日 上午9:27:24
 * @author jianxinliu
 */
public class Broker {

	private Message[] messages;
	private int cap;
	private int write = 0;
	private int read = 0;

	private Map<String, List<Subscriber>> subscribers = new HashMap<>();

	public Broker(int cap) {
		this.cap = cap;
		this.messages = new Message[cap];
	}

	/**
	 * 接收 publisher 发布的消息并及时推给订阅者
	 * 
	 * @param t
	 * @return
	 */
	private boolean push(Message t) {
		List<Subscriber> sub = subscribers.get(t.getName());
		sub.forEach(s -> {
			s.recive(t);
		});
		return true;
	}

	/**
	 * 提供给订阅者主动消费的接口
	 * 
	 * @return
	 */
	public Optional<Message> poll() {
		if (read > -1) {
			Message msg = this.messages[read % cap];
			if (null != msg) {
				this.messages[read % cap] = null;
				read++;
				return Optional.of(msg);
			}
		}
		return Optional.ofNullable(null);
	}

	/**
	 * 接收消费者的按主题订阅
	 * 
	 * @param t
	 * @param sub
	 */
	public void subscribe(String name, Subscriber sub) {
		List<Subscriber> obj = this.subscribers.get(name);
		if (obj == null) {
			this.subscribers.put(name, Arrays.asList(sub));
		} else {
			obj.add(sub);
		}
	}

	/**
	 * 生产者发布信息
	 * 
	 * @param t
	 * @return
	 */
	public boolean publish(Message t, boolean push) {
		if (this.write > this.cap - 1) {
			return false;
		} else {
			this.messages[write % cap] = t;
			write++;
			System.out.printf("%s\t publish:\t%s success!\n",Thread.currentThread().getName(), t.getMsg());
			if (push)
				push(t);
			return true;
		}
	}

}

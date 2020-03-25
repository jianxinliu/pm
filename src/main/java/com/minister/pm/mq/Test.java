package com.minister.pm.mq;

/**
 * @date 2020年3月25日 上午9:37:41
 * @author jianxinliu
 */
public class Test {

	private final static int WRITE_INTERNAL = 5000;
	private final static int MESSAGE_COUNT = 2;
	private final static int READ_INTERNAL = 3000;

	public static void main(String[] args) {
		Broker b = new Broker(10);

		Subscriber sub = new Subscriber(b);
		sub.subscribe("jianxin");

		Subscriber sub2 = new Subscriber(b);
		sub2.subscribe("jianxinliu");

		Publisher pub = new Publisher(b);

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < MESSAGE_COUNT; i++) {
					try {
						Thread.sleep(WRITE_INTERNAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// pub.publishAndPush(new Message("jianxin", "hello"+i));
					pub.publish(new Message("jianxin", "hello" + i));
				}
			}
		}, "Writer").start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(READ_INTERNAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sub.consume();
				}
			}
		}, "Reader").start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < MESSAGE_COUNT; i++) {
					try {
						Thread.sleep(WRITE_INTERNAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// pub.publishAndPush(new Message("jianxin", "hello"+i));
					pub.publish(new Message("jianxinliu", "hahah" + i));
				}
			}
		}, "Writer2").start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(READ_INTERNAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sub2.consume();
				}
			}
		}, "Reader2").start();
		
		pub.broadcase(new Message("abc", "mq!"));
	}
}

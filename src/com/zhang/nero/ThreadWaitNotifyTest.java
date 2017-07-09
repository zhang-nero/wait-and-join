package com.zhang.nero;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadWaitNotifyTest {
	public static void main(String args[]) throws InterruptedException {
		ExecutorService excutors = Executors.newCachedThreadPool();
		Vector<String> obj = new Vector<String>();
		Consumer consumer1 = new Consumer(obj, "1");
		Consumer consumer2 = new Consumer(obj, "2");
		Producter producter1 = new Producter(obj, "1");
		Producter producter2 = new Producter(obj, "2");
		excutors.submit(producter1);
		excutors.submit(producter2);
		excutors.submit(consumer1);
		excutors.submit(consumer2);
		TimeUnit.SECONDS.sleep(10);
		excutors.shutdownNow();
	}
}

/* 消费者 */
class Consumer implements Runnable {
	private Vector<String> obj;
	private String id;

	public Consumer(Vector<String> v,String id) {
		this.obj = v;
		this.id = id;
	}

	public void run() {
		synchronized (obj) {
			while (!Thread.interrupted()) {
				try {
					while (obj.size() == 0) {
						System.out.println("Consumer: " + id +" There is no good. Wait.");
						obj.wait();
					}
					System.out.println("Consumer: " + id + " goods have been taken, " + obj.toString());
					obj.remove(0);
					obj.notifyAll();
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}

/* 生产者 */
class Producter implements Runnable {
	private Vector<String> obj; 
	private String id;

	public Producter(Vector<String> v, String id) {
		this.obj = v;
		this.id = id;
	}

	public void run() {
		synchronized (obj) {
			while (!Thread.interrupted()) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
					while (obj.size() != 0) {
						System.out.println("Producter: " + id + " obj is not empty. Waiting.");
						obj.wait();
					}

					obj.add("apples");
					obj.notifyAll();
					System.out.println("Producter: " + id + ":obj are ready; " + "obj is" + obj.toString());
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
}
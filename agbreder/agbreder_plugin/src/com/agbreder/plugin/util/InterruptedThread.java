package com.agbreder.plugin.util;

public class InterruptedThread extends Thread {

	private Thread thread;
	private long miliseg;
	private Runnable runnable;

	public InterruptedThread(long miliseg) {
		this(miliseg, null);
	}

	public InterruptedThread(long miliseg, Runnable runnable) {
		this.setName(InterruptedThread.class.getSimpleName());
		this.thread = Thread.currentThread();
		this.miliseg = miliseg;
		this.runnable = runnable;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(this.miliseg);
		} catch (InterruptedException e) {
			return;
		}
		this.thread.interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		if (this.runnable != null && this.thread.isAlive()) {
			this.runnable.run();
		}
	}

}

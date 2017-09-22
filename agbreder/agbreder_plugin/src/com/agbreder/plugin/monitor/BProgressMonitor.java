package com.agbreder.plugin.monitor;

import org.eclipse.core.runtime.IProgressMonitor;

public class BProgressMonitor extends AbstractProgressMonitor implements IProgressMonitor {

	public static final BProgressMonitor DEFAULT = new BProgressMonitor();

	private Thread thread;

	private int count;

	@Override
	public synchronized void beginTask(String name, int totalWork) {
		// if (this.thread == null || this.thread == Thread.currentThread()) {
		// this.thread = Thread.currentThread();
		// this.count++;
		// } else {
		// try {
		// this.wait();
		// } catch (InterruptedException e) {
		// }
		// this.thread = Thread.currentThread();
		// }
	}

	@Override
	public synchronized void done() {
		// this.count--;
		// if (this.thread == Thread.currentThread() && this.count == 0) {
		// this.thread = null;
		// this.notify();
		// }
	}

}

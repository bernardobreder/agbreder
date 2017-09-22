package com.age.gui;

import javax.swing.SwingUtilities;

public abstract class RemoteTask extends Thread {

	private boolean stop;

	public abstract void perform() throws Throwable;

	public abstract void updateUI();

	public abstract void handler(Throwable e);

	public void cancel() {
		this.stop = true;
		this.interrupt();
	}

	public void run() {
		try {
			this.perform();
			if (!this.stop) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		} catch (final Throwable e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					handler(e);
				}
			});
		}
	}

}

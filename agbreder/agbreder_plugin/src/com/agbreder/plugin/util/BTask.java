package com.agbreder.plugin.util;

import org.eclipse.ui.PlatformUI;

public abstract class BTask extends Thread {

	public BTask() {
		this.setName(this.getClass().getSimpleName());
	}

	@Override
	public final void run() {
		try {
			action();
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					updateUI();
				}
			});
		} catch (Throwable e) {
			handler(e);
		} finally {
			finallyed();
		}
	}

	public abstract void action() throws Exception;

	public void updateUI() {
	}

	public void handler(Throwable e) {
		e.printStackTrace();
	}

	public void finallyed() {
	}

}

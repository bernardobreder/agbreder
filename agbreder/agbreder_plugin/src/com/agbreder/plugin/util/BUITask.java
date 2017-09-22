package com.agbreder.plugin.util;

import org.eclipse.ui.PlatformUI;

public abstract class BUITask implements Runnable {

	@Override
	public final void run() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				updateUI();
			}
		});
	}

	public abstract void updateUI();

	public void start() {
		this.run();
	}

}

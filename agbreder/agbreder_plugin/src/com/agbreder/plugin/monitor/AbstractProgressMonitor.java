package com.agbreder.plugin.monitor;

import org.eclipse.core.runtime.IProgressMonitor;

public abstract class AbstractProgressMonitor implements IProgressMonitor {

	@Override
	public void internalWorked(double work) {
		// System.out.println(String.format("Work = " + work));
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
		// System.out.println(String.format("Cancel = " + value));
	}

	@Override
	public void setTaskName(String name) {
		// System.out.println(String.format("TaskName = %s", name));
	}

	@Override
	public void subTask(String name) {
		// System.out.println(String.format("SubTask = %s", name));
	}

	@Override
	public void worked(int work) {
		// System.out.println(String.format("Worked = " + work));
	}

}

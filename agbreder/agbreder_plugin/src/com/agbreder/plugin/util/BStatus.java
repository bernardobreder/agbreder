package com.agbreder.plugin.util;

import org.eclipse.core.runtime.Status;

import com.agbreder.plugin.BActivator;

public class BStatus extends Status {

	public BStatus(int severity, String message) {
		super(severity, BActivator.PLUGIN_ID, message);
	}

}

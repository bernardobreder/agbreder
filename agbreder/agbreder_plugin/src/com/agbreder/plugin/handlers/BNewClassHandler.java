package com.agbreder.plugin.handlers;

import org.eclipse.jface.wizard.IWizard;

import com.agbreder.plugin.wizard.build.bclass.BClassNewWizard;

public class BNewClassHandler extends ShortcutHandler {

	@Override
	public IWizard newInstance() {
		return new BClassNewWizard();
	}

}

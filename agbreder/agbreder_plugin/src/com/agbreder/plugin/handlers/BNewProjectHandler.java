package com.agbreder.plugin.handlers;

import org.eclipse.jface.wizard.IWizard;

import com.agbreder.plugin.wizard.build.bproject.BProjectNewWizard;

public class BNewProjectHandler extends ShortcutHandler {

	@Override
	public IWizard newInstance() {
		return new BProjectNewWizard();
	}

}

package com.agbreder.plugin.handlers;

import org.eclipse.jface.wizard.IWizard;

import com.agbreder.plugin.wizard.build.bpackage.BPackageNewWizard;

public class BNewPackageHandler extends ShortcutHandler {

	@Override
	public IWizard newInstance() {
		return new BPackageNewWizard();
	}

}

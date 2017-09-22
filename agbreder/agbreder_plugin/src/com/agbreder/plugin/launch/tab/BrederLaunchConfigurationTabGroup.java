package com.agbreder.plugin.launch.tab;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class BrederLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup implements ILaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		this.setTabs(new ILaunchConfigurationTab[] { new MainLaunchConfigurationTab() });
	}

}

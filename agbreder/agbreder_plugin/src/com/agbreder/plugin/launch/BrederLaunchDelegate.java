package com.agbreder.plugin.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import com.agbreder.plugin.Eclipse;

public class BrederLaunchDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String projectname = Eclipse.getProjectName(configuration);
		if (projectname == null) {
			Eclipse.showMessageInLaunch("Launch Configuration '%s' do not have a projet associated", configuration.getName());
			return;
		}
		IProject project = Eclipse.getProject(projectname);
		if (project == null) {
			Eclipse.showMessageInLaunch("Not found the Project with name '%s'", projectname);
			return;
		}
		execute(project, launch, monitor);
	}

	private void execute(IProject project, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (Eclipse.needSave()) {
			Eclipse.saveAll();
			Eclipse.build(project, monitor);
		}
		Process process = Eclipse.buildAgbProcess(project, "Main");
		Eclipse.buildConsoleProcess(launch, process, "AGB Virtual Machine");
	}

}

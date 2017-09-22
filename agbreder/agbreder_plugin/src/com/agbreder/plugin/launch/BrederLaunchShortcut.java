package com.agbreder.plugin.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.Eclipse;
import com.agbreder.plugin.builder.BNature;
import com.agbreder.plugin.monitor.BProgressMonitor;

public class BrederLaunchShortcut implements ILaunchShortcut {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection s = (IStructuredSelection) selection;
			if (s.getFirstElement() instanceof IResource) {
				IResource file = (IResource) s.getFirstElement();
				this.launch(file.getProject(), mode);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof FileEditorInput) {
			FileEditorInput finput = (FileEditorInput) input;
			this.launch(finput.getFile().getProject(), mode);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	private void launch(IProject project, String mode) {
		try {
			if (project.getNature(BNature.NATURE_ID) == null) {
				return;
			}
			ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();
			for (ILaunchConfiguration config : configs) {
				if (config.getAttribute(BrederProjectConstant.PROJECT_NAME, "").equals(project.getName())) {
					config.launch(mode, BProgressMonitor.DEFAULT);
					return;
				}
			}
			Eclipse.showMessageInLaunch("Not found Launch Configuration");
		} catch (Exception e) {
			Eclipse.showMessageInLaunch(e.getMessage());
		}
	}

}

package com.agbreder.plugin.wizard.build.bproject.template;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public abstract class AbstractTemplate {

	public abstract void build(IProject project) throws CoreException;

	public abstract String getTemplateName();

}

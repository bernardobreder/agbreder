package com.agbreder.plugin.wizard.validator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.agbreder.plugin.element.breder.BProject;
import com.agbreder.plugin.util.BStatus;

public class BProjectViewerValidator extends BSelectionStatusValidator {

	@Override
	public IStatus validate(Object[] selection) {
		for (Object element : selection) {
			if (element.getClass() != BProject.class) {
				return new BStatus(Status.ERROR, "Not a project");
			}
		}
		return new BStatus(Status.OK, "Ok");
	}

}

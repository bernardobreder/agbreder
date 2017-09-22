package com.agbreder.plugin.wizard.validator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.agbreder.plugin.element.breder.BClass;
import com.agbreder.plugin.util.BStatus;

public class BClassViewerValidator extends BSelectionStatusValidator {

	@Override
	public IStatus validate(Object[] selection) {
		for (Object element : selection) {
			if (element.getClass() != BClass.class) {
				return new BStatus(Status.ERROR, "Not a class");
			}
		}
		return new BStatus(Status.OK, "Ok");
	}

}

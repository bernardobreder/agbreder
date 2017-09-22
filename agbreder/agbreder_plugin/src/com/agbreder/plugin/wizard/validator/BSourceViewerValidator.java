package com.agbreder.plugin.wizard.validator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.agbreder.plugin.element.breder.BSource;
import com.agbreder.plugin.util.BStatus;

public class BSourceViewerValidator extends BSelectionStatusValidator {

	@Override
	public IStatus validate(Object[] selection) {
		if (selection.length == 1 && selection[0].getClass() == BSource.class) {
			return new BStatus(Status.OK, "Ok");
		} else {
			return new BStatus(Status.ERROR, "Not a source folder");
		}
	}

}

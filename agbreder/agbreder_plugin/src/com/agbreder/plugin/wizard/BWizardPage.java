package com.agbreder.plugin.wizard;

import org.eclipse.jface.wizard.WizardPage;

import com.agbreder.plugin.ui.UIValidated;

public abstract class BWizardPage extends WizardPage implements UIValidated {

	private BWizardValidator validator;

	public BWizardPage() {
		super("");
	}

	@Override
	public void validate() {
		if (this.validator != null) {
			this.validator.update();
		}
	}

	public void setValidator(BWizardValidator validator) {
		this.validator = validator;
	}

}
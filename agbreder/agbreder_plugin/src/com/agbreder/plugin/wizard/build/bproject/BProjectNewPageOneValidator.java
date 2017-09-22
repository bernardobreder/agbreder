package com.agbreder.plugin.wizard.build.bproject;

import org.eclipse.core.resources.ResourcesPlugin;

import com.agbreder.plugin.wizard.BWizardValidator;
import com.agbreder.plugin.wizard.component.PackageUtil;

public class BProjectNewPageOneValidator extends BWizardValidator {

	public BProjectNewPageOneValidator(BProjectNewPageOne page) {
		super(page);
	}

	@Override
	public String validate() {
		BProjectNewPageOne page = (BProjectNewPageOne) this.getPage();
		String value = page.getProjectname();
		if (value.length() == 0) {
			return "No project choosed";
		}
		if (!PackageUtil.isValid(value)) {
			return "BProject invalied";
		}
		if (ResourcesPlugin.getWorkspace().getRoot().getProject(value).exists())
			return "BProject already exist";
		return null;
	}

}

package com.agbreder.plugin.wizard.build.bpackage;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import com.agbreder.plugin.wizard.BWizardValidator;
import com.agbreder.plugin.wizard.component.PackageUtil;

public class BPackageNewPageOneValidator extends BWizardValidator {

	public BPackageNewPageOneValidator(BPackageNewPageOne page) {
		super(page);
	}

	@Override
	public String validate() {
		BPackageNewPageOne page = (BPackageNewPageOne) this.getPage();
		String value = page.getPackagename();
		String source = page.getSourcename();
		if (value.length() == 0) {
			return "No package choosed";
		}
		if (!PackageUtil.isValid(value)) {
			return "BPackage invalied";
		}
		IFolder sourceFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(source));
		IFolder packageFolder = sourceFolder.getFolder(value.replace('.', File.separatorChar));
		if (packageFolder.exists()) {
			return "BPackage already exist";
		}
		return null;
	}
}

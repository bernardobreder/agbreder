package com.agbreder.plugin.wizard.build.bclass;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.wizard.BWizardValidator;
import com.agbreder.plugin.wizard.component.ClassUtil;

public class BClassNewPageOneValidator extends BWizardValidator {

	public BClassNewPageOneValidator(BClassNewPageOne page) {
		super(page);
	}

	@Override
	public String validate() {
		BClassNewPageOne page = (BClassNewPageOne) this.getPage();
		String source = page.getSourcename();
		if (!ClassUtil.isValid(page.getClassname())) {
			return "Class invalied";
		}
		IFolder sourceFolder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(source));
		IFile classFile = sourceFolder.getFile(page.getClassname() + BrederProjectConstant.BREDER_EXTENSION);
		if (classFile.exists()) {
			return "Class already exist";
		}
		return null;
	}
}

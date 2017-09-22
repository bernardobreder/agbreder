package com.agbreder.plugin.wizard.build.bproject;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.agbreder.plugin.wizard.BWizard;

public class BProjectNewWizard extends BWizard implements INewWizard {

	private BProjectNewPageOne fFirstPage;

	public BProjectNewWizard() {
		super();
	}

	public void addPages() {
		addPage(fFirstPage = new BProjectNewPageOne());
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		String projectname = this.getfFirstPage().getProjectname();
		int templateIndex = this.getfFirstPage().getTemplateIndex();
		new BProjectNewTask(projectname, templateIndex).start();
		return true;
	}

	public BProjectNewPageOne getfFirstPage() {
		return fFirstPage;
	}

}

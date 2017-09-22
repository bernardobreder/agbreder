package com.agbreder.plugin.wizard.build.bclass;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.agbreder.plugin.wizard.BWizard;

public class BClassNewWizard extends BWizard implements INewWizard {

	private BClassNewPageOne fFirstPage;

	public BClassNewWizard() {
		super();
	}

	public void addPages() {
		addPage(fFirstPage = new BClassNewPageOne());
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		String sourcename = this.getfFirstPage().getSourcename();
		String classname = this.getfFirstPage().getClassname();
		new BClassNewTask(sourcename, classname).start();
		return true;
	}

	public BClassNewPageOne getfFirstPage() {
		return fFirstPage;
	}

}

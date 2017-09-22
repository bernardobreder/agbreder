package com.agbreder.plugin.wizard.build.bpackage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.agbreder.plugin.wizard.BWizard;

public class BPackageNewWizard extends BWizard implements INewWizard {

	private BPackageNewPageOne fFirstPage;

	public BPackageNewWizard() {
		super();
	}

	public void addPages() {
		addPage(fFirstPage = new BPackageNewPageOne());
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		String sourcename = this.getfFirstPage().getSourcename();
		String packagename = this.getfFirstPage().getPackagename();
		new BPackageNewTask(sourcename, packagename).start();
		return true;
	}

	public BPackageNewPageOne getfFirstPage() {
		return fFirstPage;
	}

}

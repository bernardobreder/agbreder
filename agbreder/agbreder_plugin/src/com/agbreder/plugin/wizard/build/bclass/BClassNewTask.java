package com.agbreder.plugin.wizard.build.bclass;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.agbreder.plugin.BActivator;
import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.Eclipse;
import com.agbreder.plugin.monitor.BProgressMonitor;
import com.agbreder.plugin.util.BTask;

public class BClassNewTask extends BTask {

	private final String sourcename;

	private final String classname;

	private IFile classFile;

	public BClassNewTask(String sourcename, String classname) {
		super();
		this.sourcename = sourcename;
		this.classname = classname;
	}

	@Override
	public void action() throws Exception {
		StringTokenizer token = new StringTokenizer(sourcename, "/");
		String projectName = token.nextToken();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {
			Eclipse.showMessageInWizard("Not found the BProject with name '%s'", projectName);
		}
		IFolder sourceFolder = project.getFolder(BrederProjectConstant.SOURCE_FOLDER);
		if (!sourceFolder.exists()) {
			Eclipse.showMessageInWizard("Not found the BSource with name '%s' in the BProject", BrederProjectConstant.SOURCE_FOLDER, projectName);
		}
		classFile = sourceFolder.getFile(classname + BrederProjectConstant.BREDER_EXTENSION);
		if (classFile.exists()) {
			Eclipse.showMessageInWizard("Class '%s' already exist in the BProject with name '%s'", classFile.getFullPath().toOSString(), projectName);
		}
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("class", classname);
			InputStream input = BActivator.getTemplate(BrederProjectConstant.TEMPLATE, map);
			classFile.create(input, true, BProgressMonitor.DEFAULT);
		}
		{
			Eclipse.openFile(classFile);
		}
	}

	@Override
	public void updateUI() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, classFile);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}

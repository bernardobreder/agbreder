package com.agbreder.plugin.wizard.build.bproject;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.Eclipse;
import com.agbreder.plugin.builder.BNature;
import com.agbreder.plugin.element.ElementManager;
import com.agbreder.plugin.monitor.BProgressMonitor;
import com.agbreder.plugin.util.BTask;
import com.agbreder.plugin.view.project.ProjectView;
import com.agbreder.plugin.wizard.build.bproject.template.AbstractTemplate;
import com.agbreder.plugin.wizard.build.bproject.template.TemplateProject;

public class BProjectNewTask extends BTask {

	private final String projectname;

	private int templateIndex;

	public BProjectNewTask(String projectname, int templateIndex) {
		this.projectname = projectname;
		this.templateIndex = templateIndex;
		if (this.templateIndex < 0) {
			this.templateIndex = 0;
		}
	}

	@Override
	public void action() throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);
		if (!project.exists()) {
			project.create(BProgressMonitor.DEFAULT);
		}
		if (!project.isOpen()) {
			project.open(BProgressMonitor.DEFAULT);
		}
		{
			IProjectDescription desc = project.getDescription();
			desc.setNatureIds(new String[] { BNature.NATURE_ID });
			project.setDescription(desc, BProgressMonitor.DEFAULT);
		}
		{
			{
				IFolder folder = project.getFolder(BrederProjectConstant.BINARY_FOLDER);
				if (!folder.exists()) {
					folder.create(true, true, BProgressMonitor.DEFAULT);
				}
			}
			{
				IFolder folder = project.getFolder(BrederProjectConstant.SOURCE_FOLDER);
				if (!folder.exists()) {
					folder.create(true, true, BProgressMonitor.DEFAULT);
				}
			}
			{
				IFolder folder = project.getFolder(BrederProjectConstant.BAR_FOLDER);
				if (!folder.exists()) {
					folder.create(true, true, BProgressMonitor.DEFAULT);
				}
			}
			{
				IFolder folder = project.getFolder(BrederProjectConstant.TEST_FOLDER);
				if (!folder.exists()) {
					folder.create(true, true, BProgressMonitor.DEFAULT);
				}
			}
		}
		{
			String comment = project.getDescription().getComment();
			comment = comment + String.format("source : %s\n", BrederProjectConstant.SOURCE_FOLDER);
			IProjectDescription desc = project.getDescription();
			desc.setComment(comment);
			project.setDescription(desc, null);
		}
		{
			AbstractTemplate template = TemplateProject.getInstance().get(this.templateIndex);
			template.build(project);
		}
		{
			ILaunchConfiguration lc = Eclipse.addLaunchConfig(project);
			lc.launch("run", BProgressMonitor.DEFAULT);
			ElementManager.getInstance().getRoot().refresh(1);
		}
	}

	@Override
	public void updateUI() {
		if (ProjectView.getInstance().getViewer() != null) {
			ProjectView.getInstance().getViewer().refresh();
		}
	}

}

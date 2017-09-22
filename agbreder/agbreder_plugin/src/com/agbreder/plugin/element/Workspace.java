package com.agbreder.plugin.element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.agbreder.plugin.builder.BNature;
import com.agbreder.plugin.element.breder.BProject;

public class Workspace extends Parent<IBProject> {

	public Workspace() {
		super(null);
	}

	@Override
	public IBProject[] doRefresh() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<BProject> list = new ArrayList<BProject>();
		for (IProject project : projects) {
			if (project.exists() && project.isOpen()) {
				try {
					String[] natures = project.getDescription().getNatureIds();
					if (natures.length == 1 && natures[0].equals(BNature.NATURE_ID)) {
						list.add(new BProject(this, project));
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return list.toArray(new IBProject[0]);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getFullname() {
		return "";
	}

	public BProject mkdir(String name) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if (!project.exists()) {
			project.create(null);
		}
		if (!project.isOpen()) {
			project.open(null);
		}
		return new BProject(this, project);
	}

	public Element mkfile(String name) throws IOException {
		throw new IllegalStateException();
	}

}

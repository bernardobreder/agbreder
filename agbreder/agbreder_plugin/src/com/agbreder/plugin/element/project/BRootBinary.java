package com.agbreder.plugin.element.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.agbreder.plugin.builder.BDescriberBinary;
import com.agbreder.plugin.builder.BNature;
import com.agbreder.plugin.element.Parent;

public class BRootBinary extends Parent<BProjectBinary> {

	private static final BRootBinary instance = new BRootBinary();

	private BRootBinary() {
		super(null);
	}

	@Override
	protected BProjectBinary[] doRefresh() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		List<BProjectBinary> list = new ArrayList<BProjectBinary>();
		for (IProject project : projects) {
			if (project.exists() && project.isOpen()) {
				try {
					String[] natures = project.getDescription().getNatureIds();
					if (natures.length == 1 && natures[0].equals(BNature.NATURE_ID)) {
						BDescriberBinary binary = BDescriberBinaryManager.getInstance().get(project.getName());
						if (binary != null) {
							list.add(new BProjectBinary(this, project, binary));
						}
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return list.toArray(new BProjectBinary[0]);
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getFullname() {
		return "";
	}

	public static BRootBinary getInstance() {
		return instance;
	}

}

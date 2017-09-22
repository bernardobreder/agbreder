package com.agbreder.plugin.wizard.filter;

import org.eclipse.jface.viewers.Viewer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;
import com.agbreder.plugin.element.Workspace;
import com.agbreder.plugin.element.breder.BProject;
import com.agbreder.plugin.element.breder.BSource;

public class BSourceViewerFilter extends BViewerFilter {

	@Override
	public boolean select(Viewer viewer, Parent<?> parent, Element element) {
		return element.getClass() == Workspace.class || element.getClass() == BProject.class || element.getClass() == BSource.class;
	}

}

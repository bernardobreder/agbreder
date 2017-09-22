package com.agbreder.plugin.wizard.filter;

import org.eclipse.jface.viewers.Viewer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;
import com.agbreder.plugin.element.breder.BProject;

public class BProjectViewerFilter extends BViewerFilter {

	@Override
	public boolean select(Viewer viewer, Parent<?> parent, Element element) {
		return element instanceof BProject;
	}

}

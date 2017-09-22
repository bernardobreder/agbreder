package com.agbreder.plugin.wizard.filter;

import org.eclipse.jface.viewers.Viewer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;
import com.agbreder.plugin.element.breder.BClass;
import com.agbreder.plugin.element.breder.BProject;
import com.agbreder.plugin.element.breder.BSource;

public class BClassViewerFilter extends BViewerFilter {

	@Override
	public boolean select(Viewer viewer, Parent<?> parent, Element element) {
		return element instanceof BClass || element instanceof BProject || element instanceof BSource;
	}

}

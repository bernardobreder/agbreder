package com.agbreder.plugin.wizard.filter;

import org.eclipse.jface.viewers.Viewer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;

public abstract class BViewerFilter extends org.eclipse.jface.viewers.ViewerFilter {

	public abstract boolean select(Viewer viewer, Parent<?> parent, Element element);

	@Override
	public final boolean select(Viewer viewer, Object parentElement, Object element) {
		return this.select(viewer, (Parent<?>) parentElement, (Element) element);
	}

}

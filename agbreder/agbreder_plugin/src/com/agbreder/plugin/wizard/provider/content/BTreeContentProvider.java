package com.agbreder.plugin.wizard.provider.content;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;

public abstract class BTreeContentProvider implements ITreeContentProvider {

	public abstract Element[] getChildren(Element parent);

	public abstract Parent<?> getParent(Element element);

	public abstract boolean hasChildren(Element element);

	@Override
	public final Object[] getChildren(Object parentElement) {
		return this.getChildren((Element) parentElement);
	}

	@Override
	public final Object getParent(Object element) {
		return this.getParent((Element) element);
	}

	@Override
	public final boolean hasChildren(Object element) {
		return this.hasChildren((Element) element);
	}

	@Override
	public final Object[] getElements(Object inputElement) {
		return this.getChildren((Element) inputElement);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}

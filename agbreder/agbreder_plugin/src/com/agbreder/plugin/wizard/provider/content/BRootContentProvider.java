package com.agbreder.plugin.wizard.provider.content;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;

public class BRootContentProvider extends BTreeContentProvider {

	@Override
	public Element[] getChildren(Element element) {
		return element.getChildren();
	}

	@Override
	public Parent<?> getParent(Element element) {
		return element.getParent();
	}

	@Override
	public boolean hasChildren(Element element) {
		return element.hasChildren();
	}

}

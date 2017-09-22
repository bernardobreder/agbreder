package com.agbreder.plugin.element;

import org.eclipse.core.resources.IContainer;

public class Binary extends BFolder implements Element, IProjectElement {

	public Binary(Parent<?> parent, IContainer file) {
		super(parent, file);
	}

}

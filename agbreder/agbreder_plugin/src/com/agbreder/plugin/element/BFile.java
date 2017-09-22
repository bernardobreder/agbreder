package com.agbreder.plugin.element;

import org.eclipse.core.resources.IFile;

public class BFile extends BResource implements AllElement, IBFile {

	public BFile(Parent<?> parent, IFile file) {
		super(parent, file);
	}

	@Override
	public IFile getResource() {
		return (IFile) super.getResource();
	}

}

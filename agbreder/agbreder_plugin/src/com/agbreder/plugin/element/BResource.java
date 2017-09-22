package com.agbreder.plugin.element;

import org.eclipse.core.resources.IResource;

public class BResource extends Parent implements IBResource {

	private final IResource file;

	public BResource(Parent<?> parent, IResource file) {
		super(parent);
		this.file = file;
	}

	@Override
	protected Element[] doRefresh() {
		return new BResource[0];
	}

	@Override
	public String getName() {
		return file.getName();
	}

	public IResource getResource() {
		return file;
	}

}

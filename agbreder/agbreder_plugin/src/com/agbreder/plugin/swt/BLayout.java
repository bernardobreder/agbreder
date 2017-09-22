package com.agbreder.plugin.swt;

public abstract class BLayout {

	private BContainer container;

	public BContainer getContainer() {
		return container;
	}

	public void setContainer(BContainer container) {
		this.container = container;
	}

	public abstract void build();

}

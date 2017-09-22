package com.agbreder.plugin.element;

public abstract class Parent<E extends Element> implements Element {

	private final Parent<?> parent;

	private E[] elements;

	public Parent(Parent<?> parent) {
		super();
		this.parent = parent;
	}

	@Override
	public E[] getChildren() {
		if (elements == null) {
			this.refresh(0);
		}
		return elements;
	}

	public E findElement(String name) {
		for (E element : this.getChildren()) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}

	@Override
	public Parent<?> getParent() {
		return parent;
	}

	@Override
	public boolean hasChildren() {
		if (elements == null) {
			this.refresh(0);
		}
		return elements.length != 0;
	}

	@Override
	public void refresh(int depth) {
		this.elements = this.doRefresh();
		if (depth-- != 0) {
			for (E element : elements) {
				element.refresh(depth);
			}
		}
	}

	@Override
	public String getFullname() {
		return this.getParent().getName() + "/" + this.getName();
	}

	protected abstract E[] doRefresh();

}

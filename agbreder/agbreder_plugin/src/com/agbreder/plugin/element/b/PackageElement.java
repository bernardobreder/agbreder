package com.agbreder.plugin.element.b;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.Parent;

public class PackageElement extends Parent {

	private final String name;

	public PackageElement(Parent parent, String name) {
		super(parent);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	protected Element[] doRefresh() {
		return null;
	}

}

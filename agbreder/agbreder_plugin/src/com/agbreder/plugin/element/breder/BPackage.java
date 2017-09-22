package com.agbreder.plugin.element.breder;

import org.eclipse.core.resources.IContainer;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.IBPackage;
import com.agbreder.plugin.element.IProjectElement;
import com.agbreder.plugin.element.ISourceElement;
import com.agbreder.plugin.element.Parent;

public class BPackage extends BSource implements Element, IProjectElement, ISourceElement, IBPackage {

	public BPackage(Parent<?> parent, IContainer file) {
		super(parent, file);
	}

}

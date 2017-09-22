package com.agbreder.plugin.element.breder;

import org.eclipse.core.resources.IContainer;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.element.BFile;
import com.agbreder.plugin.element.BFolder;
import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.IBSource;
import com.agbreder.plugin.element.IProjectElement;
import com.agbreder.plugin.element.Parent;

public class BSource extends BFolder implements Element, IProjectElement, IBSource {

	public BSource(Parent<?> parent, IContainer file) {
		super(parent, file);
	}

	@Override
	protected Element[] doRefresh() {
		Element[] elements = super.doRefresh();
		for (int n = 0; n < elements.length; n++) {
			Element element = elements[n];
			if (element instanceof BFolder) {
				BFolder bdirectory = (BFolder) element;
				elements[n] = new BPackage(element.getParent(), bdirectory.getResource());
			} else if (element instanceof BFile) {
				BFile bfile = (BFile) element;
				if (bfile.getName().endsWith(BrederProjectConstant.BREDER_EXTENSION)) {
					elements[n] = new BClass(element.getParent(), bfile.getResource());
				}
			}
		}
		return elements;
	}

}
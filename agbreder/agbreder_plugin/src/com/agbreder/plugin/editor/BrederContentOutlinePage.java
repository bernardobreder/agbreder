package com.agbreder.plugin.editor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.breder.BClass;
import com.agbreder.plugin.wizard.provider.content.BRootContentProvider;
import com.agbreder.plugin.wizard.provider.label.BLabelProvider;

public class BrederContentOutlinePage extends ContentOutlinePage {

	private final Element root;

	public BrederContentOutlinePage(BClass root) {
		super();
		this.root = root;
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new BRootContentProvider());
		viewer.setLabelProvider(new BLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(root);
	}

}

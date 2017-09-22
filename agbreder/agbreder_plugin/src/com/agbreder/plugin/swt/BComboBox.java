package com.agbreder.plugin.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BComboBox extends BComponent {

	private Combo component;

	@Override
	public Combo getComposite() {
		return this.component;
	}

	@Override
	public Control build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Control build(Composite parent) {
		this.component = new Combo(parent, SWT.NULL);
		return this.component;
	}

}

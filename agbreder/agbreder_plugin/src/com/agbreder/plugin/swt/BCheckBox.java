package com.agbreder.plugin.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BCheckBox extends BComponent {

	private Button component;

	@Override
	public Button getComposite() {
		return this.component;
	}

	@Override
	public Control build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Control build(Composite parent) {
		this.component = new Button(parent, SWT.CHECK);
		return this.component;
	}

}

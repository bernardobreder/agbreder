package com.agbreder.plugin.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class BLabel extends BComponent {

	private String text;

	private Label component;

	public BLabel(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public Label getComposite() {
		return this.component;
	}

	@Override
	public Control build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Control build(Composite parent) {
		this.component = new Label(parent, SWT.NULL);
		this.component.setText(this.getText());
		return this.component;
	}

}

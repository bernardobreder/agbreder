package com.agbreder.plugin.wizard.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.agbreder.plugin.ui.UIValidated;

public class BText implements ModifyListener {

	private final Text component;

	private final UIValidated validater;

	public BText(UIValidated validater, Composite composite) {
		this(validater, composite, "");
	}

	public BText(UIValidated validater, Composite composite, String defaultText) {
		super();
		this.validater = validater;
		this.component = new Text(composite, SWT.BORDER | SWT.SINGLE);
		this.component.setText(defaultText);
		this.component.addModifyListener(this);
	}

	public void modifyText(ModifyEvent e) {
		validater.validate();
	}

	public Text getComponent() {
		return component;
	}

}

package com.agbreder.plugin.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

public class BText extends BComponent {

	private Text composite;

	public String getText() {
		return this.getComposite().getText();
	}

	@Override
	public Text getComposite() {
		return composite;
	}

	@Override
	public Control build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Control build(Composite parent) {
		this.composite = new Text(parent, SWT.BORDER | SWT.SINGLE);
		return this.composite;
	}

}

package com.agbreder.plugin.swt;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class BComponent {

	public abstract Control build();

	protected abstract Control build(Composite parent);

	public abstract Control getComposite();

}

package com.agbreder.plugin.swt;

import org.eclipse.swt.widgets.Composite;

public class BSWT {

	private static ThreadLocal<Composite> roots = new ThreadLocal<Composite>();

	public static void setRoot(Composite composite) {
		roots.set(composite);
	}

	public static Composite getRoot() {
		return roots.get();
	}

}

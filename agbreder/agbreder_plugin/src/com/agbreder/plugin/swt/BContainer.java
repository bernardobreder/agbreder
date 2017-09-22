package com.agbreder.plugin.swt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BContainer extends BComponent {

	private final BLayout layout;

	private final List<BContainerItem> children = new ArrayList<BContainerItem>();

	private Composite composite;

	public BContainer(BLayout layout) {
		super();
		this.layout = layout;
		this.layout.setContainer(this);
	}

	public List<BContainerItem> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public void addChild(BComponent component, Object data) {
		this.children.add(new BContainerItem(component, data));
	}

	@Override
	public Composite getComposite() {
		return this.composite;
	}

	@Override
	public Control build() {
		this.composite = new Composite(BSWT.getRoot(), SWT.NONE);
		BSWT.setRoot(null);
		this.layout.build();
		return composite;
	}

	@Override
	protected Control build(Composite parent) {
		throw new RuntimeException("not implemented yet");
	}

	public class BContainerItem {

		private final BComponent component;

		private final Object data;

		public BContainerItem(BComponent component, Object data) {
			super();
			this.component = component;
			this.data = data;
		}

		public BComponent getComponent() {
			return component;
		}

		public Object getData() {
			return data;
		}

	}

}

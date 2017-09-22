package com.agbreder.plugin.wizard.build.bclass;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.breder.BSource;
import com.agbreder.plugin.wizard.BWizardPage;
import com.agbreder.plugin.wizard.BWizardUtil;
import com.agbreder.plugin.wizard.build.BuilderHelper;
import com.agbreder.plugin.wizard.component.BText;
import com.agbreder.plugin.wizard.filter.BSourceViewerFilter;
import com.agbreder.plugin.wizard.validator.BSourceViewerValidator;

public class BClassNewPageOne extends BWizardPage {

	private BText sourceText;

	private BText classText;

	public BClassNewPageOne() {
		super();
		this.setValidator(new BClassNewPageOneValidator(this));
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(3, false);
		composite.setLayout(gridLayout);
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("Source : ");
			this.initSource(composite);
			{
				GridData gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				this.sourceText.getComponent().setLayoutData(gridData);
			}
			this.initSourceButton(composite);
		}
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("Class : ");
			this.initClass(composite);
			{
				GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
				gridData.horizontalSpan = 2;
				this.classText.getComponent().setLayoutData(gridData);
			}
		}
		this.setControl(composite);
		this.requestFocus();
		this.validate();
	}

	private void requestFocus() {
		if (this.sourceText.getComponent().getText().length() == 0) {
			this.sourceText.getComponent().setFocus();
		} else {
			this.classText.getComponent().setFocus();
		}
	}

	private void initSource(Composite composite) {
		this.sourceText = new BText(this, composite, BuilderHelper.getDefaultSource());
		this.sourceText.getComponent().setEditable(false);
	}

	private void initClass(Composite composite) {
		this.classText = new BText(this, composite);
	}

	private void initSourceButton(Composite composite) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText("...");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Element element = BWizardUtil.chooseContainer(new BSourceViewerFilter(), new BSourceViewerValidator());
				if (element != null && element instanceof BSource) {
					sourceText.getComponent().setText(element.getFullname());
					validate();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	public String getClassname() {
		return classText.getComponent().getText();
	}

	public String getSourcename() {
		return sourceText.getComponent().getText();
	}

}

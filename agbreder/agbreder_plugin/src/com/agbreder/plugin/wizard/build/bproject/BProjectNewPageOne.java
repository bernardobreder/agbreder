package com.agbreder.plugin.wizard.build.bproject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.agbreder.plugin.wizard.BWizardPage;
import com.agbreder.plugin.wizard.build.bproject.template.AbstractTemplate;
import com.agbreder.plugin.wizard.build.bproject.template.TemplateProject;
import com.agbreder.plugin.wizard.component.BText;

public class BProjectNewPageOne extends BWizardPage {

	private BText projectText;

	private Combo exampleText;

	public BProjectNewPageOne() {
		super();
		this.setValidator(new BProjectNewPageOneValidator(this));
	}

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(2, false);
		composite.setLayout(gridLayout);
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("Project : ");
			this.initSource(composite);
			{
				GridData gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				this.projectText.getComponent().setLayoutData(gridData);
			}
		}
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("Example : ");
			this.initExample(composite);
			{
				GridData gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				this.exampleText.setLayoutData(gridData);
			}
		}
		this.setControl(composite);
		this.projectText.getComponent().setFocus();
		this.validate();
	}

	private void initExample(Composite composite) {
		this.exampleText = new Combo(composite, SWT.NULL);
		for (AbstractTemplate template : TemplateProject.getInstance()) {
			this.exampleText.add(template.getTemplateName());
		}
		if (this.exampleText.getItemCount() > 0) {
			this.exampleText.select(0);
		}
	}

	private void initSource(Composite composite) {
		this.projectText = new BText(this, composite);
	}

	public String getProjectname() {
		return projectText.getComponent().getText();
	}

	public int getTemplateIndex() {
		return this.exampleText.getSelectionIndex();
	}

}

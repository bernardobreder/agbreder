package com.agbreder.plugin.launch.tab;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.element.Element;
import com.agbreder.plugin.element.breder.BClass;
import com.agbreder.plugin.element.breder.BProject;
import com.agbreder.plugin.ui.UIValidated;
import com.agbreder.plugin.util.FileUtil;
import com.agbreder.plugin.wizard.BWizardUtil;
import com.agbreder.plugin.wizard.component.BText;
import com.agbreder.plugin.wizard.filter.BClassViewerFilter;
import com.agbreder.plugin.wizard.filter.BProjectViewerFilter;
import com.agbreder.plugin.wizard.validator.BClassViewerValidator;
import com.agbreder.plugin.wizard.validator.BProjectViewerValidator;

public class MainLaunchConfigurationTab extends AbstractLaunchConfigurationTab implements ILaunchConfigurationTab, UIValidated {

	private BText projectText;

	private BText classText;

	@Override
	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(3, false);
		composite.setLayout(gridLayout);
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("BProject");
			this.initSource(composite);
			{
				GridData gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				this.projectText.getComponent().setLayoutData(gridData);
			}
			this.initProjectButton(composite);
		}
		{
			Label label = new Label(composite, SWT.NULL);
			label.setText("Class-Main");
			this.initPackage(composite);
			{
				GridData gridData = new GridData();
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalAlignment = GridData.FILL;
				this.classText.getComponent().setLayoutData(gridData);
			}
			this.initClassMainButton(composite);
		}
		this.setControl(composite);
	}

	private void initSource(Composite composite) {
		this.projectText = new BText(this, composite);
		this.projectText.getComponent().setEditable(false);
	}

	private void initProjectButton(Composite composite) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText("...");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Element element = BWizardUtil.chooseContainer(new BProjectViewerFilter(), new BProjectViewerValidator());
				if (element != null && element instanceof BProject) {
					BProject project = (BProject) element;
					projectText.getComponent().setText(project.getName());
					validate();
					setDirty(true);
					updateLaunchConfigurationDialog();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void initClassMainButton(Composite composite) {
		Button button = new Button(composite, SWT.PUSH);
		button.setText("...");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Element element = BWizardUtil.chooseContainer(new BClassViewerFilter(), new BClassViewerValidator());
				if (element != null && element instanceof BClass) {
					BClass c = (BClass) element;
					String classname = FileUtil.getClassname(c.getResource().getProject(), c.getResource());
					classText.getComponent().setText(classname);
					validate();
					setDirty(true);
					updateLaunchConfigurationDialog();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void initPackage(Composite composite) {
		this.classText = new BText(this, composite);
	}

	public String getSourcename() {
		return classText.getComponent().getText();
	}

	public String getPackagename() {
		return classText.getComponent().getText();
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		if (this.projectText != null) {
			try {
				this.projectText.getComponent().setText(configuration.getAttribute(BrederProjectConstant.PROJECT_NAME, ""));
				this.classText.getComponent().setText(configuration.getAttribute(BrederProjectConstant.MAIN_CLASS_NAME, ""));
			} catch (CoreException e) {
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param configuration
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BrederProjectConstant.PROJECT_NAME, this.projectText.getComponent().getText());
		configuration.setAttribute(BrederProjectConstant.MAIN_CLASS_NAME, this.classText.getComponent().getText());
	}

	public String getName() {
		return "Main";
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		this.initializeFrom(configuration);
	}

	@Override
	public void validate() {

	}

}

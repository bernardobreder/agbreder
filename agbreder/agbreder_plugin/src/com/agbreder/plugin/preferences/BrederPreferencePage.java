package com.agbreder.plugin.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.agbreder.plugin.BActivator;
import com.agbreder.plugin.builder.BNature;
import com.agbreder.plugin.monitor.BProgressMonitor;

public class BrederPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public BrederPreferencePage() {
		super(GRID);
		setPreferenceStore(BActivator.getDefault().getPreferenceStore());
		setDescription("Preference of Breder Language Plugin");
	}

	public void createFieldEditors() {
		// addField(new DirectoryFieldEditor(PreferenceConstants.BREDER_HOME,
		// "BREDER_HOME :", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.IS_DEBUG, "&All Project is Release ?", getFieldEditorParent()));
		addField(new RadioGroupFieldEditor(PreferenceConstants.ARCH, "Which arch do you want ?", 1, new String[][] { { "Arch Default", "" }, { "Arch 32", "-m32" }, { "Arch 64", "-m64" } }, getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	public boolean performOk() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			try {
				if (project.isNatureEnabled(BNature.NATURE_ID)) {
					project.build(IncrementalProjectBuilder.CLEAN_BUILD, BProgressMonitor.DEFAULT);
				}
			} catch (CoreException e) {
			}
		}
		return super.performOk();
	}

}
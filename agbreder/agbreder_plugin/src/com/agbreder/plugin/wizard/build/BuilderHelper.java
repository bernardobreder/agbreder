package com.agbreder.plugin.wizard.build;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.internal.ui.stringsubstitution.SelectedResourceManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.agbreder.plugin.BrederProjectConstant;

public class BuilderHelper {

	public static String getDefaultSource() {
		IResource file = BuilderHelper.getFile();
		if (file != null) {
			IProject project = file.getProject();
			return project.getFolder(BrederProjectConstant.SOURCE_FOLDER).toString().substring(2);
		}
		return "";
	}

	public static String getDefaultPackage() {
		IResource file = BuilderHelper.getFile();
		if (file != null) {
			IProject project = file.getProject();
			IFolder sourceFolder = project.getFolder(BrederProjectConstant.SOURCE_FOLDER);
			String classname = "";
			if (file.toString().length() > sourceFolder.toString().length()) {
				classname = file.toString().substring(sourceFolder.toString().length() + 1);
			}
			if (file.toString().endsWith(BrederProjectConstant.BREDER_EXTENSION)) {
				int lastIndex = classname.toString().lastIndexOf(File.separatorChar);
				if (lastIndex >= 0) {
					classname = classname.substring(0, lastIndex);
				} else {
					classname = "";
				}
			}
			return classname.replace(File.separatorChar, '.');
		}
		return "";
	}

	private static IResource getFile() {
		IResource selectedResource = SelectedResourceManager.getDefault().getSelectedResource();
		if (selectedResource != null) {
			return selectedResource;
		}
		IStructuredSelection currentSelection = SelectedResourceManager.getDefault().getCurrentSelection();
		if (currentSelection.getFirstElement() instanceof IFile) {
			return (IFile) currentSelection.getFirstElement();
		}
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page.getActiveEditor() != null) {
			IEditorInput editorInput = page.getActiveEditor().getEditorInput();
			if (editorInput instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
				return fileEditorInput.getFile();
			}
		}
		return null;
	}

}

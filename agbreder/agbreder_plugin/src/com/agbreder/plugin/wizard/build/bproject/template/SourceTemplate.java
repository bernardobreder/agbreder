package com.agbreder.plugin.wizard.build.bproject.template;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.agbreder.plugin.BActivator;
import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.monitor.BProgressMonitor;

public abstract class SourceTemplate extends AbstractTemplate {

	public abstract String getTemplateFolderName();

	public abstract String[] getTemplateSources();

	@Override
	public void build(IProject project) throws CoreException {
		IFolder sourceFolder = project.getFolder(BrederProjectConstant.SOURCE_FOLDER);
		for (String source : this.getTemplateSources()) {
			Map<String, String> map = new HashMap<String, String>();
			int index = source.lastIndexOf("/");
			String packagename;
			String classname;
			if (index < 0) {
				InputStream input = BActivator.getTemplate("/templates/" + this.getTemplateFolderName() + "/" + source);
				project.getFile(source).create(input, true, BProgressMonitor.DEFAULT);
			} else {
				packagename = source.substring(0, index);
				classname = source.substring(index + 1);
				IFolder packageFolder = sourceFolder;
				if (packagename.length() != 0) {
					String[] itens = packagename.split("[/]");
					for (int n = 1; n < itens.length; n++) {
						String item = itens[n];
						packageFolder = packageFolder.getFolder(item);
						if (!packageFolder.exists()) {
							packageFolder.create(true, true, BProgressMonitor.DEFAULT);
						}
					}
				}
				IFile mainClassFile = packageFolder.getFile(classname);
				InputStream input = BActivator.getTemplate("/templates/" + this.getTemplateFolderName() + "/" + source, map);
				mainClassFile.create(input, true, BProgressMonitor.DEFAULT);
			}
		}
	}
}

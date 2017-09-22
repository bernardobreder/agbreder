package com.agbreder.plugin.element.breder;

import java.io.File;

import org.eclipse.core.resources.IFile;

import com.agbreder.plugin.element.BFile;
import com.agbreder.plugin.element.IBClass;
import com.agbreder.plugin.element.Parent;

public class BClass extends BFile implements IBClass {

	public BClass(Parent<?> parent, IFile file) {
		super(parent, file);
	}

	public static String getClassname(IFile file) {
		String filename = file.getLocation().toOSString();
		filename = filename.substring(file.getProject().getLocation().toOSString().length() + 1);
		filename = filename.substring(filename.indexOf(File.separatorChar) + 1);
		filename = filename.replace(File.separatorChar, '.');
		filename = filename.substring(0, filename.lastIndexOf('.'));
		return filename;
	}

}

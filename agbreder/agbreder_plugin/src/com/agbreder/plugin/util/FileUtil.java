package com.agbreder.plugin.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.agbreder.plugin.BrederProjectConstant;

public class FileUtil {

	public static String getClassname(IProject project, IFile file) {
		IResource binaryResource = project.findMember(BrederProjectConstant.SOURCE_FOLDER);
		String classname = file.getLocation().toOSString().substring(binaryResource.getLocation().toOSString().length() + 1).replace('/', '.');
		return classname.substring(0, classname.length() - BrederProjectConstant.BREDER_EXTENSION.length());
	}

	public static IFile getFile(IProject project, String classname) {
		IResource sourceResource = project.findMember(BrederProjectConstant.SOURCE_FOLDER);
		String pathname = classname.replace('.', '/');
		IFolder auxFolder = (IFolder) sourceResource;
		int index = pathname.indexOf("/");
		while (index != -1) {
			String aux = pathname.substring(0, index);
			auxFolder = auxFolder.getFolder(aux);
			if (!auxFolder.exists()) {
				return null;
			}
			pathname = pathname.substring(index + 1);
			index = pathname.indexOf("/");
		}
		return auxFolder.getFile(pathname + BrederProjectConstant.BREDER_EXTENSION);
	}

	public static File[] list(File dir, String ext) {
		List<File> list = new ArrayList<File>();
		list(dir, ext, list);
		return list.toArray(new File[list.size()]);
	}

	private static void list(File dir, String ext, List<File> list) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					list(file, ext, list);
				} else if (file.isFile()) {
					if (file.getName().endsWith("." + ext)) {
						list.add(file);
					}
				}
			}
		}
	}

	public static File build(String... strings) {
		if (strings.length == 0) {
			return new File("");
		}
		File file = new File(strings[0]);
		for (int n = 1; n < strings.length; n++) {
			file = new File(file, strings[n]);
		}
		return file;
	}

	public static File build(File dir, String... strings) {
		if (strings.length == 0) {
			return dir;
		}
		File file = dir;
		for (int n = 0; n < strings.length; n++) {
			file = new File(file, strings[n]);
		}
		return file;
	}

	public static File buildTmp() {
		Object o = new Object();
		File file = new File("tmp-");
		file = new File("tmp-" + o.hashCode());
		return file;
	}

}

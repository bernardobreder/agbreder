package com.agbreder.plugin.element;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.agbreder.plugin.BrederProjectConstant;
import com.agbreder.plugin.element.breder.BClass;
import com.agbreder.plugin.monitor.BProgressMonitor;

public class BFolder extends BResource implements AllElement, IBFolder {

	public BFolder(Parent<?> parent, IContainer file) {
		super(parent, file);
	}

	@Override
	protected Element[] doRefresh() {
		List<Element> list = new ArrayList<Element>();
		try {
			for (IResource resource : this.getResource().members()) {
				if (resource instanceof IFolder) {
					IFolder folder = (IFolder) resource;
					list.add(new BFolder(this, folder));
				} else if (resource instanceof IFile) {
					IFile file = (IFile) resource;
					list.add(new BFile(this, file));
				} else {
					list.add(new BResource(this, resource));
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return list.toArray(new Element[0]);
	}

	public BFolder mkdir(String name) throws IOException {
		IContainer folder = this.getResource();
		IFolder child = folder.getFolder(new Path(name));
		try {
			child.create(true, true, BProgressMonitor.DEFAULT);
		} catch (CoreException e) {
			throw new IOException(e);
		}
		this.refresh(0);
		return new BFolder(this, child);
	}

	public boolean exist(String name) {
		IContainer folder = this.getResource();
		return folder.exists(new Path(name));
	}

	public BFile mkfile(InputStream input, String name) throws IOException {
		IContainer folder = this.getResource();
		IFile child = folder.getFile(new Path(name));
		try {
			child.create(input, true, BProgressMonitor.DEFAULT);
		} catch (CoreException e) {
			throw new IOException(e);
		}
		this.refresh(0);
		if (name.endsWith(BrederProjectConstant.BREDER_EXTENSION))
			return new BClass(this, child);
		else
			return new BFile(this, child);
	}

	@Override
	public IContainer getResource() {
		return (IContainer) super.getResource();
	}

}

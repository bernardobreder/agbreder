package com.agbreder.ide.model.io.local;

import java.io.File;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileSystem;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;

/**
 * Implementação de um recurso
 * 
 * @author Bernardo Breder
 */
public abstract class BResource implements IResource {
	
	/** Arquivo */
	protected java.io.File file;
	
	/**
	 * Construtor
	 * 
	 * @param file
	 */
	BResource(java.io.File file) {
		this.file = file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exist() {
		if (this.file == null) {
			return true;
		} else {
			return this.file.exists();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		if (this.file == null) {
			return "";
		} else {
			return this.file.getName();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFile() {
		return this instanceof IFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFolder() {
		if (this.file == null) {
			return true;
		} else {
			return this instanceof IFolder;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProject() {
		return this.getParent() == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSource() {
		IFolder parent = this.getParent();
		if (parent == null) {
			return false;
		}
		return parent.isProject();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPackage() {
		if (this.isFile()) {
			return false;
		}
		if (this.isProject()) {
			return false;
		}
		if (this.isSource()) {
			return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHidden() {
		if (this.file == null) {
			return false;
		} else {
			return this.file.isHidden();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAbsoluteName() {
		if (this.file == null) {
			return "";
		} else {
			return this.file.getAbsolutePath().replace('\\', '/');
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath() {
		return this.getAbsoluteName().substring(
			IFileSystem.DEFAULT.getAbsoluteName().length() + 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getParent() {
		if (this.file == null) {
			return null;
		}
		if (this.file.getParentFile().equals(BFileSystem.DIR)) {
			return null;
		}
		File parent = this.file.getParentFile();
		return BFolder.newInstance(parent);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getProject() {
		if (this.isProject()) {
			return this.toFolder();
		}
		return this.getParent().getProject();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.getPath().hashCode();
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BResource other = (BResource) obj;
		if (file == null) {
			if (other.file != null) {
				return false;
			}
		} else if (!this.getPath().equals(other.getPath())) {
			return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder toFolder() {
		return (IFolder) this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFile toFile() {
		return (IFile) this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getPath();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(IResource o) {
		return this.getName().compareTo(o.getName());
	}
	
}

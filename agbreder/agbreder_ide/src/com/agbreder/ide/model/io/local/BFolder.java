package com.agbreder.ide.model.io.local;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;

/**
 * Implementação de diretório
 * 
 * @author Bernardo Breder
 */
public class BFolder extends BResource implements IFolder {
	
	/**
	 * Construtor
	 * 
	 * @param dir
	 */
	protected BFolder(java.io.File dir) {
		super(dir);
	}
	
	/**
	 * Constroi uma instancia
	 * 
	 * @param dir
	 * @return instancia
	 */
	public static IFolder newInstance(File dir) {
		IFolder folder = new BFolder(dir);
		return folder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResource[] list() throws IOException {
		java.io.File[] files;
		if (this.file == null) {
			files = File.listRoots();
		} else {
			files = this.file.listFiles();
		}
		if (files == null) {
			return new IResource[0];
		}
		IResource[] result = new IResource[files.length];
		for (int n = 0; n < files.length; n++) {
			java.io.File file = files[n];
			if (file.isDirectory()) {
				result[n] = BFolder.newInstance(file);
			} else {
				result[n] = BFile.newInstance(file);
			}
		}
		Arrays.sort(result, new Comparator<IResource>() {
			@Override
			public int compare(IResource o1, IResource o2) {
				char c1 = o1.getName().charAt(0);
				char c2 = o2.getName().charAt(0);
				if (Character.isLetter(c1)) {
					if (Character.isLetter(c2)) {
						return o1.getName().toLowerCase()
							.compareTo(o2.getName().toLowerCase());
					} else {
						return -1;
					}
				} else {
					if (Character.isLetter(c2)) {
						return 1;
					} else {
						return o1.getName().toLowerCase()
							.compareTo(o2.getName().toLowerCase());
					}
				}
			}
		});
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		String name = this.file.getName();
		if (name.length() == 0) {
			return this.file.getAbsolutePath();
		} else {
			return name;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFile createFile(String name) throws IOException {
		File file = new File(this.file, name);
		int dotIndex = name.lastIndexOf('.');
		int index = 1;
		while (file.exists()) {
			file =
				new File(this.file, name.substring(0, dotIndex) + "(" + index++ + ")."
					+ name.substring(dotIndex + 1));
		}
		file.createNewFile();
		IFile newFile = BFile.newInstance(file);
		IFileEvent.DEFAULT.fireCreatedFile(newFile);
		return newFile;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder createFolder(String name) throws IOException {
		File file = new File(this.file, name);
		int index = 1;
		while (file.exists()) {
			file = new File(this.file, name + "(" + index++ + ")");
		}
		file.mkdirs();
		IFolder folder = BFolder.newInstance(file);
		IFileEvent.DEFAULT.fireCreatedFolder(folder);
		return folder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getFolder(String path) {
		File file = new File(path);
		if (file.isAbsolute()) {
			return BFolder.newInstance(file);
		} else {
			return BFolder.newInstance(new File(this.file, path));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFile getFile(String nane) {
		File file = new File(nane);
		if (file.isAbsolute()) {
			return BFile.newInstance(file);
		} else {
			return BFile.newInstance(new File(this.file, nane));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() throws IOException {
		for (IResource resource : this.list()) {
			resource.delete();
		}
		this.file.delete();
		IFileEvent.DEFAULT.fireDeletedFolder(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLength() {
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder rename(String name) throws IOException {
		File file = new File(this.file.getParent(), name);
		this.file.renameTo(file);
		IFolder result = BFolder.newInstance(file);
		IFileEvent.DEFAULT.fireChangedFolder(this);
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder findFolder(String name) {
		return this.findFolder(name, -1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder findFolder(String name, int deep) {
		return this.findFolder(this.file, name, deep);
	}
	
	/**
	 * Busca por diretório
	 * 
	 * @param folder
	 * @param name
	 * @param deep
	 * @return diretório
	 */
	private IFolder findFolder(File folder, String name, int deep) {
		if (deep < 0) {
			return null;
		}
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory() && !file.isHidden()) {
					if (this.findPattern(name, file.getName())) {
						return BFolder.newInstance(file);
					} else {
						IFolder aux = this.findFolder(file, name, deep - 1);
						if (aux != null) {
							return aux;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Busca por padrão
	 * 
	 * @param format
	 * @param name
	 * @return achou
	 */
	private boolean findPattern(String format, String name) {
		format = format.toLowerCase();
		name = name.toLowerCase();
		int index = format.indexOf('*');
		if (index == -1) {
			return format.equals(name);
		} else {
			String pre = format.substring(0, index);
			if (!name.startsWith(pre)) {
				return false;
			} else {
				String fpos = format.substring(index + 1);
				String ffpos = fpos;
				String npos = name.substring(index);
				if (format.charAt(format.length() - 1) == '*') {
					return true;
				}
				{
					int index2 = fpos.indexOf('*');
					if (index2 >= 0) {
						fpos = fpos.substring(0, index2);
					}
				}
				int index2 = npos.indexOf(fpos);
				if (index2 >= 0) {
					String oname = npos.substring(index2);
					return this.findPattern(ffpos, oname);
				} else {
					return false;
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResource[] find(String pattern) {
		return this.find(pattern, Integer.MAX_VALUE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResource[] find(String pattern, int deep) {
		List<IResource> list = new ArrayList<IResource>();
		this.find(this.file, pattern, list, deep);
		return list.toArray(new IResource[list.size()]);
	}
	
	/**
	 * Busca
	 * 
	 * @param folder
	 * @param format
	 * @param list
	 * @param deep
	 */
	private void find(File folder, String format, List<IResource> list, int deep) {
		if (deep < 0) {
			return;
		}
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (!file.isHidden()) {
					String name = file.getName();
					boolean flag = this.findPattern(format, name);
					if (file.isFile()) {
						if (flag) {
							list.add(BFile.newInstance(file));
						}
					} else if (file.isDirectory()) {
						if (flag) {
							list.add(BFolder.newInstance(file));
						}
						this.find(file, format, list, deep - 1);
					}
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void move(IFolder dest) throws IOException {
		this.copy(dest);
		this.delete();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copy(IFolder dest) throws IOException {
		this.copy(this, dest);
		IFileEvent.DEFAULT.fireCreatedFolder(dest);
	}
	
	/**
	 * Copia
	 * 
	 * @param folder
	 * @param dest
	 * @throws IOException
	 */
	private void copy(IFolder folder, IFolder dest) throws IOException {
		IFolder newFolder = dest.createFolder(folder.getName());
		for (IResource resource : folder.list()) {
			resource.copy(newFolder);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exist(String name) {
		return new File(this.file, name).exists();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh() {
		IFileEvent.DEFAULT.fireRefresh(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder mkdirs() {
		this.file.mkdirs();
		IFileEvent.DEFAULT.fireCreatedFolder(this);
		return this;
	}
	
}

package com.agbreder.ide.gui.destkop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import breder.util.swing.tree.AbstractTreeNode;

import com.agbreder.ide.gui.builder.agb.compiler.AGBCompileTask;
import com.agbreder.ide.gui.console.IDEConsole;
import com.agbreder.ide.gui.editor.AbstractEditor;
import com.agbreder.ide.gui.editor.agb.AGBEditor;
import com.agbreder.ide.gui.explorer.node.FileNode;
import com.agbreder.ide.gui.explorer.node.FolderNode;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;
import com.agbreder.ide.resource.Resource;
import com.agbreder.ide.task.ide.AGBRunTask;
import com.agbreder.ide.task.ide.UpdateIdeTask;

/**
 * Classe de ações de desktop
 * 
 * @author bernardobreder
 */
public class DesktopAction extends DesktopFrame implements IDesktop {
	
	/** Instancia unica */
	private static DesktopAction instance;
	
	/**
	 * Construtor
	 */
	private DesktopAction() {
		super();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMarker(IFile file, int lin, int col, int length, String message) {
		for (int n = 0; n < this.getEditorCount(); n++) {
			FileOpened opened = this.files.get(n);
			if (opened.getFile().equals(file)) {
				// AbstractEditor editor = this.getEditor(n);
				// editor.mark(lin, col, length);
				break;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IResource getSelectedResource() {
		AbstractTreeNode node = getExplorerTree().getSelectNode();
		if (node instanceof FolderNode) {
			return ((FolderNode) node).getFolder();
		} else if (node instanceof FileNode) {
			return ((FileNode) node).getFile();
		} else {
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFile getSelectedFile() {
		IResource resource = this.getSelectedResource();
		if (resource.isFile()) {
			return resource.toFile();
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getSelectedProject() {
		IResource file = this.getSelectedResource();
		if (file == null) {
			return null;
		}
		if (file.isProject()) {
			return file.toFolder();
		}
		IFolder folder = file.getParent();
		while (folder.getParent() != null) {
			folder = folder.getParent();
		}
		return folder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getSelectedSource() {
		IFolder project = this.getSelectedProject();
		if (project == null) {
			return null;
		}
		return project.getFolder("src");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IFolder getSelectedPackage() {
		IResource source = this.getSelectedResource();
		if (source == null) {
			return null;
		}
		if (source.isFile()) {
			return source.getParent();
		}
		IFolder folder = source.toFolder();
		if (folder.isPackage()) {
			return folder;
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void openEditor(IFile file, String content) {
		int size = this.editorTab.getTabCount();
		String path = file.getPath();
		for (int n = 0; n < size; n++) {
			if (this.editorTab.getTitleAt(n).equals(path)) {
				this.editorTab.setSelectedIndex(n);
				this.getEditor(n).requestFocus();
				return;
			}
		}
		AbstractEditor editor = new AGBEditor();
		JScrollPane scroll = new JScrollPane(editor);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		ImageIcon icon = new ImageIcon(Resource.getInstance().getFile());
		this.editorTab.addTab(path, icon, scroll);
		this.editorTab.setSelectedIndex(this.editorTab.getTabCount() - 1);
		this.files.add(new FileOpened(file, content));
		editor.setText(content);
		editor.requestFocus();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveAllEditors() {
		List<IFile> files = new ArrayList<IFile>();
		for (int n = 0; n < getEditorCount(); n++) {
			FileOpened opened = this.getFileOpened(n);
			AbstractEditor editor = this.getEditor(n);
			String content = editor.getText();
			if (!opened.getContent().equals(content)) {
				files.add(opened.getFile());
				opened.getFile().setOutputString(content);
				opened.setContent(content);
			}
		}
		if (files.size() > 0) {
			Set<IFolder> projects = new HashSet<IFolder>(files.size());
			for (IFile file : files) {
				projects.add(file.getProject());
			}
			for (IFolder project : projects) {
				new AGBCompileTask(project).start();
			}
		}
		return files.size() > 0;
	}
	
	private int getEditorCount() {
		return this.files.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeAllEditors() {
		this.saveAllEditors();
		while (this.editorTab.getTabCount() > 0) {
			this.editorTab.removeTabAt(0);
			this.files.remove(0);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveEditor() {
		int index = this.editorTab.getSelectedIndex();
		if (index >= 0) {
			FileOpened opened = this.getFileOpened(index);
			AbstractEditor editor = this.getEditor(index);
			String content = editor.getText();
			if (!opened.getContent().equals(content)) {
				opened.getFile().setOutputString(content);
				opened.setContent(content);
				new AGBCompileTask(opened.getFile().getProject()).start();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna o editor
	 * 
	 * @param index
	 * @return editor
	 */
	public AbstractEditor getEditor(int index) {
		JScrollPane scroll = (JScrollPane) this.editorTab.getComponentAt(index);
		return (AbstractEditor) scroll.getViewport().getView();
	}
	
	/**
	 * Retorna o editor
	 * 
	 * @param index
	 * @return editor
	 */
	public FileOpened getFileOpened(int index) {
		return this.files.get(index);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeEditor() {
		if (this.editorTab.getTabCount() > 0) {
			this.saveEditor();
			int index = this.editorTab.getSelectedIndex();
			this.editorTab.removeTabAt(index);
			this.files.remove(index);
			if (this.editorTab.getTabCount() == 0) {
				this.getExplorerTree().requestFocus();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDEConsole getConsole() {
		return this.console;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() {
		this.setVisible(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void runProject() {
		new AGBRunTask(getSelectedProject()).start();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateIde() {
		new UpdateIdeTask().start();
	}
	
	/**
	 * @return the instance
	 */
	public static DesktopAction getInstance() {
		if (instance == null) {
			instance = new DesktopAction();
			instance.fireEvents();
		}
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DesktopAction getFrame() {
		return DesktopAction.getInstance();
	}
	
}

package com.agbreder.ide.gui.explorer;

import javax.swing.JPopupMenu;

import breder.util.swing.table.IOpenCellListener;
import breder.util.swing.tree.AbstractTreeNode;
import breder.util.swing.tree.BTree;

import com.agbreder.ide.gui.destkop.IDesktop;
import com.agbreder.ide.gui.editor.OpenFileTask;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFileEvent;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.event.FileEvent;

/**
 * Arquivote de Explorer
 * 
 * @author Bernardo Breder
 */
public class ExplorerTree extends BTree {
	
	/**
	 * Construtor
	 */
	public ExplorerTree() {
		super(new ExplorerModel());
		IFileEvent.DEFAULT.addListener(new FileEvent() {
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void refreshFolder(IFolder folder) {
				refresh();
			}
			
			@Override
			public void deletedFolder(IFolder folder) {
				refresh();
			}
			
			@Override
			public void deletedFile(IFile file) {
				refresh();
			}
			
			@Override
			public void createdFolder(IFolder folder) {
				refresh();
			}
			
			@Override
			public void createdFile(IFile file) {
				refresh();
			}
			
			@Override
			public void changedFolder(IFolder folder) {
				refresh();
			}
			
			@Override
			public void changedFile(IFile file) {
				refresh();
			}
		});
		this.add(new IOpenCellListener<AbstractTreeNode>() {
			@Override
			public JPopupMenu getPopupMenu(int row, AbstractTreeNode cell) {
				return null;
			}
			
			@Override
			public void actionPerformed(int row, AbstractTreeNode cell) {
				onOpenAction();
			}
		});
	}
	
	/**
	 * Ação de abrir um arquivo
	 */
	protected void onOpenAction() {
		IFile file = IDesktop.DEFAULT.getSelectedFile();
		if (file != null) {
			new OpenFileTask(file).start();
		}
	}
	
}

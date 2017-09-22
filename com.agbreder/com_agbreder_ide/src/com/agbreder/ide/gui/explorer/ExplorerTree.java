package com.agbreder.ide.gui.explorer;

import javax.swing.JPopupMenu;

import com.agbreder.ide.gui.explorer.node.FileNode;
import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.plugin.IEditorManager;

import breder.util.swing.BExceptonFrame;
import breder.util.swing.table.IOpenCellListener;
import breder.util.swing.tree.AbstractTreeNode;
import breder.util.swing.tree.BTree;

/**
 * Arquivote de Explorer
 * 
 * 
 * @author Bernardo Breder
 */
public class ExplorerTree extends BTree {

  /**
   * Construtor
   */
  public ExplorerTree() {
    super(new ExplorerModel());
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
    AbstractTreeNode selectNode = this.getSelectNode();
    if (selectNode instanceof FileNode) {
      FileNode node = (FileNode) selectNode;
      IFile file = node.getFile();
      try {
        IEditorManager.DEFAULT.open(file);
      }
      catch (Exception e) {
        new BExceptonFrame(e).setVisible(true);
      }
    }
  }

}

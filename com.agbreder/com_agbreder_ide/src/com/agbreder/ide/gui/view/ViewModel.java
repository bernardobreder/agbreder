package com.agbreder.ide.gui.view;

import com.agbreder.ide.model.view.ViewManager;
import com.agbreder.ide.plugin.IView;

import breder.util.swing.tree.AbstractTreeNode;
import breder.util.swing.tree.DirectoryTreeNode;

/**
 * Modelo da visão
 * 
 * 
 * @author Bernardo Breder
 */
public class ViewModel extends DirectoryTreeNode {

  /**
   * Construtor
   */
  public ViewModel() {
    super(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractTreeNode[] getChildren() {
    IView[] gets = ViewManager.getDefault().gets();
    AbstractTreeNode[] result = new AbstractTreeNode[gets.length];
    for (int n = 0; n < gets.length; n++) {
      result[n] = new ViewNode(this, gets[n]);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "";
  }

}

package com.agbreder.ide.gui.explorer.node;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import breder.util.swing.tree.AbstractTreeNode;
import breder.util.util.ImageUtil;

import com.agbreder.ide.model.io.IFolder;

/**
 * Nó binário
 * 
 * 
 * @author Bernardo Breder
 */
public class BinaryNode extends FolderNode {

  /** Icone */
  private static final ImageIcon icon = new ImageIcon(ImageUtil
    .load("/com/agbreder/ide/resource/binary.png"));

  /**
   * Construtor
   * 
   * @param parent
   * @param folder
   */
  public BinaryNode(AbstractTreeNode parent, IFolder folder) {
    super(parent, folder);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return icon;
  }

}

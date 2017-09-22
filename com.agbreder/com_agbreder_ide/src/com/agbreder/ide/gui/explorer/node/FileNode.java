package com.agbreder.ide.gui.explorer.node;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import breder.util.swing.tree.AbstractTreeNode;
import breder.util.swing.tree.FileTreeNode;
import breder.util.util.ImageUtil;

import com.agbreder.ide.model.io.IFile;

/**
 * Nó de arquivo
 * 
 * 
 * @author Bernardo Breder
 */
public class FileNode extends FileTreeNode {

  /** Arquivo */
  private final IFile file;
  /** Icone */
  private static final ImageIcon icon = new ImageIcon(ImageUtil
    .load("/com/agbreder/ide/resource/file.png"));

  /**
   * Construtor
   * 
   * @param parent
   * @param file
   */
  public FileNode(AbstractTreeNode parent, IFile file) {
    super(parent);
    this.file = file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.file.getName();
  }

  /**
   * Retorna o arquivo
   * 
   * @return arquivo
   */
  public IFile getFile() {
    return file;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return icon;
  }

}

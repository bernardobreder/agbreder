package com.agbreder.ide.gui.explorer.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import breder.util.swing.tree.AbstractTreeNode;
import breder.util.swing.tree.DirectoryTreeNode;
import breder.util.util.ImageUtil;

import com.agbreder.ide.model.io.IFile;
import com.agbreder.ide.model.io.IFolder;
import com.agbreder.ide.model.io.IResource;

/**
 * Node de diretório
 * 
 * 
 * @author Bernardo Breder
 */
public class FolderNode extends DirectoryTreeNode {

  /** Diretório */
  private final IFolder folder;
  /** Icone */
  private static final ImageIcon icon = new ImageIcon(ImageUtil
    .load("/com/agbreder/ide/resource/folder.png"));

  /**
   * Construtor
   * 
   * @param parent
   * @param folder
   */
  public FolderNode(AbstractTreeNode parent, IFolder folder) {
    super(parent);
    this.folder = folder;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractTreeNode[] getChildren() {
    IResource[] resources;
    try {
      resources = this.folder.list();
    }
    catch (IOException e) {
      return new AbstractTreeNode[0];
    }
    List<AbstractTreeNode> result =
      new ArrayList<AbstractTreeNode>(resources.length);
    for (int n = 0; n < resources.length; n++) {
      IResource resource = resources[n];
      if (!resource.isHidden()) {
        if (resource.isFolder()) {
          result.add(new FolderNode(this, (IFolder) resource));
        }
        else if (resource.isFile()) {
          result.add(new FileNode(this, (IFile) resource));
        }
      }
    }
    return result.toArray(new AbstractTreeNode[result.size()]);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return this.folder.getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Icon getIcon() {
    return icon;
  }

}

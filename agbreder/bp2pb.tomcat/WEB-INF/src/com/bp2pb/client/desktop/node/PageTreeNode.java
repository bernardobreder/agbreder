package com.bp2pb.client.desktop.node;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Arvore de páginas
 * 
 * @author bernardobreder
 * 
 */
public abstract class PageTreeNode extends DefaultMutableTreeNode {

	/**
	 * Retorna o código do node
	 * 
	 * @return código do node
	 */
	public abstract String getId();

}

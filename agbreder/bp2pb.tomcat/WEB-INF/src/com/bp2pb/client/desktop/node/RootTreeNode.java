package com.bp2pb.client.desktop.node;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class RootTreeNode extends DefaultMutableTreeNode {

	/**
	 * Construtor
	 */
	public RootTreeNode() {
		this.add(new IndexTreeNode());
		this.add(new BrowserTreeNode());
		this.add(new LanguageTreeNode());
		this.add(new FrameworkTreeNode());
		this.add(new DatabaseTreeNode());
		this.add(new EclipseTreeNode());
		this.add(new AboutTreeNode());
	}

}

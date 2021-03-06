package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class BrowserTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public BrowserTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "browser";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Navegador";
	}

}

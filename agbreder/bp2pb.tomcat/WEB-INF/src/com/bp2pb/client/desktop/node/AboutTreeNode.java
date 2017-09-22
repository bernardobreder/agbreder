package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class AboutTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public AboutTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "about";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Sobre";
	}

}

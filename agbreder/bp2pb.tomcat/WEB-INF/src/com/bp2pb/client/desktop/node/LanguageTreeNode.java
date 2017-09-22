package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class LanguageTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public LanguageTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "language";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Linguagem";
	}

}

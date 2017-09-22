package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class IndexTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public IndexTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "index";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Resumo";
	}

}

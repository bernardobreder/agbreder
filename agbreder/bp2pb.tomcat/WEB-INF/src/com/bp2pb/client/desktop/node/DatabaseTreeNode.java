package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class DatabaseTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public DatabaseTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "database";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Banco de Dados";
	}

}

package com.bp2pb.client.desktop.node;

/**
 * Root dos documentos
 * 
 * @author bernardobreder
 * 
 */
public class FrameworkTreeNode extends PageTreeNode {

	/**
	 * Construtor
	 */
	public FrameworkTreeNode() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return "framework";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Framework";
	}

}

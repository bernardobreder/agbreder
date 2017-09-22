package com.agbreder.compiler.parser.node;

/**
 * Nó mas básico
 * 
 * @author bernardobreder
 */
public abstract class AGBNode implements AGBNodeInterface {

	/** Parent */
	private AGBNode parent;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBNode(AGBNode parent) {
		super();
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	public AGBNode getParent() {
		return parent;
	}

	/**
	 * @param c
	 * @return the parent
	 */
	public <E extends AGBNode> E getParent(Class<E> c) {
		AGBNode node = parent;
		while (node != null && !c.isInstance(node)) {
			node = node.getParent();
		}
		if (node == null) {
			return null;
		} else {
			return c.cast(node);
		}
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(AGBNode parent) {
		this.parent = parent;
	}

}

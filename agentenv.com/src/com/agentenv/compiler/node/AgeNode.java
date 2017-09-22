package com.agentenv.compiler.node;

public abstract class AgeNode implements AgeNodeInterface {

	/** Parent */
	private AgeNodeInterface parent;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AgeNode(AgeNodeInterface parent) {
		super();
		this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgeNodeInterface getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends AgeNodeInterface> E getParent(Class<E> c) {
		AgeNodeInterface node = parent;
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
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(AgeNodeInterface parent) {
		this.parent = parent;
	}

}

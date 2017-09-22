package com.agentenv.compiler.node.cmd;

import com.agentenv.compiler.AgeCompiler;
import com.agentenv.compiler.node.AgeNodeInterface;

public abstract class AgeCommandNode extends AgeCompiler implements AgeCommand {

	private AgeNodeInterface parent;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgeNodeInterface getParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends AgeNodeInterface> E getParent(Class<E> c) {
		AgeNodeInterface parent = this.parent;
		while (parent != null && !c.isInstance(parent)) {
			parent = parent.getParent();
		}
		if (parent == null) {
			return null;
		}
		return c.cast(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParent(AgeNodeInterface parent) {
		this.parent = parent;
	}

}

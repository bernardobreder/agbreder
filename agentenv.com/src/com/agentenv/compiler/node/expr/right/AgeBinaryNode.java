package com.agentenv.compiler.node.expr.right;

import java.io.IOException;

import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.AgeNodeInterface;
import com.agentenv.compiler.node.expr.AgeValueInterface;

/**
 * Expressão unária
 * 
 * @author bernardobreder
 * 
 */
public abstract class AgeBinaryNode extends AgeUnaryNode {

	/** Right Value */
	private AgeValueInterface right;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 */
	public AgeBinaryNode(AgeNodeInterface parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {
		super.build(context, linker);
		linker.setFirstRegister(false);
		this.getRight().build(context, linker);
	}

	/**
	 * @return the right
	 */
	public AgeValueInterface getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(AgeValueInterface right) {
		this.right = right;
	}

}

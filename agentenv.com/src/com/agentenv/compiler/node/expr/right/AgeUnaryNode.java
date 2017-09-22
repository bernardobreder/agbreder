package com.agentenv.compiler.node.expr.right;

import java.io.IOException;

import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.AgeNodeInterface;
import com.agentenv.compiler.node.expr.AgeValueInterface;
import com.agentenv.compiler.node.expr.AgeValueNode;

/**
 * Expressão unária
 * 
 * @author bernardobreder
 * 
 */
public abstract class AgeUnaryNode extends AgeValueNode implements AgeRightValueInterface {

	/** Left */
	private AgeValueInterface left;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 */
	public AgeUnaryNode(AgeNodeInterface parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {
		linker.setFirstRegister(true);
		this.getLeft().build(context, linker);
	}

	/**
	 * @return the left
	 */
	public AgeValueInterface getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AgeValueInterface left) {
		this.left = left;
	}

}

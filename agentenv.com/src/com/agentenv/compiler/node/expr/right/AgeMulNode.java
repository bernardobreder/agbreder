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
public class AgeMulNode extends AgeBinaryNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 * @param right
	 */
	public AgeMulNode(AgeNodeInterface parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {
		boolean firstRegister = linker.isFirstRegister();
		AgeValueInterface left = this.getLeft();
		AgeValueInterface right = this.getRight();
		if (left.isPrimitive() && right.isPrimitive()) {
			linker.setFirstRegister(true);
			left.build(context, linker);
			linker.setFirstRegister(false);
			right.build(context, linker);
		} else if (!left.isPrimitive()) {
			linker.setFirstRegister(true);
			left.build(context, linker);
			linker.setFirstRegister(false);
			right.build(context, linker);
		} else {
			linker.setFirstRegister(false);
			right.build(context, linker);
			linker.setFirstRegister(true);
			left.build(context, linker);
		}
		linker.setFirstRegister(firstRegister);
		linker.mulNumber();
	}

}

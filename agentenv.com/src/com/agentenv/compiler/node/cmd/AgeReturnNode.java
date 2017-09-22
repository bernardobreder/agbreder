package com.agentenv.compiler.node.cmd;

import java.io.IOException;

import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.expr.right.AgeRightValueInterface;

public class AgeReturnNode extends AgeCommandNode {

	private AgeRightValueInterface value;

	/**
	 * Construtor
	 * 
	 * @param value
	 */
	public AgeReturnNode(AgeRightValueInterface value) {
		super();
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {
		linker.setFirstRegister(true).setLeftRegister(true);
		this.value.build(context, linker);
		linker.returnLeft();
	}

}

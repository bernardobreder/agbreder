package com.agentenv.compiler.node.expr.right;

import java.io.IOException;

import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.AgeNodeInterface;
import com.agentenv.compiler.node.expr.AgeValueNode;

/**
 * Estrutura de funções nativas
 * 
 * @author bernardobreder
 * 
 */
public class AgeNativeNode extends AgeValueNode {

	public AgeNativeNode(AgeNodeInterface parent) {
		super(parent);
	}

	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {

	}

}

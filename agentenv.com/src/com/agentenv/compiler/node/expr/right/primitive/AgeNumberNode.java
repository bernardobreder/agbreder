package com.agentenv.compiler.node.expr.right.primitive;

import java.io.IOException;

import com.agentenv.compiler.lexical.AgeToken;
import com.agentenv.compiler.linker.AgeContext;
import com.agentenv.compiler.linker.AgeLinker;
import com.agentenv.compiler.node.AgeNodeInterface;
import com.agentenv.compiler.node.expr.AgeValueNode;

/**
 * Estrutura de número
 * 
 * @author bernardobreder
 * 
 */
public class AgeNumberNode extends AgeValueNode implements AgePrimitiveInterface {

	private Integer index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param check
	 */
	public AgeNumberNode(AgeNodeInterface parent, AgeToken token) {
		super(parent);
		this.setToken(token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(AgeContext context, AgeLinker linker) throws IOException {
		if (index == null) {
			index = context.getNumbers().add(this.getToken().getImage());
		}
		linker.loadConstant(this.index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPrimitive() {
		return true;
	}

}

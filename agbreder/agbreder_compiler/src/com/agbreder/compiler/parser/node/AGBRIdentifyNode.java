package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBReferenceException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBRIdentifyNode extends AGBRValueNode {

	private int index;

	private AGBVariableNode node;

	private AGBClassNode classnode;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBRIdentifyNode(AGBNode parent, AGBToken token) {
		super(parent);
		this.setToken(token);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) {
		String name = this.getToken().getImage();
		// Verifica as variáveis do método
		if (this.getNode() == null) {
			AGBMethodNode methodNode = this.getParent(AGBMethodNode.class);
			int variableSize = methodNode.getVariables().size();
			AGBVariableNode variable = this.getParent(AGBBlockNode.class).findVariable(name, true);
			if (variable != null) {
				this.setIndex(variableSize - variable.getIndex() - 1);
				this.setNode(variable);
			}
		}
		// Verifica os parametros do método
		if (this.getNode() == null) {
			AGBMethodNode method = this.getParent(AGBMethodNode.class);
			if (method != null) {
				List<AGBVariableNode> params = method.getParams();
				int paramCount = params.size();
				for (int n = 0; n < paramCount; n++) {
					AGBVariableNode param = params.get(n);
					if (param.getToken().getImage().equals(name)) {
						int index = paramCount + method.getVariables().size() - n;
						if (method.isStatic()) {
							index--;
						}
						this.setIndex(index);
						this.setNode(param);
						break;
					}
				}
			}
		}
		// Verifica se existe alguma classe
		if (this.getNode() == null) {
			for (AGBClassNode classnode : context.getClasses()) {
				if (classnode.getName().getImage().equals(name)) {
					this.setClassnode(classnode);
					break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		if (this.getNode() != null) {
			this.setType(getNode().getType());
		} else if (this.getClassnode() != null) {
			this.setType(getClassnode().getType());
		} else {
			throw new AGBReferenceException(this.getToken());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException {
		output.opStackLoad(this.getIndex());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStatic() {
		return this.getClassnode() != null;
	}

	/**
	 * @return the vari
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the node
	 */
	public AGBVariableNode getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(AGBVariableNode node) {
		this.node = node;
	}

	/**
	 * @param variableIndex
	 *            the variableIndex to set
	 */
	public void setIndex(int variableIndex) {
		this.index = variableIndex;
	}

	/**
	 * @return the classnode
	 */
	public AGBClassNode getClassnode() {
		return classnode;
	}

	/**
	 * @param classnode
	 *            the classnode to set
	 */
	public void setClassnode(AGBClassNode classnode) {
		this.classnode = classnode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.node != null) {
			return this.node.getToken().getImage();
		} else if (this.classnode != null) {
			return this.classnode.getName().getImage();
		} else {
			return this.getToken().getImage();
		}
	}
}

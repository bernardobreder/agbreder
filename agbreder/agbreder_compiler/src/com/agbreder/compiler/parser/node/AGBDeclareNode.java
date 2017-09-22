package com.agbreder.compiler.parser.node;

import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBDeclareNode extends AGBAssignNode {

	private AGBTypeNode type;

	private AGBVariableNode variable;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 * @param right
	 * @param type
	 */
	public AGBDeclareNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param left
	 * @param right
	 * @param type
	 */
	public AGBDeclareNode(AGBNode parent, AGBLValue left, AGBRValue right, AGBTypeNode type) {
		super(parent);
		this.setToken(left.getToken());
		this.setLeft(left);
		this.setRight(right);
		this.setType(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		super.header(context);
		this.checkVariableAlreadyExist(context);
		this.variable = new AGBVariableNode(this, this.getToken(), this.getType());
		context.getBlockStack().peek().getVariables().add(variable);
		context.getMethodStack().peek().addVariable(variable);
	}

	/**
	 * Verifica se já foi declarada um variável com o mesmo nome
	 * 
	 * @param context
	 * @throws AGBTokenException
	 */
	private void checkVariableAlreadyExist(AGBCompileContext context) throws AGBTokenException {
		AGBBlockNode block = context.getBlockStack().peek();
		List<AGBVariableNode> list = block.getVariables();
		while (list != null) {
			for (int n = 0; n < list.size(); n++) {
				AGBVariableNode var = list.get(n);
				if (var.getToken().getImage().equals(this.getToken().getImage())) {
					throw new AGBTokenException("variable already declared", this.getToken());
				}
			}
			if (block == null) {
				break;
			}
			AGBNode node = block.getParent();
			block = null;
			list = null;
			while (node != null) {
				if (node instanceof AGBBlockNode) {
					block = (AGBBlockNode) node;
					list = block.getVariables();
					break;
				} else if (node instanceof AGBMethodNode) {
					AGBMethodNode method = (AGBMethodNode) node;
					list = method.getParams();
					break;
				} else {
					node = node.getParent();
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.variable.setDeclared(true);
		super.body(context);
	}

	/**
	 * @return the type
	 */
	public AGBTypeNode getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(AGBTypeNode type) {
		this.type = type;
	}

	/**
	 * @return the variable
	 */
	public AGBVariableNode getVariable() {
		return variable;
	}

	/**
	 * @param variable
	 *            the variable to set
	 */
	public void setVariable(AGBVariableNode variable) {
		this.variable = variable;
	}

}

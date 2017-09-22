package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBFunctionTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBFunctionNode extends AGBRValueNode {

	/** Retorno do método */
	private AGBTypeNode returnType;

	/** Parametros do metodo */
	private final List<AGBVariableNode> params = new LightArrayList<AGBVariableNode>();

	/** Parametros do metodo */
	private final List<AGBRValue> declares = new LightArrayList<AGBRValue>();

	/** Throws */
	private final List<AGBTypeNode> throwsTypes = new LightArrayList<AGBTypeNode>();

	/** Comando */
	private AGBCommandNode command;

	/** Method */
	private AGBMethodNode method;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param params
	 * @param type
	 * @param params
	 */
	public AGBFunctionNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getMethod().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getMethod().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getMethod().link(context);
		this.setType(new AGBFunctionTypeNode(this.getToken(), this.getReturnType(), this.getParams(), this.getThrowsTypes()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		{
			List<AGBRValue> variables = this.getDeclares();
			int varSize = variables.size();
			for (int n = 0; n < varSize; n++) {
				variables.get(n).build(output);
			}
		}
		output.opLoadFunc(this.getMethod().getIndex(), this.getDeclares().size());
	}

	/**
	 * @return the returnType
	 */
	public AGBTypeNode getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(AGBTypeNode returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the params
	 */
	public List<AGBVariableNode> getParams() {
		return params;
	}

	/**
	 * @return the throwsTypes
	 */
	public List<AGBTypeNode> getThrowsTypes() {
		return throwsTypes;
	}

	/**
	 * @return the command
	 */
	public AGBCommandNode getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(AGBCommandNode command) {
		this.command = command;
	}

	/**
	 * @return the method
	 */
	public AGBMethodNode getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(AGBMethodNode method) {
		this.method = method;
	}

	/**
	 * @return the variables
	 */
	public List<AGBRValue> getDeclares() {
		return declares;
	}

	/**
	 * Adiciona uma declaração
	 * 
	 * @param node
	 */
	public void addDeclare(AGBDeclareNode node) {
		this.getDeclares().add(node.getRight());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("function");
		sb.append(" ");
		sb.append(this.getType());
		sb.append(" ");
		sb.append(this.getParams());
		sb.append(" ");
		sb.append(this.getThrowsTypes());
		sb.append(" ");
		sb.append(this.getDeclares());
		sb.append(" ");
		sb.append(this.getCommand());
		return sb.toString();
	}

}

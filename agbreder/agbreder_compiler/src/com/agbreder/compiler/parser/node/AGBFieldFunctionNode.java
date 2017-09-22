package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBFieldFunctionNode extends AGBRValueNode implements AGBRValue {

	/** Valor */
	private AGBRValue value;

	/** Indice da variável */
	private int index;

	private int variables;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param value
	 * @param index
	 * @param token
	 * @param params
	 * @param type
	 * @param params
	 */
	public AGBFieldFunctionNode(AGBNode parent, AGBRValue value, int index) {
		super(parent);
		this.value = value;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getValue().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getValue().body(context);
		this.setVariables(context.getMethodStack().peek().getVariables().size());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getValue().link(context);
		this.setType(this.getValue().getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		output.opStackLoad(this.getVariables());
		output.opObjectGet(getIndex() + 2);
	}

	/**
	 * @return the value
	 */
	public AGBRValue getValue() {
		return value;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return the variables
	 */
	public int getVariables() {
		return variables;
	}

	/**
	 * @param variables
	 *            the variables to set
	 */
	public void setVariables(int variables) {
		this.variables = variables;
	}

}

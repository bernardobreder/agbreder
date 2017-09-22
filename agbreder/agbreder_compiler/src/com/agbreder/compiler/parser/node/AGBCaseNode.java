package com.agbreder.compiler.parser.node;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBCaseNode extends AGBCommandNode {

	private AGBRValueNode value;

	private AGBCommandNode command;

	private int beginPc;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBCaseNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getValue().header(context);
		this.getCommand().header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getValue().body(context);
		this.getCommand().body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getValue().link(context);
		this.getCommand().link(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		return this.getCommand().isReturned();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * @return the val
	 */
	public AGBRValueNode getValue() {
		return value;
	}

	/**
	 * @return the cmd
	 */
	public AGBCommandNode getCommand() {
		return command;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(AGBRValueNode value) {
		this.value = value;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(AGBCommandNode command) {
		this.command = command;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("case");
		sb.append(" ");
		sb.append(this.getValue());
		sb.append(" ");
		sb.append(this.getCommand());
		return sb.toString();
	}

	/**
	 * @return the index
	 */
	public int getBeginPc() {
		return beginPc;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setBeginPc(int index) {
		this.beginPc = index;
	}

}

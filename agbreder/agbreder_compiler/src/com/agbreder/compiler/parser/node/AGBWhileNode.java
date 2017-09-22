package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBBooleanTypeNode;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBWhileNode extends AGBLoopNode {

	private AGBRValueNode condition;

	private AGBCommandNode command;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBWhileNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.condition.header(context);
		this.command.header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.condition.body(context);
		this.command.body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.condition.link(context);
		this.command.link(context);
		{
			if (!this.getCondition().getType().isBoolean()) {
				throw new AGBCastException(this.getCondition().getType(), AGBBooleanTypeNode.getInstance(), this.getCondition().getToken());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.setBeginPc(output.getPc());
		this.getCondition().build(output);
		output.opJumpFalse(this.getEndPc());
		this.getCommand().build(output);
		output.opJumpInt(this.getBeginPc());
		this.setEndPc(output.getPc());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		return this.command.isReturned();
	}

	/**
	 * @return the condition
	 */
	public AGBRValueNode getCondition() {
		return condition;
	}

	/**
	 * @return the command
	 */
	public AGBCommandNode getCommand() {
		return command;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(AGBRValueNode condition) {
		this.condition = condition;
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
		sb.append("while");
		sb.append(" ");
		sb.append(this.getCondition().toString());
		sb.append(" ");
		sb.append(this.getCommand().toString());
		return sb.toString();
	}

}

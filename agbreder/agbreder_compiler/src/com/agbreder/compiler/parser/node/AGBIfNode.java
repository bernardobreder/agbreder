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
public class AGBIfNode extends AGBCommandNode {

	private AGBRValueNode condition;

	private AGBCommandNode command;

	private AGBCommandNode elseCommand;

	private int elsePc;

	private int endPc;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param cond
	 * @param cmd
	 * @param ecmd
	 */
	public AGBIfNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.condition.header(context);
		this.command.header(context);
		if (elseCommand != null) {
			this.elseCommand.header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.condition.body(context);
		this.command.body(context);
		if (elseCommand != null) {
			this.elseCommand.body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.condition.link(context);
		this.command.link(context);
		if (elseCommand != null) {
			this.elseCommand.link(context);
		}
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
	public boolean isReturned() {
		if (this.elseCommand == null) {
			return false;
		}
		return this.command.isReturned() && this.elseCommand.isReturned();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getCondition().build(output);
		output.opJumpFalse(this.elsePc);
		this.getCommand().build(output);
		if (this.getElseCommand() != null) {
			output.opJumpInt(this.endPc);
		}
		this.elsePc = output.getPc();
		if (this.getElseCommand() != null) {
			this.getElseCommand().build(output);
		}
		this.endPc = output.getPc();
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
	 * @return the elseCommand
	 */
	public AGBCommandNode getElseCommand() {
		return elseCommand;
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
	 * @param elseCommand
	 *            the elseCommand to set
	 */
	public void setElseCommand(AGBCommandNode elseCommand) {
		this.elseCommand = elseCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("if");
		sb.append(" ");
		sb.append(this.getCondition().toString());
		sb.append(" ");
		sb.append(this.getCommand().toString());
		return sb.toString();
	}

}

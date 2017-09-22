package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBForNode extends AGBLoopNode {

	private List<AGBNode> inits;

	private AGBNode condition;

	private List<AGBNode> nexts;

	private final AGBBlockNode block = new AGBBlockNode(this);

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBForNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		for (AGBNode node : this.getInits()) {
			node.header(context);
		}
		this.getCondition().header(context);
		for (AGBNode node : this.getNexts()) {
			node.header(context);
		}
		this.getCommand().header(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		for (AGBNode node : this.getInits()) {
			node.body(context);
		}
		this.getCondition().body(context);
		for (AGBNode node : this.getNexts()) {
			node.body(context);
		}
		this.getCommand().body(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		for (AGBNode node : this.getInits()) {
			node.link(context);
		}
		this.getCondition().link(context);
		for (AGBNode node : this.getNexts()) {
			node.link(context);
		}
		this.getCommand().link(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		for (int n = 0; n < this.getInits().size(); n++) {
			this.getInits().get(n).build(output);
		}
		this.setBeginPc(output.getPc());
		this.getCondition().build(output);
		output.opJumpFalse(this.getEndPc());
		this.getCommand().build(output);
		for (int n = 0; n < this.getNexts().size(); n++) {
			this.getNexts().get(n).build(output);
		}
		output.opJumpInt(this.getBeginPc());
		this.setEndPc(output.getPc());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		return this.getCommand().isReturned();
	}

	/**
	 * @return the command
	 */
	public AGBBlockNode getCommand() {
		return getBlock();
	}

	/**
	 * @return the condition
	 */
	public AGBNode getCondition() {
		return condition;
	}

	/**
	 * @return the nexts
	 */
	public List<AGBNode> getNexts() {
		return nexts;
	}

	/**
	 * @return the inits
	 */
	public List<AGBNode> getInits() {
		return inits;
	}

	/**
	 * @param inits
	 *            the inits to set
	 */
	public void setInits(List<AGBNode> inits) {
		this.inits = inits;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(AGBNode condition) {
		this.condition = condition;
	}

	/**
	 * @param nexts
	 *            the nexts to set
	 */
	public void setNexts(List<AGBNode> nexts) {
		this.nexts = nexts;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("for (...)");
		sb.append(" ");
		sb.append(this.getCommand());
		return sb.toString();
	}

	/**
	 * @return the block
	 */
	public AGBBlockNode getBlock() {
		return block;
	}

}

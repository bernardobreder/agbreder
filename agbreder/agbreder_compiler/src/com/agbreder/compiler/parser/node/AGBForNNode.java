package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBNumberTypeNode;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBForNNode extends AGBLoopNode {

	private AGBToken name;

	private AGBRValueNode left;

	private AGBRValueNode right;

	private AGBLValue lvalue;

	private AGBNumberNode number;

	private AGBDeclareNode declare;

	private AGBRIdentifyNode rvalue;

	private final AGBBlockNode block = new AGBBlockNode(this);

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBForNNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		this.lvalue = new AGBLIdentifyNode(this.getBlock(), this.name);
		this.number = new AGBNumberNode(this.getBlock(), new AGBToken("", "1", AGBTokenType.NUMBER.getId(), -1, -1));
		this.declare = new AGBDeclareNode(this.getBlock(), lvalue, number, new AGBNumberTypeNode(this.name));
		this.rvalue = new AGBRIdentifyNode(this.getBlock(), this.name);
		this.declare.header(context);
		this.rvalue.header(context);
		this.left.header(context);
		this.right.header(context);
		this.getCommand().header(context);
		this.number.header(context);
		this.lvalue.header(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		this.declare.body(context);
		this.rvalue.body(context);
		this.left.body(context);
		this.right.body(context);
		this.getCommand().body(context);
		this.number.body(context);
		this.lvalue.body(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getCommand());
		this.declare.link(context);
		this.left.link(context);
		this.number.link(context);
		this.lvalue.link(context);
		this.right.link(context);
		this.rvalue.link(context);
		this.getCommand().link(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.setBeginPc(output.getPc());
		this.getLeft().build(output);
		this.getLvalue().build(output);
		this.getRight().build(output);
		this.setBeginPc(output.getPc());
		int varIndex = this.getRvalue().getIndex();
		output.opStackForIncBegin(varIndex, this.getEndPc());
		this.getCommand().build(output);
		output.opStackForIncEnd(varIndex, this.getBeginPc());
		this.setEndPc(output.getPc());
		output.opStackPop(1);
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
	 * @return the name
	 */
	public AGBToken getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(AGBToken name) {
		this.name = name;
	}

	/**
	 * @return the left
	 */
	public AGBRValueNode getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBRValueNode left) {
		this.left = left;
		this.left.setParent(this);
	}

	/**
	 * @return the right
	 */
	public AGBRValueNode getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(AGBRValueNode right) {
		this.right = right;
		this.right.setParent(this);
	}

	/**
	 * @return the lvalue
	 */
	public AGBLValue getLvalue() {
		return lvalue;
	}

	/**
	 * @param lvalue
	 *            the lvalue to set
	 */
	public void setLvalue(AGBLValue lvalue) {
		this.lvalue = lvalue;
		this.lvalue.setParent(this);
	}

	/**
	 * @return the number
	 */
	public AGBNumberNode getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(AGBNumberNode number) {
		this.number = number;
		this.number.setParent(this);
	}

	/**
	 * @return the declare
	 */
	public AGBDeclareNode getDeclare() {
		return declare;
	}

	/**
	 * @param declare
	 *            the declare to set
	 */
	public void setDeclare(AGBDeclareNode declare) {
		this.declare = declare;
		this.declare.setParent(this);
	}

	/**
	 * @return the rvalue
	 */
	public AGBRIdentifyNode getRvalue() {
		return rvalue;
	}

	/**
	 * @param rvalue
	 *            the rvalue to set
	 */
	public void setRvalue(AGBRIdentifyNode rvalue) {
		this.rvalue = rvalue;
		this.rvalue.setParent(this);
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(AGBCommandNode command) {
		List<AGBCommandNode> commands = this.getCommand().getCommands();
		commands.clear();
		commands.add(command);
		command.setParent(this.getCommand());
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

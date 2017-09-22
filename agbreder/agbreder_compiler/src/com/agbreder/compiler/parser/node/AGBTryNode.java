package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBTryNode extends AGBCommandNode {

	/** Token do Try */
	private final AGBToken token;
	/** Comando protegido */
	private AGBCommandNode command;
	/** Captura de erro */
	private final List<AGBCatchNode> catchs = new ArrayList<AGBCatchNode>();
	/** Indice de classes dos catchs */
	private final List<Integer> classIndexs = new ArrayList<Integer>();
	/** Indice do Try */
	private int tryIndex;
	/** Final do Try */
	private int endIndex;
	/** Indice do catchs */
	private int[] catchIndexs;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBTryNode(AGBCommandNode parent, AGBToken token) {
		super(parent);
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.catchIndexs = new int[this.catchs.size()];
		this.command.header(context);
		for (AGBCatchNode node : catchs) {
			node.header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.command.body(context);
		for (AGBCatchNode node : catchs) {
			node.body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.command.link(context);
		for (AGBCatchNode node : catchs) {
			node.link(context);
			this.classIndexs.add(node.getIndex());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		output.opThrowTry(tryIndex);
		this.getCommand().build(output);
		output.opThrowTrue();
		output.opJumpInt(endIndex);
		tryIndex = output.getPc();
		for (int n = 0; n < this.classIndexs.size(); n++) {
			int index = this.classIndexs.get(n);
			output.opThrowJump(index, this.catchIndexs[n]);
		}
		output.opThrowFalse();
		for (int n = 0; n < this.catchs.size(); n++) {
			AGBCatchNode catchNode = this.catchs.get(n);
			this.catchIndexs[n] = output.getPc();
			output.setCounter(1);
			catchNode.getDeclare().getLeft().build(output);
			catchNode.build(output);
			if (n != this.catchs.size() - 1) {
				output.opJumpInt(endIndex);
			}
		}
		endIndex = output.getPc();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		for (AGBCatchNode node : this.catchs) {
			if (!node.isReturned()) {
				return false;
			}
		}
		return this.getCommand().isReturned();
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
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
	 * @return the catchs
	 */
	public List<AGBCatchNode> getCatchs() {
		return catchs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "try " + this.command.toString() + " " + this.catchs.toString();
	}

}

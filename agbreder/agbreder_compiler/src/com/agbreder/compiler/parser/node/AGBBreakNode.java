package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBBreakNode extends AGBCommandNode {

	private final AGBToken token;

	private AGBLoopNode node;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBBreakNode(AGBCommandNode parent, AGBToken token) {
		super(parent);
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		AGBNode parent = this.getParent();
		while (parent != null) {
			if (parent instanceof AGBLoopNode) {
				this.setNode((AGBLoopNode) parent);
				break;
			}
			parent = parent.getParent();
		}
		if (this.getNode() == null) {
			throw new AGBTokenException("not found the loop statement", this.getToken());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		output.opJumpInt(this.getNode().getEndPc());
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * @return the node
	 */
	public AGBLoopNode getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(AGBLoopNode node) {
		this.node = node;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getToken().getImage();
	}

}

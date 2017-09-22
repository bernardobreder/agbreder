package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBCatchNode extends AGBCommandNode {

	/** Tipo de classe declarada */
	private AGBTypeNode type;
	/** Nome da variável */
	private AGBToken name;
	/** declaração da variável */
	private AGBDeclareNode declare;
	/** Bloco de comando */
	private final AGBBlockNode block = new AGBBlockNode(this);
	/** Indice da classe */
	private int index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBCatchNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.declare = new AGBDeclareNode(this.getBlock(), new AGBLIdentifyNode(this, this.name), new AGBNullNode(this, null), this.type);
		context.getBlockStack().push(this.getBlock());
		this.type.header(context);
		this.block.header(context);
		this.declare.header(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getBlock());
		this.declare.body(context);
		this.type.body(context);
		this.block.body(context);
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this.getBlock());
		this.declare.link(context);
		this.type.link(context);
		this.block.link(context);
		this.declare.link(context);
		context.getBlockStack().pop();
		if (this.type.isType()) {
			if (!this.type.getRef(context).isThrow()) {
				throw new AGBTokenException("class must be a throw class", this.type.getToken());
			}
			this.index = this.type.getRef(context).getIndex();
		} else {
			throw new AGBTokenException("catch type only class", type.getToken());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getBlock().build(output);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		return this.getBlock().isReturned();
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
	 * @return the block
	 */
	public AGBBlockNode getBlock() {
		return block;
	}

	/**
	 * @return the declare
	 */
	public AGBDeclareNode getDeclare() {
		return declare;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "catch (" + this.type.toString() + " " + this.name.toString() + ") " + this.block.toString();
	}

	/**
	 * Retorna
	 * 
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

}

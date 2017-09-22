package com.agbreder.compiler.parser.node;

import java.io.IOException;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBAskNode extends AGBTernaryNode {

	private int rightPc;

	private int endPc;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBAskNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		super.header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		super.body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		super.link(context);
		if (!this.getLeft().getType().isBoolean()) {
			throw new AGBTokenException("and operator work with boolean types", this.getLeft().getToken());
		}
		if (this.getCenter().getType().canCast(context, this.getRight().getType())) {
			if (this.getRight().getType().canCast(context, this.getCenter().getType())) {
				this.setType(this.getCenter().getType());
			} else {
				this.setType(this.getCenter().getType());
			}
		} else {
			if (this.getRight().getType().canCast(context, this.getCenter().getType())) {
				this.setType(this.getRight().getType());
			} else {
				throw new AGBTokenException("can not have the same type", this.getToken());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.getLeft().build(output);
		output.opJumpFalse(this.rightPc);
		this.getCenter().build(output);
		output.decCounter(1);
		output.opJumpInt(this.endPc);
		this.rightPc = output.getPc();
		this.getRight().build(output);
		this.endPc = output.getPc();
	}

}

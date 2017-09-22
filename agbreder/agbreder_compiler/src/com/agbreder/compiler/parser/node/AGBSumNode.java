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
public class AGBSumNode extends AGBBinaryNode {

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBSumNode(AGBNode parent) {
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
		if (this.getLeft().getType().isString() || this.getRight().getType().isString()) {
			if (this.getLeft().getType().isString()) {
				this.setType(this.getLeft().getType());
			} else {
				this.setType(this.getRight().getType());
			}
		} else {
			if (!this.getLeft().getType().isNumber() && !this.getLeft().getType().isString()) {
				throw new AGBTokenException("sum operator work with number and string types", this.getLeft().getToken());
			}
			if (!this.getRight().getType().isNumber() && !this.getRight().getType().isString()) {
				throw new AGBTokenException("sum operator work with number and string types", this.getLeft().getToken());
			}
			this.setType(this.getLeft().getType());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		if (this.getType().isString()) {
			this.getLeft().build(output);
			if (!this.getLeft().getType().isString()) {
				output.opToString(this.getLeft());
			}
			this.getRight().build(output);
			if (!this.getRight().getType().isString()) {
				output.opToString(this.getRight());
			}
			output.opStringSum();
		} else {
			this.getLeft().build(output);
			this.getRight().build(output);
			output.opSum(this.getLeft());
		}
	}

}

package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBCastException;
import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBFunctionTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBCallFunctionNode extends AGBRValueNode implements AGBRValue {

	private AGBRValue left;

	/** Valores */
	private final List<AGBRValue> values = new LightArrayList<AGBRValue>();

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param value
	 * @param index
	 * @param token
	 * @param params
	 * @param type
	 * @param params
	 */
	public AGBCallFunctionNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getLeft().header(context);
		{
			List<AGBRValue> values = this.getValues();
			int size = values.size();
			for (int n = 0; n < size; n++) {
				AGBRValue value = values.get(n);
				value.header(context);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getLeft().body(context);
		{
			List<AGBRValue> values = this.getValues();
			int size = values.size();
			for (int n = 0; n < size; n++) {
				AGBRValue value = values.get(n);
				value.body(context);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getLeft().link(context);
		{
			List<AGBRValue> values = this.getValues();
			int size = values.size();
			for (int n = 0; n < size; n++) {
				AGBRValue value = values.get(n);
				value.link(context);
			}
		}
		AGBTypeNode type = this.getLeft().getType();
		if (!type.isFunction()) {
			throw new AGBTokenException("left value must be a function", this.getLeft().getToken());
		}
		AGBFunctionTypeNode funcType = (AGBFunctionTypeNode) type;
		if (this.getValues().size() != funcType.getParamTypes().size()) {
			throw new AGBTokenException("wrong number of parameters", this.getLeft().getToken());
		}
		for (int n = 0; n < this.getValues().size(); n++) {
			AGBTypeNode leftType = this.getValues().get(n).getType();
			AGBTypeNode rightType = funcType.getParamTypes().get(n);
			if (!leftType.canCast(context, rightType)) {
				throw new AGBCastException(leftType, rightType, this.getLeft().getToken());
			}
		}
		if (funcType.getReturnType().isThis()) {
			this.setType(funcType);
		} else {
			this.setType(funcType.getReturnType());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		{
			List<AGBRValue> values = this.getValues();
			int size = values.size();
			for (int n = 0; n < size; n++) {
				AGBRValue value = values.get(n);
				value.build(output);
			}
		}
		this.getLeft().build(output);
		output.opJumpCallFunc();
	}

	/**
	 * @return the values
	 */
	public List<AGBRValue> getValues() {
		return values;
	}

	/**
	 * @return the left
	 */
	public AGBRValue getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBRValue left) {
		this.left = left;
	}

}

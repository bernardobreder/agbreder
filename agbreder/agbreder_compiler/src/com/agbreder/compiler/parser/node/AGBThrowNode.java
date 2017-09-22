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
public class AGBThrowNode extends AGBCommandNode {

	/** Token */
	private final AGBToken token;
	/** Value */
	private AGBRValueNode value;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBThrowNode(AGBCommandNode parent, AGBToken token) {
		super(parent);
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.value.header(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.value.body(context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.value.link(context);
		checkType(context);
		checkTryThrow(context);
	}

	/**
	 * @param context
	 * @throws AGBException
	 */
	private void checkType(AGBCompileContext context) throws AGBException {
		AGBTypeNode type = this.value.getType();
		AGBClassNode ref = type.getRef(context);
		if (!ref.isThrow()) {
			throw new AGBTokenException("expression must be a type of throw class", this.value.getToken());
		}
	}

	/**
	 * @param context
	 * @throws AGBException
	 * @throws AGBTokenException
	 */
	private void checkTryThrow(AGBCompileContext context) throws AGBException, AGBTokenException {
		AGBTypeNode type = this.value.getType();
		AGBClassNode ref = type.getRef(context);
		AGBThrowClassNode throwNode = (AGBThrowClassNode) ref;
		if (!throwNode.isRuntime()) {
			boolean found = false;
			AGBNode parent = this.getParent();
			while (parent != null) {
				if (parent instanceof AGBTryNode) {
					AGBTryNode tryNode = (AGBTryNode) parent;
					for (AGBCatchNode catchNode : tryNode.getCatchs()) {
						if (type.canCast(context, catchNode.getType())) {
							found = true;
							break;
						}
					}
				}
				if (found) {
					break;
				}
				parent = parent.getParent();
			}
			if (!found) {
				AGBMethodNode methodNode = context.getMethodStack().peek();
				if (!methodNode.getThrowsTypes().contains(type)) {
					throw new AGBTokenException(String.format("current method must throw '%s'", ref.getName().getImage()), methodNode.getName());
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		this.value.build(output);
		output.opThrowFalse();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReturned() {
		return true;
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * @return the value
	 */
	public AGBRValueNode getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(AGBRValueNode value) {
		this.value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getToken().toString() + " " + this.value.toString();
	}

}

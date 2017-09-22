package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBNativeNode extends AGBRValueNode {

	/** Params */
	private final List<AGBValueNode> params = new LightArrayList<AGBValueNode>();

	private AGBToken opcodeToken;

	private AGBNativeEnum func;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBNativeNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SemanticException
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		int size = this.params.size();
		for (int n = 0; n < size; n++) {
			params.get(n).header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		int size = this.params.size();
		for (int n = 0; n < size; n++) {
			params.get(n).body(context);
		}
		String name = this.getToken().getImage() + "." + this.opcodeToken.getImage();
		for (AGBNativeEnum func : AGBNativeEnum.values()) {
			if (func.getName().equals(name)) {
				this.func = func;
				break;
			}
		}
		if (this.func == null) {
			throw new AGBTokenException(String.format("not found the native function '%s'", name), this.opcodeToken);
		}
		this.setType(this.func.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		int size = this.params.size();
		if (size != this.func.getParameters().size()) {
			throw new AGBTokenException("wrong number of parameter", this.getToken());
		}
		for (int n = 0; n < size; n++) {
			AGBValueNode param = params.get(n);
			param.link(context);
			if (!param.getType().canCast(context, this.func.getParameters().get(n))) {
				throw new AGBTokenException(String.format("can not cast '%s' to '%s'", param.getType().getToken().getImage(), this.func.getParameters().get(n).getToken()), this.opcodeToken);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int size = this.params.size();
		for (int n = 0; n < size; n++) {
			AGBValueNode parameter = params.get(n);
			parameter.build(output);
		}
		output.op(this.func.getGroup(), this.func.getOpcode(), 1 - this.params.size());
	}

	/**
	 * @return the opcodeToken
	 */
	public AGBToken getOpcodeToken() {
		return opcodeToken;
	}

	/**
	 * @param opcodeToken
	 *            the opcodeToken to set
	 */
	public void setOpcodeToken(AGBToken opcodeToken) {
		this.opcodeToken = opcodeToken;
	}

	/**
	 * @return the params
	 */
	public List<AGBValueNode> getParams() {
		return params;
	}

}

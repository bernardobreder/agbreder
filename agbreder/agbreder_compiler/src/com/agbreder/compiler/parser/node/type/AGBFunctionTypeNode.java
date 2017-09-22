package com.agbreder.compiler.parser.node.type;

import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.AGBVariableNode;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBFunctionTypeNode extends AGBTypeNode {

	/** Token derivado */
	public static final AGBToken TOKEN = new AGBToken("", "function", AGBTokenType.FUNCTION.getId(), -1, -1);

	/** Retorno */
	private AGBTypeNode returnType;

	/** Parametros */
	private List<AGBTypeNode> paramTypes;

	/** Parametros */
	private List<AGBTypeNode> throwTypes;

	/**
	 * Construtor
	 * 
	 * @param token
	 * @param returnType
	 * @param vars
	 * @param throwTypes
	 */
	public AGBFunctionTypeNode(AGBToken token, AGBTypeNode returnType, List<AGBVariableNode> vars, List<AGBTypeNode> throwTypes) {
		super(token);
		this.setReturnType(returnType);
		List<AGBTypeNode> params = new LightArrayList<AGBTypeNode>();
		for (AGBVariableNode var : vars) {
			params.add(var.getType());
		}
		this.setParamTypes(params);
		this.setThrowTypes(throwTypes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) {
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
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFunction() {
		return true;
	}

	/**
	 * @return the returnType
	 */
	public AGBTypeNode getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(AGBTypeNode returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the params
	 */
	public List<AGBTypeNode> getParamTypes() {
		return paramTypes;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParamTypes(List<AGBTypeNode> params) {
		this.paramTypes = params;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((paramTypes == null) ? 0 : paramTypes.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
		result = prime * result + ((throwTypes == null) ? 0 : throwTypes.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AGBFunctionTypeNode other = (AGBFunctionTypeNode) obj;
		if (paramTypes == null) {
			if (other.paramTypes != null)
				return false;
		} else if (!paramTypes.equals(other.paramTypes))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		if (throwTypes == null) {
			if (other.throwTypes != null)
				return false;
		} else if (!throwTypes.equals(other.throwTypes))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getReturnType() + " " + this.getParamTypes();
	}

	/**
	 * @return the throwTypes
	 */
	public List<AGBTypeNode> getThrowTypes() {
		return throwTypes;
	}

	/**
	 * @param throwTypes
	 *            the throwTypes to set
	 */
	public void setThrowTypes(List<AGBTypeNode> throwTypes) {
		this.throwTypes = throwTypes;
	}

}

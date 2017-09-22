package com.agbreder.compiler.parser.node.type;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.parser.node.AGBNode;
import com.agbreder.compiler.parser.node.AGBSuperTypeNode;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.token.AGBTokenType;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public abstract class AGBTypeNode extends AGBNode {

	/** Token */
	private final AGBToken token;

	/** Node Class */
	protected AGBClassNode classNode;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 */
	public AGBTypeNode(AGBToken token) {
		super(null);
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
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
	public void build(OpcodeOutputStream output) {
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isBoolean() {
		return this instanceof AGBBooleanTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isByte() {
		return this instanceof AGBByteTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isBooleanArray() {
		return this instanceof AGBBooleanArrayTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isByteArray() {
		return this instanceof AGBByteArrayTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isFunction() {
		return this instanceof AGBFunctionTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isPrimitive() {
		return this.isBoolean() || this.isNumber() || this.isString() || this.isObject();
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isNumber() {
		return this instanceof AGBNumberTypeNode || this instanceof AGBByteTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isNumberArray() {
		return this instanceof AGBNumberArrayTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isString() {
		return this instanceof AGBStringTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isThis() {
		return this instanceof AGBThisTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isType() {
		return this instanceof AGBClassTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isSuper() {
		return this instanceof AGBSuperTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isNull() {
		return this instanceof AGBNullTypeNode;
	}

	/**
	 * Indica se é do tipo boolean
	 * 
	 * @return tipo especifico
	 */
	public boolean isObject() {
		return this instanceof AGBObjectTypeNode;
	}

	/**
	 * Indica se pode fazer cast
	 * 
	 * @param context
	 * @param type
	 * @return cast
	 * @throws AGBException
	 */
	public boolean canCast(AGBCompileContext context, AGBTypeNode type) throws AGBException {
		if (this.isNull() && (type.isNumber() || this.isBoolean())) {
			return false;
		} else if (this.isNull() && !type.isNumber() && !this.isBoolean()) {
			return true;
		} else if (type.isObject()) {
			return true;
		} else if (this.isNumber() && type.isNumber()) {
			return true;
		} else if (this.isNumberArray() && type.isNumberArray()) {
			return true;
		} else if (this.isString() && type.isString()) {
			return true;
		} else if (this.isBoolean() && type.isBoolean()) {
			return true;
		} else if (this.isByte() && type.isByte()) {
			return true;
		} else if (this.isNumber() && type.isByte()) {
			return true;
		} else if (this.isNumber() && type.isByte()) {
			return true;
		} else if (this.isByte() && type.isNumber()) {
			return true;
		} else if (this.isByteArray() && type.isByteArray()) {
			return true;
		} else if (this.isBooleanArray() && type.isBooleanArray()) {
			return true;
		} else if (this.isFunction() && type.isFunction()) {
			return this.equals(type);
		} else if ((this.isType() && !type.isType()) || (!this.isType() && type.isType())) {
			return false;
		} else if (this.isFunction() && type.isFunction()) {
			return this.equals(type);
		} else if (this.isType()) {
			AGBClassNode left = this.getRef(context);
			AGBClassNode right = type.getRef(context);
			while (left != null) {
				if (left == right) {
					return true;
				}
				left = left.getExtend();
			}
		}
		return false;
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * Retorna o tipo
	 * 
	 * @param context
	 * @return referncia
	 * @throws AGBException
	 */
	public AGBClassNode getRef(AGBCompileContext context) throws AGBException {
		if (classNode == null) {
			String name = this.token.getImage();
			if (this.token.getType() == AGBTokenType.THIS.getId()) {
				classNode = context.getClassStack().peek();
			} else if (this.isSuper()) {
				classNode = context.getClassStack().peek().getExtend();
			} else {
				classNode = context.getClassMap().get(name);
			}
			if (classNode == null) {
				throw new AGBTokenException("not found the type", this.token);
			}
		}
		return classNode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.token.getImage();
	}

	/**
	 * Indica se é a mesma classe
	 * 
	 * @param context
	 * @param type
	 * @return é a mesma classe
	 * @throws AGBException
	 */
	public boolean isSameClass(AGBCompileContext context, AGBTypeNode type) throws AGBException {
		if (!this.canCast(context, type)) {
			return false;
		}
		if (this.isType() && type.isType()) {
			return this.getRef(context) == type.getRef(context);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AGBTypeNode other = (AGBTypeNode) obj;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

}

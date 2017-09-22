package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementacao de uma node
 * 
 * @author bernardobreder
 */
public class AGBCallNode extends AGBRValueNode {

	/** Parameters */
	private final List<AGBRValueNode> params = new LightArrayList<AGBRValueNode>();

	/** Left Value */
	private AGBRValueNode left;

	/** Method to Call */
	private AGBMethodNode method;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBCallNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		this.getLeft().header(context);
		for (int n = 0; n < this.getParams().size(); n++) {
			this.getParams().get(n).header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		this.getLeft().body(context);
		for (int n = 0; n < this.getParams().size(); n++) {
			this.getParams().get(n).body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		this.getLeft().link(context);
		for (int n = 0; n < this.getParams().size(); n++) {
			AGBRValueNode node = this.getParams().get(n);
			node.link(context);
		}
		AGBTypeNode leftType = this.getLeft().getType();
		if (leftType.isPrimitive()) {
			throw new AGBTokenException("can not call method with primitive object", this.getToken());
		}
		if (leftType.isSuper()) {
			AGBClassNode classnode = context.getClassStack().peek().getExtend();
			while (this.method == null && classnode != null) {
				this.method = classnode.findMethod(context, this.getToken(), params, false);
				classnode = classnode.getExtend();
			}
			if (this.method == null) {
				throw new AGBTokenException("not found the method", this.getToken());
			}
		} else {
			AGBClassNode classnode = leftType.getRef(context);
			boolean isStatic = left.isStatic();
			while (classnode != null && this.method == null) {
				this.method = classnode.findMethod(context, this.getToken(), params, isStatic);
				classnode = classnode.getExtend();
			}
		}
		if (this.method == null) {
			StringBuilder sb = new StringBuilder();
			for (int n = 0; n < this.getParams().size(); n++) {
				sb.append(this.getParams().get(n).getType().getToken().getImage());
				if (n != this.getParams().size() - 1) {
					sb.append(", ");
				}
			}
			throw new AGBTokenException(String.format("not found '%s%s.%s(%s)'", this.isStatic() ? "static " : "", leftType.getRef(context).getName().getImage(), this.getToken().getImage(), sb.toString()), this.getToken());
		}
		{
			String name = this.method.getName().getImage();
			AGBClassNode classname = this.getLeft().getType().getRef(context);
			while (classname != null) {
				if (name.equals(classname.getName().getImage())) {
					throw new AGBTokenException("can not call a construtor", this.getToken());
				}
				classname = classname.getExtend();
			}
		}
		checkThrow(context, this, this.method);
		if (!this.isStatic() && this.method.getType(context).isThis()) {
			if (this.getLeft().getType().isSuper()) {
				this.setType(context.getClassStack().peek().getType());
			} else {
				this.setType(this.getLeft().getType());
			}
		} else {
			this.setType(this.method.getType(context));
		}
	}

	/**
	 * Verifica se o método corrente possui um Try ou Throws para o erro do
	 * método
	 * 
	 * @param context
	 * @param node
	 * @param method
	 * @throws AGBException
	 */
	public static void checkThrow(AGBCompileContext context, AGBRValueNode node, AGBMethodNode method) throws AGBException {
		List<AGBTypeNode> throwsTypes = method.getThrowsTypes();
		if (throwsTypes.size() > 0) {
			for (int n = 0; n < throwsTypes.size(); n++) {
				AGBTypeNode throwType = throwsTypes.get(n);
				AGBThrowClassNode ref = (AGBThrowClassNode) throwType.getRef(context);
				if (!ref.isRuntime()) {
					checkThrow(context, node, throwType);
				}
			}
		}
	}

	/**
	 * @param context
	 * @param node
	 * @param throwType
	 * @throws AGBException
	 */
	private static void checkThrow(AGBCompileContext context, AGBRValueNode node, AGBTypeNode throwType) throws AGBException {
		List<AGBTypeNode> throwsCurrentTypes = context.getMethodStack().peek().getThrowsTypes();
		for (int n = 0; n < throwsCurrentTypes.size(); n++) {
			if (throwType.canCast(context, throwsCurrentTypes.get(n))) {
				return;
			}
		}
		AGBNode parent = node.getParent();
		while (parent != null) {
			if (parent instanceof AGBTryNode) {
				AGBTryNode tryNode = (AGBTryNode) parent;
				int msize = tryNode.getCatchs().size();
				for (int m = 0; m < msize; m++) {
					AGBCatchNode catchNode = tryNode.getCatchs().get(m);
					if (throwType.canCast(context, catchNode.getType())) {
						return;
					}
				}
			}
			parent = parent.getParent();
		}
		throw new AGBTokenException(String.format("current method must throw '%s'", throwType.getToken().getImage()), node.getToken());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int counter = output.getCounter();
		if (this.getLeft().getType().isSuper()) {
			for (AGBValueNode param : this.getParams()) {
				param.build(output);
			}
			this.getLeft().build(output);
			output.opJumpStaticCall(this.method.getIndex(), this.method.getParams().size());
		} else {
			if (this.method.isStatic()) {
				if (this.getLeft() instanceof AGBRIdentifyNode == false) {
					this.getLeft().build(output);
					output.opStackPop(1);
				}
				for (AGBValueNode param : this.getParams()) {
					param.build(output);
				}
				output.opJumpStaticCall(this.method.getIndex(), this.method.getParams().size());
			} else {
				for (AGBValueNode param : this.getParams()) {
					param.build(output);
				}
				this.getLeft().build(output);
				output.opJumpCall(this.method.getIndex(), this.method.getParams().size());
			}
		}
		output.setCounter(counter + 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStatic() {
		if (this.getLeft().isStatic()) {
			if (this.method == null) {
				return false;
			}
			return this.method.getReturnType().isThis();
		}
		return false;
	}

	/**
	 * Retorna os parametros
	 * 
	 * @return parametros
	 */
	public List<AGBRValueNode> getParams() {
		return params;
	}

	/**
	 * Retorna o left
	 * 
	 * @return left
	 */
	public AGBRValueNode getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(AGBRValueNode left) {
		this.left = left;
		this.left.setParent(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getLeft());
		sb.append(".");
		sb.append(this.getToken());
		sb.append(this.getParams());
		return sb.toString();
	}

}

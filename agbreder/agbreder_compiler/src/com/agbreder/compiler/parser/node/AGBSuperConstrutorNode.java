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
public class AGBSuperConstrutorNode extends AGBCommandNode {

	/** Token */
	private AGBToken token;

	/** Parametros */
	private final List<AGBRValueNode> params = new LightArrayList<AGBRValueNode>();

	private AGBMethodNode method;

	private int index;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param token
	 * @param params
	 */
	public AGBSuperConstrutorNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		AGBMethodNode methodnode = context.getMethodStack().peek();
		if (!methodnode.isConstrutorAtt(context)) {
			throw new AGBTokenException("super command only for construtor", this.getToken());
		}
		if (methodnode.getBlock().getCommands().get(0) != this && (methodnode.getBlock().getCommands().get(0) instanceof AGBBlockNode && ((AGBBlockNode) methodnode.getBlock().getCommands().get(0)).getCommands().get(0) != this)) {
			throw new AGBTokenException("super command must be the first", this.getToken());
		}
		List<AGBRValueNode> params = this.getParams();
		int size = params.size();
		for (int n = 0; n < size; n++) {
			AGBRValueNode param = params.get(n);
			param.header(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		List<AGBRValueNode> params = this.getParams();
		int size = params.size();
		for (int n = 0; n < size; n++) {
			AGBRValueNode param = params.get(n);
			param.body(context);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		List<AGBRValueNode> params = this.getParams();
		int size = params.size();
		for (int n = 0; n < size; n++) {
			AGBRValueNode param = params.get(n);
			param.link(context);
		}
		{
			AGBClassNode classnode = context.getClassStack().peek().getExtend();
			while (classnode != null && this.method == null) {
				this.method = classnode.findMethod(context, classnode.getName(), this.getParams(), false);
				classnode = classnode.getExtend();
			}
			if (this.method == null) {
				throw new AGBTokenException("can not found the constructor", this.getToken());
			}
		}
		{
			AGBMethodNode methodnode = context.getMethodStack().peek();
			this.index = methodnode.getVariables().size();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int counter = output.getCounter();
		List<AGBRValueNode> params = this.getParams();
		int size = params.size();
		for (int n = 0; n < size; n++) {
			AGBRValueNode param = params.get(n);
			param.build(output);
		}
		{
			output.opStackLoad(this.index);
		}
		output.opJumpStaticCall(this.method.getIndex(), this.method.getParams().size());
		output.opStackPop(1);
		output.setCounter(counter);
	}

	/**
	 * @return the params
	 */
	public List<AGBRValueNode> getParams() {
		return params;
	}

	/**
	 * @return the token
	 */
	public AGBToken getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(AGBToken token) {
		this.token = token;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("super");
		sb.append(" ");
		sb.append(this.getParams().toString());
		return sb.toString();
	}

}

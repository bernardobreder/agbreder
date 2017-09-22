package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;
import com.agbreder.compiler.util.LightArrayList;

/**
 * Implementacao de uma node
 * 
 * @author bernardobreder
 */
public class AGBMethodNode extends AGBCommandNode {

	/** Indice */
	private Integer index;

	/** Nome */
	private AGBToken name;

	/** Retorno do método */
	private AGBTypeNode returnType;

	/** Parametros do metodo */
	private final List<AGBVariableNode> params = new LightArrayList<AGBVariableNode>();

	/** Bloco do metodo */
	private final AGBBlockNode block = new AGBBlockNode(this);

	/** Variáveis */
	private final List<AGBVariableNode> variables = new ArrayList<AGBVariableNode>();

	/** Variáveis */
	private final List<AGBTypeNode> throwsTypes = new LightArrayList<AGBTypeNode>();

	/** Indica se é estático */
	private boolean staticAtt;

	/** Indica se é construtor */
	private Boolean isConstructor;

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBMethodNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.pushMethod(this);
		{
			this.isConstrutorAtt(context);
			{
				this.getType(context).header(context);
			}
			{
				List<AGBVariableNode> params = this.getParams();
				int size = params.size();
				for (int n = 0; n < size; n++) {
					params.get(n).header(context);
				}
			}
			{
				List<AGBTypeNode> throwTypes = this.getThrowsTypes();
				int size = throwTypes.size();
				for (int n = 0; n < size; n++) {
					throwTypes.get(n).header(context);
				}
			}
			{
				this.getBlock().header(context);
			}
			if (!this.getBlock().isReturned()) {
				throw new AGBTokenException("not returning for all ways", this.getName());
			}
		}
		context.popMethod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.pushMethod(this);
		{
			this.getReturnType().body(context);
		}
		{
			List<AGBVariableNode> params = this.getParams();
			int size = params.size();
			for (int n = 0; n < size; n++) {
				params.get(n).body(context);
			}
		}
		{
			List<AGBTypeNode> throwTypes = this.getThrowsTypes();
			int size = throwTypes.size();
			for (int n = 0; n < size; n++) {
				throwTypes.get(n).body(context);
			}
		}
		this.getBlock().body(context);
		this.checkConstructorSuperFirst(context);
		this.checkTypeThrow(context);
		context.popMethod();
	}

	/**
	 * Verifica os tipos de Throws. Todos os tipos devem ser não Runtime
	 * Exception
	 * 
	 * @param context
	 */
	private void checkTypeThrow(AGBCompileContext context) {
	}

	/**
	 * Verifica se o método for construtor se possui o comando super na primeira
	 * linha
	 * 
	 * @param context
	 * @throws AGBTokenException
	 */
	private void checkConstructorSuperFirst(AGBCompileContext context) throws AGBTokenException {
		if (this.isConstrutorAtt(context)) {
			AGBClassNode classnode = context.getClassStack().peek();
			if (classnode.getExtend() != null) {
				if (classnode.getExtend().hasConstrutor(context)) {
					if (this.getBlock().getCommands().get(0) instanceof AGBSuperConstrutorNode == false && (this.getBlock().getCommands().get(0) instanceof AGBBlockNode && ((AGBBlockNode) this.getBlock().getCommands().get(0)).getCommands().get(0) instanceof AGBSuperConstrutorNode == false)) {
						throw new AGBTokenException("constructor must have a super command", this.getName());
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.pushMethod(this);
		this.getReturnType().link(context);
		if (this.isConstrutorAtt(context)) {
			if (!this.getReturnType().isThis()) {
				throw new AGBTokenException("constructor must return this", this.getReturnType().getToken());
			}
		}
		{
			int size = this.getParams().size();
			for (int n = 0; n < size; n++) {
				this.getParams().get(n).link(context);
			}
		}
		{
			List<AGBTypeNode> throwTypes = this.getThrowsTypes();
			int size = throwTypes.size();
			for (int n = 0; n < size; n++) {
				throwTypes.get(n).link(context);
			}
		}
		this.getBlock().link(context);
		this.checkOverride(context);
		context.popMethod();
	}

	/**
	 * @param context
	 * @throws AGBException
	 * @throws AGBTokenException
	 */
	private void checkOverride(AGBCompileContext context) throws AGBException, AGBTokenException {
		AGBClassNode classnode = context.getClassStack().peek().getExtend();
		while (classnode != null) {
			for (AGBMethodNode method : classnode.getMethods()) {
				if (!method.isStatic()) {
					if (method.getNameParameters().equals(this.getNameParameters())) {
						checkOverride(context, method);
						return;
					}
				}
			}
			classnode = classnode.getExtend();
		}
	}

	/**
	 * @param context
	 * @param method
	 * @throws AGBException
	 * @throws AGBTokenException
	 */
	private void checkOverride(AGBCompileContext context, AGBMethodNode method) throws AGBException, AGBTokenException {
		if (!this.getType(context).canCast(context, method.getType(context))) {
			throw new AGBTokenException("override changing the header method", this.getType(context).getToken());
		}
		if (this.getThrowsTypes().size() != method.getThrowsTypes().size()) {
			throw new AGBTokenException("number of throws must be the same of the override method", this.getName());
		}
		for (AGBTypeNode throwType : method.getThrowsTypes()) {
			if (!this.getThrowsTypes().contains(throwType)) {
				throw new AGBTokenException("can not found the exception '%s'", throwType.getToken());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int varSize = this.getVariables().size();
		output.opStackPush(varSize);
		output.setCounter(0);
		this.getBlock().build(output);
		output.opStackPop(varSize);
	}

	/**
	 * @return the token
	 */
	public AGBToken getName() {
		return name;
	}

	/**
	 * Retorna o nome completo
	 * 
	 * @return nome completo
	 */
	public String getNameParameters() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getName().getImage());
		sb.append("(");
		List<AGBVariableNode> params = this.getParams();
		int size = params.size();
		for (int n = 0; n < size; n++) {
			AGBVariableNode paramnode = params.get(n);
			String typename = paramnode.getType().getToken().getImage();
			sb.append(typename);
			if (n != size - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @param context
	 * @return the type
	 */
	public AGBTypeNode getType(AGBCompileContext context) {
		return returnType;
	}

	/**
	 * @return the params
	 */
	public List<AGBVariableNode> getParams() {
		return params;
	}

	/**
	 * @param cmd
	 *            the cmd to set
	 */
	public void addCommand(AGBCommandNode cmd) {
		AGBBlockNode blocks = this.getBlock();
		blocks.getCommands().add(cmd);
		cmd.setParent(blocks);
	}

	/**
	 * @param var
	 */
	public void addVariable(AGBVariableNode var) {
		var.setIndex(this.getVariables().size());
		this.getVariables().add(var);
	}

	/**
	 * @return the variables
	 */
	public List<AGBVariableNode> getVariables() {
		return variables;
	}

	/**
	 * @return the variables
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Indica se é estatico ou não
	 * 
	 * @return estatico ou não
	 */
	public boolean isStatic() {
		return staticAtt;
	}

	/**
	 * Indica se é estatico ou não
	 * 
	 * @param context
	 * @return estatico ou não
	 */
	public boolean isConstrutorAtt(AGBCompileContext context) {
		if (this.isConstructor == null) {
			this.isConstructor = !this.isStatic() && this.getName().getImage().equals(context.getClassStack().peek().getName().getImage());
		}
		return this.isConstructor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getNameParameters() + " " + this.getBlock().toString();
	}

	/**
	 * @return the cmd
	 */
	public AGBBlockNode getBlock() {
		return block;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Retorna o tipo
	 * 
	 * @return tipo
	 */
	public AGBTypeNode getReturnType() {
		return returnType;
	}

	/**
	 * Altera o tipo
	 * 
	 * @param type
	 */
	public void setReturnType(AGBTypeNode type) {
		this.returnType = type;
	}

	/**
	 * @return the throwsTypes
	 */
	public List<AGBTypeNode> getThrowsTypes() {
		return throwsTypes;
	}

	/**
	 * @return the staticAtt
	 */
	public boolean isStaticAtt() {
		return staticAtt;
	}

	/**
	 * @param staticAtt
	 *            the staticAtt to set
	 */
	public void setStaticAtt(boolean staticAtt) {
		this.staticAtt = staticAtt;
	}

	/**
	 * @return the isConstructor
	 */
	public Boolean getIsConstructor() {
		return isConstructor;
	}

	/**
	 * @param isConstructor
	 *            the isConstructor to set
	 */
	public void setIsConstructor(Boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(AGBToken name) {
		this.name = name;
	}

}

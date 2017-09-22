package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;
import com.agbreder.compiler.parser.node.type.AGBClassTypeNode;
import com.agbreder.compiler.parser.node.type.AGBTypeNode;
import com.agbreder.compiler.token.AGBToken;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBClassNode extends AGBCommandNode {

	private AGBToken name;

	private final List<AGBFieldNode> fields = new ArrayList<AGBFieldNode>();

	private final List<AGBFieldNode> allFields = new ArrayList<AGBFieldNode>();

	private final List<AGBMethodNode> methods = new ArrayList<AGBMethodNode>();

	private Map<String, AGBMethodNode> mapMethods = new HashMap<String, AGBMethodNode>();

	private final List<AGBNode> commands = new ArrayList<AGBNode>();

	private AGBToken extendToken;

	private Integer index;

	private AGBTypeNode type;

	private AGBClassNode extend;

	/**
	 * Construtor
	 * 
	 * @param parent
	 * @param name
	 * @param commands
	 * @param extendToken
	 */
	public AGBClassNode(AGBCommandNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.pushClass(this);
		int size = this.getCommands().size();
		for (int n = 0; n < size; n++) {
			this.getCommands().get(n).header(context);
		}
		this.checkFieldMethodSameName();
		// Atribui a herança
		{
			if (this.getExtendToken() != null) {
				this.setExtend(context.getClassMap().get(this.getExtendToken().getImage()));
				if (this.getExtend() == null) {
					throw new AGBTokenException.AGBReferenceTypeTokenException(this.getExtendToken());
				}
			}
		}
		context.popClass();
	}

	/**
	 * Verifica se existe Campos e Metodos com a mesma Assinatura
	 * 
	 * @throws AGBException
	 */
	private void checkFieldMethodSameName() throws AGBException {
		{
			Set<String> set = new HashSet<String>();
			for (int n = 0; n < this.fields.size(); n++) {
				AGBFieldNode node = this.fields.get(n);
				String name = node.getName().getImage();
				if (set.contains(name)) {
					throw new AGBTokenException.AGBDuplicateFieldTokenException(node.getName());
				}
				set.add(name);
			}
			set.clear();
			for (int n = 0; n < this.methods.size(); n++) {
				AGBMethodNode node = this.methods.get(n);
				if (!node.getName().getImage().equals("function")) {
					String name = node.getNameParameters();
					if (node.isStatic()) {
						name = "(S)" + name;
					}
					if (set.contains(name)) {
						throw new AGBTokenException.AGBDuplicateMethodTokenException(node.getName());
					}
					set.add(name);
				}
			}
		}
	}

	/**
	 * @param fieldnode
	 */
	public void addField(AGBFieldNode fieldnode) {
		this.fields.add(fieldnode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.pushClass(this);
		{
			int size = this.getCommands().size();
			for (int n = 0; n < size; n++) {
				this.getCommands().get(n).body(context);
			}
		}
		checkRecursiveExtend();
		{
			AGBClassNode classnode = this;
			while (classnode != null) {
				List<AGBFieldNode> fields = classnode.getFields();
				int size = fields.size();
				for (int n = size - 1; n >= 0; n--) {
					this.getAllFields().add(0, fields.get(n));
				}
				classnode = classnode.getExtend();
			}
		}
		// Verifica se existe um construtor que irá chamar o construtor super
		{
			AGBClassNode extendnode = this.getExtend();
			if (extendnode != null) {
				List<AGBMethodNode> methods = this.findMethod(this.getName().getImage(), false);
				if (methods.size() == 0) {
					List<AGBMethodNode> extendMethods = extendnode.findMethod(extendnode.getName().getImage(), false);
					for (AGBMethodNode extendMethod : extendMethods) {
						if (extendMethod.getParams().size() > 0) {
							throw new AGBTokenException("not declare the construtor", this.getName());
						}
					}
				}
			}
		}
		context.popClass();
	}

	/**
	 * @throws AGBTokenException
	 */
	private void checkRecursiveExtend() throws AGBTokenException {
		AGBToken token = this.getName();
		if (token != null) {
			AGBClassNode classnode = this.getExtend();
			while (classnode != null) {
				if (classnode.getName().equals(token)) {
					throw new AGBTokenException("recursive extend class", this.getExtendToken());
				}
				classnode = classnode.getExtend();
			}
		}
	}

	/**
	 * Busca por um método
	 * 
	 * @param name
	 * @param isStatic
	 * @return busca
	 */
	public List<AGBMethodNode> findMethod(String name, boolean isStatic) {
		List<AGBMethodNode> list = new ArrayList<AGBMethodNode>();
		List<AGBMethodNode> methods = this.getMethods();
		int size = methods.size();
		for (int n = 0; n < size; n++) {
			AGBMethodNode method = methods.get(n);
			if (method.isStatic() == isStatic) {
				if (method.getName().getImage().equals(name)) {
					list.add(method);
				}
			}
		}
		return list;
	}

	/**
	 * Busca por um método
	 * 
	 * @param name
	 * @param params
	 * @param context
	 * @param isStatic
	 * @return busca
	 * @throws AGBException
	 */
	public AGBMethodNode findMethod(AGBCompileContext context, AGBToken name, List<AGBRValueNode> params, boolean isStatic) throws AGBException {
		List<AGBMethodNode> methods = this.findMethod(name.getImage(), isStatic);
		List<AGBMethodNode> founds = new ArrayList<AGBMethodNode>(methods.size());
		for (int n = 0; n < methods.size(); n++) {
			AGBMethodNode method = methods.get(n);
			List<AGBVariableNode> mparams = method.getParams();
			boolean found = true;
			if (mparams.size() == params.size()) {
				for (int p = 0; p < mparams.size(); p++) {
					if (!params.get(p).getType().canCast(context, mparams.get(p).getType())) {
						found = false;
						break;
					}
				}
				if (found) {
					founds.add(method);
				}
			}
		}
		if (founds.size() > 1) {
			for (AGBMethodNode method : founds) {
				boolean found = true;
				for (int n = 0; n < params.size(); n++) {
					AGBVariableNode param = method.getParams().get(n);
					if (!param.getType().isSameClass(context, params.get(n).getType())) {
						found = false;
						break;
					}
				}
				if (found) {
					founds = Arrays.asList(method);
					break;
				}
			}
		}
		if (founds.size() > 1) {
			throw new AGBTokenException("ambiguity with the method", name);
		}
		if (founds.size() == 0) {
			return null;
		}
		return founds.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.pushClass(this);
		int size = this.getCommands().size();
		for (int n = 0; n < size; n++) {
			this.getCommands().get(n).link(context);
		}
		context.popClass();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException {
	}

	/**
	 * Adiciona um método
	 * 
	 * @param method
	 */
	public void addMethod(AGBMethodNode method) {
		this.methods.add(method);
		this.mapMethods.put(method.getNameParameters(), method);
	}

	/**
	 * @return the name
	 */
	public AGBToken getName() {
		return name;
	}

	/**
	 * @return the cmds
	 */
	public List<AGBNode> getCommands() {
		return commands;
	}

	/**
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Retorna todos os campos
	 * 
	 * @return campos
	 */
	public List<AGBFieldNode> getFields() {
		return fields;
	}

	/**
	 * Retorna todos os metodos
	 * 
	 * @return metodos
	 */
	public List<AGBMethodNode> getMethods() {
		return methods;
	}

	/**
	 * @return the extend
	 */
	public AGBClassNode getExtend() {
		return extend;
	}

	/**
	 * Retorna o tipo
	 * 
	 * @return tipo
	 */
	public AGBTypeNode getType() {
		if (this.type == null) {
			this.type = new AGBClassTypeNode(this.getName());
		}
		return this.type;
	}

	/**
	 * @return the allfields
	 */
	public List<AGBFieldNode> getAllFields() {
		return allFields;
	}

	/**
	 * Retorna o token de Extend
	 * 
	 * @return token
	 */
	public AGBToken getExtendToken() {
		return extendToken;
	}

	/**
	 * @param extend
	 *            the extend to set
	 */
	public void setExtend(AGBClassNode extend) {
		this.extend = extend;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.getName().getImage() + " " + this.getCommands().toString();
	}

	/**
	 * Indica se tem construtor
	 * 
	 * @param context
	 * @return se tem construtor
	 */
	public boolean hasConstrutor(AGBCompileContext context) {
		for (int n = 0; n < this.getMethods().size(); n++) {
			AGBMethodNode method = this.getMethods().get(n);
			if (method.isConstrutorAtt(context)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(AGBToken name) {
		this.name = name;
	}

	/**
	 * @param extendToken
	 *            the extendToken to set
	 */
	public void setExtendToken(AGBToken extendToken) {
		this.extendToken = extendToken;
	}

	/**
	 * Indica se é um classe do tipo Throw
	 * 
	 * @return um classe do tipo Throw
	 */
	public boolean isThrow() {
		return false;
	}

}

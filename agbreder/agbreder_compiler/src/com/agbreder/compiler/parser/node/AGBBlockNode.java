package com.agbreder.compiler.parser.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.agbreder.compiler.exception.AGBException;
import com.agbreder.compiler.link.OpcodeOutputStream;
import com.agbreder.compiler.parser.AGBCompileContext;

/**
 * Implementação de uma node
 * 
 * @author bernardobreder
 */
public class AGBBlockNode extends AGBCommandNode {

	private final List<AGBCommandNode> commands = new ArrayList<AGBCommandNode>();

	private final List<AGBVariableNode> variables = new ArrayList<AGBVariableNode>();

	/**
	 * Construtor
	 * 
	 * @param parent
	 */
	public AGBBlockNode(AGBNode parent) {
		super(parent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void header(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this);
		for (AGBNode node : this.commands) {
			node.header(context);
		}
		for (AGBVariableNode node : this.variables) {
			node.header(context);
		}
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void body(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this);
		for (AGBNode node : this.commands) {
			node.body(context);
		}
		for (AGBVariableNode node : this.variables) {
			node.body(context);
		}
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void link(AGBCompileContext context) throws AGBException {
		context.getBlockStack().push(this);
		for (AGBNode node : this.commands) {
			node.link(context);
		}
		for (AGBVariableNode node : this.variables) {
			node.link(context);
		}
		context.getBlockStack().pop();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void build(OpcodeOutputStream output) throws IOException, AGBException {
		int counter = output.getCounter();
		for (int n = 0; n < this.commands.size(); n++) {
			AGBCommandNode commandNode = this.commands.get(n);
			output.setCounter(counter);
			commandNode.build(output);
		}
		output.setCounter(counter);
	}

	/**
	 * Busca por uma variável
	 * 
	 * @param name
	 * @param deep
	 * @return variavel ou null
	 */
	public AGBVariableNode findVariable(String name, boolean deep) {
		List<AGBVariableNode> vars = this.getVariables();
		int size = vars.size();
		for (int n = size - 1; n >= 0; n--) {
			AGBVariableNode node = vars.get(n);
			if (node.isDeclared()) {
				if (node.getToken().getImage().equals(name)) {
					return node;
				}
			}
		}
		AGBNode parent = this.getParent();
		while (deep) {
			if (parent == null) {
				return null;
			}
			if (parent instanceof AGBMethodNode) {
				return null;
			}
			if (parent instanceof AGBBlockNode) {
				return ((AGBBlockNode) parent).findVariable(name, deep);
			}
			parent = parent.getParent();
		}
		return null;
	}

	@Override
	public boolean isReturned() {
		for (AGBCommandNode command : this.getCommands()) {
			if (command.isReturned()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the cmds
	 */
	public List<AGBCommandNode> getCommands() {
		return commands;
	}

	/**
	 * @return the variables
	 */
	public List<AGBVariableNode> getVariables() {
		return variables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("do");
		sb.append(" ");
		sb.append(this.getCommands());
		sb.append(" ");
		sb.append("end");
		return sb.toString();
	}

}

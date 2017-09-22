package com.agbreder.compiler.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agbreder.compiler.exception.AGBTokenException;
import com.agbreder.compiler.parser.node.AGBBlockNode;
import com.agbreder.compiler.parser.node.AGBClassNode;
import com.agbreder.compiler.parser.node.AGBMethodNode;
import com.agbreder.compiler.util.ArrayStack;

/**
 * Contexto de execução do compilador
 * 
 * @author bernardobreder
 */
public class AGBCompileContext {

	private final List<String> dependences = new ArrayList<String>();

	private final Map<String, Integer> stringIndex = new HashMap<String, Integer>();

	private final List<String> strings = new ArrayList<String>();

	private final Map<String, Integer> numberIndex = new HashMap<String, Integer>();

	private final List<String> numbers = new ArrayList<String>();

	private final ArrayStack<AGBClassNode> classStack = new ArrayStack<AGBClassNode>();

	private final List<AGBClassNode> classes = new ArrayList<AGBClassNode>();

	private final ArrayStack<AGBMethodNode> methodStack = new ArrayStack<AGBMethodNode>();

	private final List<AGBMethodNode> methods = new ArrayList<AGBMethodNode>();

	private final ArrayStack<AGBBlockNode> blockStack = new ArrayStack<AGBBlockNode>();

	private final Map<String, AGBClassNode> classMap = new HashMap<String, AGBClassNode>();

	/**
	 * Adiciona uma string no pool
	 * 
	 * @param value
	 * @return indice
	 */
	public int addString(String value) {
		Integer index = this.stringIndex.get(value);
		if (index == null) {
			this.stringIndex.put(value, index = this.strings.size());
			this.strings.add(value);
		}
		return index;
	}

	/**
	 * Adiciona uma string no pool
	 * 
	 * @param value
	 * @return indice
	 */
	public int addNumber(String value) {
		Integer index = this.numberIndex.get(value);
		if (index == null) {
			this.numberIndex.put(value, index = this.numbers.size());
			this.numbers.add(value);
		}
		return index;
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 * @return indice da classe
	 * @throws AGBTokenException
	 */
	public int addClass(AGBClassNode node) throws AGBTokenException {
		int index = this.classes.size();
		this.classes.add(node);
		AGBClassNode oldValue = this.classMap.put(node.getName().getImage(), node);
		if (oldValue != null) {
			throw new AGBTokenException("Duplicate Class", node.getName());
		}
		return index;
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 * @return indice do metodo
	 */
	public int addMethod(AGBMethodNode node) {
		int index = this.methods.size();
		this.methods.add(node);
		return index;
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 */
	public void pushClass(AGBClassNode node) {
		this.classStack.push(node);
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 */
	public void popClass() {
		this.classStack.pop();
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 */
	public void pushMethod(AGBMethodNode node) {
		this.methodStack.push(node);
	}

	/**
	 * Adiciona uma classe
	 * 
	 * @param node
	 */
	public void popMethod() {
		this.methodStack.pop();
	}

	/**
	 * @return the stringIndex
	 */
	public Map<String, Integer> getStringIndex() {
		return stringIndex;
	}

	/**
	 * @return the numberIndex
	 */
	public Map<String, Integer> getNumberIndex() {
		return numberIndex;
	}

	/**
	 * @return the classStack
	 */
	public ArrayStack<AGBClassNode> getClassStack() {
		return classStack;
	}

	/**
	 * @return the classes
	 */
	public List<AGBClassNode> getClasses() {
		return classes;
	}

	/**
	 * @return the strings
	 */
	public List<String> getStrings() {
		return strings;
	}

	/**
	 * @return the numbers
	 */
	public List<String> getNumbers() {
		return numbers;
	}

	/**
	 * @return the methodStack
	 */
	public ArrayStack<AGBMethodNode> getMethodStack() {
		return methodStack;
	}

	/**
	 * @return the methods
	 */
	public List<AGBMethodNode> getMethods() {
		return methods;
	}

	/**
	 * @return the blockStack
	 */
	public ArrayStack<AGBBlockNode> getBlockStack() {
		return blockStack;
	}

	/**
	 * @return the classMap
	 */
	public Map<String, AGBClassNode> getClassMap() {
		return classMap;
	}

	/**
	 * @return the dependences
	 */
	public List<String> getDependences() {
		return dependences;
	}

}

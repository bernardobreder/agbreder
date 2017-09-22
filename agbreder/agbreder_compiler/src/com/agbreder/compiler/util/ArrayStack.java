package com.agbreder.compiler.util;

import java.util.ArrayList;

/**
 * Pilha implementada com array
 * 
 * @author bernardobreder
 * @param <E>
 */
public class ArrayStack<E> extends ArrayList<E> {

	/**
	 * Adiciona um elemento na pilha
	 * 
	 * @param value
	 */
	public void push(E value) {
		this.add(value);
	}

	/**
	 * Recupera o elemento da pilha
	 * 
	 * @return elemento da pilha
	 */
	public E peek() {
		return this.get(this.size() - 1);
	}

	/**
	 * Desempilha
	 * 
	 * @return elemento do topo
	 */
	public E pop() {
		return this.remove(this.size() - 1);
	}

}

package com.agbreder.compiler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uma lista que se comporta como um conjunto
 * 
 * @author bernardobreder
 * 
 * @param <E>
 */
public class ListSet<E> {

	/** Indices dos elementos */
	private Map<E, Integer> indexs = new HashMap<E, Integer>();
	/** Elementos */
	private List<E> list = new ArrayList<E>();

	/**
	 * Adiciona um elemento
	 * 
	 * @param element
	 * @return indice do elemento
	 */
	public int add(E element) {
		Integer index = this.indexs.get(element);
		if (index == null) {
			index = this.list.size();
			this.indexs.put(element, index);
		}
		return index;
	}

	/**
	 * Número de elementos
	 * 
	 * @return
	 */
	public int size() {
		return this.list.size();
	}

	/**
	 * Retorna um elemento
	 * 
	 * @param index
	 * @return
	 */
	public E get(int index) {
		return this.list.get(index);
	}

	/**
	 * Altera um elemento
	 * 
	 * @param index
	 * @param element
	 */
	public void set(int index, E element) {
		this.list.set(index, element);
	}

}

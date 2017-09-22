package com.agbreder.service.gui;

import java.awt.Component;
import java.util.List;

import org.dom4j.Element;

/**
 * Estrutura mais básica que representa um nó de um arquivo xml
 * 
 * @author bernardobreder
 */
public abstract class AGBNode implements Cloneable {
	
	/**
	 * Carrega as propriedades do nó através do elemento
	 * 
	 * @param element
	 */
	public abstract void load(Element element);
	
	/**
	 * Realiza o pre-processamento no lado do servidor
	 * 
	 * @param context
	 * @return nós para ser substituido
	 */
	public List<AGBNode> process(AGBContextNode context){
		return null;
	}
	
	/**
	 * Constroi o componente swing
	 * 
	 * @return componente swing
	 */
	public abstract Component build();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
}

package com.agbreder.service.gui;

import java.awt.Component;
import java.util.List;

import org.dom4j.Element;

/**
 * Estrutura que representa o cabeçalho da página
 * 
 * @author bernardobreder
 */
public class AGBBodyNode extends AGBSetNode {
	
	private AGBComponentNode node;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Element element) {
		List<Element> elements = element.elements();
		if (elements.size() > 0) {
			Element child = elements.get(0);
			this.node = AGBTypeNode.build(child.getName());
			this.node.load(child);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component build() {
		return this.node.build();
	}
	
}

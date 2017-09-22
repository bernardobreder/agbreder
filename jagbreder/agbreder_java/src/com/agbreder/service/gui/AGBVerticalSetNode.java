package com.agbreder.service.gui;

import java.awt.Component;

import javax.swing.JPanel;

import org.dom4j.Element;

/**
 * Estrutura que representa o cabeçalho da página
 * 
 * @author bernardobreder
 */
public class AGBVerticalSetNode extends AGBSetNode {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Element element) {
		for (Element childElem : element.elements()) {
			AGBComponentNode node = AGBTypeNode.build(childElem.getName());
			node.load(childElem);
			this.getChildren().add(node);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component build() {
		JPanel panel = new JPanel(new VerticalLayout());
		panel.setOpaque(false);
		for (AGBComponentNode component : this.getChildren()) {
			panel.add(component.build());
		}
		return panel;
	}
	
}

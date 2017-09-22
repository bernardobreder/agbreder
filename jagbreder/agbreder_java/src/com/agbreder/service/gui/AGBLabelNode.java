package com.agbreder.service.gui;

import java.awt.Component;

import javax.swing.JLabel;

import org.dom4j.Element;

/**
 * Estrutura que representa o cabeçalho da página
 * 
 * @author bernardobreder
 */
public class AGBLabelNode extends AGBComponentNode {
	
	private String text;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Element element) {
		this.text = element.getTextTrim();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component build() {
		JLabel c = new JLabel();
		c.setOpaque(false);
		c.setText(text);
		return c;
	}
	
}

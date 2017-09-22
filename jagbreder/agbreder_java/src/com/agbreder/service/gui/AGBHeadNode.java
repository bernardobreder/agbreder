package com.agbreder.service.gui;

import java.awt.Component;

import org.dom4j.Element;

/**
 * Estrutura que representa o cabeçalho da página
 * 
 * @author bernardobreder
 */
public class AGBHeadNode extends AGBNode {
	
	private String title;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Element element) {
		{
			Element titleElem = element.element("title");
			if (titleElem != null) {
				this.title = titleElem.getTextTrim();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component build() {
		return null;
	}
	
}

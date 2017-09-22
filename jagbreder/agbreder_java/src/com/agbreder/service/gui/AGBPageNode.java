package com.agbreder.service.gui;

import java.awt.Component;
import java.util.List;

import org.dom4j.Element;

/**
 * Estrutura que representa uma página
 * 
 * @author bernardobreder
 */
public class AGBPageNode extends AGBNode {
	
	private AGBHeadNode head;
	
	private AGBBodyNode body;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void load(Element element) {
		{
			Element headElem = element.element("head");
			this.head = new AGBHeadNode();
			this.head.load(headElem);
		}
		{
			Element bodyElem = element.element("body");
			this.body = new AGBBodyNode();
			body.load(bodyElem);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AGBNode> process(AGBContextNode context) {
		this.head.process(context);
		this.body.process(context);
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component build() {
		return this.body.build();
	}
	
}

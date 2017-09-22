package com.agbreder.service.gui;

/**
 * Enumero os tipos de nó
 * 
 * @author bernardobreder
 */
public enum AGBTypeNode {
	
	XAGB(new AGBPageNode()),
	
	HEAD(new AGBHeadNode()),
	
	BODY(new AGBBodyNode()),
	
	LABEL(new AGBLabelNode()),
	
	VSET(new AGBVerticalSetNode()),
	
	;
	
	private final AGBNode node;
	
	/**
	 * Construtor
	 */
	private AGBTypeNode(AGBNode node) {
		this.node = node;
	}
	
	public static <E extends AGBNode> E build(String name) {
		return AGBTypeNode.valueOf(name.toUpperCase()).build();
	}
	
	@SuppressWarnings("unchecked")
	public <E extends AGBNode> E build() {
		return (E) this.node.clone();
	}
	
}

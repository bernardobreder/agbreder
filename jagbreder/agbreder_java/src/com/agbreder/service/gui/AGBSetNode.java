package com.agbreder.service.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Estrutura que representa o cabeçalho da página
 * 
 * @author bernardobreder
 */
public abstract class AGBSetNode extends AGBComponentNode {
	
	/** Armazena os filhos */
	private List<AGBComponentNode> children;;
	
	/**
	 * @return the children
	 */
	public List<AGBComponentNode> getChildren() {
		if (this.children == null) {
			this.children = new ArrayList<AGBComponentNode>();
		}
		return children;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AGBNode> process(AGBContextNode context) {
		List<AGBNode> list = null;
		List<AGBComponentNode> children = this.getChildren();
		for (int n = 0; n < children.size(); n++) {
			AGBComponentNode node = children.get(n);
			List<AGBNode> values = node.process(context);
			if (values == null) {
				if (list != null) {
					list.add(node);
				}
			} else {
				if (list == null) {
					list = new ArrayList<AGBNode>(children.size() + values.size());
					for (int m = 0; m < n; m++) {
						list.add(children.get(m));
					}
				}
				list.addAll(values);
			}
		}
		return list;
	}
	
}

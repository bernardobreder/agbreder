package com.agbreder.api.gui.ui;

import java.util.List;

/**
 * Componente de grupamento
 * 
 * @author bernardobreder
 */
public interface IPanel extends IComponent {
	
	/**
	 * Retorna os filhos do componente
	 * 
	 * @return filhos do componente
	 */
	public List<IComponent> getChildren();
	
	/**
	 * Indica se tem filho
	 * 
	 * @return se tem filho
	 */
	public boolean hasChildren();
	
}

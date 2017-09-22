package com.agbreder.api.gui.ui;

import com.agbreder.api.gui.event.IMouseEvent;

/**
 * Componente básico
 * 
 * @author bernardobreder
 */
public interface IComponent extends IStruct {
	
	/**
	 * Pinta o componente
	 * 
	 * @param g
	 */
	public void paint(IGraphic g);
	
	/**
	 * Retorna o componente pai
	 * 
	 * @return coordenada X
	 */
	public IPanel getParent();
	
	/**
	 * Atribui um pai
	 * 
	 * @param panel
	 */
	public void setParent(IPanel panel);
	
	/**
	 * Retorna a coordenada X
	 * 
	 * @return coordenada X
	 */
	public int getX();
	
	/**
	 * Retorna a coordenada Y
	 * 
	 * @return coordenada Y
	 */
	public int getY();
	
	/**
	 * Retorna a coordenada X Absoluta
	 * 
	 * @return coordenada X
	 */
	public int getAbsoluteX();
	
	/**
	 * Retorna a coordenada Y Absoluta
	 * 
	 * @return coordenada Y
	 */
	public int getAbsoluteY();
	
	/**
	 * Retorna o comprimento
	 * 
	 * @return comprimento
	 */
	public int getWidth();
	
	/**
	 * Retorna a altura
	 * 
	 * @return altura
	 */
	public int getHeight();
	
	/**
	 * Atribui o valor ao X
	 * 
	 * @param x
	 */
	public void setX(int x);
	
	/**
	 * Atribui o valor ao Y
	 * 
	 * @param y
	 */
	public void setY(int y);
	
	/**
	 * Atribui o valor ao Width
	 * 
	 * @param width
	 */
	public void setWidth(int width);
	
	/**
	 * Atribui o valor ao Height
	 * 
	 * @param height
	 */
	public void setHeight(int height);
	
	/**
	 * Altera o tamanho do componente
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height);
	
	/**
	 * Altera a localização do componente
	 * 
	 * @param x
	 * @param y
	 */
	public void setLocale(int x, int y);
	
	/**
	 * Realiza a repintura
	 */
	public void repaint();
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseClicked(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMousePressed(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseReleased(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseDragged(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseMoved(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseEntered(IMouseEvent e);
	
	/**
	 * Evento de Mouse
	 * 
	 * @param e
	 */
	public void onMouseExited(IMouseEvent e);
	
}

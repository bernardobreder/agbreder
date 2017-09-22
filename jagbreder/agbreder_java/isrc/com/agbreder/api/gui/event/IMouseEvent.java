package com.agbreder.api.gui.event;

/**
 * Evento de mouse
 * 
 * @author bernardobreder
 */
public interface IMouseEvent extends IEvent {
	
	/**
	 * @return the x
	 */
	public int getX();
	
	/**
	 * @param x
	 *        the x to set
	 */
	public void setX(int x);
	
	/**
	 * @return the y
	 */
	public int getY();
	
	/**
	 * @param y
	 *        the y to set
	 */
	public void setY(int y);
	
	/**
	 * @return the button
	 */
	public int getButton();
	
	/**
	 * @param button
	 *        the button to set
	 */
	public void setButton(int button);
	
}

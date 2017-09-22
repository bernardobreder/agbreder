package com.agbreder.api.gui.ui;

import java.awt.Color;

/**
 * Estrutura de pintura
 * 
 * @author bernardobreder
 */
public interface IGraphic extends IStruct {
	
	/**
	 * Pinta uma String
	 * 
	 * @param text
	 * @param x
	 * @param y
	 */
	public void drawString(String text, int x, int y);
	
	/**
	 * Desenha uma quadrado
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void drawRect(int x, int y, int width, int height);
	
	/**
	 * Desenha um quadrado arredondado
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth
	 * @param arcHeight
	 */
	public void drawRound(int x, int y, int width, int height, int arcWidth,
		int arcHeight);
	
	/**
	 * Desenha uma quadrado
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void fillRect(int x, int y, int width, int height);
	
	/**
	 * Desenha um quadrado arredondado
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param arcWidth
	 * @param arcHeight
	 */
	public void fillRound(int x, int y, int width, int height, int arcWidth,
		int arcHeight);
	
	/**
	 * Translada as coordenadas
	 * 
	 * @param x
	 * @param y
	 */
	public void translate(int x, int y);
	
	/**
	 * Retorna a altura da fonte corrente
	 * 
	 * @return altura da fonte corrente
	 */
	public int getFontHeight();
	
	/**
	 * Atribui uma cor corrente
	 * 
	 * @param color
	 */
	public void pushColor(Color color);
	
	/**
	 * Volta a cor anterior
	 */
	public void popColor();
	
}

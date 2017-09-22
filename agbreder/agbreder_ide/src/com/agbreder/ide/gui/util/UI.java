package com.agbreder.ide.gui.util;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Facilitador de componentes gráfico
 * 
 * @author bernardobreder
 */
public class UI {
	
	/**
	 * Equalisa o tamanho dos componentes
	 * 
	 * @param components
	 */
	public static void equalizeHeight(Component... components) {
		int min = components[0].getPreferredSize().height;
		for (int n = 1; n < components.length; n++) {
			Component component = components[n];
			int height = component.getPreferredSize().height;
			if (height >= 0) {
				min = Math.min(min, height);
			}
		}
		for (int n = 0; n < components.length; n++) {
			Component component = components[n];
			component.setPreferredSize(new Dimension(
				component.getPreferredSize().width, min));
		}
	}
	
	/**
	 * Equalisa o tamanho dos componentes
	 * 
	 * @param components
	 */
	public static void equalizeWidth(Component... components) {
		int min = components[0].getPreferredSize().width;
		for (int n = 1; n < components.length; n++) {
			Component component = components[n];
			min = Math.min(min, component.getPreferredSize().width);
		}
		for (int n = 0; n < components.length; n++) {
			Component component = components[n];
			component.setPreferredSize(new Dimension(min, component
				.getPreferredSize().height));
		}
	}
	
}

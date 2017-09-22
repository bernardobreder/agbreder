package com.agbreder.plugin.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager {

	protected Map<RGB, Color> colorMap = new HashMap<RGB, Color>(10);

	public void dispose() {
		for (Color color : colorMap.values()) {
			color.dispose();
		}
	}

	public Color getColor(RGB rgb) {
		Color color = colorMap.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			colorMap.put(rgb, color);
		}
		return color;
	}
}

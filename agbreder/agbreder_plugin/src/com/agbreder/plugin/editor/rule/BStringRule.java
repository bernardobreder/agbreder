package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;

public class BStringRule extends SingleLineRule {

	public BStringRule(ColorManager manager) {
		super("\"", "\"", new Token(new TextAttribute(manager.getColor(ColorConstants.DARK_BLUE))), '\\', true, true);
	}

}

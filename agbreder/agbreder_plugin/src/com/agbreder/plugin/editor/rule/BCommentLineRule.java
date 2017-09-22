package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.Token;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;

public class BCommentLineRule extends EndOfLineRule {

	public BCommentLineRule(ColorManager manager) {
		super("//", new Token(new TextAttribute(manager.getColor(ColorConstants.COMMENT))));
	}

}

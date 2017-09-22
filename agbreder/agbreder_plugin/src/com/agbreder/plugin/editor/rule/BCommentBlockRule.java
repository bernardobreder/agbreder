package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;

public class BCommentBlockRule extends MultiLineRule {

	public BCommentBlockRule(ColorManager manager) {
		super("/*", "*/", new Token(new TextAttribute(manager.getColor(ColorConstants.COMMENT))), (char) 0);
	}

}

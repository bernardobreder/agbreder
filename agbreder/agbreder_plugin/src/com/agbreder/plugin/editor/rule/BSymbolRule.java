package com.agbreder.plugin.editor.rule;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;

public class BSymbolRule extends BWordRule {

	public BSymbolRule(ColorManager manager) {
		super(new BSymbolDetector(), manager);
		this.addWord("{", ColorConstants.LBODY);
		this.addWord("}", ColorConstants.RBODY);
		this.addWord("[", ColorConstants.RED);
		this.addWord("]", ColorConstants.RED);
		this.addWord("<", ColorConstants.RED);
		this.addWord(">", ColorConstants.RED);
		this.addWord("(", ColorConstants.LPARAM);
		this.addWord(")", ColorConstants.RPARAM);
		this.addWord("+", ColorConstants.SUM);
		this.addWord("-", ColorConstants.SUB);
		this.addWord("*", ColorConstants.MUL);
		this.addWord("/", ColorConstants.DIV);
		this.addWord("=", ColorConstants.EQUAL);
		this.addWord(";", ColorConstants.SEMICOMMA);
		this.addWord(".", ColorConstants.DOT);
		this.addWord(",", ColorConstants.RED);
		this.addWord("!", ColorConstants.RED);
	}

}

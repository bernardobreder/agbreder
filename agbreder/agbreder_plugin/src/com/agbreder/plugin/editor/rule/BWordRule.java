package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.graphics.RGB;

import com.agbreder.plugin.editor.ColorManager;

public class BWordRule extends WordRule {

	protected ColorManager manager;

	public BWordRule(IWordDetector detector, ColorManager manager) {
		super(detector);
		this.manager = manager;
	}

	public void addWord(String word, RGB rgb) {
		super.addWord(word, new Token(new TextAttribute(manager.getColor(rgb))));
	}

	@Override
	protected void unreadBuffer(ICharacterScanner scanner) {
		scanner.unread();
	}

}

package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.rules.IWordDetector;

public class BSymbolDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		return true;
	}

	@Override
	public boolean isWordPart(char c) {
		return false;
	}

}

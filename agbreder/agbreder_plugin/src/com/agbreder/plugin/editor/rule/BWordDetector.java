package com.agbreder.plugin.editor.rule;

import org.eclipse.jface.text.rules.IWordDetector;

class BWordDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		return Character.isJavaIdentifierStart(c);
	}

	@Override
	public boolean isWordPart(char c) {
		return Character.isJavaIdentifierPart(c);
	}

}

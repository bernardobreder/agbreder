package com.agbreder.plugin.editor.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;
import com.agbreder.plugin.editor.rule.AGBNumberRule;
import com.agbreder.plugin.editor.rule.BKeyWordRule;
import com.agbreder.plugin.editor.rule.BStringRule;
import com.agbreder.plugin.editor.rule.BSymbolRule;

public class BrederScanner extends RuleBasedScanner {

	public BrederScanner(ColorManager manager) {
		Token javadoc = new Token(new TextAttribute(manager.getColor(ColorConstants.COMMENT)));
		List<IRule> rules = new ArrayList<IRule>();
		rules.add(new EndOfLineRule("//", javadoc));
		rules.add(new MultiLineRule("/**", "*/", javadoc, (char) 0, true));
		rules.add(new MultiLineRule("/*", "*/", javadoc, (char) 0, true));
		rules.add(new WhitespaceRule(new BrederWhitespaceDetector()));
		rules.add(BKeyWordRule.getInstance());
		rules.add(new BSymbolRule(manager));
		rules.add(new BStringRule(manager));
		rules.add(new AGBNumberRule(manager));
		super.setRules(rules.toArray(new IRule[rules.size()]));
	}

}

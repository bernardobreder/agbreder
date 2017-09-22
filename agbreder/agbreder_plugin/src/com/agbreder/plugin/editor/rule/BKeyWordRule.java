package com.agbreder.plugin.editor.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;

import com.agbreder.plugin.Eclipse;
import com.agbreder.plugin.editor.ColorConstants;
import com.agbreder.plugin.editor.ColorManager;
import com.agbreder.plugin.util.AGBrederLanguageFile;
import com.agbreder.plugin.util.FileUtil;

public class BKeyWordRule extends BWordRule {

	private static final BKeyWordRule instance = new BKeyWordRule(new ColorManager());

	private BKeyWordRule(ColorManager manager) {
		super(new BWordDetector(), manager);
		{
			this.addWord("class", ColorConstants.DARK_RED);
			this.addWord("extends", ColorConstants.DARK_RED);
			this.addWord("static", ColorConstants.DARK_RED);
			this.addWord("function", ColorConstants.DARK_RED);
			this.addWord("runtime", ColorConstants.DARK_RED);
			this.addWord("return", ColorConstants.DARK_RED);
			this.addWord("new", ColorConstants.DARK_RED);
			this.addWord("for", ColorConstants.DARK_RED);
			this.addWord("or", ColorConstants.DARK_RED);
			this.addWord("and", ColorConstants.DARK_RED);
			this.addWord("orbit", ColorConstants.DARK_RED);
			this.addWord("andbit", ColorConstants.DARK_RED);
			this.addWord("throw", ColorConstants.DARK_RED);
			this.addWord("super", ColorConstants.DARK_RED);
			this.addWord("try", ColorConstants.DARK_RED);
			this.addWord("catch", ColorConstants.DARK_RED);
			this.addWord("while", ColorConstants.DARK_RED);
			this.addWord("repeat", ColorConstants.DARK_RED);
			this.addWord("if", ColorConstants.DARK_RED);
			this.addWord("else", ColorConstants.DARK_RED);
			this.addWord("switch", ColorConstants.DARK_RED);
			this.addWord("break", ColorConstants.DARK_RED);
			this.addWord("case", ColorConstants.DARK_RED);
			this.addWord("do", ColorConstants.DARK_RED);
			this.addWord("end", ColorConstants.DARK_RED);
		}
		{
			this.addWord("this", ColorConstants.DARK_BLUE);
			this.addWord("null", ColorConstants.DARK_BLUE);
			this.addWord("true", ColorConstants.DARK_BLUE);
			this.addWord("false", ColorConstants.DARK_BLUE);
		}
		{
			this.addWord("num", ColorConstants.DARK_GREEN);
			this.addWord("str", ColorConstants.DARK_GREEN);
			this.addWord("bool", ColorConstants.DARK_GREEN);
			this.addWord("byte", ColorConstants.DARK_GREEN);
			this.addWord("obj", ColorConstants.DARK_GREEN);
		}
		{
			File brederFolder = new File(new AGBrederLanguageFile(), "src");
			List<File> files = new ArrayList<File>();
			files.addAll(Arrays.asList(FileUtil.list(brederFolder, "agb")));
			for (IProject project : Eclipse.getProjects()) {
				files.addAll(Arrays.asList(FileUtil.list(project.getLocation().toFile(), "agb")));
			}
			for (File brederSource : files) {
				String name = brederSource.getName();
				name = name.substring(0, name.length() - ".agb".length());
				if (name.startsWith("I") && name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
					this.addWord(name, ColorConstants.DARK_GREEN);
				} else {
					this.addWord(name, ColorConstants.DARK_GREEN);
				}
			}
		}
	}

	public static BKeyWordRule getInstance() {
		return instance;
	}

}

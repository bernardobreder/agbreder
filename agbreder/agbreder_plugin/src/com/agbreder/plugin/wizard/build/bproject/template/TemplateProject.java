package com.agbreder.plugin.wizard.build.bproject.template;

import java.util.ArrayList;

public class TemplateProject extends ArrayList<AbstractTemplate> {

	private static final TemplateProject instance = new TemplateProject();

	private TemplateProject() {
		this.add(new HelloWorldTemplate());
		// this.add(new CameraTemplate());
	}

	public static TemplateProject getInstance() {
		return instance;
	}

}

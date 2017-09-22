package com.agbreder.plugin.element;

public class ElementManager {

	private final Workspace root = new Workspace();

	public static final ElementManager instance = new ElementManager();

	private ElementManager() {
	}

	public synchronized Workspace getRoot() {
		return root;
	}

	public static ElementManager getInstance() {
		return instance;
	}

}

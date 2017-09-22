package com.agbreder.plugin.element;

public interface Element {

	public String getName();

	public String getFullname();

	public Element[] getChildren();

	public boolean hasChildren();

	public Parent<?> getParent();

	public void refresh(int depth);

}

package com.agbreder.ide.plugin;


import java.io.Serializable;

public interface IMenu extends Serializable {

	public String getName();
	
	public IMenu getParent();

}

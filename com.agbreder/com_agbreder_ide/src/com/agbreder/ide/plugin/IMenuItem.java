package com.agbreder.ide.plugin;


import java.io.Serializable;

public interface IMenuItem extends Serializable {

	public String getName();

	public IMenu getMenu();

}

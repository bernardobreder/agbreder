package com.agbreder.ide.plugin.impl;

import com.agbreder.ide.plugin.IMenu;
import com.agbreder.ide.plugin.IMenuItem;

/**
 * MenuItem do Menu de IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class MenuItem implements IMenuItem {

  /** Nome */
  private final String name;
  /** Menu */
  private final IMenu menu;

  /**
   * Construtor
   * 
   * @param menu
   * @param name
   */
  public MenuItem(IMenu menu, String name) {
    super();
    this.menu = menu;
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMenu getMenu() {
    return this.menu;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

}

package com.agbreder.ide.plugin.impl;

import com.agbreder.ide.plugin.IMenu;

/**
 * Menu da IDE
 * 
 * 
 * @author Bernardo Breder
 */
public class Menu implements IMenu {

  /** Menu Pai */
  private final IMenu parent;
  /** Nome */
  private final String name;

  /**
   * Construtor
   * 
   * @param name
   */
  public Menu(String name) {
    this(null, name);
  }

  /**
   * Construtor
   * 
   * @param parent
   * @param name
   */
  public Menu(IMenu parent, String name) {
    this.parent = parent;
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMenu getParent() {
    return this.parent;
  }

}

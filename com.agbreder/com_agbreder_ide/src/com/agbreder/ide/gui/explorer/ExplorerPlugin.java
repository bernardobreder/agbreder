package com.agbreder.ide.gui.explorer;

import com.agbreder.ide.plugin.IMenu;
import com.agbreder.ide.plugin.IMenuItem;
import com.agbreder.ide.plugin.IPlugin;
import com.agbreder.ide.plugin.IView;
import com.agbreder.ide.plugin.impl.Menu;
import com.agbreder.ide.plugin.impl.MenuItem;

/**
 * Estrutura de Plugin
 * 
 * 
 * @author Bernardo Breder
 */
public class ExplorerPlugin implements IPlugin {

  /** Menus */
  private IMenuItem[] menus;

  /**
   * Construtor
   */
  public ExplorerPlugin() {
    IMenu menu = new Menu("Tool");
    IMenuItem item = new MenuItem(menu, "Calc");
    this.menus = new IMenuItem[] { item };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IMenuItem[] getMenuItens() {
    return menus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IView[] getViews() {
    return new IView[] { new ExplorerView() };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "Explorer";
  }

}

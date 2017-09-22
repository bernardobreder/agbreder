package com.agbreder.ide.plugin;

import com.agbreder.ide.gui.ide.IdeFrame;
import com.agbreder.ide.model.view.ViewManager;

/**
 * Inicializa um plugin
 * 
 * 
 * @author Bernardo Breder
 */
public class PluginStarter implements Runnable {

  /** Plugin */
  private IPlugin plugin;

  /**
   * Construtor
   * 
   * @param plugin
   */
  public PluginStarter(IPlugin plugin) {
    this.plugin = plugin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    this.addMenu();
    this.addView();
  }

  /**
   * Adiciona um menu
   */
  private void addMenu() {
    IdeFrame frame = IdeFrame.getInstance();
    IMenuItem[] menuItens = this.plugin.getMenuItens();
    if (menuItens != null) {
      for (IMenuItem item : menuItens) {
        frame.addMenuItem(item);
      }
      frame.setJMenuBar(frame.getJMenuBar());
    }
  }

  /**
   * Adiciona uma visão
   */
  private void addView() {
    IView[] views = this.plugin.getViews();
    if (views != null) {
      for (IView view : views) {
        ViewManager.getDefault().put(view.getName(), view);
      }
    }
  }

}

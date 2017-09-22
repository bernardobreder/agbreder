package com.agbreder.ide.model.view;

import java.util.HashMap;
import java.util.Map;

import com.agbreder.ide.plugin.IView;


/**
 * Gerencia de visões
 * 
 * 
 * @author Bernardo Breder
 */
public class ViewManager {

  /** Instancia única */
  private static final ViewManager DEFAULT = new ViewManager();
  /** Visões disponíveis */
  private final Map<String, IView> views = new HashMap<String, IView>();

  /**
   * Construtor
   */
  private ViewManager() {
  }

  /**
   * Adiciona uma visão
   * 
   * @param name
   * @param view
   */
  public synchronized void put(String name, IView view) {
    this.views.put(name, view);
  }

  /**
   * Retorna uma visão
   * 
   * @param name
   * @return visão
   */
  public synchronized IView get(String name) {
    return this.views.get(name);
  }

  /**
   * Retorna todas as visões
   * 
   * @return visões
   */
  public synchronized IView[] gets() {
    return this.views.values().toArray(new IView[this.views.size()]);
  }

  /**
   * Retorna a instancia
   * 
   * @return instance
   */
  public static ViewManager getDefault() {
    return ViewManager.DEFAULT;
  }

}

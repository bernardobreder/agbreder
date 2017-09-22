package com.agbreder.ide.plugin;

import java.awt.Component;

import javax.swing.Icon;

/**
 * Uma visão da IDE
 * 
 * 
 * @author Bernardo Breder
 */
public interface IView extends IComponent {

  /**
   * Constroi o componente
   * 
   * @return component
   */
  public Component build();

  /**
   * Retorna o icone
   * 
   * @return icone
   */
  public Icon getIcon();

}

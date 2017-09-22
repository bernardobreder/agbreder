package com.agbreder.client.util.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tarefa a ser realizada
 * 
 * 
 * @author bbreder
 */
public abstract class SimpleTask implements ActionListener {

  /**
   * Inicializa uma tarefa
   */
  public abstract void start();

  /**
   * Stop
   */
  public abstract void stop();

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    this.start();
  }

}

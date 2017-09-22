package com.agbreder.api;

/**
 * Eventos de mensagens
 * 
 * 
 * @author Bernardo Breder
 */
public interface AGListener {

  /**
   * Evento de nova mensagem
   * 
   * @param mail
   */
  public void busy(AGMail mail);

  /**
   * Evento de offline
   */
  public void offline();

  /**
   * Evento de online
   */
  public void online();

  /**
   * Evento de erro
   * 
   * @param t
   */
  public void handler(Throwable t);

}

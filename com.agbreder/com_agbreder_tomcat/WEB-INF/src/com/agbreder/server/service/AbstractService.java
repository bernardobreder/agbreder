package com.agbreder.server.service;

/**
 * Modelo de serviço para próximo
 * 
 * @author bernardobreder
 * 
 * @param <E>
 */
public abstract class AbstractService<E> implements IService {

  /** Próximo */
  private final E next;

  /**
   * Construtor
   * 
   * @param next
   */
  public AbstractService(E next) {
    super();
    this.next = next;
  }

  /**
   * @return the next
   */
  public E getNext() {
    return next;
  }

}

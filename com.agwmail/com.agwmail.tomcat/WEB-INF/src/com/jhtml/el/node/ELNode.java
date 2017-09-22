package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Representa um nó da linguagem
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ELNode {

  /**
   * Construtor padrão
   */
  public ELNode() {
    super();
  }

  /**
   * Execute um nó
   * 
   * @param req
   * @param resp
   * @return valor
   */
  public abstract Object execute(HttpServletRequest req,
    HttpServletResponse resp);

  /**
   * Retorna o proprio
   * 
   * @param <E>
   * @return proprio
   */
  @SuppressWarnings("unchecked")
  public <E extends ELNode> E cast() {
    return (E) this;
  }

}

package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Nó de Identificador
 * 
 * 
 * @author Bernardo Breder
 */
public class IdentifyNode extends ELNode {

  /** Token */
  private String token;

  /**
   * Construtor padrão
   * 
   * @param token nome do identificador
   */
  public IdentifyNode(String token) {
    this.token = token;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    return req.getAttribute(token);
  }

  /**
   * Retorna
   * 
   * @return token
   */
  public String getToken() {
    return token;
  }

}

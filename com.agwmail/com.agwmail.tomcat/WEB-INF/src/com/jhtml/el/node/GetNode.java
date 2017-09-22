package com.jhtml.el.node;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Not
 * 
 * 
 * @author Bernardo Breder
 */
public class GetNode extends UnaryNode {

  /** Nome */
  private String name;

  /**
   * Construtor
   * 
   * @param left
   * @param name
   */
  public GetNode(ELNode left, String name) {
    super(left);
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    try {
      return left.getClass().getMethod(
        "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
        .invoke(left);

    }
    catch (Exception e) {
    }
    try {
      return left.getClass().getMethod(
        "is" + Character.toUpperCase(name.charAt(0)) + name.substring(1))
        .invoke(left);

    }
    catch (Exception e) {
    }
    try {
      return left.getClass().getMethod(name).invoke(left);
    }
    catch (Exception e) {
    }
    try {
      return left.getClass().getField(name).get(left);
    }
    catch (Exception e) {
    }
    return left;
  }

  /**
   * Retorna
   * 
   * @return name
   */
  public String getName() {
    return name;
  }

}

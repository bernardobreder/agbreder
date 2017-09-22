package com.jhtml.el.node;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Not
 * 
 * 
 * @author Bernardo Breder
 */
public class CallNode extends UnaryNode {

  /** Nome */
  private String name;
  /** */
  private List<ELNode> params;

  /**
   * Construtor
   * 
   * @param left
   * @param name
   */
  public CallNode(ELNode left, String name) {
    super(left);
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object execute(HttpServletRequest req, HttpServletResponse resp) {
    Object left = this.getLeft().execute(req, resp);
    int size = params == null ? 0 : params.size();
    Class<?>[] classes = new Class<?>[size];
    Object[] args = new Object[size];
    for (int n = 0; n < size; n++) {
      Object param = params.get(n).execute(req, resp);
      args[n] = param;
      classes[n] = param.getClass();
    }
    try {
      return left.getClass().getMethod(name, classes).invoke(left, args);
    }
    catch (Exception e) {
    }
    return left;
  }

  /**
   * Adiciona um parametro
   * 
   * @param node
   */
  public void addParam(ELNode node) {
    if (params == null) {
      params = new ArrayList<ELNode>();
    }
    params.add(node);
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

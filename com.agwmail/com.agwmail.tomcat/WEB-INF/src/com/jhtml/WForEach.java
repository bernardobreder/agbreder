package com.jhtml;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WForEach extends WTag {

  /**
   * Construtor
   */
  public WForEach() {
    super("forEach");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String var = this.getAttributes().get("var").execute(req, resp).toString();
    String index = this.get("index").execute(req, resp).toString();
    List<?> list =
      (List<?>) this.getAttributes().get("list").execute(req, resp);
    Object oldIndexValue = req.getAttribute(index);
    Object oldVarValue = req.getAttribute(var);
    for (int n = 0; n < list.size(); n++) {
      Object value = list.get(n);
      req.setAttribute(var, value);
      req.setAttribute(index, n);
      for (WNode node : this.getChildren()) {
        node.execute(req, resp);
      }
    }
    if (oldVarValue != null) {
      req.setAttribute(var, oldVarValue);
    }
    else {
      req.removeAttribute(var);
    }
    if (oldIndexValue != null) {
      req.setAttribute(index, oldIndexValue);
    }
    else {
      req.removeAttribute(index);
    }
  }

}

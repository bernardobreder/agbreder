package com.jhtml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WFor extends WTag {

  /**
   * Construtor
   */
  public WFor() {
    super("for");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String var = this.getAttributes().get("var").execute(req, resp).toString();
    int begin =
      Integer.valueOf(this.getAttributes().get("begin").execute(req, resp)
        .toString());
    int end =
      Integer.valueOf(this.getAttributes().get("end").execute(req, resp)
        .toString());
    Object oldValue = req.getAttribute(var);
    for (int n = begin; n <= end; n++) {
      req.setAttribute(var, Integer.valueOf(n));
      for (WNode node : this.getChildren()) {
        node.execute(req, resp);
      }
    }
    if (oldValue != null) {
      req.setAttribute(var, oldValue);
    }
    else {
      req.removeAttribute(var);
    }
  }

}

package com.jhtml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WHtml extends WTag {

  /**
   * Construtor
   */
  public WHtml() {
    super("html");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    resp.getOutputStream().write(
      "<!DOCTYPE html PUBLIC '-//WAPFORUM//DTD XHTML Mobile 1.0//EN'>"
        .getBytes(WNode.CHAR_SET));
    super.execute(req, resp);
  }

}

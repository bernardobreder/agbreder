package com.jhtml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WNbsp extends WTag {

  /**
   * Construtor
   */
  public WNbsp() {
    super("nbsp");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    resp.getOutputStream().write("&nbsp;".getBytes());
  }

}

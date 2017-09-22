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
public class WElse extends WTag {

  /**
   * Construtor
   */
  public WElse() {
    super("if");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    List<WNode> children = this.getChildren();
    for (WNode node : children) {
      node.execute(req, resp);
    }
  }

}

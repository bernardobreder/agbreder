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
public class WIf extends WTag {

  /**
   * Construtor
   */
  public WIf() {
    super("if");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    Object test = this.getAttributes().get("test").execute(req, resp);
    List<WNode> children = this.getChildren();
    if (test instanceof Boolean && (Boolean) test) {
      for (WNode node : children) {
        if (node instanceof WElse == false) {
          node.execute(req, resp);
        }
      }
    }
    else {
      if (children.size() > 0) {
        WNode lastNode = children.get(children.size() - 1);
        if (lastNode instanceof WElse) {
          lastNode.execute(req, resp);
        }
      }
    }
  }

}

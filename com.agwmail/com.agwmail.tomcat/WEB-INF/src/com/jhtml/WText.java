package com.jhtml;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

import com.jhtml.el.node.ELNode;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WText extends WNode {

  /** Texto */
  private ELNode text;

  /**
   * Construtor
   * 
   * @param node
   */
  public WText(ELNode node) {
    this.text = node;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WNode load(Element elem) throws IOException {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    resp.getOutputStream().write(
      this.text.execute(req, resp).toString().getBytes(WNode.CHAR_SET));
  }

}

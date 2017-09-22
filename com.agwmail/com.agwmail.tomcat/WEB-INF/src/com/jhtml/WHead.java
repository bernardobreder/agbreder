package com.jhtml;

import java.io.IOException;

import org.dom4j.Element;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public class WHead extends WTag {

  /**
   * Construtor
   */
  public WHead() {
    super("head");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WTag load(Element elem) throws IOException {
    this.add(new WTag("meta").add("http-equiv", "Content-Type").add("content",
      "text/html;charset=utf-8").load(null));
    return super.load(elem);
  }

}

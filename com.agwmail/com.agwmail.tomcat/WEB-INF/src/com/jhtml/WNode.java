package com.jhtml;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;

/**
 * Tag html
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class WNode implements Serializable, Cloneable {

  /** CharSet */
  public static final Charset CHAR_SET = Charset.forName("utf-8");

  /**
   * Carrega o nó
   * 
   * @param elem
   * @return this
   * @throws IOException
   */
  public abstract WNode load(Element elem) throws IOException;

  /**
   * Salva o nó
   * 
   * @param req
   * @param resp
   * @throws Exception
   */
  public abstract void execute(HttpServletRequest req, HttpServletResponse resp)
    throws Exception;

}

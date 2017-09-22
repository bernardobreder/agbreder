package com.agwmail.servlet.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.JsonWriter;

/**
 * Servlet Online que retorna objeto json
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ObjectOfflineServlet extends OfflineServlet {

  /**
   * Retorna o objeto json
   * 
   * @param req
   * @param resp
   * @return json
   * @throws Exception
   */
  public abstract Object build(HttpServletRequest req, HttpServletResponse resp)
    throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public final void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    Object object = this.build(req, resp);
    resp.getOutputStream().write(
      new JsonWriter(object).toString().getBytes("utf-8"));
  }

}

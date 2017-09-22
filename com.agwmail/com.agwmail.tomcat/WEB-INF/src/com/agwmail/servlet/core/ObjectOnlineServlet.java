package com.agwmail.servlet.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Json64;

import com.agwmail.model.user.User;

/**
 * Servlet Online que retorna objeto json
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class ObjectOnlineServlet extends OnlineServlet {

  /**
   * Retorna o objeto json
   * 
   * @param req
   * @param resp
   * @param user
   * @return json
   * @throws Exception
   */
  public abstract Object build(HttpServletRequest req,
    HttpServletResponse resp, User user) throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public final void action(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception {
    Object object = this.build(req, resp, user);
    resp.getOutputStream().write(Json64.encode(object).getBytes());
  }

}

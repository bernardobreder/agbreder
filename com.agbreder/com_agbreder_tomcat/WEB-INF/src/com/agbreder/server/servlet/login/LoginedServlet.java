package com.agbreder.server.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.user.UserManager;
import com.agbreder.server.servlet.core.ObjectOfflineServlet;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class LoginedServlet extends ObjectOfflineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean build(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String session = req.getSession(true).getId();
    return UserManager.getInstance().getUserContext(session) != null;
  }

}

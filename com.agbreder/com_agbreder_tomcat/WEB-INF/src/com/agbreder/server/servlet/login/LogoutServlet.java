package com.agbreder.server.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.user.User;
import com.agbreder.server.model.user.UserManager;
import com.agbreder.server.servlet.core.ObjectOnlineServlet;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class LogoutServlet extends ObjectOnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object build(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception {
    String session = req.getSession(true).getId();
    UserManager.getInstance().removeUserContext(session);
    return true;
  }

}

package com.agwmail.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.user.User;
import com.agwmail.model.user.UserManager;
import com.agwmail.servlet.core.ObjectOnlineServlet;

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
    UserManager.getInstance().removeUserContext();
    return true;
  }

}

package com.agbreder.server.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.ObjectOfflineServlet;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class ForgotPasswordServlet extends ObjectOfflineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public Object build(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String session = req.getSession(true).getId();
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    Integer userId = ServiceLocator.user.login(username, password, session);
    if (userId != null) {
    }
    else {
    }
    return null;
  }

}

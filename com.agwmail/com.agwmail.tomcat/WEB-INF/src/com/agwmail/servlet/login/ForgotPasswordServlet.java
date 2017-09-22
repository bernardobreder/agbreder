package com.agwmail.servlet.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.service.ServiceLocator;
import com.agwmail.servlet.core.ObjectOfflineServlet;

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
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    Integer userId = ServiceLocator.user.login(username, password);
    if (userId != null) {
    }
    else {
    }
    return null;
  }

}

package com.agbreder.server.servlet.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agbreder.server.model.user.User;
import com.agbreder.server.model.user.UserContext;
import com.agbreder.server.model.user.UserManager;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class OnlineServlet extends HtmlServlet {

  /**
   * Ação online
   * 
   * @param req
   * @param resp
   * @param user
   * @throws Exception
   */
  public abstract void action(HttpServletRequest req, HttpServletResponse resp,
    User user) throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  public final void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception {
    String session = req.getSession(true).getId();
    UserContext userContext = UserManager.getInstance().getUserContext(session);
    if (userContext == null) {
      throw new AutenticatedException("autenticated");
    }
    else {
      this.action(req, resp, userContext.getUser());
    }
  }
  
}
package com.agbreder.server.servlet.source;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.util.Base64;
import breder.util.util.input.InputStreamUtil;

import com.agbreder.server.model.user.User;
import com.agbreder.server.service.ServiceLocator;
import com.agbreder.server.servlet.core.OnlineServlet;

/**
 * Publica um fonte
 * 
 * 
 * @author Bernardo Breder
 */
public class PublishSourceServlet extends OnlineServlet {

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    int userId = user.getId();
    String name = req.getParameter("name");
    if (name == null) {
      throw new IllegalArgumentException("name");
    }
    String text =
      Base64.decodeString(new String(InputStreamUtil.getBytes(req
        .getInputStream())));
    ServiceLocator.source.publish(userId, name, text);
  }

}

package com.agwmail.servlet.inbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.mail.Mail;
import com.agwmail.model.user.User;
import com.agwmail.service.ServiceLocator;
import com.agwmail.servlet.core.PageOnlineServlet;
import com.jhtml.el.Grammer;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class MailHtmlServlet extends PageOnlineServlet {

  /** Path */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.file =
      new File(config.getServletContext().getRealPath("/WEB-INF/page/mail.xml"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    int mailId = Integer.parseInt(req.getParameter("id"));
    Mail mail = ServiceLocator.mail.getMail(user.getId(), mailId);
    req.setAttribute("mail", mail);
    InputStream input = new FileInputStream(file);
    Grammer.build(input).execute(req, resp);
  }

}

package com.agwmail.servlet.inbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.mail.InboxMail;
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
public class InboxServlet extends PageOnlineServlet {

  /** Path */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.file =
      new File(config.getServletContext()
        .getRealPath("/WEB-INF/page/inbox.xml"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    List<InboxMail> mails = ServiceLocator.mail.getInboxs(user.getId());
    req.setAttribute("mails", mails);
    InputStream input = new FileInputStream(file);
    Grammer.build(input).execute(req, resp);
  }

}

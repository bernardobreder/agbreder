package com.agwmail.servlet.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agwmail.model.fs.AgentFileSystem;
import com.agwmail.model.fs.AgentResource;
import com.agwmail.model.user.User;
import com.agwmail.servlet.core.PageOnlineServlet;
import com.jhtml.el.Grammer;

/**
 * Servlet inicial
 * 
 * 
 * @author Bernardo Breder
 */
public class FolderServlet extends PageOnlineServlet {

  /** Path */
  private File file;

  /**
   * {@inheritDoc}
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.file =
      new File(config.getServletContext().getRealPath(
        "/WEB-INF/page/folder.xml"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void action(HttpServletRequest req, HttpServletResponse resp, User user)
    throws Exception {
    String idStr = req.getParameter("id");
    AgentResource folder = AgentFileSystem.ROOT;
    if (idStr != null) {
      long id = Long.valueOf(idStr);
      for (AgentResource resource : AgentFileSystem.ROOT.list()) {
        if (resource.getId() == id) {
          folder = resource;
        }
      }
    }
    req.setAttribute("file", folder);
    InputStream input = new FileInputStream(file);
    Grammer.build(input).execute(req, resp);
  }

}

package com.agbreder.server.servlet.core;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.sql.HSQL;
import breder.util.util.Base64;

/**
 * Servlet Html
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class HtmlServlet extends HttpServlet {

  /**
   * Ação de requisição
   * 
   * @param req
   * @param resp
   * @throws Exception
   */
  public abstract void action(HttpServletRequest req, HttpServletResponse resp)
    throws Exception;

  /**
   * {@inheritDoc}
   */
  @Override
  protected final void service(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
    resp.setCharacterEncoding("utf-8");
    req.setCharacterEncoding("utf-8");
    resp.setContentType("text/html");
    try {
      this.action(req, resp);
      HSQL.commit();
      //      DB.getInstance().commit();
    }
    catch (SocketException e) {
      HSQL.rollback();
      //      DB.getInstance().rollback();
    }
    catch (AutenticatedException e) {
      HSQL.rollback();
      //      DB.getInstance().rollback();
    }
    catch (Exception e) {
      HSQL.rollback();
      //      DB.getInstance().rollback();
      resp.getOutputStream().write(
        Base64.encodeString(
          String.format("{class=\"Exception\", detailMessage='%s'}",
            e.getMessage())).getBytes());
      e.printStackTrace();
    }
  }

}

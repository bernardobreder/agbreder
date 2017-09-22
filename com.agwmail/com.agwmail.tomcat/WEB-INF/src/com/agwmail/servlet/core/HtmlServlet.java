package com.agwmail.servlet.core;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.sql.DB;
import breder.util.util.ClassLoaderStack;

import com.agwmail.model.RequestManager;

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
    ClassLoaderStack.push(this.getClass().getClassLoader());
    try {
      RequestManager.getInstance().execute(this, req, resp);
      this.action(req, resp);
      DB.getInstance().commit();
    }
    catch (SocketException e) {
      DB.getInstance().rollback();
    }
    catch (Exception e) {
      DB.getInstance().rollback();
      e.printStackTrace();
    }
    finally {
      RequestManager.getInstance().free();
      ClassLoaderStack.pop();
    }
  }

}

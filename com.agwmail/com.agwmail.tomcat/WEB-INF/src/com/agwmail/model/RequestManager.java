package com.agwmail.model;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import breder.util.sql.ConnectionPool;
import breder.util.sql.DB;
import breder.util.sql.driver.SqlLiteMemoryDriver;

/**
 * Gerente de usuário
 * 
 * 
 * @author Bernardo Breder
 */
public class RequestManager {

  /** Instance */
  private static final RequestManager instance = new RequestManager();
  /** Contextos */
  private ThreadLocal<HttpServlet> servlets = new ThreadLocal<HttpServlet>();
  /** Contextos */
  private ThreadLocal<HttpServletRequest> requests =
    new ThreadLocal<HttpServletRequest>();
  /** Contextos */
  private ThreadLocal<HttpServletResponse> responses =
    new ThreadLocal<HttpServletResponse>();
  /** Ultimo Session Id */
  private Boolean developer;
  /** Ultimo Session Id */
  private String lastSessionID;

  /**
   * Construtor
   */
  private RequestManager() {
  }

  /**
   * Inicializa o armazenamento
   * 
   * @param servlet
   * @param req
   * @param resp
   * @throws ServletException
   */
  public void execute(HttpServlet servlet, HttpServletRequest req,
    HttpServletResponse resp) throws ServletException {
    servlets.set(servlet);
    requests.set(req);
    responses.set(resp);
    {
      if (this.developer == null) {
        this.initDeveloper();
      }
      String session = req.getSession(true).getId();
      if (developer && lastSessionID == null) {
        lastSessionID = session;
        this.init();
      }
    }
  }

  /**
   * Inicializa o armazenamento
   */
  public void free() {
    servlets.remove();
    requests.remove();
    responses.remove();
  }

  /**
   * Inicializa
   * 
   * @throws ServletException
   */
  private void init() throws ServletException {
    this.initDataBase();
  }

  /**
   * Inicializa o banco de dados
   * 
   * @throws ServletException
   */
  private void initDeveloper() throws ServletException {
    Object attribute =
      this.getServlet().getServletContext().getAttribute(
        "javax.servlet.context.tempdir");
    developer =
      attribute != null && attribute.toString().contains("com.agwmail.tomcat");
  }

  /**
   * Inicializa o banco de dados
   * 
   * @throws ServletException
   */
  private void initDataBase() throws ServletException {
    if (developer) {
      ConnectionPool.getInstance().setDriver(new SqlLiteMemoryDriver());
      ConnectionPool.getInstance().reconnect();
      try {
        DB.getInstance().write(
          new FileInputStream(this.getServlet().getServletContext()
            .getRealPath("WEB-INF/db.sql")));
      }
      catch (IOException e) {
        throw new ServletException(e);
      }
    }
  }

  /**
   * Retorna a requisição
   * 
   * @return requisição
   */
  public HttpServlet getServlet() {
    return servlets.get();
  }

  /**
   * Retorna a requisição
   * 
   * @return requisição
   */
  public HttpServletRequest getRequest() {
    return requests.get();
  }

  /**
   * Retorna a requisição
   * 
   * @return requisição
   */
  public HttpServletResponse getResponse() {
    return responses.get();
  }

  /**
   * Retorna
   * 
   * @return instance
   */
  public static RequestManager getInstance() {
    return instance;
  }

}

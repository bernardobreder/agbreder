package com.agbreder.server.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.agbreder.server.service.ServiceLocator;

/**
 * Inicializa o banco de dados
 * 
 * 
 * @author Bernardo Breder
 */
public class DatabaseServletContextListener implements ServletContextListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextDestroyed(ServletContextEvent e) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void contextInitialized(ServletContextEvent e) {
    ServiceLocator.mail.toString();
    //    ConnectionPool.getInstance().setDriver(
    //      new MySqlDriver("mysql.breder.org", "breder", "24813612"));
    //    ConnectionPool.getInstance().reconnect();
    //    try {
    //      DB.getInstance()
    //        .write(
    //          new FileInputStream(e.getServletContext().getRealPath(
    //            "WEB-INF/db.sql")));
    //      DB.getInstance().commit();
    //    }
    //    catch (IOException t) {
    //      t.printStackTrace();
    //    }
  }

}

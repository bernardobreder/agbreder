package com.agwmail.api;

import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Pagina do AgentMy
 * 
 * 
 * @author Bernardo Breder
 */
public class AgentMy {

  /** Driver */
  private FirefoxDriver driver;
  /** Pagina */
  private LoginPage page;

  /**
   * Construtor
   */
  public AgentMy() {
    driver = new FirefoxDriver();
    //    if (driver instanceof HtmlUnitDriver) {
    //      HtmlUnitDriver htmlDriver = (HtmlUnitDriver) driver;
    //      htmlDriver.setJavascriptEnabled(true);
    //      String[] classes =
    //        new String[] {
    //            "com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument",
    //            "com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter",
    //            "com.gargoylesoftware.htmlunit.IncorrectnessListenerImpl" };
    //      for (String clazz : classes) {
    //        Logger logger = LogManager.getLogManager().getLogger(clazz);
    //        logger.setLevel(Level.OFF);
    //      }
    //    }
    driver.get("http://localhost:8080/agwmail");
    this.page = new LoginPage(driver);
  }

  /**
   * Retorna
   * 
   * @return page
   */
  public LoginPage getPage() {
    return page;
  }

  /**
   * Fecha a ferramenta
   */
  public void close() {
    driver.quit();
  }

}

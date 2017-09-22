package com.agwmail.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Pagina de inbox
 * 
 * 
 * @author Bernardo Breder
 */
public class InboxPage extends HeaderPage {

  /**
   * Construtor
   * 
   * @param driver
   */
  public InboxPage(WebDriver driver) {
    super(driver);
    inboxToolCheck();
    inboxToolArchive();
    inboxToolCopy();
    inboxToolMove();
    inboxToolDelete();
    inboxToolRefresh();
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolRefresh() {
    return findId("inbox-tool-refresh");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolDelete() {
    return findId("inbox-tool-delete");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolMove() {
    return findId("inbox-tool-move");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolCopy() {
    return findId("inbox-tool-copy");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolArchive() {
    return findId("inbox-tool-archive");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement inboxToolCheck() {
    return findId("inbox-tool-check");
  }

}

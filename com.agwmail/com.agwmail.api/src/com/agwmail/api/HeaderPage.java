package com.agwmail.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Pagina de inbox
 * 
 * 
 * @author Bernardo Breder
 */
public class HeaderPage extends AbstractPage {

  /**
   * Construtor
   * 
   * @param driver
   */
  public HeaderPage(WebDriver driver) {
    super(driver);
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public InboxPage clickInbox() {
    leftInbox().click();
    return new InboxPage(driver);
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public HeaderPage clickScript() {
    leftScript().click();

    return new ScriptPage(driver);
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public ComposePage clickCompose() {
    leftCompose().click();
    return new ComposePage(driver);
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public HeaderPage clickAgent() {
    leftAgent().click();
    return new AgentPage(driver);
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public LoginPage clickLogout() {
    leftLogout().click();
    return new LoginPage(driver);
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement leftAgent() {
    return findId("left-index-agent");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement leftCompose() {
    return findId("left-index-compose");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement leftInbox() {
    return findId("left-index-inbox");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement leftScript() {
    return findId("left-index-script");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement leftLogout() {
    return findId("left-index-logout");
  }

}

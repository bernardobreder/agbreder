package com.agwmail.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Pagina de escrever uma mensagem
 * 
 * 
 * @author Bernardo Breder
 */
public class ComposePage extends HeaderPage {

  /**
   * Construtor
   * 
   * @param driver
   */
  public ComposePage(WebDriver driver) {
    super(driver);
    composeTo();
    composeSubject();
    composeText();
  }

  /**
   * Escreve no To
   * 
   * @param to
   * @return this
   */
  public ComposePage typeTo(String to) {
    WebElement elem = composeTo();
    elem.clear();
    elem.sendKeys(to);
    return this;
  }

  /**
   * Escreve no To
   * 
   * @param to
   * @return this
   */
  public ComposePage typeSubject(String to) {
    WebElement elem = composeSubject();
    elem.clear();
    elem.sendKeys(to);
    return this;
  }

  /**
   * Escreve no To
   * 
   * @param to
   * @return this
   */
  public ComposePage typeText(String to) {
    WebElement elem = composeText();
    elem.clear();
    elem.sendKeys(to);
    return this;
  }

  /**
   * Escreve no To
   * 
   * @return this
   */
  public InboxPage send() {
    composeSubmit().click();
    return new InboxPage(driver);
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement composeText() {
    return findId("compose_text");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement composeSubject() {
    return findId("compose_subject");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement composeTo() {
    return findId("compose_to");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement composeSubmit() {
    return findId("compose_submit");
  }

}

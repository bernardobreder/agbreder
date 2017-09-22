package com.agwmail.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Pagina de escrever uma mensagem
 * 
 * 
 * @author Bernardo Breder
 */
public class ScriptPage extends HeaderPage {

  /**
   * Construtor
   * 
   * @param driver
   */
  public ScriptPage(WebDriver driver) {
    super(driver);
    scriptTextArea();
    scriptExecute();
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement scriptExecute() {
    return findId("script-execute");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement scriptTextArea() {
    return findId("script-text");
  }

}

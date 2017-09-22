package com.agwmail.api;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Paginas Web
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class AbstractPage {

  /** Driver */
  protected final WebDriver driver;

  /**
   * Construtor
   * 
   * @param driver
   */
  public AbstractPage(WebDriver driver) {
    super();
    this.driver = driver;
  }

  /**
   * Busca pelo elemento por um id
   * 
   * @param id
   * @return elemento
   */
  protected WebElement findId(final String id) {
    return new WebDriverWait(driver, 30)
      .until(new ExpectedCondition<WebElement>() {
        @Override
        public WebElement apply(WebDriver driver) {
          return driver.findElement(By.id(id));
        }
      });
  }

  /**
   * Retorna
   * 
   * @return driver
   */
  public WebDriver getDriver() {
    return driver;
  }

}

package com.agwmail.api;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Pagina de inbox
 * 
 * 
 * @author Bernardo Breder
 */
public class LoginPage extends AbstractPage {

  /**
   * Construtor
   * 
   * @param driver
   */
  public LoginPage(WebDriver driver) {
    super(driver);
    userUsernameField();
    userPasswordField();
    userSubmitButton();
    createAccountLink();
    forgotPasswordLink();
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public LoginPage clickCreateAccount() {
    createAccountLink().click();
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @param username
   * @return this
   */
  public LoginPage typeAccountUsername(String username) {
    createUsernameField().sendKeys(username);
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @param email
   * @return this
   */
  public LoginPage typeAccountEmail(String email) {
    createEmailField().sendKeys(email);
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @param password
   * @return this
   */
  public LoginPage typeAccountPassword(String password) {
    createPasswordField().sendKeys(password);
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public LoginPage clickAccountSubmit() {
    createSubmitButton().click();
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @param username
   * @return this
   */
  public LoginPage typeUserUsername(String username) {
    userUsernameField().sendKeys(username);
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @param password
   * @return this
   */
  public LoginPage typeUserPassword(String password) {
    userPasswordField().sendKeys(password);
    return this;
  }

  /**
   * Carrega a pagina de inbox
   * 
   * @return this
   */
  public HeaderPage clickUserSubmit() {
    userSubmitButton().click();
    return new HeaderPage(driver);
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement userUsernameField() {
    return findId("username");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement userPasswordField() {
    return findId("password");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement userSubmitButton() {
    return findId("user_button");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement forgotPasswordLink() {
    return findId("login-header-forgot-password");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement createAccountLink() {
    return findId("login-header-create_account");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement createUsernameField() {
    return findId("create_account_username");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement createEmailField() {
    return findId("create_account_email");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement createPasswordField() {
    return findId("create_account_password");
  }

  /**
   * Retorna o componente
   * 
   * @return componente
   */
  protected WebElement createSubmitButton() {
    return findId("create_account_button");
  }

}

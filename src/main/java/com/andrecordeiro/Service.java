package com.andrecordeiro;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.and;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Service {

  private static final String MINHA_VACINA_LOGIN_URL = "https://minhavacina.recife.pe.gov.br/login";
  private static final String USERNAME_FIELD_ID = "cpf";
  private static final String PASSWORD_FIELD_CSS_SELECTOR = "input[type='password']";
  private static final String SIGN_IN_BUTTON_CSS_SELECTOR = "button[type='submit']";
  public static final String WAIT_MESSAGE =
      "Assim que sua vez chegar, entraremos em contato para que você possa agendar local, "
          + "data e hora de sua vacinação. Até lá.";

  private String conecta_recife_username;
  private String conecta_recife_password;
  private WebDriver driver;

  public Service(
      @NotNull String conecta_recife_username,
      @NotNull String conecta_recife_password,
      @NotNull WebDriver driver) {
    this.conecta_recife_username = conecta_recife_username;
    this.conecta_recife_password = conecta_recife_password;
    this.driver = driver;
  }

  public void process() {
    goToConectaRecifePage();
    fillUsernameAndPasswordFields();
    signIn();
  }

  private void goToConectaRecifePage() {
    driver.get(MINHA_VACINA_LOGIN_URL);
    new WebDriverWait(driver, SECONDS.toSeconds(10))
        .withMessage("Campos necessários presentes para o sign in não estão visíveis")
        .until(
            and(
                presenceOfElementLocated(By.id(USERNAME_FIELD_ID)),
                presenceOfElementLocated(By.cssSelector(PASSWORD_FIELD_CSS_SELECTOR)),
                presenceOfElementLocated(By.cssSelector(SIGN_IN_BUTTON_CSS_SELECTOR))));
  }

  private void fillUsernameAndPasswordFields() {
    driver.findElement(By.id(USERNAME_FIELD_ID)).sendKeys(conecta_recife_username);
    driver
        .findElement(By.cssSelector(PASSWORD_FIELD_CSS_SELECTOR))
        .sendKeys(conecta_recife_password);
  }

  private void signIn() {
    driver.findElement(By.cssSelector(SIGN_IN_BUTTON_CSS_SELECTOR)).click();
    new WebDriverWait(driver, SECONDS.toSeconds(30)).withMessage(WAIT_MESSAGE);
  }
}

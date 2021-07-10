package com.andrecordeiro;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.openqa.selenium.support.ui.ExpectedConditions.and;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Service {

  private static final String MINHA_VACINA_LOGIN_URL = "https://minhavacina.recife.pe.gov.br/login";
  private static final String USERNAME_FIELD_ID = "cpf";
  private static final String PASSWORD_FIELD_CSS_SELECTOR = "input[type='password']";
  private static final String SIGN_IN_BUTTON_CSS_SELECTOR = "button[type='submit']";
  public static final String WAIT_MESSAGE =
      "Você se cadastrou com sucesso. No momento, você "
          + "não faz parte de nenhum dos grupos prioritários definidos pelo Ministério da Saúde.";

  private final String conectaRecifeUsername;
  private final String conectaRecifePassword;
  private final WebDriver driver;

  Service(
      @NotNull String conectaRecifeUsername,
      @NotNull String conectaRecifePassword,
      @NotNull WebDriver driver) {
    this.conectaRecifeUsername = conectaRecifeUsername;
    this.conectaRecifePassword = conectaRecifePassword;
    this.driver = driver;
  }

  void process() {
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
    driver.findElement(By.id(USERNAME_FIELD_ID)).sendKeys(conectaRecifeUsername);
    driver.findElement(By.cssSelector(PASSWORD_FIELD_CSS_SELECTOR)).sendKeys(conectaRecifePassword);
  }

  private void signIn() {
    driver.findElement(By.cssSelector(SIGN_IN_BUTTON_CSS_SELECTOR)).click();
    new WebDriverWait(driver, SECONDS.toSeconds(10))
        .withMessage("Página não foi redirecionada para depois do login")
        .until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("blockquote"), WAIT_MESSAGE));
  }
}

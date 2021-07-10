package com.andrecordeiro;

import static org.apache.commons.lang3.reflect.MethodUtils.invokeMethod;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class ServiceTest {
  private WebDriver driver;
  private Service service;

  @BeforeEach
  void beforeEach() {
    this.driver = Mockito.mock(WebDriver.class);
    this.service = new Service("user", "password", driver);
  }

  @Test
  void shouldGoToConectaRecifeLoginPage() throws Exception {
    when(driver.findElement(By.id("cpf"))).thenReturn(mock(WebElement.class));
    when(driver.findElement(By.cssSelector("input[type='password']")))
        .thenReturn(mock(WebElement.class));
    when(driver.findElement(By.cssSelector("button[type='submit']")))
        .thenReturn(mock(WebElement.class));

    invokeMethod(this.service, true, "goToConectaRecifePage");

    verify(driver).get("https://minhavacina.recife.pe.gov.br/login");
    verify(driver).findElement(By.id("cpf"));
    verify(driver).findElement(By.cssSelector("input[type='password']"));
    verify(driver).findElement(By.cssSelector("button[type='submit']"));
  }

  @Test
  void shouldGoToConectaRecifeLoginPageFailsForNotFindingElement() throws Exception {

    try {
      invokeMethod(this.service, true, "goToConectaRecifePage");
      Assertions.fail("Should have thrown an exception");

    } catch (InvocationTargetException e) {
      var targetException = e.getTargetException();
      Assertions.assertEquals(targetException.getClass(), TimeoutException.class);
      Assertions.assertTrue(
          targetException
              .getMessage()
              .contains("Campos necessários presentes para o sign in não estão visíveis"),
          "Mensagem de erro errada");
      verify(driver).get("https://minhavacina.recife.pe.gov.br/login");
      verify(driver, atLeastOnce()).findElement(By.id("cpf"));
      verify(driver, never()).findElement(By.cssSelector("input[type='password']"));
      verify(driver, never()).findElement(By.cssSelector("button[type='submit']"));
    }
  }

  @Test
  void shouldFillUsernameAndPasswordFields() throws Exception {
    var username = "user";
    var password = "password";
    var usernameWebElement = mock(WebElement.class);
    var passwordWebElement = mock(WebElement.class);
    when(driver.findElement(By.id("cpf"))).thenReturn(usernameWebElement);
    when(driver.findElement(By.cssSelector("input[type='password']")))
        .thenReturn(passwordWebElement);

    invokeMethod(new Service(username, password, driver), true, "fillUsernameAndPasswordFields");

    verify(driver).findElement(By.id("cpf"));
    verify(driver).findElement(By.cssSelector("input[type='password']"));
    verify(usernameWebElement).sendKeys(username);
    verify(passwordWebElement).sendKeys(password);
  }

  @Test
  void shouldSubmitLoginSuccessfully() throws Exception {
    var signInButtonWebElement = mock(WebElement.class);
    var blockquoteWebElement = mock(WebElement.class);
    when(driver.findElement(By.cssSelector("button[type='submit']")))
        .thenReturn(signInButtonWebElement);
    when(driver.findElement(By.cssSelector("blockquote"))).thenReturn(blockquoteWebElement);
    when(blockquoteWebElement.getText())
        .thenReturn(
            "Você se cadastrou com sucesso. No momento, você "
                + "não faz parte de nenhum dos grupos prioritários definidos pelo Ministério da Saúde.");

    invokeMethod(this.service, true, "signIn");

    verify(driver).findElement(By.cssSelector("button[type='submit']"));
    verify(signInButtonWebElement).click();
  }

  @Test
  void shouldSubmitLoginFailsAfterTimeout() throws Exception {
    var signInButtonWebElement = mock(WebElement.class);
    var blockquoteWebElement = mock(WebElement.class);
    when(driver.findElement(By.cssSelector("button[type='submit']")))
        .thenReturn(signInButtonWebElement);
    when(driver.findElement(By.cssSelector("blockquote"))).thenReturn(blockquoteWebElement);
    when(blockquoteWebElement.getText()).thenReturn("");

    try {
      invokeMethod(this.service, true, "signIn");
      Assertions.fail("Should have thrown an exception");
    } catch (InvocationTargetException e) {
      var targetException = e.getTargetException();
      Assertions.assertEquals(TimeoutException.class, targetException.getClass());
      Assertions.assertTrue(
          targetException
              .getMessage()
              .contains("Página não foi redirecionada para depois do login"),
          "Mensagem de erro errada");
      verify(driver, atLeastOnce()).findElement(By.cssSelector("blockquote"));
    }
  }
}

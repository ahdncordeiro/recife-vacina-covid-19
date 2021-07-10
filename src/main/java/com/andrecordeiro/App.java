package com.andrecordeiro;

import static java.lang.System.getenv;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class App {

  public static void main(String[] args) {
    var conecta_recife_username = getenv("CONECTA_RECIFE_USERNAME");
    var conecta_recife_password = getenv("CONECTA_RECIFE_PASSWORD");

    var webDriver = getWebDriver();

    try {
      new Service(conecta_recife_username, conecta_recife_password, webDriver).process();
    } finally {
      webDriver.quit();
    }
  }

  private static FirefoxDriver getWebDriver() {
    var options = new FirefoxOptions();
    options.setHeadless(true);
    return new FirefoxDriver(options);
  }
}

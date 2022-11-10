package com.browserstack.local.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LocalPage {
  private String DEFAULT_LOCAL_URL = "http://bs-local.com:45454/";
  private WebDriver driver;
  private WebElement element;

  public LocalPage(WebDriver driver) {
    this.driver = driver;
  }

  public String getPageTitle(){
    return driver.getTitle();
  }

  public void openHealthCheck() {
    driver.get(DEFAULT_LOCAL_URL);
  }
}

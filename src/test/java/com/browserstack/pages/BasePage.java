package com.browserstack.pages;

import org.openqa.selenium.WebDriver;

public class BasePage {

  private WebDriver driver;

  public BasePage(WebDriver driver) {
    this.driver = driver;
  }

  public String getTitle() {
    return driver.getTitle();
  }

}

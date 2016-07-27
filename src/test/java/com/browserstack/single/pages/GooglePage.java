package com.browserstack.single.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GooglePage {
  private String DEFAULT_GOOGLE_URL = "https://www.google.com/ncr";
  private WebDriver driver;
  private WebElement element;

  public GooglePage(WebDriver driver) {
    this.driver = driver;
  }

  public String getTitle(){
    return driver.getTitle();
  }

  public void searchOnGoogle(String keyword) {
    driver.get(DEFAULT_GOOGLE_URL);
    element = driver.findElement(By.name("q"));
    element.sendKeys(keyword);
  }

  public void submitSearch() throws Exception {
    element.submit();
    Thread.sleep(5000);
  }
}

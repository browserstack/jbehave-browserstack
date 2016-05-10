package com.browserstack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.browserstack.utils.WebdriverUtils;

public class GooglePage extends BasePage {
  private String DEFAULT_GOOGLE_URL = "https://google.com";
  private WebDriverWait wait;

  private WebDriver driver;

  public GooglePage(WebDriver driver) {
    super(driver);
    this.driver = driver;
    wait = new WebDriverWait(driver, 10);
  }

  public void searchOnGoogle(String keyword) {
    By locator = By.name("q");
    WebdriverUtils.openPage(driver, DEFAULT_GOOGLE_URL);
    WebElement searchBoxElement = WebdriverUtils.getVisibleElement(driver, locator);
    searchBoxElement.sendKeys(keyword);
    clickSearch();
  }

  public void clickSearch() {
    By locator = By.name("btnG");
    WebElement searchButton = WebdriverUtils.getVisibleElement(driver, locator);
    searchButton.click();
    locator = By.id("top_nav");
    WebElement navBar = WebdriverUtils.getVisibleElement(driver, locator);
    wait.until(ExpectedConditions.visibilityOf(navBar));
  }

  public String getSelectedCountry(){
    By locator = By.cssSelector(".lan_country_selector a");
    WebElement country = WebdriverUtils.getVisibleElement(driver, locator);
    return country.getText().trim();
  }
}

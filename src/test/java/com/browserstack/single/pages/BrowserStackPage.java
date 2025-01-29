package com.browserstack.single.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BrowserStackPage {
    private WebDriver driver;
    private String selectedProductName;
    private By firstProductName = By.xpath("//*[@id=\"1\"]/p");

    private By firstProductAddToCartButton = By.xpath("//*[@id=\"1\"]/div[4]");

    private By cartPane = By.cssSelector(".float-cart__content");

    private By productCartText = By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]");

    public BrowserStackPage(WebDriver driver) {
        this.driver = driver;
        this.selectedProductName = "";
    }

  public void setSelectedProductName(String selectedProductName) {
    this.selectedProductName = selectedProductName;
  }

  public void visitUrl(String url) throws InterruptedException {
        driver.get(url);
        Thread.sleep(2000);
    }

    public void selectFirstProductName() {
      String firstProduct = driver.findElement(firstProductName).getText();
      setSelectedProductName(firstProduct);
    }

    public void clickAddToCartButton() {
      driver.findElement(firstProductAddToCartButton).click();
    }

    public void waitForCartToOpen() {
      new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartPane));
    }

    public String getSelectedProductName() {
        return selectedProductName;
    }

    public String getProductCartText() {
        return driver.findElement(productCartText).getText();
    }
}

package com.browserstack.utils;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WebdriverUtils {
  private static int WEBELEMENT_TIMOUT = 60;

  public static WebElement getClickableElement(WebDriver driver, By locator) {
    WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_TIMOUT);
    WebElement element = wait.until(ExpectedConditions
        .elementToBeClickable(locator));
    return element;
  }

  public static WebElement getVisibleElement(WebDriver driver, By locator) {
    WebDriverWait wait = new WebDriverWait(driver, WEBELEMENT_TIMOUT);
    WebElement element = wait.until(ExpectedConditions
        .visibilityOfElementLocated(locator));
    return element;
  }

  public static void openPage(WebDriver driver, String url) {
    driver.get(url);
  }
}

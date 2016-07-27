package com.browserstack.single.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.browserstack.single.pages.LocalPage;

public class LocalSteps {
  private LocalPage page;

  public LocalSteps(WebDriver driver) {
    page = new LocalPage(driver);
  }

  @When("I open health check")
  public void openHealthCheck() {
    page.openHealthCheck();
  }

  @Then("I should see \"$keyword\"")
  public void pageShouldContain(String keyword) {
    Assert.assertTrue(page.getPageSource().contains(keyword));
  }
}

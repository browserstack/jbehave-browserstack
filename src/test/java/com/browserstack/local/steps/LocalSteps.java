package com.browserstack.local.steps;

import com.browserstack.local.pages.LocalPage;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import org.openqa.selenium.WebDriver;

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
    Assert.assertTrue(page.getPageTitle().contains(keyword));
  }
}

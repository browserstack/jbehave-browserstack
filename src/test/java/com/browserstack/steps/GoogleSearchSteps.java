package com.browserstack.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.browserstack.pages.GooglePage;

public class GoogleSearchSteps {
  private static int SHORT_WAIT = 30;

  private GooglePage googlePage;

  public GoogleSearchSteps(WebDriver driver) {
    googlePage = new GooglePage(driver);
  }

  @When("I search Google for $keyword")
  public void searchGoogle(String keyword) {
    googlePage.searchOnGoogle(keyword);
  }

  @Then("the title should contain $keyword")
  public void titleShouldContain(String keyword) {
    String actual = googlePage.getTitle();
    if (actual.toLowerCase().indexOf(keyword.toLowerCase()) < 0) {
      Assert.fail("Actual: " + actual + " Expected: " + keyword);
    }
  }

}

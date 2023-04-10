package com.browserstack.single.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import org.openqa.selenium.WebDriver;

import com.browserstack.single.pages.BrowserStackPage;

public class BrowserStackSteps {
    private BrowserStackPage browserStackPage;

    public BrowserStackSteps(WebDriver driver) {
        browserStackPage = new BrowserStackPage(driver);
    }

    @Given("I am on the website \"$url\"")
    public void visitUrl(String url) throws InterruptedException {
        browserStackPage.visitUrl(url);
    }

    @When("I select a product and click on \"$buttonName\" button")
    public void selectProductAndClickButton(String buttonName) throws InterruptedException {
      browserStackPage.selectFirstProductName();
      browserStackPage.clickAddToCartButton();
      Thread.sleep(2000);
    }

    @Then("the product should be added to cart")
    public void verifyProduct() throws Exception {
      browserStackPage.waitForCartToOpen();
      Assert.assertEquals(browserStackPage.getSelectedProductName(), browserStackPage.getProductCartText());
    }
}
